package me.jjae.signinput.classes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PriceGui {
    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "가격 설정");

        ItemStack emerald = new ItemStack(Material.EMERALD);
        ItemMeta meta = emerald.getItemMeta();
        meta.setDisplayName("가격 입력");
        emerald.setItemMeta(meta);

        gui.setItem(4, emerald);

        player.openInventory(gui);
    }
}
