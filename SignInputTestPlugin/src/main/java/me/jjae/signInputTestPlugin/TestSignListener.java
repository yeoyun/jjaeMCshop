package me.jjae.signInputTestPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class TestSignListener extends PacketAdapter {

    public TestSignListener() {
        super(SignInputTestPlugin.getInstance(), PacketType.Play.Client.UPDATE_SIGN);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        // ✅ 직접 입력한 텍스트 줄 4개 가져오기
        String[] lines = event.getPacket().getStringArrays().read(0);

        System.out.println("▶ DEBUG: UPDATE_SIGN 패킷 수신됨");

        if (lines.length > 0) {
            String input = lines[0].trim(); // 첫 줄만 사용
            if (!input.isEmpty()) {
                player.sendMessage("💬 입력한 값: " + input);
            } else {
                player.sendMessage("⚠️ 아무것도 입력되지 않았습니다.");
            }
        }
    }
}
