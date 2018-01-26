package hello.auth;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${route.authentication}")
public class AuthController {

	private final Logger log = Logger.getLogger(this.getClass());

	@Value("${token.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "${route.authentication.login}", method = RequestMethod.POST)
	public ResponseEntity<?> authenticationRequest(@RequestBody DisplayUser user, Device device)
			throws AuthenticationException {

		// Perform the authentication
		Authentication authentication = this.authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-authentication so we can generate token
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
				
		String token = this.tokenUtils.generateToken(userDetails, device);

		// Return the token
		// mimic the oauth2 token for simplicity instead of creating a similar
		// token class
		// providing token as an argumenet for the sake of initializing the
		// class hoping it would not break while parsing
		DefaultOAuth2AccessToken authResponse = new DefaultOAuth2AccessToken(token);
		// setting token here
		authResponse.setValue(token);
		return ResponseEntity.ok(authResponse);
	}

}