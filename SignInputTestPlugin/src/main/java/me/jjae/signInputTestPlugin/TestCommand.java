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

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        Location fakeLoc = player.getLocation().clone().add(0, 1, 0);
        BlockPosition bp = new BlockPosition(fakeLoc.getBlockX(), fakeLoc.getBlockY(), fakeLoc.getBlockZ());

        // 1. 가짜 표지판 설치 (클라이언트 전용)
        PacketContainer fakeBlock = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
        fakeBlock.getBlockPositionModifier().write(0, bp);
        fakeBlock.getBlockData().write(0, WrappedBlockData.createData(Material.OAK_SIGN));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, fakeBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. GUI 열기 (20L 지연)
        Bukkit.getScheduler().runTaskLater(SignInputTestPlugin.getInstance(), () -> {
            PacketContainer openSignGui = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            openSignGui.getBlockPositionModifier().write(0, bp);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSignGui);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 20L);

        // 3. 표지판 제거 (60L 지연 → GUI 뜨고 약 2초 후 제거)
        Bukkit.getScheduler().runTaskLater(SignInputTestPlugin.getInstance(), () -> {
            PacketContainer removeBlock = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
            removeBlock.getBlockPositionModifier().write(0, bp);
            removeBlock.getBlockData().write(0, WrappedBlockData.createData(Material.AIR));

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, removeBlock);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 60L); // GUI 열고 충분한 시간 후 제거

        return true;
    }
}
