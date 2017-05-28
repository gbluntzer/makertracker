package org.tenbitworks.controllers;

import java.util.Arrays;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
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
    
    @PersistenceContext
	EntityManager em;
    
    @RequestMapping(value="/members/{id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Member getMemberJson(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
    	Member member = null;
    	if (security.isUserInRole("ADMIN")) {
	    	member = memberRepository.findOne(id);
    	} else {
    		String username = security.getUserPrincipal().getName();
    		org.tenbitworks.model.User user = userRepository.findOne(username);
    		member = em.createQuery("select m from Member m where m.user = :user", Member.class) //TODO Move to named query
					.setParameter("user", user)
					.getSingleResult();
    		
    		if (member == null || !id.equals(member.getId())) {
    			throw new AccessDeniedException("Access Denied");
    		}
    	}
    	
    	return member;
    }
    
    @RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getMember(@PathVariable UUID id, Model model, SecurityContextHolderAwareRequestWrapper security) {
    	model.addAttribute("membercount", memberRepository.count());
    	
    	if (security.isUserInRole("ADMIN")) {
    		model.addAttribute("members", memberRepository.findAll());
	        model.addAttribute("member", memberRepository.findOne(id));
    	} else {
    		String username = security.getUserPrincipal().getName();
    		org.tenbitworks.model.User user = userRepository.findOne(username);
    		Member member = em.createQuery("select m from Member m where m.user = :user", Member.class)
					.setParameter("user", user)
					.getSingleResult();
    		model.addAttribute("members", Arrays.asList(new Member[] { member }));
    		model.addAttribute("member", member);
    		
    		if (member == null || !id.equals(member.getId())) {
    			throw new AccessDeniedException("Access Denied");
    		}
    	}
    	return "members";
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String membersList(Model model, SecurityContextHolderAwareRequestWrapper security) {
    	if (security.isUserInRole("ADMIN")) {
    		model.addAttribute("members", memberRepository.findAll());
    	} else {
    		try {
	    		String username = security.getUserPrincipal().getName();
	    		org.tenbitworks.model.User user = userRepository.findOne(username);
	    		Member m = em.createQuery("select m from Member m where m.user = :user", Member.class)
						.setParameter("user", user)
						.getSingleResult();
	    		model.addAttribute("members", Arrays.asList(new Member[] { m }));
    		} catch (NoResultException e) {
    			// 
    		}
    	}
    	
        model.addAttribute("membercount", memberRepository.count());
        return "members";
    }

    @RequestMapping(value = "/members", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public UUID saveMember(@RequestBody Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public String removeMember(@PathVariable UUID id){
        memberRepository.delete(id);
        return id.toString();
    }
}
