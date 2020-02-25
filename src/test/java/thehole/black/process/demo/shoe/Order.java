package thehole.black.process.demo.shoe;

import static thehole.black.process.demo.trip.internal.TripOrderRepository.ERROR_ID;

import thehole.black.process.demo.shoe.internal.Shoe;
import thehole.black.process.exception.ProcessRuntimeException;
import thehole.black.process.impl.ProcessSubjectBase;
import thehole.black.process.internal.util.id.IdGenerator;

public class Order extends ProcessSubjectBase {

	static final long serialVersionUID = 14662657334L;

	private Integer id;
	private Integer orderId = IdGenerator.getInstance().getNextId();
	private String adressId = IdGenerator.getInstance().getId(16);
	private Shoe shoe;
	private boolean isPacked;
	private String trackinId;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public Integer id() {
		return orderId;
	}

	@Override
	public String subjectIdentifier() {
		return adressId;
	}

	public void packageOrder(){
		isPacked = true;
		if (orderId.intValue() == ERROR_ID) {
			throw new ProcessRuntimeException("WTF - simulating 'planned' went wrong");
		}
	}

	public String getRequestedColor() {
		return "grue";
	}

	public long getRequestedSize() {
		return 42l;
	}

	public void setShoe(Shoe shoe) {
		this.shoe = shoe;
		if (orderId.intValue() == ERROR_ID) {
			throw new RuntimeException("WTF - simulating something went to shitz in runtime!");
		}
	}

	public void send() {
		trackinId = IdGenerator.getInstance().getId(12);
	}

	public Shoe getShoe() {
		return shoe;
	}

	@Override
	public Enum[] getCycle() {
		return OrderStatus.Status.values();
	}

	@Override
	public Enum getCurrentStatus() {
		return status;
	}

	@Override
	public String doToString() {
		return Integer.toString(id);
	}
}
