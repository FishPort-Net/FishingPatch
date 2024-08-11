package cn.iasoc.fishingpatch;

import org.bukkit.plugin.java.JavaPlugin;

public final class FP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EListener(this), this);
        getLogger().info("FishingPatch enabled!");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("FishingPatch disabled!");
    }
}
