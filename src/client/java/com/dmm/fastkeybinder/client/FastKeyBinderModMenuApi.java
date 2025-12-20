package com.dmm.fastkeybinder.client;

import com.dmm.fastkeybinder.config.FastKeyBinderConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class FastKeyBinderModMenuApi implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(FastKeyBinderConfig.class, parent).get();
    }
}
