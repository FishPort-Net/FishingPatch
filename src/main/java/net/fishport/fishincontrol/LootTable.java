package net.fishport.fishincontrol;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class LootTable {
    public static class Loot {
        String name;
        ItemStack stack;
        double weight;
        public Loot(String name, ItemStack stack, double weight) {
            this.name = name;
            this.stack = stack;
            this.weight = weight;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public ItemStack getStack() {
            return stack;
        }
        public void setStack(ItemStack stack) {
            this.stack = stack;
        }
        public double getWeight() {
            return weight;
        }
        public void setWeight(double weight) {
            this.weight = weight;
        }

    }

    private final NavigableMap<Double, Loot> map = new TreeMap<Double, Loot>();
    private final Random random;
    private double total = 0;
    FishInControl plugin;

    public LootTable(FishInControl plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    public void clear() {
        this.map.clear();
        this.total = 0;
    }

    public void add(String name, ItemStack stack, double weight) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, new Loot(name, stack, weight));
    }

    public Loot get(int lootingLevel) {
        if (lootingLevel <= 0) {
            lootingLevel = 1 + random.nextInt(5);
        }

        double max = 0;
        double score = 0;
        for (int i=0;i<=random.nextInt((lootingLevel/2)+1);i++) {
            score = random.nextDouble() * total;
            if (score > max) max = score;
        }
        if (max >= total) {
            max = total-1;
        }
        return map.ceilingEntry(max).getValue();
    }

    public void reload() {
        this.clear();
        ConfigurationSection cfg = null;
        for (String key: getLootConfig().getKeys(false)) {
            cfg = getLootConfig().getConfigurationSection(key);
            if (cfg == null) continue;
            add(key, cfg.getItemStack("item"), cfg.getDouble("weight"));
        }
    }

    private ConfigurationSection getLootConfig() {
        return plugin.getConfig().getConfigurationSection("LootItems");
    }
}
