package com.studg.dao;

import com.studg.model.ShopItem;
import java.util.List;

public interface ShopDAO {
    List<ShopItem> getAllItems();
    ShopItem findByKey(String itemKey);
    boolean addItem(ShopItem item);
    boolean deleteByKey(String itemKey);
}

