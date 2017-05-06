package org.tenbitworks.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.MemberRepository;

@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    
    @RequestMapping(value="/members/{id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    public Member getMemberJson(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
    	if (security.isUserInRole("ADMIN")) {
	    	Member member = memberRepository.findOne(id);
	        return member;
    	} else { //TODO Add returning self for non-admins
    		throw new AccessDeniedException("The user is not authorized to view this member");
    	}
    }
    
    @RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
    public String getMember(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
    	if (security.isUserInRole("ADMIN")) {
    		model.addAttribute("members", memberRepository.findAll());
	        model.addAttribute("membercount", memberRepository.count());
	        model.addAttribute("member", memberRepository.findOne(id));
	        return "members";
    	} else { //TODO Add returning self for non-admins
    		throw new AccessDeniedException("The user is not authorized to view this member");
    	}
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public String membersList(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("membercount", memberRepository.count());
        return "members";
    }


    @RequestMapping(value = "/members", method = RequestMethod.POST)
    @ResponseBody
    public UUID saveMember(@RequestBody Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String removeMemeber(@PathVariable UUID id){
        memberRepository.delete(id);
        return id.toString();
    }

}
