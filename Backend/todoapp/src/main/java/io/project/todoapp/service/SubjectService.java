package io.project.todoapp.service;

import io.project.todoapp.model.Subject;
import io.project.todoapp.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    public void activateOrDeactivateSubject(Long subjectId) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            subject.setActive(!subject.isActive());
            subjectRepository.save(subject);
        }
    }
}
