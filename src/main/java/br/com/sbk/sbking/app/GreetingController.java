package br.com.sbk.sbking.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import br.com.sbk.sbking.networking.websockets.Greeting;
import br.com.sbk.sbking.networking.websockets.HelloMessage;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new Greeting("Hello!" + time);
    }

}
