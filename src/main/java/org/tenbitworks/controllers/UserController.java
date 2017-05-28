package org.tenbitworks.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.dto.NewUserDTO;
import org.tenbitworks.dto.UserDTO;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.MemberRepository;
import org.tenbitworks.repositories.UserRepository;

@Controller
public class UserController {

	@Autowired
	UserDetailsManager userDetailsManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
    MemberRepository memberRepository;
	
	@PersistenceContext
	EntityManager em;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@Secured({"ROLE_ADMIN"})
	public String usersList(Model model) {
		model.addAttribute("members",memberRepository.findAll());
		model.addAttribute("users", userRepository.findAll());
		model.addAttribute("usercount", userRepository.count());
		return "users";
	}

	@RequestMapping(value="/users/{username}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
	public String getUser(@PathVariable String username, Model model, SecurityContextHolderAwareRequestWrapper security) {
		if (security.isUserInRole("ADMIN")) {
			model.addAttribute("members",memberRepository.findAll());
			model.addAttribute("users", userRepository.findAll());
			model.addAttribute("usercount", userRepository.count());
		}

		model.addAttribute("user", loadUserDTO(username, security));
		return "users";
	}

	@RequestMapping(value="/users/{username}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
	public UserDTO getUserJson(@PathVariable String username, Model model, SecurityContextHolderAwareRequestWrapper security) {
		UserDTO userDTO = loadUserDTO(username, security);

		return userDTO;
	}

	private UserDTO loadUserDTO(String username, SecurityContextHolderAwareRequestWrapper security) {
		List<String> authoritiesList = new ArrayList<>();
		
		if (security.isUserInRole("ADMIN")) {
			Collection<? extends GrantedAuthority> authorities = userDetailsManager.loadUserByUsername(username).getAuthorities();
			authorities.forEach(authority -> {
				authoritiesList.add(authority.getAuthority());
			});
		}
		
		org.tenbitworks.model.User user = userRepository.findOne(username);
		UserDTO userDTO = new UserDTO(user, authoritiesList);
		
		try {
			Member m = em.createQuery("select m from Member m where m.user = :user", Member.class)
					.setParameter("user", user)
					.getSingleResult();
			userDTO.setMemberName(m.getMemberName());
			userDTO.setMemberId(m.getId());
		} catch (NoResultException e) {
			// Do nothing
		}
		return userDTO;
	}

	@RequestMapping(value="/users", method = RequestMethod.POST, consumes = { "application/json" })
	@ResponseBody
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<String> addUser(@RequestBody @Valid NewUserDTO newUser) {
		if (!userDetailsManager.userExists(newUser.getUsername())) {
			Member member = null;
			if (newUser.getMemberId() != null) {
				member = memberRepository.findOne(newUser.getMemberId());
				if (member.getUser() != null) {
					return new ResponseEntity<String>("Invalid Member selection", HttpStatus.BAD_REQUEST);
				}
			}
			userDetailsManager.createUser(new User(newUser.getUsername(), passwordEncoder.encode(newUser.getPlainPassword()), getAuthorities(newUser.getRoles())));
			
			if (member != null) {
				org.tenbitworks.model.User newUserInRepo = userRepository.findOne(newUser.getUsername());
				member.setUser(newUserInRepo);
				memberRepository.save(member);
			}
		} else {
			return new ResponseEntity<String>("Cannot create user", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>("{\"status\":\"User Created\"}", HttpStatus.OK);
	}

	private static List<GrantedAuthority> getAuthorities (List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
