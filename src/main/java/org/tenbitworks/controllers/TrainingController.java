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
import org.tenbitworks.model.Training;
import org.tenbitworks.repositories.AssetRepository;
import org.tenbitworks.repositories.TrainingRepository;

@Controller
public class TrainingController {

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    AssetRepository assetRepository;

    @RequestMapping(value="/trainings/{id}", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getTraining(@PathVariable Long id, Model model){
        model.addAttribute("trainings", trainingRepository.findAll());
        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("training", trainingRepository.findOne(id));
        return "trainings";
    }

    @RequestMapping(value="/trainings/{id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Training getTrainingJson(@PathVariable Long id, Model model){
        Training training = trainingRepository.findOne(id);
        return training;
    }

    @RequestMapping(value = "/trainings", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String trainingsList(Model model){
        model.addAttribute("trainings", trainingRepository.findAll());
        model.addAttribute("assets", assetRepository.findAll());
        
        return "trainings";
    }

    @RequestMapping(value = "/trainings", method = RequestMethod.POST)
    @ResponseBody
    @Secured({"ROLE_ADMIN"})
    public Long saveTraining(@RequestBody Training training) {
        trainingRepository.save(training);
        return training.getId();
    }

    @RequestMapping(value = "/trainings/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured({"ROLE_ADMIN"})
    public String removeTraining(@PathVariable Long id){
        trainingRepository.delete(id);
        return id.toString();
    }
}
