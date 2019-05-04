package wallit.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import wallit.javaserver.JavaServer;
import wallit.springbootserver.SpringbootServer;

@SpringBootApplication
@ComponentScan(basePackages={"main, spring"})
public class Main {
	
	public static void main(String[] args) {
		new JavaServer().start();
		//SpringApplication.run(Main.class, args);
		
		new SpringbootServer().run();
	}
	
}
