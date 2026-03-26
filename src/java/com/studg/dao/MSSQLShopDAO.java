package com.studg.dao;

import com.studg.model.ShopItem;
import com.studg.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MSSQLShopDAO implements ShopDAO {
    @Override
    public List<ShopItem> getAllItems() {
        List<ShopItem> list = new ArrayList<>();
        String sql = "SELECT id, item_key, display_name, price FROM shop_items";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ShopItem it = new ShopItem(rs.getInt("id"), rs.getString("item_key"), rs.getString("display_name"), rs.getInt("price"));
                list.add(it);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public ShopItem findByKey(String itemKey) {
        String sql = "SELECT id, item_key, display_name, price FROM shop_items WHERE item_key = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, itemKey);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ShopItem(rs.getInt("id"), rs.getString("item_key"), rs.getString("display_name"), rs.getInt("price"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean addItem(ShopItem item) {
        String sql = "INSERT INTO shop_items(item_key, display_name, price) VALUES(?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, item.getItemKey());
            ps.setString(2, item.getDisplayName());
            ps.setInt(3, item.getPrice());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean deleteByKey(String itemKey) {
        String sql = "DELETE FROM shop_items WHERE item_key = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, itemKey);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}

