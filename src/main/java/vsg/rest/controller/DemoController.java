package vsg.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import vsg.rest.msg.HelloWorldMessage;

/**
 * Created by Denis Orlov.
 */
@RestController
public class DemoController {

	private static final String TEMPLATE = "Hello %s!";

	@RequestMapping("/helloWorld")
	private HelloWorldMessage sayHello() {
		return new HelloWorldMessage("Hello SpringApplication");
	}

	@RequestMapping("/helloWorldNamed")
	private HelloWorldMessage sayHello(@RequestParam(value = "name", defaultValue = "no-name") String templateName) {
		return new HelloWorldMessage(String.format(TEMPLATE, templateName));
	}

	@RequestMapping("/")
	public ModelAndView testModel() {
		ModelAndView view = new ModelAndView();
		view.setViewName("test");
		view.addObject("message", "HelloWorld");
		view.addObject("msg", "Hello msg");
		return view;
	}
}
