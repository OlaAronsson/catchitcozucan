package com.github.olaaronsson.process.demo.shoe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.olaaronsson.process.demo.shoe.internal.OrderRepository;
import com.github.olaaronsson.process.impl.JobBase;
import com.github.olaaronsson.process.interfaces.Job;
import com.github.olaaronsson.process.interfaces.ProcessSubject;

public class ShippingShoesJob extends JobBase implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingShoesJob.class);

	@Override
	public String name() {
		return "Shipping ordered shoes";
	}

	@Override
	public void doJob() {
		fetchSubjects().stream().forEachOrdered(o -> exec(new ShipAShoeProcess(o, OrderRepository.getInstance())));
		LOGGER.info(getTotalExectime());
	}

	@Override
	public ProcessSubject provideSubjectSample() {
		return new Order();
	}

	public List<ProcessSubject> fetchSubjects() {
		return OrderRepository.getInstance().load().stream()
				.filter(subject -> isOrderInPickupState(subject))
				.collect(Collectors.toList());
	}

	private boolean isOrderInPickupState(ProcessSubject p){
		return Arrays.stream(ShipAShoeProcess.CRITERIA_STATES).filter(state -> state.equals(p.getCurrentStatus())).findFirst().isPresent();
	}


}
