package io.project.todoapp;

import io.project.todoapp.model.Semester;
import io.project.todoapp.model.Subject;
import io.project.todoapp.model.Task;
import io.project.todoapp.repository.TaskRepository;
import io.project.todoapp.security.user.Role;
import io.project.todoapp.security.user.User;
import io.project.todoapp.service.TaskService;
import io.project.todoapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests
{
    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testAddNewTask()
    {
        Subject subject1 = new Subject(0L, "Matematyka", 5, new ArrayList<Task>());
        Subject subject2 = new Subject(1L, "Inżynieria Obliczeniowa", 4, new ArrayList<Task>());
        Subject subject3 = new Subject(2L, "Bazy Danych", 6, new ArrayList<Task>());
        ArrayList<Subject> subjects = new ArrayList<>();
        Collections.addAll(subjects, subject1, subject2, subject3);

        Semester semester1 = new Semester(0L, 1, 2023, subjects, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-06-30"));
        Semester semester2 = new Semester(1L, 2, 2023, subjects, LocalDate.parse("2023-07-01"), LocalDate.parse("2023-12-31"));
        Semester semester3 = new Semester(2L, 3, 2023, subjects, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-06-30"));

        User user1 = new User(0L, "First", "Last", "student0@student.agh.edu.pl", "Qwerty123", semester1, Role.STUDENT);
        User user2 = new User(1L, "First", "Last", "student1@student.agh.edu.pl", "Qwerty123", semester1, Role.STUDENT);
        User user3 = new User(2L, "First", "Last", "student2@student.agh.edu.pl", "Qwerty123", semester1, Role.STUDENT);
        User user4 = new User(3L, "First", "Last", "student3@student.agh.edu.pl", "Qwerty123", semester1, Role.STUDENT);
        User user5 = new User(4L, "First", "Last", "student4@student.agh.edu.pl", "Qwerty123", semester1, Role.STUDENT);

        ArrayList<User> users = new ArrayList<>();
        Collections.addAll(users, user1, user2, user3, user4, user5);

        when(userService.findAll()).thenReturn(users);

        taskService.addNewTask(new Task(0L, 0L, 0L, "Kolokwium", "Czas umierać", false), 1L, 1L);

        verify(taskRepository, times(0)).save(any());

        User user6 = new User(5L, "First", "Last", "student2@student.agh.edu.pl", "Qwerty123", semester2, Role.STUDENT);
        User user7 = new User(6L, "First", "Last", "student3@student.agh.edu.pl", "Qwerty123", semester2, Role.STUDENT);
        User user8 = new User(7L, "First", "Last", "student4@student.agh.edu.pl", "Qwerty123", semester2, Role.STUDENT);
        User user9 = new User(8L, "First", "Last", "student2@student.agh.edu.pl", "Qwerty123", semester3, Role.STUDENT);
        User user10 = new User(9L, "First", "Last", "student3@student.agh.edu.pl", "Qwerty123", semester3, Role.STUDENT);
        User user11 = new User(10L, "First", "Last", "student4@student.agh.edu.pl", "Qwerty123", semester3, Role.STUDENT);

        Collections.addAll(users, user6, user7, user8, user9, user10, user11);

        taskService.addNewTask(new Task(0L, 0L, 0L, "Kolokwium", "Czas umierać", false), 1L, 1L);

        verify(taskRepository, times(3)).save(any());

        //taskService.addNewTask(new Task(0L, 0L, 0L, "Kolokwium", "Czas umierać", false), 1L, 3L);

        //verify(taskRepository, times(3)).save(any());
    }
}
