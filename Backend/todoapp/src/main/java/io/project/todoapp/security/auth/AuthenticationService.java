package io.project.todoapp.security.auth;

import io.project.todoapp.model.Semester;
import io.project.todoapp.model.Subject;
import io.project.todoapp.model.Task;
import io.project.todoapp.security.config.JwtService;
import io.project.todoapp.security.user.Role;
import io.project.todoapp.security.user.User;
import io.project.todoapp.security.user.UserRepository;
import io.project.todoapp.service.SemesterService;
import io.project.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.project.todoapp.model.Subject.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SemesterService semesterService;
    private final TaskService taskService;

    public AuthenticationResponse register(RegisterRequest request) {

        Semester foundedSemester = semesterService.getSemesterById(request.getSemesterId());
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .actualSemester(foundedSemester)
                .build();
        
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void registerClassPresident(RegisterClassPresidentRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .actualSemester(request.getSemester())
                .build();

        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        List<Task> allTasksByUserId = taskService.findAllByUserId(user.getId());
        List<Subject> subjects = user.getActualSemester().getSubjects();
        List<Subject> mappedSubjects = new ArrayList<>();

        for(Subject subject : subjects) {
            List<Task> mappedTasks = new ArrayList<>();
            for (Task task : allTasksByUserId) {
                if (subject.getId().equals(task.getSubjectId())) {
                    mappedTasks.add(task);
                }

            }
            subject.setTasks(mappedTasks);
            mappedSubjects.add(subject);
        }

        String jwtToken = jwtService.generateToken(user);
        Semester actualSemester = user.getActualSemester();
        actualSemester.setSubjects(mappedSubjects);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .semester(actualSemester)
                .build();
    }
}
