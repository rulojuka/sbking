package br.com.sbk.sbking.clientapp;

import java.awt.EventQueue;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ClientApplication.class)
                .web(WebApplicationType.NONE) // No Spring Web
                .headless(false) // Not headless for swing to work
                .run(args);

        EventQueue.invokeLater(() -> {
            SBKingClientJFrame ex = ctx.getBean(SBKingClientJFrame.class);
            ex.setVisible(true);
        });
    }

}
