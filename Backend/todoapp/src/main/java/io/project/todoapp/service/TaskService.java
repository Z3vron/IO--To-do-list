package io.project.todoapp.service;

import io.project.todoapp.model.Task;
import io.project.todoapp.repository.TaskRepository;
import io.project.todoapp.security.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;

    public void addNewTask(Task task, Long semesterId, Long subjectId) {

        List<User> allStudentsBySemesterId = userService.findAll().stream().filter(e -> e.getActualSemester().getId().equals(semesterId)).toList();
        Task.TaskBuilder taskBuilder = Task.builder()
                .name(task.getName())
                .subjectId(subjectId)
                .done(false)
                .description(task.getDescription());

        for (User user : allStudentsBySemesterId) {
            Task newTask = taskBuilder
                    .userId(user.getId())
                    .build();
            taskRepository.save(newTask);
        }

    }

    public void makeTaskDone(Long taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        task.setDone(true);
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }
}
