package org.tenbitworks.controllers;

import java.util.Arrays;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tenbitworks.model.News;
import org.tenbitworks.repositories.NewsRepository;
import org.tenbitworks.repositories.UserRepository;

@Controller
public class NewsController {

	@Autowired
	NewsRepository newsRepository;
	
	@Autowired
	UserRepository userRepository;

	@RequestMapping(value={ "/", "/news" }, method = RequestMethod.GET)
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public String getNewsPage(Model model) {
		model.addAttribute("news", newsRepository.findAll(new Sort(Direction.DESC, "createdAt")));

		return "news";
	}
	
	@RequestMapping(value="/news", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public Iterable<News> getNewsJson(Model model) {
		return newsRepository.findAll();
	}
	
	@RequestMapping(value = "/news", method = RequestMethod.POST)
	@ResponseBody
	@Secured("ROLE_ADMIN")
	public Long postNews(@RequestBody News newsItem, SecurityContextHolderAwareRequestWrapper security) {
		newsItem.setUser(userRepository.findOne(security.getRemoteUser()));
		newsItem.setCreatedAt(Calendar.getInstance().getTime());
		
		newsRepository.save(newsItem);
		
		System.out.println("USERNAME: " + security.getRemoteUser());
		
		return newsItem.getId();
	}

	@RequestMapping(value = "/news/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@Secured("ROLE_ADMIN")
	public String deleteNews(@PathVariable Long id){
		newsRepository.delete(id);
		return id.toString();
	}

	@RequestMapping(value="/news/{id}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public News getSingleNewsJson(@PathVariable Long id, Model model) {
		return newsRepository.findOne(id);
	}
	
	@RequestMapping(value = "/news/{id}", method = RequestMethod.GET)
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public String getSingleNewsPage(@PathVariable Long id, Model model) {
		model.addAttribute("news", Arrays.asList(newsRepository.findOne(id)));

		return "news";
	}
}
