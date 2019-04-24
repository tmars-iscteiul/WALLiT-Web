package wallit.springbootserver;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppData {
	@JsonProperty("date") private String date; //date of the deposit/withdraw
	@JsonProperty("value") private double value; //deposit amount or withdraw amount
}
