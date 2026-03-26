package com.studg.dao;

import com.studg.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MSSQLSubjectDAO implements SubjectDAO {
    @Override
    public List<String> availableSubjects() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT subject FROM subjects ORDER BY subject";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("subject"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean addSubject(String subject) {
        String sql = "INSERT INTO subjects(subject) VALUES(?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, subject);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean deleteSubject(String subject) {
        String sql = "DELETE FROM subjects WHERE subject = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, subject);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}

