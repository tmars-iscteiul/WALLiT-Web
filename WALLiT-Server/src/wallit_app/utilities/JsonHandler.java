package wallit_app.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import wallit_app.data.FundInfoEntry;

public class JsonHandler {
	
	public static final String FUND_INFO_LOCATION = "./fundinfo_data.json";

	// Adds a new entry to the main FundInfoEntry data file
	public static void addEntryToFundInfoDataFile (FundInfoEntry entryToAdd) throws JsonSyntaxException, JsonIOException, IOException { 
		// Reads the current data file, transforming it into a FundInfoEntry ArrayList object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ArrayList<FundInfoEntry> aux = gson.fromJson(new FileReader(FUND_INFO_LOCATION), new TypeToken<ArrayList<FundInfoEntry>>(){}.getType());
		
		// Calculates the number of days between the last entry and the current date
		System.out.println(aux.get(aux.size()-1));
		long noOfDaysBetween = ChronoUnit.DAYS.between(aux.get(aux.size()-1).getDate().toInstant(), entryToAdd.getDate().toInstant());
		
		// Fills fund info list with missing entries until the current date
		Calendar c = Calendar.getInstance();
		c.setTime(aux.get(aux.size()-1).getDate());
		for(int i = 0; i<noOfDaysBetween; i++)	{
			c.add(Calendar.DATE, 1);
			aux.add(new FundInfoEntry(c.getTime(), aux.get(aux.size()-1).getValue()));
		}
		
		// Replaces last value with value that needs to be added
		aux.get(aux.size()-1).setValue(entryToAdd.getValue());
		
		// Updates the data file
		Writer writer = new FileWriter(FUND_INFO_LOCATION);
		gson.toJson(aux, writer);
		writer.flush();
        writer.close();
	}
	
	// Returns an arraylist of FundInfoEntries, based on a timescale
	public static ArrayList<FundInfoEntry> getTimescaledFundInfoList(TimeScaleType timescale) throws JsonIOException, JsonSyntaxException, FileNotFoundException	{
		// Reads the current data file, transforming it into a FundInfoEntry ArrayList object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ArrayList<FundInfoEntry> listFromFile = gson.fromJson(new FileReader("./fundinfo_data.json"), new TypeToken<ArrayList<FundInfoEntry>>(){}.getType());
		
		// Creates a new empty list, and inserts the entries depending on the timescale
		// This is assuming the list has enough entries to support at least 5 years (360*5 entries minimum, otherwise it will crash)
		ArrayList<FundInfoEntry> aux = new ArrayList<>();
		for(int i = listFromFile.size()-1; i > ((listFromFile.size()-1)-(timescale.getEntriesPerScale()*timescale.getJumpsPerLine())); i -= timescale.getJumpsPerLine())	{
			aux.add(listFromFile.get(i));
		}
		
		// Inverts the list to sent, since the read operation is inverted
		ArrayList<FundInfoEntry> res = new ArrayList<>();
		for(int i = 0; i < aux.size(); i++)	{
			res.add(aux.get(aux.size()-1-i));
		}
		return res;
	}
	
	// Returns the last balance on the fund info file
	public static double getLastFundInfoEntryValue() throws JsonIOException, JsonSyntaxException, FileNotFoundException	{
		// Reads the current data file, transforming it into a FundInfoEntry ArrayList object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ArrayList<FundInfoEntry> listFromFile = gson.fromJson(new FileReader("./fundinfo_data.json"), new TypeToken<ArrayList<FundInfoEntry>>(){}.getType());
		
		return listFromFile.get(listFromFile.size()-1).getValue();
	}
	
}
