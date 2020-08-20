package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.repo.ProjectRepo;
import ru.scadarnull.project_manager.repo.TaskRepo;

import java.util.List;

@Service
public class TaskService {
    private TaskRepo taskRepo;
    private ProjectRepo projectRepo;
    @Autowired
    public TaskService(TaskRepo taskRepo, ProjectRepo projectRepo) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
    }

    public List<Task> getAll() {
        return taskRepo.findAll();
    }

    public boolean addTask(Task task, String project) {
        if(taskIsExist(task)){
            return false;
        }
        task.setProject(projectRepo.findByName(project));
        taskRepo.save(task);
        return true;
    }

    public boolean taskIsExist(Task task) {
        Task taskFromDb = taskRepo.findByName(task.getName());
        return taskFromDb != null;
    }

    public void deleteTask(Task task) {
        taskRepo.delete(task);
    }

    public void updateTask(Task task) {
        taskRepo.save(task);
    }
}
