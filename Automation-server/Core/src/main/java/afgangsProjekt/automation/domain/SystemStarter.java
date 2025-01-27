package afgangsProjekt.automation.domain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.sql.SQLException;
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
@ComponentScan("afgangsProjekt.automation")
public class SystemStarter  {
	public static ApplicationContext applicationContext;
	public SystemStarter(ApplicationContext _applicationContext) {
		applicationContext = _applicationContext;
	}

	public static void main(String[] args) throws InterruptedException, SQLException {
		applicationContext = SpringApplication.run(SystemStarter.class, args);
	}
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}