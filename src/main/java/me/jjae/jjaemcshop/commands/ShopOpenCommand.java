package me.jjae.jjaemcshop.commands;

import me.jjae.jjaemcshop.jjshop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Set;

public class ShopOpenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c플레이어만 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;
        String shopName = args[0];

        // shops.yml에서 상점 데이터 로드
        File file = new File(jjshop.getPlugin().getDataFolder(), "shops.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("상점목록." + shopName)) {
            player.sendMessage("§c해당 상점은 존재하지 않습니다.");
            return true;
        }

        // 상점 GUI 열기
        Inventory shopInventory = Bukkit.createInventory(null, 54, "상점: " + shopName);

        // 상점의 아이템 목록을 GUI에 추가
        Set<String> itemKeys = config.getConfigurationSection("상점목록." + shopName + ".아이템목록").getKeys(false);

        int slot = 0; // 아이템 슬롯 인덱스

        for (String itemKey : itemKeys) {
            String itemName = config.getString("상점목록." + shopName + ".아이템목록." + itemKey + ".아이템");
            double price = config.getDouble("상점목록." + shopName + ".아이템목록." + itemKey + ".가격");

            ItemStack item = new ItemStack(Material.valueOf(itemName));
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§a" + itemName + " - §e가격: " + price);
                item.setItemMeta(meta);
            }

            shopInventory.setItem(slot, item);
            slot++;
        }

        // 상점 GUI 열기
        player.openInventory(shopInventory);
        return true;
    }
}