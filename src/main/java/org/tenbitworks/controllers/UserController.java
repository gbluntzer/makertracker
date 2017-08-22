package org.tenbitworks.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

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
import org.tenbitworks.dto.ChangePasswordDTO;
import org.tenbitworks.dto.NewUserDTO;
import org.tenbitworks.dto.UserDTO;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.MemberRepository;
import org.tenbitworks.repositories.UserRepository;

@Controller
public class UserController {
	private static final String CLAZZ = UserController.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	@Autowired
	UserDetailsManager userDetailsManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
    MemberRepository memberRepository;
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@Secured({"ROLE_ADMIN"})
	public String usersList(Model model) {
		model.addAttribute("members",memberRepository.findAllByUser(null));
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
		
		Member member = memberRepository.findOneByUser(user);
		if (member != null) {
			userDTO.setMemberName(member.getMemberName());
			userDTO.setMemberId(member.getId());
		}
		
		return userDTO;
	}

	@RequestMapping(value="/users", method = RequestMethod.POST, consumes = { "application/json" })
	@ResponseBody
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<String> addUser(@RequestBody @Valid NewUserDTO newUser, SecurityContextHolderAwareRequestWrapper security) {
		if (!userDetailsManager.userExists(newUser.getUsername())) {
			Member member = null;
			if (newUser.getMemberId() != null) {
				member = memberRepository.findOne(newUser.getMemberId());
				if (member.getUser() != null) {
					return new ResponseEntity<String>("Invalid Member selection", HttpStatus.BAD_REQUEST);
				}
			}
			List<String> validRoles = new ArrayList<>(); //TODO Creates Roles checkbox dropdown on users page
			for (String role : newUser.getRoles()) {
				if (security.isUserInRole(role) && !validRoles.contains(role)) {
					validRoles.add(role);
				} else {
					LOGGER.severe("Attempt to add invalid role " + role + " by user " + security.getUserPrincipal().getName());
				}
			}
			
			if (validRoles == null || validRoles.isEmpty()) {
				return new ResponseEntity<String>("Cannot create user", HttpStatus.BAD_REQUEST);
			}
			
			userDetailsManager.createUser(new User(newUser.getUsername(), passwordEncoder.encode(newUser.getPlainPassword()), getAuthorities(validRoles)));
			
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
	
	@RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured({"ROLE_ADMIN"})
    public String deleteUser(@PathVariable String username) {
		if (userDetailsManager.userExists(username)) {
			org.tenbitworks.model.User user = userRepository.findOne(username);
			Member m = memberRepository.findOneByUser(user);
			
			if (m != null) {
				m.setUser(null);
				memberRepository.save(m);
			}
			
			userDetailsManager.deleteUser(username);
		}
        
        return username;
    }
	
	@RequestMapping(value="/users/{username}/password", method = RequestMethod.DELETE, produces = { "application/json" })
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')") //should current user be able to reset their own pass, or maybe just a forgot pass function for this?
	public ResponseEntity<String> resetPasswordForUser(@PathVariable String username, SecurityContextHolderAwareRequestWrapper security) {
		org.tenbitworks.model.User user = userRepository.findOne(username);
		
		String newPasswordPlain = UUID.randomUUID().toString();
		String newPassword = passwordEncoder.encode(newPasswordPlain);
		
		user.setPassword(newPassword);
		userRepository.save(user);
		
		sendPasswordResetNotification(username, newPasswordPlain);
		
		return new ResponseEntity<String>("Password Reset", HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/{username}/password", method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
	public ResponseEntity<String> changePassword(@PathVariable String username, @RequestBody @Valid ChangePasswordDTO passwordDTO, SecurityContextHolderAwareRequestWrapper security) {
		org.tenbitworks.model.User user = userRepository.findOne(username);
		
		boolean passwordChanged = false;
		if (security.isUserInRole("ADMIN")) {
			user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
			passwordChanged = true;
		} else if (passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
			passwordChanged = true;
		}

		if (passwordChanged) {
			userRepository.save(user);
			sendPasswordChangedNotification(username);
			
			return new ResponseEntity<String>("Password Changed", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Cannot change password", HttpStatus.FORBIDDEN);
		}
	}

	private void sendPasswordChangedNotification(String username) {
		// TODO Auto-generated method stub
		
	}
	
	private void sendPasswordResetNotification(String username, String newPasswordPlain) {
		// TODO Auto-generated method stub
		
	}
}
