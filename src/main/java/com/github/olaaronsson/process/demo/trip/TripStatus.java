package com.github.olaaronsson.process.demo.trip;

import com.github.olaaronsson.process.ProcessStatus;

@ProcessStatus
public class TripStatus {

	// @formatter:off
	public enum Status {
		NEW_ORDER,
		FLIGHT_NOT_CONFIRMED,
		FLIGHT_CONFIRMED,
		HOTEL_NOT_CONFIRMED,
		HOTEL_CONFIRMED,
		CAR_NOT_CONFIRMED,
		CAR_CONFIRMED
	}
	// @formatter:on
}
