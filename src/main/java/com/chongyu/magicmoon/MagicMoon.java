package com.chongyu.magicmoon;

import com.chongyu.magicmoon.config.Config;
import com.chongyu.magicmoon.core.DisableSleep;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;

public class MagicMoon implements ModInitializer {
    public static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
    public static Config config;

    @Override
    public void onInitialize() {
        config = new Config();
        config.load();

        DisableSleep.init();
    }
}
