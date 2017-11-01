package vsg.rest.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Created by Denis Orlov.
 */
@Aspect
@Component
public class AspectSample {
	@Before(value = "execution(* vsg.rest.service.SampleService.createSample(java.lang.String)) && args(sampleName)")
	public void logBefore(String sampleName) {
		System.out.println("AspectSample() is running! sampleName = " + sampleName);
		System.out.println("******");
	}
}
