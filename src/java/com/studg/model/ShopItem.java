package com.studg.model;

public class ShopItem {
    private int id;
    private String itemKey;
    private String displayName;
    private int price;

    public ShopItem() {}

    public ShopItem(int id, String itemKey, String displayName, int price) {
        this.id = id;
        this.itemKey = itemKey;
        this.displayName = displayName;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getItemKey() { return itemKey; }
    public void setItemKey(String itemKey) { this.itemKey = itemKey; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}

