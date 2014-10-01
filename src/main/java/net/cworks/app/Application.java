package net.cworks.app;

import net.cworks.app.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    private final Logger log = LoggerFactory.getLogger(Application.class);

    @Inject
    private Environment env;

    /**
     * Initializes springrest.
     *
     * Spring profiles can be configured with a program arguments
     *     --spring.profiles.active=your-active-profile
     *
     */
    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration");
        } else {
            log.info("Running with Spring profile(s) : {}",
                Arrays.toString(env.getActiveProfiles()));
        }
    }

    /**
     * Main method, used to run the application.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);

        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);

        // Check if the selected profile has been set as argument.
        // if not the development profile will be added
        addDefaultProfile(app, source);

        app.run(args);
    }

    /**
     * Set a default profile if it has not been set.  By default dev profile is set.
     */
    private static void addDefaultProfile(SpringApplication app,
        SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active")) {
            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }
}
