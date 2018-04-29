package hello;

import org.apache.log4j.Logger;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import hello.auth.WebSocketChannelSecurityConfig;

@Controller
public class GreetingController {

	Logger log = Logger.getLogger(GreetingController.class);
	Users user=new Users();
    @MessageMapping("/hello")
    public Greeting greeting(SimpMessageHeaderAccessor  headerAccessor,Authentication authentication) throws Exception {
        //Thread.sleep(1000); // simulated delay
        
        return new Greeting("Hello !");
    }
    
}