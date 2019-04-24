package wallit.springbootserver;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
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

	@RequestMapping(method = RequestMethod.POST, value = "/receivedValues", consumes = "application/json")
	public void receivedValues (@RequestBody AppData appData) {
		
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/sendValuesSite", consumes = "application/graphic")
	public @ResponseBody ResponseEntity<String> sendValuesSite (@RequestBody AppData appData) {
		JSONObject jsonObject;
		JSONParser parser = new JSONParser();
		
		try {

				Object obj = parser.parse(new FileReader("./dataTestEuro.json"));
				jsonObject =  (JSONObject) obj;
				final HttpHeaders httpHeaders= new HttpHeaders();
			    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			    return new ResponseEntity<String>(jsonObject.toString(), httpHeaders, HttpStatus.OK);
			
		} catch (ParseException e) {
			e.printStackTrace();   
			System.err.println("Cannot open the results json file.");
			return null;
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open the results json file.");
			return null;
		} catch (IOException e) {
			System.err.println("Cannot open the results json file.");
			return null;
		}
		
	}
	

}

