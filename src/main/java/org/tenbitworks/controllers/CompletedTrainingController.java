package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.model.CompletedTraining;
import org.tenbitworks.repositories.CompletedTrainingRepository;
import org.tenbitworks.repositories.MemberRepository;
import org.tenbitworks.repositories.TrainingRepository;

@Controller
public class CompletedTrainingController {

    @Autowired
    CompletedTrainingRepository completedTrainingRepository;

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    MemberRepository memberRepository;

    @RequestMapping(value="/completedtrainings/{id}", method=RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getCompletedTrainingHtml(@PathVariable Long id, Model model) {
    	model.addAttribute("trainings", trainingRepository.findAll());
        model.addAttribute("members",memberRepository.findAll());
    	model.addAttribute("completedtrainings", completedTrainingRepository.findAll());
        model.addAttribute("completedtraining", completedTrainingRepository.findOne(id));
        
        return "completedtrainings";
    }

    @RequestMapping(value="/completedtrainings/{id}", method=RequestMethod.GET, produces={"application/json"})
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public CompletedTraining getCompletedTraining(@PathVariable Long id, Model model){
        CompletedTraining completedTraining = completedTrainingRepository.findOne(id);
        
        return completedTraining;
    }

    @RequestMapping(value = "/completedtrainings", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String trainingsList(Model model) {
        model.addAttribute("trainings", trainingRepository.findAll());
        model.addAttribute("members",memberRepository.findAll());
        model.addAttribute("completedtrainings", completedTrainingRepository.findAll());
        
        return "completedtrainings";
    }

    @RequestMapping(value = "/completedtrainings", method = RequestMethod.POST)
    @ResponseBody
    @Secured({"ROLE_ADMIN"})
    public Long saveTraining(@RequestBody CompletedTraining completedTraining) {
        completedTrainingRepository.save(completedTraining);
        
        return completedTraining.getId();
    }

    @RequestMapping(value = "/completedtrainings/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured({"ROLE_ADMIN"})
    public String removeTraining(@PathVariable Long id) {
        completedTrainingRepository.delete(id);
        
        return id.toString();
    }
}
