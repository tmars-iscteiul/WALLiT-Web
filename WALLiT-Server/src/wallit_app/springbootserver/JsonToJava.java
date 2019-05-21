package wallit_app.springbootserver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import wallit_app.data.FundInfoEntry;
import wallit_app.data.FundInfoEntryChunk;
import wallit_app.utilities.TimeScaleType;

public class JsonToJava {
		 String date;
		 double value;
		 List<String> list_date;
		 List<Double> list_value;
		
		 static ArrayList<FundInfoEntry> list_oneMonth;// testing
		 
		 static List<String> list_threeMonths;
		 static List<String> list_sixMonths;
		 static List<String> list_oneYear;
		 static List<String> list_fiveYears;
		 
		 public static FundInfoEntryChunk fiec_oneMonth;
		 
		
		public static void readJson () throws ParseException {

			 
			JsonParser parser = new JsonParser();
            Object obj;
			try {
				obj = parser.parse(new FileReader(".\\dataTestOneMonth.json"));
				
				JsonObject jsonObject = (JsonObject) obj;
		        System.out.println(jsonObject);
				
		        // oneMonth
		        list_oneMonth = new ArrayList<FundInfoEntry>();
	            JsonArray msg_oneMonth = (JsonArray) jsonObject.get("oneMonth");
	            Iterator<JsonElement> iteratorOneMonth = msg_oneMonth.iterator();
	            
	            FundInfoEntry oneMonthRead;
				while (iteratorOneMonth.hasNext()) {
					
					String[] vectorAux1= iteratorOneMonth.next().toString().split("\""); //vectorAux1[1]= Data		
					String[] vectorAux2= vectorAux1[2].split(":");
					String[] vectorAux3= vectorAux2[1].split("}"); //vectorAux3[0] = Valor		
					//System.out.println("Data ->" + vectorAux1[1] + ";"+ "Valor ->" + vectorAux3[0]);
					
					String formatDate = "yyyy-MM-dd" + " " + "HH:mm:ss";
					DateFormat df = new SimpleDateFormat(formatDate);	
					Date dateRead = df.parse(vectorAux1[1]); //TODO change the configuration of the date
					
					double valueRead = Double.parseDouble(vectorAux3[0]);
					
					System.out.println("DateRead: " + dateRead + "    ValueRead: " +valueRead );
					
					oneMonthRead = new FundInfoEntry(dateRead, valueRead);
	            	list_oneMonth.add(oneMonthRead);                
	            } 	            
				System.out.println("Ultimo valor da lista : " + list_oneMonth.get(list_oneMonth.size()-1).getValue());  
				System.out.println("Ultima data lista : " + list_oneMonth.get(list_oneMonth.size()-1).getDate());
				
				fiec_oneMonth = new FundInfoEntryChunk(list_oneMonth, TimeScaleType.ONE_MONTH);
				
				
	            /*
	            
	            //threeMonths
	            list_threeMonths = new ArrayList<String>();            
	            JsonArray msg_threeMonths = (JsonArray) jsonObject.get("threeMonths");
	            Iterator<JsonElement> iterator_threeMonths = msg_threeMonths.iterator();
	            while (iterator_threeMonths.hasNext()) {
	            	String threeMonthsRead = iterator_threeMonths.next().toString();
	            	list_threeMonths.add(threeMonthsRead);                
	            } 	            
	            System.out.println("threeMonths---->" + list_threeMonths);
	            
	            //sixMonths
	            list_sixMonths = new ArrayList<String>();	            
	            JsonArray msg_sixMonths = (JsonArray) jsonObject.get("sixMonths");
	            Iterator<JsonElement> iterator_sixMonths = msg_sixMonths.iterator();
	            while (iterator_sixMonths.hasNext()) {
	            	String sixMonthsRead = iterator_sixMonths.next().toString();
	            	list_sixMonths.add(sixMonthsRead);                
	            } 	            
	            System.out.println("sixMonths---->" + list_sixMonths);
	            
	            //oneYear
	            list_oneYear = new ArrayList<String>();	            
	            JsonArray msg_oneYear = (JsonArray) jsonObject.get("oneYear");
	            Iterator<JsonElement> iterator_oneYear = msg_oneYear.iterator();
	            while (iterator_oneYear.hasNext()) {
	            	String oneYearRead = iterator_oneYear.next().toString();
	            	list_oneYear.add(oneYearRead);                
	            } 	            
	            System.out.println("oneYear---->" + list_oneYear);
	            
	            //fiveYears
	            list_fiveYears = new ArrayList<String>();	            
	            JsonArray msg_fiveYears = (JsonArray) jsonObject.get("fiveYears");
	            Iterator<JsonElement> iterator_fiveYears = msg_fiveYears.iterator();
	            while (iterator_fiveYears.hasNext()) {
	            	String fiveYearsRead = iterator_fiveYears.next().toString();
	            	list_fiveYears.add(fiveYearsRead);                
	            } 	            
	            System.out.println("fiveYears---->" + list_fiveYears + "\n");
	            */
				
			} catch (JsonIOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
            
		}

		
		public static void main(String[] args) throws ParseException {
			readJson();
			//writeJson();
		}

		
}
