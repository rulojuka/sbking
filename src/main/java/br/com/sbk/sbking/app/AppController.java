package br.com.sbk.sbking.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AppController {

    @Autowired
    private ServerComponent serverComponent;

    @PostMapping("/playcard")
    void playCard(@RequestBody RequestCard requestCard) {
        serverComponent.getSbKingServer().play(requestCard.getCard(), requestCard.getUUID());
    }

}
