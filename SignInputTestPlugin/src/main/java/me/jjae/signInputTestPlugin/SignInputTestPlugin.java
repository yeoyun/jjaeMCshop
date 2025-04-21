package me.jjae.signInputTestPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SignInputTestPlugin extends JavaPlugin {

    private static SignInputTestPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("입력테스트").setExecutor(new TestCommand());
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        pm.addPacketListener(new TestSignListener());
        getLogger().info("✅ [SignInputTestPlugin] 활성화 완료!");
    }

    public static SignInputTestPlugin getInstance() {
        return instance;
    }
}
