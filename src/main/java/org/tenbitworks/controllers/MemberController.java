package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.MemberRepository;

@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @RequestMapping(value="/members/{id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    public Member getMemberJson(@PathVariable Long id, Model model){
    	Member member = memberRepository.findOne(id);
        return member;
    }
    
    @RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
    public String getMember(@PathVariable Long id, Model model){
        model.addAttribute("member", memberRepository.findOne(id));
        model.addAttribute("membercount", memberRepository.count());
        return "members";
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public String membersList(Model model){
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("membercount", memberRepository.count());
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
