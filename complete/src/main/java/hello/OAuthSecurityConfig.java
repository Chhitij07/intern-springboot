package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import hello.auth.AuthenticationTokenFilter;
/**
 * Modifying or overriding the default spring boot security.
 */
@Configurable
@EnableWebSecurity
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    
    /*This method is used for override HttpSecurity of the web Application.
    We can specify our authorization criteria inside this method.*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // Starts authorizing configurations.
                .authorizeRequests()
                // Ignore the "/" and "/index.html"
                .antMatchers("/","/auth/login","/gs-guide-websocket/websocket", "/**.html", "/**.js").permitAll()
                // Authenticate all remaining URLs.
                .anyRequest().fullyAuthenticated()
                .and()
                // Setting the logout URL "/logout" - default logout URL.
                .logout()
                // After successful logout the application will redirect to "/" path.
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable();
                 }


    }
