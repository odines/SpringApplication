package vsg.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import vsg.model.Sample;
import vsg.rest.msg.HelloWorldMessage;
import vsg.rest.service.SampleService;

/**
 * Created by Denis Orlov.
 */
@RestController
public class DemoController {

	private static final String TEMPLATE = "Hello %s!";
	@Autowired
	private SampleService sampleService;

	@RequestMapping("/sample")
	public Sample sample(String sampleName) {
		return sampleService.createSample(sampleName);
	}

	@RequestMapping("/helloWorldNamed")
	private HelloWorldMessage sayHello(@RequestParam(value = "name", defaultValue = "no-name") String templateName) {
		return new HelloWorldMessage(String.format(TEMPLATE, templateName));
	}

	@RequestMapping("/")
	public ModelAndView testModel() {
		ModelAndView view = new ModelAndView();
		view.setViewName("index");
		view.addObject("message", "HelloWorld");
		view.addObject("msg", "Hello msg");
		return view;
	}
}
