package net.cworks.app;

import net.cworks.app.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * This is an helper Java class that provides an alternative to creating a web.xml.
 *
 */
public class ApplicationWebXml extends SpringBootServletInitializer {

    /**
     * Logger
     */
    private final Logger log = LoggerFactory.getLogger(ApplicationWebXml.class);

    /**
     * Configure the application
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.profiles(addDefaultProfile())
            .showBanner(false)
            .sources(Application.class);
    }

    /**
     * Set a default profile if it has not been set.
     *
     * Use -Dspring.profiles.active=dev
     */
    private String addDefaultProfile() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null) {
            log.info("Running with Spring profile(s) : {}", profile);
            return profile;
        }

        log.warn("No Spring profile configured, running with default configuration");
        return Constants.SPRING_PROFILE_DEVELOPMENT;
    }
}
