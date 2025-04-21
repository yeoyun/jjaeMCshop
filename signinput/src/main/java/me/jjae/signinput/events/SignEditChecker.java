package me.jjae.signinput.events;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SignEditChecker {

    private static final int MAX_ATTEMPTS = 100; // 최대 시도 횟수 (5초 동안 체크)
    private static final int CHECK_DELAY_TICKS = 2; // 매 2틱마다 체크

    public static void checkAndEditSign(Player player, Block block) {
        new BukkitRunnable() {
            int attempts = 0;

            @Override
            public void run() {
                // 최대 시도 횟수 초과 시 종료
                if (attempts >= MAX_ATTEMPTS) {
                    player.sendMessage("❌ 표지판을 편집할 수 없습니다.");
                    cancel(); // 루프 종료
                    return;
                }

                // 표지판이 존재하고 편집 가능한 상태인지 체크
                if (block.getState() instanceof Sign sign && sign.isEditable()) {
                    sign.update(); // 편집 가능 상태로 업데이트
                    player.sendMessage("✅ 표지판이 편집 가능 상태로 설정되었습니다.");
                    cancel(); // 정상적으로 설정되었으므로 루프 종료
                    return;
                }

                // 시도 횟수 증가
                attempts++;
            }
        }.runTaskTimer(me.jjae.signinput.Signinput.getPlugin(me.jjae.signinput.Signinput.class), 0L, CHECK_DELAY_TICKS); // 0틱 후 시작, 2틱마다 체크
    }
}
