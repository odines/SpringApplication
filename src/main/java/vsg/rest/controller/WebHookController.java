package vsg.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Denis Orlov.
 */
@RestController
public class WebHookController {
	@PostMapping(value = "/post/test", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public Response handleWebHook(String msg) {
		String output = "POST:Jersey say : " + msg;
		return Response.ok().entity(output).build();
	}

}
