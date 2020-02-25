package thehole.black.process.demo.shoe.internal;

import java.io.Serializable;

import lombok.Getter;
import thehole.black.process.internal.util.domain.ToStringBuilder;
import thehole.black.process.internal.util.domain.BaseDomainObject;

@Getter
public class Laces extends BaseDomainObject implements Serializable {

	static final long serialVersionUID = 14232654634L;
	private static final String COLOR = "color";
	private static final String LENGTH = "length";

	private final String color;
	private final Long length;

	public Laces(String color, Long length) {
		this.color = color;
		this.length = length;
	}

	@Override
	public String doToString() {
		return new ToStringBuilder(COLOR, color).append(LENGTH, length).toString();
	}
}
