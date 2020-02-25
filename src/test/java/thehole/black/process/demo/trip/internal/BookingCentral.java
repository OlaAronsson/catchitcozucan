package thehole.black.process.demo.trip.internal;

import thehole.black.process.internal.util.id.IdGenerator;

public class BookingCentral {

	private static final int ID_LEN = 6;

	public static final String getFlightConfirmation() {
		return IdGenerator.getInstance().getId(ID_LEN);
	}

	public static final String getHotelConfirmation() {
		return IdGenerator.getInstance().getId(ID_LEN);
	}

	public static final String getCarConfirmation() {
		return IdGenerator.getInstance().getId(ID_LEN);
	}

}
