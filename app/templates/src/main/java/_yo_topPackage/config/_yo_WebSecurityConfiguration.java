package <%= app.packageName %>.app.config;

import java.lang.Integer;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
    implements EnvironmentAware {

    /**
     * Security realm name: TODO bake from yeoman
     */
    private static final String REALM = <%= app.projectName %>"SecurityRealm";

    private static final Integer NONCE_SEC = <%= app.digestAuthNonceSec %>

    /**
     * Property resolver
     */
    private RelaxedPropertyResolver propertyResolver;

    /**
     * EnvironmentAware override that sets the async properties
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "webauth.");
    }

    /**
     * Setup assets that we're going to ignore
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/bower_components/**");
    }

    /**
     * Bean for HTTP Digest Authentication, used if webauth.method=digest
     * TODO: bake key from yeoman
     * @return
     */
    @Bean
    public DigestAuthenticationEntryPoint digestEntryPoint () {
        DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint();
        entryPoint.setKey(<%= app.digestAuthKey %>);
        entryPoint.setRealmName(WebSecurityConfiguration.REALM);
        entryPoint.setNonceValiditySeconds(WebSecurityConfiguration.NONCE_SEC);

        return entryPoint;
    }

    /**
     * Bean for HTTP Basic Auth, used if webauth.method=basic
     * @return
     */
    @Bean
    public BasicAuthenticationEntryPoint basicEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
        entryPoint.setRealmName(WebSecurityConfiguration.REALM);
        return entryPoint;
    }

    /**
     * In memory UserDetailService implementation
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String authMethod = this.propertyResolver.getProperty("method", "basic");
        if("digest".equalsIgnoreCase(authMethod)) {
            configureDigestAuth(http);
        } else {
            configureBasicAuth(http);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("<%= app.basicAuthUser %>")
                .password("<%= app.basicAuthPass %>").roles("<%= app.basicAuthRole %>");
    }

    protected void configureBasicAuth(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(basicEntryPoint())
                .and()
                .sessionManagement().enableSessionUrlRewriting(false)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("<%= app.apiUrl %>/**").authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
    }

    protected void configureDigestAuth(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(digestEntryPoint())
                .and()
                .sessionManagement().enableSessionUrlRewriting(false)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("<%= app.apiUrl %>/**").authenticated()
                .and()
                .csrf().disable()
                .addFilter(digestAuthenticationFilter());
    }

    private DigestAuthenticationFilter digestAuthenticationFilter() throws Exception {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
        digestAuthenticationFilter.setUserDetailsService(userDetailsServiceBean());
        return digestAuthenticationFilter;
    }

    private static class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
            response.addHeader("Access-Control-Allow-Origin", "null");
            response.addHeader("WWW-Authenticate", "<%= app.projectName %>RestBasic realm=\"" + getRealmName() + "\"");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentLength(0);
        }
    }

}
