package vsg.rest.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Denis Orlov.
 */
@Aspect
@Component
public class AspectSample {

	public static final Logger LOGGER = LoggerFactory.getLogger(AspectSample.class);

	@Before(value = "execution(* vsg.rest.service.SampleService.createSample(java.lang.String))&&args(sampleName)")
	public void logBefore(JoinPoint joinPoint, String sampleName) {
		LOGGER.info("SampleAspect ->" + joinPoint.toShortString() + "\nsampleName = " + sampleName);
		LOGGER.info("******");
	}
}
