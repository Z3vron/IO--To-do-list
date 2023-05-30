package io.project.todoapp.service;

import io.project.todoapp.model.Semester;
import io.project.todoapp.repository.SemesterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public List<Semester> getAllSemesters() {
        List<Semester> allSemesters  = semesterRepository.findAll();
        log.info("Founded semesters: {}", allSemesters);
        return allSemesters;
    }

    public Semester getSemesterById(Long id) {
        return semesterRepository.getReferenceById(id);
    }
}
