package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.stomp.StompSession;

@SpringBootApplication
public class ClientApplication {

    @Bean
    public CountDownLatch closeLatch() {
        return new CountDownLatch(1);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ClientApplication.class)
                .web(WebApplicationType.NONE) // No Spring Web
                .headless(false) // Not headless for swing to work
                .run(args);

        // Check
        // https://stackoverflow.com/questions/52331480/keep-spring-boot-application-alive-with-websocket-connection
        // This is for the application to not exit after configuring. Also, to exit
        // after a ^C
        final CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                closeLatch.countDown();
            }
        });

        // Main "loop"
        MyApplicationWebSocketClient webSocketClientComponent = ctx.getBean(MyApplicationWebSocketClient.class);
        LOGGER.trace("Running MyApplicationWebSocketClient");
        StompSession stompSession = webSocketClientComponent.connect();

        ClientComponent clientComponent = ctx.getBean(ClientComponent.class);
        LOGGER.trace("Running ClientComponent");
        clientComponent.getSBKingClient().setStompSession(stompSession);
        clientComponent.run(); // This takes hold of the thread forever
        // Execution never gets to this line
        LOGGER.error("SHOULD NOT GET HERE");

        try {
            closeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // EventQueue.invokeLater(() -> {
        // SBKingClientJFrame ex = ctx.getBean(SBKingClientJFrame.class);
        // ex.setVisible(true);
        // });
    }

}
