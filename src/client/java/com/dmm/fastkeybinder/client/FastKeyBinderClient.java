package com.dmm.fastkeybinder.client;

import com.dmm.fastkeybinder.config.FastKeyBinderConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FastKeyBinderClient implements ClientModInitializer {

    private final Map<FastKeyBinderConfig.KeyBindingEntry, Boolean> keyStateMap = new HashMap<>();
    private static KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        // 1. Config & Registry
        AutoConfig.register(FastKeyBinderConfig.class, GsonConfigSerializer::new);
        
        GuiRegistry registry = AutoConfig.getGuiRegistry(FastKeyBinderConfig.class);
        registry.registerTypeProvider((i13n, field, config, defaults, guiProvider) -> {
            try {
                ModifierKeyCode value = (ModifierKeyCode) field.get(config);
                ModifierKeyCode def = (ModifierKeyCode) field.get(defaults);

                Consumer<ModifierKeyCode> saveConsumer = newValue -> {
                    try {
                        field.set(config, newValue);
                        
                        // DTO 동기화: 저장용 필드에 값 업데이트
                        if (config instanceof FastKeyBinderConfig.KeyBindingEntry entry) {
                            entry.storedKeyCode = newValue.getKeyCode().getCode();
                            entry.storedModifier = newValue.getModifier().getValue();
                        }

                        // 값이 변경될 때마다 즉시 파일에 저장 (강제 Flush)
                        AutoConfig.getConfigHolder(FastKeyBinderConfig.class).save();

                        MinecraftClient mc = MinecraftClient.getInstance();
                        if (mc.player != null) {
                            int code = newValue.getKeyCode().getCode();
                            boolean hasModifier = newValue.getModifier().hasShift() || newValue.getModifier().hasControl() || newValue.getModifier().hasAlt();

                            // 1. 내부 중복 검사 (이름 알려주기)
                            FastKeyBinderConfig globalConfig = AutoConfig.getConfigHolder(FastKeyBinderConfig.class).getConfig();
                            long duplicateCount = globalConfig.keyBindings.stream()
                                .filter(e -> e.key.getKeyCode().getCode() == code &&
                                             e.key.getModifier().getValue() == newValue.getModifier().getValue())
                                .count();

                            if (duplicateCount > 1) {
                                // 중복된 다른 항목 찾기 (자기 자신이 아닌 것)
                                globalConfig.keyBindings.stream()
                                    .filter(e -> e.key.getKeyCode().getCode() == code &&
                                                 e.key.getModifier().getValue() == newValue.getModifier().getValue() &&
                                                 e != config) // 현재 수정 중인 객체(config) 제외
                                    .findFirst()
                                    .ifPresent(dup -> mc.player.sendMessage(Text.translatable("message.dmm_fastkeybinder.warning.duplicate", dup.name), false));
                            }

                            // 2. 바닐라 키 충돌 검사 (Modifier 없을 때만)
                            if (!hasModifier) {
                                for (KeyBinding kb : mc.options.allKeys) {
                                    if (kb.matchesKey(code, 0)) {
                                        String conflictName = Text.translatable(kb.getTranslationKey()).getString();
                                        mc.player.sendMessage(Text.translatable("message.dmm_fastkeybinder.warning.conflict", conflictName), false);
                                    }
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                };

                return Collections.singletonList(
                    ConfigEntryBuilder.create()
                        .startModifierKeyCodeField(Text.translatable(i13n), value)
                        .setDefaultValue(def)
                        .setModifierSaveConsumer(saveConsumer)
                        .build()
                );
            } catch (IllegalAccessException e) {
                return Collections.emptyList();
            }
        }, ModifierKeyCode.class);

        FastKeyBinderConfig config = AutoConfig.getConfigHolder(FastKeyBinderConfig.class).getConfig();

        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.dmm_fastkeybinder.open_config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F12,
            "category.dmm_fastkeybinder"
        ));

        // 2. Logic: Use START_CLIENT_TICK to intercept inputs
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            
            // Open Config (F12) - Check even if screen is open (to allow closing? no, usually only open)
            // But we restrict opening to when currentScreen is null for safety
            if (client.currentScreen == null) {
                while (openConfigKey.wasPressed()) {
                    client.setScreen(AutoConfig.getConfigScreen(FastKeyBinderConfig.class, client.currentScreen).get());
                    return;
                }
            }

            // If a screen is open, skip macro execution
            if (client.currentScreen != null) return;

            long windowHandle = client.getWindow().getHandle();

            for (FastKeyBinderConfig.KeyBindingEntry entry : config.keyBindings) {
                if (!entry.active) continue;
                
                int keyCode = entry.key.getKeyCode().getCode();
                if (keyCode == GLFW.GLFW_KEY_UNKNOWN) continue;

                boolean isKeyDown = InputUtil.isKeyPressed(windowHandle, keyCode);
                boolean wasKeyDown = keyStateMap.getOrDefault(entry, false);

                // Rising Edge Detection
                if (isKeyDown && !wasKeyDown) {
                    boolean shiftReq = entry.key.getModifier().hasShift();
                    boolean ctrlReq = entry.key.getModifier().hasControl();
                    boolean altReq = entry.key.getModifier().hasAlt();

                    boolean shiftPressed = Screen.hasShiftDown();
                    boolean ctrlPressed = Screen.hasControlDown();
                    boolean altPressed = Screen.hasAltDown();

                    // Check exact modifier match
                    if (shiftReq == shiftPressed && ctrlReq == ctrlPressed && altReq == altPressed) {
                        // 1. Execute Command
                        String cmd = entry.command.trim();
                        if (cmd.startsWith("/")) cmd = cmd.substring(1);
                        if (!cmd.isEmpty()) {
                            client.player.networkHandler.sendChatCommand(cmd);
                        }

                        // 2. Suppress Vanilla Key Bindings (Interceptor)
                        // If this key is used by vanilla (e.g. 'E' for inventory), neutralize it.
                        // Because we are in START_TICK, the game hasn't processed the key logic yet.
                        for (KeyBinding vanillaKey : client.options.allKeys) {
                            if (vanillaKey.matchesKey(keyCode, 0)) {
                                // Clear any pending presses
                                while (vanillaKey.wasPressed()) { }
                                // Force release
                                vanillaKey.setPressed(false);
                            }
                        }
                    }
                }
                keyStateMap.put(entry, isKeyDown);
            }
        });
    }
}
