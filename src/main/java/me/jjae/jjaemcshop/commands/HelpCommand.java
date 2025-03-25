package me.jjae.jjaemcshop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            // OP 권한 확인
            sender.sendMessage("§e=== jjshop 사용법 ===");
            sender.sendMessage("§7/상점 [생성/수정] [구매/판매] [상점이름] §f- 상점 생성 또는 수정");
            sender.sendMessage("§7/상점목록 §f- 저장된 상점 목록 확인");
            sender.sendMessage("§7/상점열기 [상점이름] §f- 상점 열기");
            sender.sendMessage("§7/jjshop reload §f- 플러그인 리로드");
            sender.sendMessage("§7/jjshop help §f- 플러그인 도움말 출력");
            return true;
        }
        return false;
    }
}