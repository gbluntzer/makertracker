package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.tenbitworks.model.Greeting;
import org.tenbitworks.repositories.GreetingRepository;

@Controller
public class GreetingTwoController {

    @Autowired
    GreetingRepository greetingRepository;

    @GetMapping("/greetingtwo")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greetingtwo";
    }

    @PostMapping("/savegreeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        greetingRepository.save(greeting);
        model.addAttribute("greeting", new Greeting());
        model.addAttribute("greetings", greetingRepository.findAll());
        return "greetingtwo";
    }

    @RequestMapping(value = "/removegreeting", method = RequestMethod.POST)
    @ResponseBody
    public String removeGreeting(@ModelAttribute Greeting greetingVal, Model model){
        greetingRepository.delete(greetingVal.getId());
        return "redirect:/greetings";
    }

//    @RequestMapping(value = "/savegreeting", method = RequestMethod.POST)
//    @ResponseBody
//    public Long saveGreeting(@RequestBody Greeting greeting) {
//        greetingRepository.save(greeting);
//        return greeting.getId();
//    }

    @RequestMapping(value = "/greetings",method = RequestMethod.GET)
    public String greetingsList(Model model){
        model.addAttribute("greeting", new Greeting());
        model.addAttribute("greetings", greetingRepository.findAll());
        return "greetingtwo";
    }


}
