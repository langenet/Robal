package ac.project.Robal.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

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
	    		"",
	    		Collections.emptyList());
	  }
}
