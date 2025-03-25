package me.jjae.jjaemcshop.events;

import me.jjae.jjaemcshop.jjshop;
import me.jjae.jjaemcshop.events.SignInputListener.SignInputCallback;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class SignGUI {
    public static void open(Player player, SignInputCallback callback) {
        Block block = player.getLocation().getBlock();
        block.setType(Material.OAK_SIGN);
        Sign sign = (Sign) block.getState();
        sign.update();

        player.openSign(sign);
        jjshop.getInstance().signCallbacks.put(player.getUniqueId(), callback);
    }
}
