package com.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableJpaRepositories
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:8081")
						.allowedMethods("POST", "GET", "PUT", "DELETE")
						.exposedHeaders("Access-Token", "Refresh-Token");
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				final String[] CLASSPATH_RESOURCE_LOCATIONS = {
						"classpath:/META-INF/resources/", "classpath:/resources/",
						"classpath:/static/", "classpath:/public/" };

				if (!registry.hasMappingForPattern("/webjars/**")) {
					registry.addResourceHandler("/webjars/**").addResourceLocations(
							"classpath:/META-INF/resources/webjars/");
				}
				if (!registry.hasMappingForPattern("/**")) {
					registry.addResourceHandler("/**").addResourceLocations(
							CLASSPATH_RESOURCE_LOCATIONS);
				}
			}
		};
	}
}
