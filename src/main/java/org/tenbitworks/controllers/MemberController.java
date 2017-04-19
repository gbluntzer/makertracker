package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.MemberRepository;

@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;


    @RequestMapping("/member/{id}")
    public String product(@PathVariable Long id, Model model){
        model.addAttribute("member", memberRepository.findOne(id));
        return "member";
    }
    @RequestMapping("/getmember")
    @ResponseBody
    public Member getMember(@RequestParam Long Id, Model model){
        Member member = memberRepository.findOne(Id);
        return member;
    }

    @RequestMapping(value = "/members",method = RequestMethod.GET)
    public String membersList(Model model){
        model.addAttribute("members", memberRepository.findAll());
        return "members";
    }

    @RequestMapping(value = "/savemember", method = RequestMethod.POST)
    @ResponseBody
    public Long saveMember(@RequestBody Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @RequestMapping(value = "/removemember", method = RequestMethod.POST)
    @ResponseBody
    public String removeMemeber(@RequestParam Long Id){
        memberRepository.delete(Id);
        return Id.toString();
    }

}
