package com.skishop.controller;

import com.skishop.model.dto.SkiAdvisorForm;
import com.skishop.model.dto.SkiAdvisorResult;
import com.skishop.service.SkiAdvisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SkiAdvisorController {
    private static final Logger log = LoggerFactory.getLogger(SkiAdvisorController.class);

    private final SkiAdvisorService skiAdvisorService;

    public SkiAdvisorController(SkiAdvisorService skiAdvisorService) {
        this.skiAdvisorService = skiAdvisorService;
    }

    @GetMapping("/ui/ski-advisor")
    public String showForm(Model model) {
        model.addAttribute("form", new SkiAdvisorForm());
        return "ski-advisor/form";
    }

    @PostMapping("/ui/ski-advisor")
    public String getRecommendations(@ModelAttribute("form") SkiAdvisorForm form, Model model) {
        try {
            SkiAdvisorResult result = skiAdvisorService.getRecommendations(form);
            model.addAttribute("result", result);
            return "ski-advisor/results";
        } catch (Exception e) {
            log.error("Error getting ski advisor recommendations", e);
            model.addAttribute("error", "Unable to generate recommendations: " + e.getMessage());
            model.addAttribute("form", form);
            return "ski-advisor/form";
        }
    }
}
