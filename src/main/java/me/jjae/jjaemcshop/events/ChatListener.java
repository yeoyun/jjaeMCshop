package me.jjae.jjaemcshop.events;

import me.jjae.jjaemcshop.commands.CreateShopCommand;
import me.jjae.jjaemcshop.jjshop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!jjshop.priceInputMap.containsKey(player)) return;

        event.setCancelled(true);
        String message = event.getMessage();
        PriceInputSession session = jjshop.priceInputMap.get(player);

        if (message.equalsIgnoreCase("취소")) {
            jjshop.priceInputMap.remove(player);
            player.sendMessage("§c가격 입력이 취소되었습니다.");
            return;
        }

        try {
            double price = Double.parseDouble(message);
            int slot = session.currentIndex;
            session.slotPriceMap.put(slot, price);

            session.currentIndex++;

            if (session.currentIndex >= session.items.size()) {
                Bukkit.getScheduler().runTask(jjshop.getPlugin(), () -> saveShop(session, player));
                jjshop.priceInputMap.remove(player);
            } else {
                ItemStack nextItem = session.items.get(session.currentIndex);
                ItemMeta meta = nextItem.getItemMeta();
                String itemName = (meta != null && meta.hasDisplayName()) ? meta.getDisplayName() : nextItem.getType().name();

                player.sendMessage("§a" + (session.currentIndex + 1) + "번째 아이템 [" + itemName + "] 가격을 입력해 주세요.");
                player.sendMessage("§7※ '취소' 입력 시 등록이 취소됩니다.");
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§c숫자만 입력해 주세요. 취소하려면 '취소' 입력");
        }
    }

    private void saveShop(PriceInputSession session, Player player) {
        File file = new File(jjshop.getPlugin().getDataFolder(), "shops.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String basePath = "상점목록." + session.shopName;

        // 상점 타입 저장
        String shopType = CreateShopCommand.playerShopTypeMap.getOrDefault(player, "구매");
        config.set(basePath + ".타입", shopType);

        for (int i = 0; i < session.items.size(); i++) {
            ItemStack item = session.items.get(i);
            double price = session.slotPriceMap.get(i);

            String itemPath = basePath + ".아이템목록." + i;
            config.set(itemPath + ".아이템", item.getType().name());
            config.set(itemPath + ".가격", price);

            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                config.set(itemPath + ".이름", meta.getDisplayName());  // Display Name 저장
            }
        }

        try {
            config.save(file);
            player.sendMessage("§a상점 저장 완료! shops.yml에 저장되었습니다.");
        } catch (IOException e) {
            player.sendMessage("§c상점 저장 중 오류 발생!");
            e.printStackTrace();
        }

        CreateShopCommand.playerShopTypeMap.remove(player);
    }
}
