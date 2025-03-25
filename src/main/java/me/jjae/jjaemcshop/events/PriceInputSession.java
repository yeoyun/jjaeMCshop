package me.jjae.jjaemcshop.events;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceInputSession {

    public String shopName;  // 상점 이름
    public List<ItemStack> items;  // 등록된 아이템 리스트
    public int currentIndex = 0;  // 현재 아이템 인덱스
    public Map<Integer, Double> slotPriceMap = new HashMap<>();  // 아이템 가격을 저장

    public PriceInputSession(String shopName, List<ItemStack> items) {
        this.shopName = shopName;
        this.items = items;
    }
}
