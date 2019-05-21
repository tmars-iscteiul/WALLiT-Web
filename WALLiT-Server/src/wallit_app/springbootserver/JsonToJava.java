package wallit_app.springbootserver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonToJava {
		 String date;
		 double value;
		 List<String> list_date;
		 List<Double> list_value;
		
		 static List<String> list_oneMonth;
		 static List<String> list_threeMonths;
		 static List<String> list_sixMonths;
		 static List<String> list_oneYear;
		 static List<String> list_fiveYears;
		 
		public static void readJson () {

			 
			JsonParser parser = new JsonParser();
            Object obj;
			try {
				obj = parser.parse(new FileReader(".\\dataTestEuro.json"));
				
				JsonObject jsonObject = (JsonObject) obj;
		        System.out.println(jsonObject);
				
		        // oneMonth
		        list_oneMonth = new ArrayList<String>();	            
	            JsonArray msg_oneMonth = (JsonArray) jsonObject.get("oneMonth");
	            Iterator<JsonElement> iteratorOneMonth = msg_oneMonth.iterator();
	            String oneMonthRead;
				while (iteratorOneMonth.hasNext()) {
	            	oneMonthRead = iteratorOneMonth.next().toString();
	            	list_oneMonth.add(oneMonthRead);                
	            } 	            
	            System.out.println("oneMonth---->" + list_oneMonth);
	            System.out.println("oneMonth 2ºelemento---->" + list_oneMonth.get(1));
	            
	            
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
	            
				
			} catch (JsonIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
		
		public static void writeJson () {
			/*
			AppData dataExample = new AppData();
			dataExample.setDate("2019-05-12T17:36:00");
			dataExample.setValue(32.03);
			System.out.println(dataExample.toString());
			*/
			
			String[] vectorAux1= list_oneMonth.get(0).split("\"");
			String[] vectorAux2= vectorAux1[1].split("T");
			String[] vectorAux3= vectorAux2[0].split("-");
			
			System.out.println("Data: " + vectorAux2[0]);
			System.out.println("Mês: " + vectorAux3[1]);
			
			//String date = vectorAux[0];
			//double value = Double.parseDouble(vectorAux[1]);
			//System.out.println("Data: "+ date + "Value: " + value);
		}
		
		public static void main(String[] args) {
			readJson();
			writeJson();
		}
}
