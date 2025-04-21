package me.jjae.signinput.events;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

import static com.comphenix.protocol.PacketType.Play.Server.OPEN_SIGN_EDITOR;

public class SignInputHandler {
    public static void openSignInput(Player player, Consumer<Integer> onValidInput) {
        Location loc = player.getLocation().clone().add(0, 1, 0); // 머리 위
        Block block = loc.getBlock();

        block.setType(Material.OAK_SIGN); // 표지판 설치

        // 표지판 패킷을 보낸 후, 루프를 통해 편집 가능 상태로 변경 확인
        checkSignEditing(player, block, 0); // 루프에서 타이밍 체크 시작

        // 입력 후 표지판 제거
        SignInputListener.registerInput(player.getUniqueId(), (lines) -> {
            String input = lines[0];
            try {
                int price = Integer.parseInt(input);
                if (price <= 0) {
                    player.sendMessage("❌ 가격은 0보다 커야 합니다!");
                    return;
                }

                // 설정된 가격 메시지만 출력
                onValidInput.accept(price); // 가격 설정 콜백 실행

                // 표지판 제거는 메인 스레드에서
                Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(SignInputHandler.class), () -> {
                    block.setType(Material.AIR); // 표지판 제거
                    player.sendMessage("✅ 표지판이 제거되었습니다.");
                });

            } catch (NumberFormatException e) {
                player.sendMessage("❌ 유효한 숫자가 아닙니다. 숫자만 입력하세요!");
            }
        });
    }

    // 타이밍 체크 루프
    private static void checkSignEditing(Player player, Block block, int attempts) {
        if (attempts >= 100) {
            player.sendMessage("❌ 표지판을 편집할 수 없습니다.");
            return;
        }

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(SignInputHandler.class), () -> {
            if (block.getState() instanceof Sign sign && sign.isEditable()) {
                sign.update(); // 편집 가능 상태로 업데이트
                player.sendMessage("✅ 표지판이 편집 가능 상태로 설정되었습니다.");
                openSignEditor(player, block); // 편집 창 열기
            } else {
                // 다시 시도
                checkSignEditing(player, block, attempts + 1);
            }
        }, 2L); // 2틱마다 다시 체크
    }

    // 표지판 수정 창 열기
    private static void openSignEditor(Player player, Block block) {
        Location loc = block.getLocation();
        BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        PacketContainer packet = new PacketContainer(OPEN_SIGN_EDITOR);
        packet.getBlockPositionModifier().write(0, pos);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet); // 패킷 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
