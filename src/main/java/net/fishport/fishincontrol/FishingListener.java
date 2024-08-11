package net.fishport.fishincontrol;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FishingListener implements Listener {
    private final FishInControl plugin;
    private final Random random = new Random();

    public FishingListener(FishInControl plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled=true, priority= EventPriority.HIGH)
    public void onPlayerFish(PlayerFishEvent event) {
        final Player player = event.getPlayer();

        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            if (player.hasPermission("fishincontrol.bypass")) return;

            Item item = (Item) event.getCaught();
            if (item == null) return;
            ItemStack itemStack = item.getItemStack();
            boolean shouldReplace = false;

            if (plugin.getConfig().getBoolean("FullReplacement", false) || player.hasPermission("fishincontrol.forced")) {
                shouldReplace = true;
            } else if(!itemStack.getEnchantments().isEmpty()){
                if(random.nextDouble() < Math.min(plugin.getConfig().getDouble("EnchantItem" ,0.0), 1.0)){
                    shouldReplace = true;
                }
            } else if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                if(random.nextDouble() < Math.min(plugin.getConfig().getDouble("EnchantBook" ,0.0), 1.0)){
                    shouldReplace = true;
                }
            }

            if (shouldReplace) {
                ItemStack newItemStack = plugin.getLootTable().get(plugin.getConfig().getInt("DefaultLootingLevel", 1)).getStack().clone();

                if (plugin.getConfig().getBoolean("MessageOnReplace", false)) {
                    String message = plugin.getConfig().getString("Message", "");
                    if (!message.isEmpty()) {
                        String itemBefore = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName());
                        String itemReplaced = PlainTextComponentSerializer.plainText().serialize(newItemStack.displayName());

                        player.sendMessage(MiniMessage.miniMessage().deserialize(
                                message,
                                Placeholder.parsed("item", itemBefore),
                                Placeholder.parsed("item_replaced", itemReplaced)
                        ));
                    }
                }

                item.setItemStack(newItemStack);
            }
        }
    }
}
