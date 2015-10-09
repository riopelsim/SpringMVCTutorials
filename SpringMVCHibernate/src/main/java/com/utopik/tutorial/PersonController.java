package com.utopik.tutorial;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.utopik.tutorial.model.Person;
import com.utopik.tutorial.service.PersonService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class PersonController {

	private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	private PersonService personService;

	@Autowired(required = true)
	@Qualifier(value = "personService")
	public void setPersonService(PersonService ps) {
		this.personService = ps;
	}
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	//public String home(Locale locale, Model model) {
	public String redirect(Locale locale, Model model) {
		logger.debug("Connection! Redirecting to /persons");
		return "redirect:persons";
	}


	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public String listPersons(Model model) {
		logger.debug("Initializing Model Objects");
		model.addAttribute("person", new Person());
		model.addAttribute("listPersons", this.personService.listPersons());
		return "person";
	}

	// For add and update person both
	@RequestMapping(value = "/person/add", method = RequestMethod.POST)
	public String addPerson(@ModelAttribute("person") Person p) {
		if (p.getId() == 0) {
			// new person, add it
			logger.info("Request to add: "+p);
			this.personService.addPerson(p);
		} else {
			// existing person, call update
			logger.info("Request to update: "+p);
			this.personService.updatePerson(p);
		}

		return "redirect:/persons";

	}

	@RequestMapping("/remove/{id}")
	public String removePerson(@PathVariable("id") int id) {
		logger.info("Request to remove person with id: "+id);
		this.personService.removePerson(id);
		return "redirect:/persons";
	}

	@RequestMapping("/edit/{id}")
	public String editPerson(@PathVariable("id") int id, Model model) {
		logger.info("Request to edit person with id: "+id);
		model.addAttribute("person", this.personService.getPersonById(id));
		model.addAttribute("listPersons", this.personService.listPersons());
		return "person";
	}

}
