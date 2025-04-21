package me.jjae.signinput;

import me.jjae.signinput.classes.PriceGui;
import me.jjae.signinput.events.GuiClickListener;
import me.jjae.signinput.events.SignInputHandler;
import me.jjae.signinput.events.SignInputListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Signinput extends JavaPlugin {

    @Override
    public void onEnable() {
        // ✅ 리스너 등록
        getServer().getPluginManager().registerEvents(new GuiClickListener(), this);

        // ✅ 표지판 입력 리스너 등록
        new SignInputListener(this);

        // ✅ /가격입력 명령어 실행 → 표지판 GUI
        getCommand("가격입력").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) return true;

            SignInputHandler.openSignInput(player, (price) -> {
                player.sendMessage("✅ 입력한 가격: " + price);
            });

            return true;
        });

        // ✅ /가격gui 명령어 실행 → GUI 열기
        getCommand("가격gui").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) return true;

            PriceGui.open(player);
            return true;
        });

        getLogger().info("✅ Signinput 플러그인 활성화됨!");
    }

    @Override
    public void onDisable() {
        getLogger().info("🛑 Signinput 플러그인 비활성화됨");
    }
}
