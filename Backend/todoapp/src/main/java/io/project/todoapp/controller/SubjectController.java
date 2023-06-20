package io.project.todoapp.controller;

import io.project.todoapp.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PutMapping("/api/v1/subjects/{subjectId}")
    public void activateOrDeactivateSubject(@PathVariable("subjectId") Long subjectId) {
        subjectService.activateOrDeactivateSubject(subjectId);
    }
}
