package me.jjae.signInputTestPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TestSignListener extends PacketAdapter {

    public TestSignListener() {
        super(SignInputTestPlugin.getInstance(), PacketType.Play.Client.UPDATE_SIGN);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        String[] lines = event.getPacket().getStringArrays().read(0);
        String input = lines.length > 0 ? lines[0].trim() : "";

        // ✅ 정확한 위치에서 제거
        BlockPosition bp = TestCommand.signLocations.remove(playerId);
        if (bp != null) {
            Bukkit.getScheduler().runTask(SignInputTestPlugin.getInstance(), () -> {
                PacketContainer removeBlock = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
                removeBlock.getBlockPositionModifier().write(0, bp);
                removeBlock.getBlockData().write(0, WrappedBlockData.createData(Material.AIR));
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, removeBlock);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // ✅ 입력 출력
        if (!input.isEmpty()) {
            player.sendMessage("입력한 값: " + input);
        } else {
            player.sendMessage("⚠️ 아무 것도 입력되지 않았습니다.");
        }
    }
}
