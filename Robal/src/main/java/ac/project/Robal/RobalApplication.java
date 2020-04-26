package ac.project.Robal;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RobalApplication {

	public static void main(String[] args) {
		SpringApplication.run(RobalApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	  public Docket swaggerConfiguration() {
	    return new Docket(DocumentationType.SWAGGER_2)
	      .select()
	      .apis(RequestHandlerSelectors.basePackage("ac.project.Robal"))
	      .paths(PathSelectors.ant("/**"))
	      .build()
	      .groupName("api")
	      .apiInfo(apiDetails());
	  }

	  // Describe the apis
	  private ApiInfo apiDetails() {
	    return new ApiInfo(
	    		"Robal e-commerce API",
	    		"Project - e-commerce",
	    		"1.0",
	    		"Free to use",
	    		new springfox.documentation.service.Contact("Alex and Robert", "http://algonquincollege.com", "robal@robal.com"),
	    		"API License",
	    		"blah",
	    		Collections.emptyList());
	  }

}
