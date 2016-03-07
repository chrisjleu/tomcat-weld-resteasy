package resource;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Encapsulates a command that must be executed.
 */
public class Command implements Serializable {

	/**
	 * Version 1.
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	String id;

	@JsonProperty("user")
	String user;

	@JsonProperty("machine")
	String machine;

	@JsonProperty("type")
	String type;

	@JsonProperty("instruction")
	String instruction;

	@JsonCreator
	public Command(@JsonProperty("id") String id, @JsonProperty("user") String user,
			@JsonProperty("machine") String machine, @JsonProperty("type") String type,
			@JsonProperty("instruction") String instruction) {
		super();
		this.id = id;
		this.user = user;
		this.machine = machine;
		this.type = type;
		this.instruction = instruction;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(id).append("|")
				.append(user).append("|")
				.append(machine).append("|")
				.append(type).append("|")
				.append(instruction).toString();
	}

	public String getId() {
		return id;
	}

	public String getUser() {
		return user;
	}

	public String getMachine() {
		return machine;
	}
	
	public String getType() {
		return type;
	}

	public String getInstruction() {
		return instruction;
	}

}
