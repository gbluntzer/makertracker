package org.tenbitworks.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.model.CompletedTraining;
import org.tenbitworks.model.Member;
import org.tenbitworks.model.Training;
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
    public String product(@PathVariable Long id, Model model) {
    	Iterable<Training> trainingIterable =  trainingRepository.findAll();
        Iterable<Member> memberIterable =  memberRepository.findAll();
        Iterable<CompletedTraining> completedTrainingIterable =  completedTrainingRepository.findAll();
        //TODO replace this with a Join SQL for performance
        for(CompletedTraining completedTraining : completedTrainingIterable){
            UUID memberId = completedTraining.getMemberId();
            long trainingId = completedTraining.getTrainingId();
            for(Member member : memberIterable){
                UUID uuid = member.getId();
                if(memberId.equals(uuid)){
                    completedTraining.setMemberName(member.getMemberName());
                    break;
                }
            }

            for(Training training : trainingIterable){
                long tid = training.getId();
                if(trainingId == tid){
                    completedTraining.setTrainingTitle(training.getTitle());
                    break;
                }
            }
        }
        model.addAttribute("trainings", trainingIterable);
        model.addAttribute("members",memberIterable);
        model.addAttribute("completedtrainings", completedTrainingRepository.findAll());
        model.addAttribute("completedtraining", completedTrainingRepository.findOne(id));
        return "completedtrainings";
    }

    @RequestMapping(value="/completedtrainings/{id}", method=RequestMethod.GET, produces={"application/json"})
    @ResponseBody
    public CompletedTraining getCompletedtraining(@PathVariable Long id, Model model){
        CompletedTraining completedTraining = completedTrainingRepository.findOne(id);
        return completedTraining;
    }

    @RequestMapping(value = "/completedtrainings", method = RequestMethod.GET)
    public String trainingsList(Model model) {
        Iterable<Training> trainingIterable =  trainingRepository.findAll();
        Iterable<Member> memberIterable =  memberRepository.findAll();
        Iterable<CompletedTraining> completedTrainingIterable =  completedTrainingRepository.findAll();
        //TODO replace this with a Join SQL for performance
        for(CompletedTraining completedTraining : completedTrainingIterable){
            UUID memberId = completedTraining.getMemberId();
            long trainingId = completedTraining.getTrainingId();
            for(Member member : memberIterable){
                UUID id = member.getId();
                if(memberId.equals(id)){
                    completedTraining.setMemberName(member.getMemberName());
                    break;
                }
            }

            for(Training training : trainingIterable){
                long id = training.getId();
                if(trainingId == id){
                    completedTraining.setTrainingTitle(training.getTitle());
                    break;
                }
            }
        }
        model.addAttribute("trainings", trainingIterable);
        model.addAttribute("members",memberIterable);
        model.addAttribute("completedtrainings", completedTrainingRepository.findAll());

        return "completedtrainings";
    }

    @RequestMapping(value = "/completedtrainings", method = RequestMethod.POST)
    @ResponseBody
    public Long saveTraining(@RequestBody CompletedTraining completedTraining) {
        completedTrainingRepository.save(completedTraining);
        return completedTraining.getId();
    }

    @RequestMapping(value = "/completedtrainings/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String removeMemeber(@PathVariable Long id) {
        completedTrainingRepository.delete(id);
        return id.toString();
    }


}
