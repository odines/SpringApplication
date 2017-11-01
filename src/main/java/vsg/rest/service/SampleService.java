package vsg.rest.service;

import org.springframework.stereotype.Service;
import vsg.model.Sample;

/**
 * Created by Denis Orlov.
 */
@Service
public class SampleService {
	public Sample createSample(String name) {
		Sample sample = new Sample();
		sample.setName(name);
		return sample;
	}
}
