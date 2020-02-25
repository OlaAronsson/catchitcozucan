package thehole.black.process.demo.trip;

import lombok.Getter;
import lombok.Setter;
import thehole.black.process.impl.ProcessSubjectBase;
import thehole.black.process.internal.util.id.IdGenerator;

@Setter
@Getter
public class TripSubject extends ProcessSubjectBase {

	static final long serialVersionUID = 14662657334L;

	private final String passPortNumber;
	private final Integer id;
	private String flightConfirmation;
	private String hotelConfirmation;
	private String carConfirmation;

	public TripSubject(String passportNumber) {
		this.passPortNumber = passportNumber;
		id = IdGenerator.getInstance().getNextId();
	}

	@Override
	public Integer id() {
		return id;
	}

	@Override
	public String subjectIdentifier() {
		return passPortNumber;
	}

	@Override
	public Enum[] getCycle() {
		return TripStatus.Status.values();
	}
}
