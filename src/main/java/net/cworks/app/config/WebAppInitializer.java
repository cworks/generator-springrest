package net.cworks.app.config;

import net.cworks.app.web.filter.gzip.GZipServletFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebAppInitializer implements ServletContextInitializer {

    /**
     * Logger
     */
    private final Logger log = LoggerFactory.getLogger(WebAppInitializer.class);

    /**
     * Environment
     */
    @Inject
    private Environment env;

    /**
     * When the WebApp starts create a GzipFilter that will compress output
     * from this server.
     *
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Web application configuration, using profiles: {}",
            Arrays.toString(env.getActiveProfiles()));

        EnumSet<DispatcherType> disps = EnumSet.of(
            DispatcherType.REQUEST,
            DispatcherType.FORWARD,
            DispatcherType.ASYNC);

        initGzipFilter(servletContext, disps);

        log.info("Web application fully configured");
    }

    /**
     * Initializes the GZip filter.
     */
    private void initGzipFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering GZip Filter");
        FilterRegistration.Dynamic compressingFilter = servletContext.addFilter(
            "gzipFilter", new GZipServletFilter());

        Map<String, String> parameters = new HashMap<>();
        compressingFilter.setInitParameters(parameters);
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
        // TODO parameterize this URL so we can make from yeoman
        compressingFilter.addMappingForUrlPatterns(disps, true, "/app/rest/*");
        compressingFilter.setAsyncSupported(true);
    }
}
