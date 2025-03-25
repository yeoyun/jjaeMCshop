// ShopListCommand.java
package me.jjae.jjaemcshop.commands;

import me.jjae.jjaemcshop.jjshop;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShopListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c플레이어만 사용할 수 있습니다.");
            return true;
        }

        Player player = (Player) sender;
        File file = new File(jjshop.getPlugin().getDataFolder(), "shops.yml");

        if (!file.exists()) {
            player.sendMessage("§c저장된 상점이 없습니다.");
            return true;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("상점목록")) {
            player.sendMessage("§c상점 목록이 비어있습니다.");
            return true;
        }

        Set<String> shopNames = config.getConfigurationSection("상점목록").getKeys(false);
        List<String> buyShops = new ArrayList<>();
        List<String> sellShops = new ArrayList<>();

        for (String name : shopNames) {
            String type = config.getString("상점목록." + name + ".타입", "구매");
            if (type.equalsIgnoreCase("구매")) buyShops.add(name);
            else if (type.equalsIgnoreCase("판매")) sellShops.add(name);
        }

        player.sendMessage("§e생성된 상점 목록 :");

        player.sendMessage("§a[구매]");
        if (buyShops.isEmpty()) player.sendMessage("§7- 없음");
        for (String s : buyShops) {
            TextComponent msg = new TextComponent("§f" + s);
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/상점열기 " + s));
            player.spigot().sendMessage(msg);
        }

        player.sendMessage("§c[판매]");
        if (sellShops.isEmpty()) player.sendMessage("§7- 없음");
        for (String s : sellShops) {
            TextComponent msg = new TextComponent("§f" + s);
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/상점열기 " + s));
            player.spigot().sendMessage(msg);
        }

        return true;
    }
}
