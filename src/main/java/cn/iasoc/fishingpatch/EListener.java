package cn.iasoc.fishingpatch;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class EListener implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public EListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private ItemStack getRandomFish() {
        Material[] fishTypes = {
                Material.COD,
                Material.SALMON,
                Material.TROPICAL_FISH,
                Material.PUFFERFISH
        };
        return new ItemStack(fishTypes[random.nextInt(fishTypes.length)]);
    }

    @EventHandler
    public void onFishing(PlayerFishEvent event){
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        Entity caught = event.getCaught();
        if (caught == null) return;

        if (caught instanceof Item) {
            Item item = (Item) caught;
            ItemStack itemStack = item.getItemStack();

            // Modify the itemStack here
            boolean removed = false;
            String ItemType = "";
            if(!itemStack.getEnchantments().isEmpty()){
                //random
                if(Math.random() < plugin.getConfig().getDouble("EnchantItem" ,0.0)){
                    removed = true;
                    ItemType = "附魔物品";
                }
            } else if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                if(Math.random() < plugin.getConfig().getDouble("EnchantBook" ,0.0)){
                    removed = true;
                    ItemType = "附魔书";
                }
            }

            if (removed){
                String message = plugin.getConfig().getString("Message");

                if (message == null){
                    message = "<gold>[<aqua><bold>🐟<reset><gold>] <aqua>你钓到了一个 %s, 但是它突然变成鱼了!";
                }

                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(message.replace("%s", ItemType)));
                event.getHook().getWorld().strikeLightningEffect(event.getHook().getLocation());


                item.remove();
                //测试过无法直接更改itemstack, 其他更改方式未测试
                //item.setItemStack(getRandomFish());
            }

            // Set the modified itemStack back to the item
            item.setItemStack(itemStack);
        }


    }
}
