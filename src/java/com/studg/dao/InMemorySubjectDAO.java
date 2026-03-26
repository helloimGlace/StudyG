package com.studg.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemorySubjectDAO implements SubjectDAO {
    private static final List<String> subjects = new ArrayList<>();
    static {
        subjects.add("Math");
        subjects.add("Physics");
        subjects.add("Chemistry");
        subjects.add("History");
    }

    @Override
    public List<String> availableSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    @Override
    public boolean addSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) return false;
        if (subjects.contains(subject)) return false;
        subjects.add(subject);
        return true;
    }

    @Override
    public boolean deleteSubject(String subject) {
        return subjects.remove(subject);
    }
}

