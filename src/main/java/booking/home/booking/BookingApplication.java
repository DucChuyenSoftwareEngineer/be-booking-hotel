package booking.home.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import booking.home.booking.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class BookingApplication implements WebMvcConfigurer{
	
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {

	        // Register resource handler for images
	        registry.addResourceHandler("/images/**").addResourceLocations("file:uploads/")
	                ;
	    }


	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}

}
