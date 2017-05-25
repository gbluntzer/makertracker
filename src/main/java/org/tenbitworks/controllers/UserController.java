package org.tenbitworks.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import org.tenbitworks.model.NewUser;

@Controller
public class UserController {

	@Autowired
	UserDetailsManager userRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@RequestMapping(value="/users/{username}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@PreAuthorize("authenticated")
	public UserDetails getUserJson(@PathVariable String username, Model model, SecurityContextHolderAwareRequestWrapper security) {
		try {
			if (security.isUserInRole("ADMIN") || username.equals(security.getUserPrincipal().getName())) {
				return userRepo.loadUserByUsername(username);
			} else {
				throw new AccessDeniedException("Unauthorized");
			}
		} catch (UsernameNotFoundException e) {
			throw new AccessDeniedException("Unauthorized");
		}
	}
	
	@RequestMapping(value="/users", method = RequestMethod.POST, consumes = { "application/json" })
	@ResponseBody
	@Secured({"ROLE_ADMIN"})
	public boolean addUser(@RequestBody @Valid NewUser newUser) {
		System.out.println(newUser);
		
		userRepo.createUser(new User(newUser.getUsername(), passwordEncoder.encode(newUser.getPlainPassword()), getAuthorities(newUser.getRoles())));
		
		return true;
	}
	
	private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
