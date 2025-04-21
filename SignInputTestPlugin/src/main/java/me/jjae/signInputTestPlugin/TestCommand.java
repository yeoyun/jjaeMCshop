package me.jjae.signInputTestPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestCommand implements CommandExecutor {

    // ✅ 플레이어별 표지판 위치 저장
    public static final Map<UUID, BlockPosition> signLocations = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        Location fakeLoc = player.getLocation().clone().add(0, 1, 0);
        BlockPosition bp = new BlockPosition(fakeLoc.getBlockX(), fakeLoc.getBlockY(), fakeLoc.getBlockZ());

        // 위치 저장
        signLocations.put(player.getUniqueId(), bp);

        // 1. 가짜 표지판 설치 (클라이언트 전용)
        PacketContainer fakeBlock = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
        fakeBlock.getBlockPositionModifier().write(0, bp);
        fakeBlock.getBlockData().write(0, WrappedBlockData.createData(Material.OAK_SIGN));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, fakeBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. GUI 열기
        Bukkit.getScheduler().runTaskLater(SignInputTestPlugin.getInstance(), () -> {
            PacketContainer openSignGui = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            openSignGui.getBlockPositionModifier().write(0, bp);
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSignGui);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 20L);

        return true;
    }
}
