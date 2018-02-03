package hello.auth;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableWebSecurity
@EnableOAuth2Sso
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
public class HttpSecurityConfig {

	Logger log = Logger.getLogger(HttpSecurityConfig.class);

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

				List<GrantedAuthority> grantedAuthority = AuthorityUtils.createAuthorityList("ROLE_USER");
				
				if(username.equals("user") || username.equals("username")){
					return new User(username, "password", true, true, true, true,
							grantedAuthority);
				}else{
					throw new UsernameNotFoundException("could not find the user '" + username + "'");
				}
			}

		};
	}

	@RequestMapping("/user")
	
	public Principal user(Principal principal) {
		return principal;
	}

}