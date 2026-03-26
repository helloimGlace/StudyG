package com.studg.dao;

import com.studg.util.DBUtil;

import java.sql.Connection;

public class DAOFactory {
    public static UserDAO getUserDAO() {
        try (Connection c = DBUtil.getConnection()) {
            if (c!=null) return new MSSQLUserDAO();
        } catch (Exception e) {
            // fallback
        }
        return new InMemoryUserDAO();
    }

    public static ShopDAO getShopDAO() {
        try (Connection c = DBUtil.getConnection()) {
            if (c!=null) return new MSSQLShopDAO();
        } catch (Exception e) {
            // fallback
        }
        return new InMemoryShopDAO();
    }

    public static SubjectDAO getSubjectDAO() {
        try (Connection c = DBUtil.getConnection()) {
            if (c!=null) return new MSSQLSubjectDAO();
        } catch (Exception e) {
            // fallback
        }
        return new InMemorySubjectDAO();
    }
}
