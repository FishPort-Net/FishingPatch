package net.fishport.fishincontrol;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishInControl extends JavaPlugin implements CommandExecutor {
    private LootTable lootTable;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.lootTable = new LootTable(this);
        saveDefaultConfig();

        getCommand("fishincontrol").setExecutor(this);
        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
        loadConfig();

        getLogger().info("FishInControl enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        lootTable = null;
        getLogger().info("FishInControl disabled!");
    }

    private void loadConfig() {
        reloadConfig();
        lootTable.reload();
    }

    public LootTable getLootTable() { return lootTable; }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (sender == null) return false;

        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("fishincontrol.admin")) {
                this.loadConfig();
                sender.sendMessage("FishInControl config has been reloaded from disk.");
                return true;
            }
        }
        return false;
    }
}
