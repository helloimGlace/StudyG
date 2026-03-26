package com.studg.dao;

import java.util.List;

public interface SubjectDAO {
    List<String> availableSubjects();
    boolean addSubject(String subject);
    boolean deleteSubject(String subject);
}
