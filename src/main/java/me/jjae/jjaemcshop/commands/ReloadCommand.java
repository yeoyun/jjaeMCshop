package me.jjae.jjaemcshop.commands;

import me.jjae.jjaemcshop.jjshop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // OP 권한 확인
        if (!(sender instanceof Player) || !((Player) sender).isOp()) {
            sender.sendMessage("§cOP 권한이 필요합니다.");
            return false; // OP가 아니면 false 반환
        }

        Player player = (Player) sender;

        // 플러그인 리로드
        plugin.reloadConfig();  // config 재로드
        getServer().getPluginManager().disablePlugin(plugin);
        getServer().getPluginManager().enablePlugin(plugin); // 플러그인 비활성화 후 재활성화

        player.sendMessage("§a플러그인이 리로드되었습니다.");
        return true;  // 리로드가 정상적으로 실행되면 true 반환
    }
}
