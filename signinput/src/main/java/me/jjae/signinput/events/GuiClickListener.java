package me.jjae.signinput.events;

import me.jjae.signinput.events.SignInputHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // ✅ "가격 설정" GUI만 처리
        if (event.getView().getTitle() == null || !event.getView().getTitle().equals("가격 설정")) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.EMERALD) return;

        event.setCancelled(true); // 아이템 꺼내기 방지
        player.closeInventory();  // GUI 닫기

        // 표지판 편집 가능 상태로 바로 설정
        SignInputHandler.openSignInput(player, (price) -> {
            player.sendMessage("입력한 가격: " + price);
        });
    }
}
