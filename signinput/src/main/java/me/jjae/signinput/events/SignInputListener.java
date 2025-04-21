package me.jjae.signinput.events;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static com.comphenix.protocol.PacketType.Play.Client.UPDATE_SIGN;

public class SignInputListener extends PacketAdapter {

    private static final Map<UUID, Consumer<String[]>> inputHandlers = new HashMap<>();

    public SignInputListener(Plugin plugin) {
        super(plugin, UPDATE_SIGN);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    public static void registerInput(UUID playerId, Consumer<String[]> callback) {
        inputHandlers.put(playerId, callback);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        if (!inputHandlers.containsKey(playerId)) return;

        String[] lines = event.getPacket().getStringArrays().read(0);
        Consumer<String[]> callback = inputHandlers.remove(playerId);
        if (callback != null) {
            callback.accept(lines);
        }
    }
}
