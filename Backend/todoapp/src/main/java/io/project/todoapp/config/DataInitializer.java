package io.project.todoapp.config;

import io.project.todoapp.model.Semester;
import io.project.todoapp.model.Subject;
import io.project.todoapp.model.Task;
import io.project.todoapp.repository.SemesterRepository;
import io.project.todoapp.repository.TaskRepository;
import io.project.todoapp.security.auth.AuthenticationService;
import io.project.todoapp.security.auth.RegisterClassPresidentRequest;
import io.project.todoapp.security.auth.RegisterRequest;
import io.project.todoapp.security.user.Role;
import io.project.todoapp.security.user.User;
import io.project.todoapp.security.user.UserRepository;
import io.project.todoapp.service.TaskService;
import io.project.todoapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;
    private final AuthenticationService authenticationService;
    private final TaskService taskService;
  
    @Override
    public void run(String... args) throws Exception {
/*
        List<Subject> subjectsForFirstSemester2020 = List.of(Subject.builder()
                        .name("Programowanie w języku Java")
                        .ectsPoints(5)
                        .build(),
                Subject.builder()
                        .name("Programowanie w języku C++")
                        .ectsPoints(7)
                        .build(),
                Subject.builder()
                        .name("Algorytmy i struktury danych")
                        .ectsPoints(5)
                        .build(),
                Subject.builder()
                        .name("Bazy danych")
                        .ectsPoints(6)
                        .build()
        );



        Semester semesterFirst2020 = Semester.builder()
                .year(2020)
                .startDate(LocalDate.of(2020, 10, 1))
                .endDate(LocalDate.of(2021, 2, 20))
                .subjects(subjectsForFirstSemester2020)
                .number(1)
                .build();



        List<Subject> subjectsForSecondSemester2020 = List.of(
                Subject.builder()
                        .name("Grafika komputerowa")
                        .ectsPoints(6)
                        .build(),
                Subject.builder()
                        .name("Systemy operacyjne")
                        .ectsPoints(7)
                        .build(),
                Subject.builder()
                        .name("Sieci komputerowe")
                        .ectsPoints(6)
                        .build(),
                Subject.builder()
                        .name("Programowanie obiektowe")
                        .ectsPoints(5)
                        .build()
        );

        Semester semesterSecond2020 = Semester.builder()
                .year(2020)
                .startDate(LocalDate.of(2021, 3, 1))
                .endDate(LocalDate.of(2021, 6, 20))
                .subjects(subjectsForSecondSemester2020)
                .number(2)
                .build();

        semesterRepository.save(semesterFirst2020);
        semesterRepository.save(semesterSecond2020);

        initClassPresident(semesterFirst2020);

        RegisterRequest registerRequest1 = RegisterRequest.builder()
                .firstName("Jan")
                .lastName("Kowal")
                .email("test1@gmail.com")
                .password("qwerty")
                .semesterId(1L)
                .build();
        RegisterRequest registerRequest2 = RegisterRequest.builder()
                .firstName("Grzegorz")
                .lastName("Brzeczyszczykiewicz")
                .email("test2@gmail.com")
                .password("qwerty")
                .semesterId(1L)
                .build();

        authenticationService.register(registerRequest1);
        authenticationService.register(registerRequest2);

        Task task1 = Task.builder()
                .name("Kolokwium zaliczeniowe")
                .description("bla bla bla bla")
                .build();
        Task task2 = Task.builder()
                .name("Projekt")
                .description("bla bla bla bla")
                .build();
        taskService.addNewTask(task1, 1L, 1L);
        taskService.addNewTask(task2, 1L, 2L);

        semesterRepository.save(semesterSecond2020);

        taskService.addNewTask(Task.builder().name("Pierwsze kolokwium").description("asdf").done(false).build(),1L,1L);
        taskService.addNewTask(Task.builder().name("Drugie kolokwium").description("asdf").done(true).build(),1L,1L);
        taskService.addNewTask(Task.builder().name("Egzamin").description("asdf").done(true).build(),1L,1L);
        taskService.addNewTask(Task.builder().name("Pierwsze kolokwium").description("asdf").done(false).build(),1L,2L);
        taskService.addNewTask(Task.builder().name("Drugie kolokwium").description("asdf").done(false).build(),1L,2L);
        taskService.addNewTask(Task.builder().name("Egzamin").description("asdf").done(false).build(),1L,2L);
        taskService.addNewTask(Task.builder().name("Pierwsze kolokwium").description("asdf").done(false).build(),1L,3L);
        taskService.addNewTask(Task.builder().name("Drugie kolokwium").description("asdf").done(true).build(),1L,3L);
        taskService.addNewTask(Task.builder().name("Egzamin").description("asdf").done(false).build(),1L,3L);
        taskService.addNewTask(Task.builder().name("Pierwsze kolokwium").description("asdf").done(false).build(),1L,4L);
        taskService.addNewTask(Task.builder().name("Drugie kolokwium").description("asdf").done(false).build(),1L,4L);
//        taskService.addNewTask(Task.builder().name("Egzamin").description("asdf").done(false).build(),1L,4L);

    }

    private void initClassPresident(Semester semester) {

        authenticationService.registerClassPresident(
                RegisterClassPresidentRequest.builder()
                        .email("a")
                        .password("a")
                        .firstName("a")
                        .lastName("a")
                        .semester(semester)
                        .build());

//          authenticationService.registerClassPresident(
//                RegisterClassPresidentRequest.builder()
//                        .email("class_president_2020_1@poczta.pl")
//                        .password("qwerty1234")
//                        .firstName("Jan")
//                        .lastName("Kowalski")
//                        .semester(semester)
//                .build());
*/
    }
}
