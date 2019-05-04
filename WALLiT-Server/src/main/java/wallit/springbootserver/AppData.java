package wallit.springbootserver;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppData {
	@JsonProperty("date") private String date; //date of the deposit/withdraw
	@JsonProperty("value") private double value; //deposit amount or withdraw amount
	
	public AppData () {
	}
	
	public AppData (String date, double value) {
		this.date = date;
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	//"2019-02-01T00:00:00":196850.71
	
	@Override
	 public String toString() {
        return date + ":" + value;
    }
}
