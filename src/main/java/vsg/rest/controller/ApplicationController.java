package vsg.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Denis Orlov.
 */
@Controller
public class ApplicationController {

	@RequestMapping("/")
	public ModelAndView showWelcomePage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("index");
		return view;
	}
}
