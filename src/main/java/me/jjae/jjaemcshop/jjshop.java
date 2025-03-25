package me.jjae.jjaemcshop;

import me.jjae.jjaemcshop.commands.CreateShopCommand;
import me.jjae.jjaemcshop.commands.JJShopCommand;
import me.jjae.jjaemcshop.commands.ShopListCommand;
import me.jjae.jjaemcshop.events.ChatListener;
import me.jjae.jjaemcshop.events.InventoryClickListener;
import me.jjae.jjaemcshop.events.PriceInputSession;
import me.jjae.jjaemcshop.events.SignInputListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class jjshop extends JavaPlugin {

    private static jjshop instance;

    // ✅ 누락되지 않도록 정확히 추가된 부분
    public static Map<Player, PriceInputSession> priceInputMap = new HashMap<>();
    public static Map<Player, String> playerShopTypeMap = new HashMap<>(); // 누락 복구
    public Map<UUID, SignInputListener.SignInputCallback> signCallbacks = new HashMap<>();
    public Map<Player, Map<Integer, Double>> priceMap = new HashMap<>();
    public Set<Player> priceSettingPlayers = new HashSet<>();

    public static jjshop getPlugin() {
        return instance;
    }

    public static jjshop getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // ✅ 명령어 등록 (반드시 두 개 모두)
        getCommand("상점").setExecutor(new CreateShopCommand());
        getCommand("jjshop").setExecutor(new JJShopCommand());
        getCommand("상점목록").setExecutor(new ShopListCommand());// 누락 복구

        // ✅ 이벤트 등록 (반드시 세 개 모두)
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new SignInputListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this); // 누락 복구

        getLogger().info("플러그인이 활성화되었습니다!");
    }

    @Override
    public void onDisable() {
        getLogger().info("플러그인이 비활성화되었습니다!");
    }

    public void setItemPrice(Player player, int slot, double price) {
        priceMap.computeIfAbsent(player, k -> new HashMap<>()).put(slot, price);
    }
}
