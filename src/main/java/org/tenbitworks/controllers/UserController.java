package org.tenbitworks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

	@Autowired
	UserDetailsManager userRepo;
	
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
}
