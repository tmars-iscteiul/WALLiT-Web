package wallit_app.springbootserver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONObject;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import wallit_app.data.FundInfoEntry;
import wallit_app.javaserver.JavaServer;
import wallit_app.utilities.JsonHandler;
import wallit_app.utilities.TimeScaleType;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The main controller for the spring application. (localhost:8080)
 * Will serve as the communication gate between the java server and the web client
 * @author skner
 * 
 */
@RestController
public class SpringController {
	
	@Autowired
	private JavaServer javaServer;
	
	// Class for testing purposes
	@RequestMapping("/javaserver_status")
	public String getConnectedClients()	{
		return "The java server currently has " + javaServer.getConnectedClients() + " active android client(s).";
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/getFundEntries1Month")
	public ResponseEntity<ArrayList<FundInfoEntry>> getFundEntries1Month() {
		try {
			return ResponseEntity.ok(JsonHandler.getTimescaledFundInfoList(TimeScaleType.ONE_MONTH));
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return (ResponseEntity<ArrayList<FundInfoEntry>>) ResponseEntity.notFound();
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/getFundEntries3Months")
	public ResponseEntity<ArrayList<FundInfoEntry>> getFundEntries3Months() {
		try {
			return ResponseEntity.ok(JsonHandler.getTimescaledFundInfoList(TimeScaleType.THREE_MONTHS));
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return (ResponseEntity<ArrayList<FundInfoEntry>>) ResponseEntity.notFound();
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/getFundEntries6Months")
	public ResponseEntity<ArrayList<FundInfoEntry>> getFundEntries6Months() {
		try {
			return ResponseEntity.ok(JsonHandler.getTimescaledFundInfoList(TimeScaleType.SIX_MONTHS));
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return (ResponseEntity<ArrayList<FundInfoEntry>>) ResponseEntity.notFound();
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/getFundEntries1Year")
	public ResponseEntity<ArrayList<FundInfoEntry>> getFundEntries1Year() {
		try {
			return ResponseEntity.ok(JsonHandler.getTimescaledFundInfoList(TimeScaleType.ONE_YEAR));
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return (ResponseEntity<ArrayList<FundInfoEntry>>) ResponseEntity.notFound();
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/getFundEntries5Years")
	public ResponseEntity<ArrayList<FundInfoEntry>> getFundEntries5Years() {
		try {
			return ResponseEntity.ok(JsonHandler.getTimescaledFundInfoList(TimeScaleType.FIVE_YEARS));
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return (ResponseEntity<ArrayList<FundInfoEntry>>) ResponseEntity.notFound();
	}

}

