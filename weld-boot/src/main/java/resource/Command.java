package resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Encapsulates a command that must be executed.
 */
public class Command {

	@JsonProperty("id")
	String id;

	@JsonProperty("user")
	String user;

	@JsonProperty("machine")
	String machine;

	@JsonProperty("instruction")
	String instruction;

	@JsonCreator
	public Command(@JsonProperty("id") String id, @JsonProperty("user") String user,
			@JsonProperty("machine") String machine, @JsonProperty("instruction") String instruction) {
		super();
		this.id = id;
		this.user = user;
		this.machine = machine;
		this.instruction = instruction;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(id).append("|").append(user).append("|").append(machine).append("|")
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

	public String getInstruction() {
		return instruction;
	}

}
