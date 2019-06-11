package wallit_app.springbootserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import wallit_app.data.FundInfoEntry;
import wallit_app.data.MovementEntry;
import wallit_app.javaserver.JavaServer;
import wallit_app.utilities.JsonHandler;
import wallit_app.utilities.TimeScaleType;

/**
 * The main controller for the spring application. (localhost:8080)
 * Will serve as the communication gate between the java server and the web client
 * <p>
 * It will automatically run the Autowired {@link JavaServer}, booting the java server to handle mobile client's connection, 
 * alongside the spring boot service that handles web HTTP requests.
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
	
	/**
	 * @return A {@link ResponseEntity} containing a {@link MovementEntry} list from the requested user.
	 */
	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/getMovementEntriesFromUser")
	public ResponseEntity<ArrayList<MovementEntry>> getMovementEntriesFromUser() {
		try {
			return ResponseEntity.ok(JsonHandler.getMovementEntryListFromUser("User", true));
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
		return (ResponseEntity<ArrayList<MovementEntry>>) ResponseEntity.notFound();
	}
	
	/**
	 * @return A {@link ResponseEntity} containing a {@link MovementEntry} list from the requested user, filtered and grouped by day.
	 */
	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method = RequestMethod.POST, value = "/getMovementEntriesFromUserByDay")
	public ResponseEntity<ArrayList<MovementEntry>> getMovementEntriesFromUserByDay() {
		try {
			return ResponseEntity.ok(JsonHandler.getGroupedMovementEntryListByDay("User", true));
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
		return (ResponseEntity<ArrayList<MovementEntry>>) ResponseEntity.notFound();
	}
	
	/**
	 * @return A {@link ResponseEntity} containing a {@link FundInfoEntry} list with the {@link TimeScaleType#ONE_MONTH}
	 */
	@SuppressWarnings("unchecked")
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

	/**
	 * @return A {@link ResponseEntity} containing a {@link FundInfoEntry} list with the {@link TimeScaleType#THREE_MONTHS}
	 */
	@SuppressWarnings("unchecked")
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
	
	/**
	 * @return A {@link ResponseEntity} containing a {@link FundInfoEntry} list with the {@link TimeScaleType#SIX_MONTHS}
	 */
	@SuppressWarnings("unchecked")
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
	
	/**
	 * @return A {@link ResponseEntity} containing a {@link FundInfoEntry} list with the {@link TimeScaleType#ONE_YEAR}
	 */
	@SuppressWarnings("unchecked")
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
	
	/**
	 * @return A {@link ResponseEntity} containing a {@link FundInfoEntry} list with the {@link TimeScaleType#FIVE_YEARS}
	 */
	@SuppressWarnings("unchecked")
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

