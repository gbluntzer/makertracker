package org.tenbitworks.controllers;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.tenbitworks.repositories.UserRepository;

@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
	UserRepository userRepository;
    
    @RequestMapping(value="/members/{id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'Member', 'read')")
    public Member getMemberJson(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
    	return memberRepository.findOne(id);
    }
    
    @RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'Member', 'read')")
    public String getMember(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
    	model.addAttribute("membercount", memberRepository.count());
    	
    	if (security.isUserInRole("ADMIN")) {
    		model.addAttribute("members", memberRepository.findAll());
	    } else {
    		model.addAttribute("members", Arrays.asList(new Member[] { memberRepository.findOne(id) }));
    	}
    	
    	model.addAttribute("member", memberRepository.findOne(id));
    	return "members";
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String membersList(Model model, SecurityContextHolderAwareRequestWrapper security) {
    	if (security.isUserInRole("ADMIN")) {
    		model.addAttribute("members", memberRepository.findAll());
    	} else {
    		org.tenbitworks.model.User user = userRepository.findOne(security.getUserPrincipal().getName());
    		Member m = memberRepository.findOneByUser(user);
    		if (m != null) {
    			model.addAttribute("members", Arrays.asList(new Member[] { m }));
    		}
    	}
    	
        model.addAttribute("membercount", memberRepository.count());
        return "members";
    }

    @RequestMapping(value = "/members", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public UUID saveMember(@RequestBody Member member) {
    	if (member.getId() != null) {
    		Member oldMember = memberRepository.findOne(member.getId());
    		member.setUser(oldMember.getUser());
    	}
    	
        memberRepository.save(member);
        return member.getId();
    }
    
    @RequestMapping(value = "/members/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'Member', 'write')")
    public ResponseEntity<String> updateMember(@PathVariable UUID id, @RequestBody Member updatedMember) {
		Member member = memberRepository.findOne(id);
		
		if (member == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		//Copy selected fields to existing member for update. We don't want a user to just update everything
		member.setAddress(updatedMember.getAddress());
		member.setDescription(updatedMember.getDescription());
		member.setEmail(updatedMember.getEmail());
		member.setPhoneNumber(updatedMember.getPhoneNumber());
		member.setZipCode(updatedMember.getZipCode());
		
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public String removeMember(@PathVariable UUID id){
        memberRepository.delete(id);
        return id.toString();
    }
    
    
}
