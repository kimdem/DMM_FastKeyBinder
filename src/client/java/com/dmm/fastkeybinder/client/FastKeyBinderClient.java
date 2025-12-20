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
        AutoConfig.register(FastKeyBinderConfig.class, GsonConfigSerializer::new);
        
        GuiRegistry registry = AutoConfig.getGuiRegistry(FastKeyBinderConfig.class);
        registry.registerTypeProvider((i13n, field, config, defaults, guiProvider) -> {
            try {
                ModifierKeyCode value = (ModifierKeyCode) field.get(config);
                ModifierKeyCode def = (ModifierKeyCode) field.get(defaults);

                // Consumer를 명시적으로 선언하여 타입 추론 오류 방지
                Consumer<ModifierKeyCode> saveConsumer = newValue -> {
                    try {
                        field.set(config, newValue);
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

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen == null) {
                while (openConfigKey.wasPressed()) {
                    client.setScreen(AutoConfig.getConfigScreen(FastKeyBinderConfig.class, client.currentScreen).get());
                    return;
                }
            }

            if (client.player == null || client.currentScreen != null) return;

            long windowHandle = client.getWindow().getHandle();

            for (FastKeyBinderConfig.KeyBindingEntry entry : config.keyBindings) {
                if (!entry.active) continue;
                
                int keyCode = entry.key.getKeyCode().getCode();
                if (keyCode == GLFW.GLFW_KEY_UNKNOWN) continue;

                boolean isKeyDown = InputUtil.isKeyPressed(windowHandle, keyCode);
                boolean wasKeyDown = keyStateMap.getOrDefault(entry, false);

                if (isKeyDown && !wasKeyDown) {
                    boolean shiftReq = entry.key.getModifier().hasShift();
                    boolean ctrlReq = entry.key.getModifier().hasControl();
                    boolean altReq = entry.key.getModifier().hasAlt();

                    boolean shiftPressed = Screen.hasShiftDown();
                    boolean ctrlPressed = Screen.hasControlDown();
                    boolean altPressed = Screen.hasAltDown();

                    if (shiftReq == shiftPressed && ctrlReq == ctrlPressed && altReq == altPressed) {
                        String cmd = entry.command.trim();
                        if (cmd.startsWith("/")) {
                            cmd = cmd.substring(1);
                        }

                        if (!cmd.isEmpty()) {
                            client.player.networkHandler.sendChatCommand(cmd);
                        }
                    }
                }
                keyStateMap.put(entry, isKeyDown);
            }
        });
    }
}