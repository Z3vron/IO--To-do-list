package io.project.todoapp.controller;

import io.project.todoapp.model.Task;
import io.project.todoapp.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/api/v1/tasks/{semesterId}/{subjectId}")
    public void addNewTask(@RequestBody Task task, @PathVariable("semesterId") Long semesterId, @PathVariable("subjectId") Long subjectId) {
        taskService.addNewTask(task, semesterId, subjectId);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/api/v1/tasks/{taskId}")
    public void removeTask(@PathVariable("taskId") Long taskId) {
        taskService.removeTask(taskId);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/api/v1/tasks/done/{taskId}")
    public void makeTaskDone(@PathVariable("taskId") Long taskId) {
        taskService.makeTaskDone(taskId);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/api/v1/tasks/undone/{taskId}")
    public void makeTaskUndone(@PathVariable("taskId") Long taskId) {
        taskService.makeTaskUndone(taskId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/api/v1/tasks/{subjectId}")
    public List<Task> getAllTasksForSubject(@PathVariable("subjectId") Long subjectId) {
        return taskService.getAllTasksForSubject(subjectId);
    }
}
