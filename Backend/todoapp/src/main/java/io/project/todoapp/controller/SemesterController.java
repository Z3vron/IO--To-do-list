package io.project.todoapp.controller;


import io.project.todoapp.model.Semester;
import io.project.todoapp.service.SemesterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping("/api/v1/init/semesters")
    public List<Semester> getAllSemesters() {
        return semesterService.getAllSemesters();
    }
}
