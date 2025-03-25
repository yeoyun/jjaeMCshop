package me.jjae.jjaemcshop.events;

import me.jjae.jjaemcshop.jjshop;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.UUID;

public class SignInputListener implements Listener {

    public interface SignInputCallback {
        void onInput(String input);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (jjshop.getInstance().signCallbacks.containsKey(uuid)) {
            event.setCancelled(true);
            SignInputCallback callback = jjshop.getInstance().signCallbacks.remove(uuid);
            callback.onInput(event.getLine(0));
            event.getBlock().setType(Material.AIR);
        }
    }
}
