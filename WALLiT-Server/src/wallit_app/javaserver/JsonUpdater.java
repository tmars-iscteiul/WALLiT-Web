package wallit_app.javaserver;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wallit_app.springbootserver.JsonToJava;

public class JsonUpdater {

	public static void updateFile (Date dateApp, double valueApp) throws ParseException { 
		
		String formatDate = "\"" + "yyyy-MM-dd" + " " + "HH:mm:ss"+  "\"";
		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date according to the chosen format
		DateFormat df = new SimpleDateFormat(formatDate);		        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String dateString = df.format(dateApp);

		JsonToJava.readJson();
		double balance= JsonToJava.fiec_oneMonth.getFundInfoEntryList().get(JsonToJava.fiec_oneMonth.getFundInfoEntryList().size()-1).getValue() + valueApp;
		
		String inputJson = dateString + ":"+ Double.toString(balance);
		
		System.out.println("The data from the App is: " + inputJson );
		writeJson(inputJson, ".\\dataTestOneMonth.json");
		
		
	}
	
	
	public static void writeJson (String inputJson, String fileName ) {
			
		

	}
	
	public static void main(String[] args) throws ParseException {
		Date today = Calendar.getInstance().getTime();
		updateFile (today, 100.0 ); // values for testing
	}
}
