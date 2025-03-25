package me.jjae.jjaemcshop.events;

import me.jjae.jjaemcshop.jjshop;
import me.jjae.jjaemcshop.events.SignGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        if (!e.getView().getTitle().startsWith("상점 설정: ")) return;
        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();
        int slot = e.getRawSlot();

        if (clicked == null || clicked.getType() == Material.AIR) return;

        String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

        if (clicked.getType() == Material.LIME_DYE && displayName.equals("가격설정")) {
            jjshop.getInstance().priceSettingPlayers.add(player);
            player.sendMessage("§a가격 설정할 아이템을 클릭하세요.");
            return;
        }

        if (jjshop.getInstance().priceSettingPlayers.contains(player) && slot < 45) {
            SignGUI.open(player, input -> {
                try {
                    double price = Double.parseDouble(input);
                    jjshop.getInstance().setItemPrice(player, slot, price);
                    player.sendMessage("§a가격 " + price + "원이 설정되었습니다.");
                } catch (NumberFormatException ex) {
                    player.sendMessage("§c잘못된 숫자입니다.");
                }
            });
        }

        if (slot == 53 && clicked.getType() == Material.BLUE_DYE) {
            player.sendMessage("§a상점 저장 완료.");
            player.closeInventory();
        }
    }
}
