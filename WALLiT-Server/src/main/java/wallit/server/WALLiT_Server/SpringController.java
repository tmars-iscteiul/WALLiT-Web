package wallit.server.WALLiT_Server;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
//import utilities.Paths;
import java.io.OutputStream;

import org.json.JSONObject;
//import org.apache.tomcat.util.http.fileupload.IOUtils;//(?)
//import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The main controller for the spring application. 
 * Will serve as the Webserver between the WALLiT site and android application.
 * @author Catarina
 *
 */
@RestController
public class SpringController {
	
	/**
	 * Adds the submission sent by the user to the engine, so it can start the algorithm processes. This method accepts inputs from a certain origin only.
	 * @param submission The submission object, converted directly from the user's submitted JSON.
	 */
	//@CrossOrigin(origins = "http://localhost:4200")
	/*
	@RequestMapping(method = RequestMethod.POST, value = "/send_problem", consumes = "application/json")
	public void addSubmission(@RequestBody Submission submission)	{
		engine.addProblemToQueue(submission);
	}
	*/
	/**
	 * Adds the submission sent by the user to the @see Engine, so it can start the algorithm processes. This method accepts inputs from any testing origin.
	 * @param submission The @see Submission object, converted directly from the user's submitted JSON.
	 */
	/*
	@RequestMapping(method = RequestMethod.POST, value = "/send_test_problem")
	public void addTestSubmission(@RequestBody Submission submission)	{
		engine.addProblemToQueue(submission);
	}
	*/
	/**
	 * Sends results to the chart service, displaying this data to the user.
	 * @param problem The corresponding problem
	 * @return The displaying information
	 */
	/*
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/dataTestEuro.json")
	public @ResponseBody ResponseEntity<String> sendResults(@RequestBody String problem) {
		String jsonTxt;
		OutputStream out;
		InputStream in;
		try {
			
			in = new FileInputStream(DEPOSITS_FOLDER.RESULTS_FOLDER+problem+"/"+problem+"_results.json");
			jsonTxt = IOUtils.toString( is );
			
			out = new FileOutputStream("dataTestEuro.json");
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open the results json file.");
			return null;
		} catch (IOException e) {
			System.err.println("Cannot open the results json file.");
			return null;
		}
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(jsonTxt, httpHeaders, HttpStatus.OK);
	}
	*/
	@RequestMapping(method = RequestMethod.POST, value = "/receivedAmount", consumes = "application/json")
	public void receivedValues (@RequestBody AppData appData) {
		
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/sendValuesSite", consumes = "application/json")
	public @ResponseBody ResponseEntity<String> sendValuesSite (@RequestBody AppData appData) {
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();
		InputStream in;
		
		try {
			
			
			try {
				Object obj = parser.parse(new FileReader("./dataTestEuro.json"));
				jsonObject =  (JSONObject) obj;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open the results json file.");
			return null;
		} catch (IOException e) {
			System.err.println("Cannot open the results json file.");
			return null;
		}
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(jsonObject.toString(), httpHeaders, HttpStatus.OK);
	}
	

}

