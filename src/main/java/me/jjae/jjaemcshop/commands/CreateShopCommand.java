package me.jjae.jjaemcshop.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class CreateShopCommand implements CommandExecutor {

    public static Map<Player, String> playerShopTypeMap = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage("§cOP만 사용할 수 있습니다.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage("§c사용법: /상점 생성 [상점이름] [구매/판매]");
            return true;
        }

        String shopName = args[0];
        String type = args[1];

        Inventory inv = Bukkit.createInventory(null, 54, "상점 설정: " + shopName);

        ItemStack priceBtn = new ItemStack(Material.LIME_DYE);
        ItemMeta meta = priceBtn.getItemMeta();
        meta.setDisplayName("§a가격설정");
        priceBtn.setItemMeta(meta);
        inv.setItem(45, priceBtn);

        ItemStack saveBtn = new ItemStack(Material.BLUE_DYE);
        ItemMeta saveMeta = saveBtn.getItemMeta();
        saveMeta.setDisplayName("§b상점 저장");
        saveBtn.setItemMeta(saveMeta);
        inv.setItem(53, saveBtn);

        player.openInventory(inv);
        player.sendMessage("§a상점 [" + shopName + "] 생성 중입니다.");

        return true;
    }
}
