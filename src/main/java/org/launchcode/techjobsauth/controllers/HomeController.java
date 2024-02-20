package org.launchcode.techjobsauth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.launchcode.techjobsauth.models.Job;
import org.launchcode.techjobsauth.models.User;
import org.launchcode.techjobsauth.models.data.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("jobs", jobRepository.findAll());

        User user = authenticationController.getUserFromSession(request.getSession());
        model.addAttribute("isLoggedIn", user != null);

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(HttpServletRequest request, Model model) {
        model.addAttribute(new Job());

        User user = authenticationController.getUserFromSession(request.getSession());
        model.addAttribute("isLoggedIn", user != null);

        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors,
                                    HttpServletRequest request,
                                    Model model) {

        if (errors.hasErrors()) {
            model.addAttribute(new Job());

            User user = authenticationController.getUserFromSession(request.getSession());
            model.addAttribute("isLoggedIn", user != null);

            return "add";
        }

        jobRepository.save(newJob);
        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model,
                                 @PathVariable int jobId,
                                 HttpServletRequest request) {

        User user = authenticationController.getUserFromSession(request.getSession());
        model.addAttribute("isLoggedIn", user != null);

        Optional<Job> optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = (Job) optJob.get();
            model.addAttribute("job", job);
            return "view";
        } else {
            return "redirect:/";
        }
    }

}