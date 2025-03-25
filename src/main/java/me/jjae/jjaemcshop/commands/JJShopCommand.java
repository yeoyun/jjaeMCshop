package me.jjae.jjaemcshop.commands;

import me.jjae.jjaemcshop.jjshop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JJShopCommand implements CommandExecutor {

    private final jjshop plugin = jjshop.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }

        if (!player.isOp()) {
            player.sendMessage("§cOP만 사용할 수 있습니다.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadPlugin(player);
            return true;
        }

        player.sendMessage("§c알 수 없는 명령어입니다. /jjshop help 를 입력하세요.");
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§a--- jjshop 도움말 ---");
        player.sendMessage("§e/jjshop reload §7- 플러그인을 리로드합니다.");
        player.sendMessage("§e/jjshop help §7- 도움말을 표시합니다.");
    }

    private void reloadPlugin(Player player) {
        plugin.reloadConfig();
        player.sendMessage("§a플러그인이 리로드되었습니다!");
    }
}
