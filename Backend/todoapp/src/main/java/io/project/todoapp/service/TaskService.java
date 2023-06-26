package io.project.todoapp.service;

import io.project.todoapp.model.Task;
import io.project.todoapp.repository.TaskRepository;
import io.project.todoapp.security.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public void removeTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public void makeTaskDone(Long taskId) {
        Task task = getTask(taskId);
        task.setDone(true);
        saveTask(task);
    }

    public void makeTaskUndone(Long taskId) {
        Task task = getTask(taskId);
        task.setDone(false);
        saveTask(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksForSubject(Long subjectId) {
        return this.findAll().stream().filter(s -> s.getSubjectId() == subjectId).collect(Collectors.toList());
    }

    public void removeTaskFromSubject(String taskName, Long subjectId) {
//        List<Task> tasksToRemove = taskRepository.findAll().stream().filter(task -> task.getName() == taskName && task.getSubjectId() == subjectId).collect(Collectors.toList());
        List<Task> tasksToRemove = this.getAllTasksForSubject(subjectId).stream().filter(task -> Objects.equals(task.getName(),taskName)).collect(Collectors.toList());
        System.out.println(this.getAllTasksForSubject(subjectId));
        tasksToRemove.forEach(task -> this.removeTask(task.getId()));
    }

    private Task getTask(Long taskId) {
        return taskRepository.getReferenceById(taskId);
    }

    private void saveTask(Task task) {
        taskRepository.save(task);
    }
}
