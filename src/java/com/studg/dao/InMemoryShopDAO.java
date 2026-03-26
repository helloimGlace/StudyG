package com.studg.dao;

import com.studg.model.ShopItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryShopDAO implements ShopDAO {
    private static final List<ShopItem> items = new ArrayList<>();
    static {
        items.add(new ShopItem(1, "play_ticket", "Play Ticket", 100));
        items.add(new ShopItem(2, "sticker_rare", "Rare Sticker", 200));
    }

    @Override
    public List<ShopItem> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public ShopItem findByKey(String itemKey) {
        if (itemKey == null) return null;
        for (ShopItem it : items) if (itemKey.equals(it.getItemKey())) return it;
        return null;
    }

    @Override
    public synchronized boolean addItem(ShopItem item) {
        if (item == null || item.getItemKey() == null) return false;
        if (findByKey(item.getItemKey()) != null) return false;
        // assign id
        int maxId = 0;
        for (ShopItem it : items) if (it.getId() > maxId) maxId = it.getId();
        item.setId(maxId + 1);
        items.add(item);
        return true;
    }

    @Override
    public synchronized boolean deleteByKey(String itemKey) {
        ShopItem it = findByKey(itemKey);
        if (it == null) return false;
        return items.remove(it);
    }
}
