package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.tenbitworks.model.Asset;
import org.tenbitworks.model.Member;
import org.tenbitworks.model.Training;
import org.tenbitworks.repositories.AssetRepository;
import org.tenbitworks.repositories.TrainingRepository;

import java.util.List;

@Controller
public class TrainingController {

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    AssetRepository assetRepository;


    @RequestMapping("/training/{id}")
    public String product(@PathVariable Long id, Model model){
        model.addAttribute("training", trainingRepository.findOne(id));
        return "training";
    }

    @RequestMapping("/gettraining")
    @ResponseBody
    public Training getTraining(@RequestParam Long Id, Model model){
        Training training = trainingRepository.findOne(Id);
        return training;
    }

    @RequestMapping(value = "/trainings",method = RequestMethod.GET)
    public String trainingsList(Model model){
        Iterable<Training> trainingIterable = trainingRepository.findAll();
        Iterable<Asset> assetIterable = assetRepository.findAll();

        for(Training training : trainingIterable){
            long assetId = training.getAssetId();

            for(Asset asset : assetIterable){
                long id = asset.getId();
                String assetTitle = asset.getTitle();
                if(assetId == id){
                    training.setAssetTitle(assetTitle);
                    break;
                }
            }
        }

        model.addAttribute("trainings",trainingIterable );

        model.addAttribute("assets",assetIterable);
        return "trainings";
    }

    @RequestMapping(value = "/savetraining", method = RequestMethod.POST)
    @ResponseBody
    public Long saveTraining(@RequestBody Training training) {
        trainingRepository.save(training);
        return training.getId();
    }

    @RequestMapping(value = "/removetraining", method = RequestMethod.POST)
    @ResponseBody
    public String removeMemeber(@RequestParam Long Id){
        trainingRepository.delete(Id);
        return Id.toString();
    }



}
