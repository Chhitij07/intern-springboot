package hello.auth;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import groovy.util.logging.Log;
import groovyjarjarantlr.collections.List;
import hello.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketChannelSecurityConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Value("${token.header}")
	private String tokenHeader;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private UserDetailsService userDetailsService;
	Logger log = Logger.getLogger(WebSocketChannelSecurityConfig.class);

	private DefaultSimpUserRegistry userRegistry = new DefaultSimpUserRegistry();
	private DefaultUserDestinationResolver resolver = new DefaultUserDestinationResolver(userRegistry);

	Users users=new Users();
	
	@Bean
	@Primary
	public SimpUserRegistry userRegistry() {
		return userRegistry;
	}

	@Bean
	@Primary
	public UserDestinationResolver userDestinationResolver() {
		return resolver;
	}

	public static ArrayList<Map<String, String>> userList=new ArrayList<Map<String,String>>();
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(new ChannelInterceptorAdapter() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				// if
				// (StompCommand.CONNECT.equals(accessor.getHeader("stompCommand"))
				// ||
				// StompCommand.SUBSCRIBE.equals(accessor.getHeader("stompCommand")))
				// {
				String authToken = null;

				if (StompCommand.SUBSCRIBE.equals(accessor.getHeader("stompCommand"))) {
					// Object headers =
					// accessor.getNativeHeader("headers").get(0);
					authToken = accessor.getNativeHeader(tokenHeader.toUpperCase()).get(0);
					// authToken = headers.toString();//
					// .get(tokenHeader.toUpperCase());
					log.info("WS Subscribe headers: " + authToken);
					
					users.add((accessor.getUser()).getName(),accessor.getUser().getName());
					
				}
				try {
					authToken = accessor.getNativeHeader(tokenHeader.toUpperCase()).get(0);
				} catch (NullPointerException npe) {
					log.warn("X-AUTH-TOKEN not found with this request");
				}
				String username = null;
				if (authToken != null) {
					username = tokenUtils.getUsernameFromToken(authToken);
					log.info("The username is"+username);
				}

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (tokenUtils.validateToken(authToken, userDetails)) {
						
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						// authentication.setDetails(new
						// WebAuthenticationDetailsSource().buildDetails(httpRequest));
						// SecurityContextHolder.getContext().setAuthentication(authentication);
						accessor.setUser((Principal) authentication);
						// not documented anywhere but necessary otherwise NPE
						// in
						// StompSubProtocolHandler!
						if (accessor.getMessageType() == SimpMessageType.CONNECT) {
							userRegistry.onApplicationEvent(new SessionConnectedEvent(this, (Message<byte[]>) message,
									(Principal) authentication));
							
							} else if (accessor.getMessageType() == SimpMessageType.SUBSCRIBE) {
							userRegistry.onApplicationEvent(new SessionSubscribeEvent(this, (Message<byte[]>) message,
									(Principal) authentication));
							
						} else if (accessor.getMessageType() == SimpMessageType.UNSUBSCRIBE) {
							userRegistry.onApplicationEvent(new SessionUnsubscribeEvent(this, (Message<byte[]>) message,
									(Principal) authentication));
								
						} else if (accessor.getMessageType() == SimpMessageType.DISCONNECT) {
							userRegistry.onApplicationEvent(new SessionDisconnectEvent(this, (Message<byte[]>) message,
									accessor.getSessionId(), CloseStatus.NORMAL));
							Map<String,String> user=new HashMap<String,String>();
							user.put(username, "Subscribed");
						}

					}
				}
				// }
				try {
					accessor.setLeaveMutable(true);
				} catch (Exception exe) {
				}

				return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());

			}
		});
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry arg0) {
		// TODO Auto-generated method stub
	}
}