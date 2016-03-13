package domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A general purpose command.
 */
public class Command implements Serializable {

	/**
	 * Version 1.
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("issuer")
	@NotNull
	String issuer;

	@JsonProperty("type")
	@NotNull
	String type;

	@JsonProperty("payload")
	@NotNull
	String payload;

	@JsonCreator
	public Command(
			@JsonProperty("user") String issuer, 
			@JsonProperty("type") String type,
			@JsonProperty("payload") String payload) {
		super();
		this.issuer = issuer;
		this.type = type;
		this.payload = payload;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getIssuer() {
		return issuer;
	}

	public String getType() {
		return type;
	}

	public String getPayload() {
		return payload;
	}

}
