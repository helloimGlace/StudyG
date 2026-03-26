package com.studg.service;

import com.studg.dao.DAOFactory;
import com.studg.dao.SubjectDAO;

import java.util.List;

public class SimpleSubjectService implements SubjectService {
    private final SubjectDAO subjectDAO;

    public SimpleSubjectService() {
        subjectDAO = DAOFactory.getSubjectDAO();
    }

    @Override
    public List<String> availableSubjects() {
        return subjectDAO.availableSubjects();
    }
}
