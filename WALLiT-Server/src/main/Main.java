package main;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages={"main, javaserver, springbootserver"})
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
