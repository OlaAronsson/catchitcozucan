package thehole.black.process.demo.shoe.internal;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import thehole.black.process.internal.util.domain.ToStringBuilder;
import thehole.black.process.internal.util.domain.BaseDomainObject;

@Setter
@Getter
public class Shoe extends BaseDomainObject implements Serializable {

	static final long serialVersionUID = 14232657334L;
	private static final String NULL = "null";
	private static final String NAME = "name";
	private static final String HIP_FACTOR = "hipFactor";
	private static final String LACES = "laces";

	public enum HipFactor {
		NONE, SOMEWHAT, DASHIT
	}

	private final String name;
	private final HipFactor hipFactor;
	private Laces laces;

	public Shoe(String name, HipFactor hipFactor) {
		this.name = name;
		this.hipFactor = hipFactor;
	}

	@Override
	public String doToString() {
		return new ToStringBuilder(NAME, name).append(HIP_FACTOR, hipFactor).append(LACES, laces).toString();
	}
}
