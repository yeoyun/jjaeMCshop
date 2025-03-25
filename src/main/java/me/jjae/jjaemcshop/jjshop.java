package me.jjae.jjaemcshop;

import me.jjae.jjaemcshop.commands.CreateShopCommand;
import me.jjae.jjaemcshop.events.InventoryClickListener;
import me.jjae.jjaemcshop.events.PriceInputSession;
import me.jjae.jjaemcshop.events.SignInputListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class jjshop extends JavaPlugin {
    public static Map<Player, PriceInputSession> priceInputMap = new HashMap<>();
    public Map<UUID, SignInputListener.SignInputCallback> signCallbacks = new HashMap<>();
    public Map<Player, Map<Integer, Double>> priceMap = new HashMap<>();
    public Set<Player> priceSettingPlayers = new HashSet<>();
    private static jjshop instance;

    public static Plugin getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getCommand("상점").setExecutor(new CreateShopCommand());
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new SignInputListener(), this);
    }

    public static jjshop getInstance() {
        return instance;
    }

    public void setItemPrice(Player player, int slot, double price) {
        priceMap.computeIfAbsent(player, k -> new HashMap<>()).put(slot, price);
    }
}
