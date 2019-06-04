package wallit_app.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import wallit_app.data.FundInfoEntry;
import wallit_app.utilities.JsonHandler;
import wallit_app.utilities.TimeScaleType;


@SpringBootApplication
@ComponentScan(basePackages={"wallit_app"})
public class Main {
	
	//public static final int webclientPort = 4200;
	public static final int javaserverPort = 4201;
	public static final int springbootPort = 4202;
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Main.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", springbootPort));
        app.run(args);
	}
	
}
