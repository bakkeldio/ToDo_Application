package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.service.EmailService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import com.example.demo.tools.TaskStateComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();
        User currentUser = userService.findByUserName(loggedUserName);
        return currentUser;
    }

    @GetMapping("/")
    public String showHome(Model model) {

        User currenUser = this.getCurrentUser();
        List<Task> tasks = currenUser.getTasks();
        Collections.sort(tasks, new TaskStateComparator());
        Task task = new Task();
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", task);

        return "home";
    }

    @PostMapping("/addThingToDoAction")
    public String addThingToDoAction(@ModelAttribute("thindToDo") Task task) {

        User currentUser = this.getCurrentUser();
        currentUser.add(task);
        taskService.save(task);

        return "redirect:/";
    }

    @GetMapping("/setStateToDoneAction")
    public String setStateToDoneAction(@RequestParam("taskId") int taskId){

        Task task = taskService.findById(taskId);
        task.setState(1);
        taskService.save(task);

        return "redirect:/";
    }

    @GetMapping("/setStateToNotDoneAction")
    public String setStateToNotDoneAction(@RequestParam("taskId") int taskId){

        Task task = taskService.findById(taskId);
        task.setState(0);
        taskService.save(task);

        return "redirect:/";
    }

    @GetMapping("/sendEmail")
    public String sendEmail(){

        String currentUserEmail = this.getCurrentUser().getEmail();
        StringBuilder message = new StringBuilder();
        message.append("Here are the tasks you still need to complete: ");
        List<Task> tasksToDo = this.getCurrentUser().getTasks();

        for(Task x : tasksToDo) {
            if(x.getState()==0)
                message.append(x.getDescription() + ", ");
        }

        emailService.sendSimpleMessage(currentUserEmail, "ToDoApp - tasks to be performed", message.toString());

        return "redirect:/";
    }

    @GetMapping("/showAddThingToDoPage")
    public String showAddThingToDoPage(Model model) {

        Task task = new Task();
        model.addAttribute("task", task);

        return "/task/add-task-page";
    }

    @GetMapping("/showUpdateTeskPage")
    public String showUpdateTeskPage(@RequestParam("taskId") int taskId, Model model){

        Task taskToUpdate = taskService.findById(taskId);
        model.addAttribute("task", taskToUpdate);

        return "/task/add-task-page";
    }

    @GetMapping("/showDoneTasks")
    public String showDoneTasks(Model model){

        Task task = new Task();
        List<Task> tasks = taskService.findAllByStateAndUser(1, this.getCurrentUser());
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", task);

        return "home";
    }

    @GetMapping("/showNotDoneTasks")
    public String showNotDoneTasks(Model model){

        Task task = new Task();
        List<Task> tasks = taskService.findAllByStateAndUser(0, this.getCurrentUser());
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", task);

        return "home";
    }

    @GetMapping("/removeDoneTasks")
    public String removeDoneTasks(Model model) {

        taskService.deleteAllByState(1);
        User currenUser = this.getCurrentUser();
        List<Task> tasks = currenUser.getTasks();
        Task task = new Task();
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", task);

        return "home";
    }

}

