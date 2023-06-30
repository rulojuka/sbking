package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.core.MessageTypes.DEAL_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.FINISH_DEAL_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.GAME_MODE_OR_STRAIN_CHOOSER_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.INITIALIZE_DEAL_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.INVALID_RULESET_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.POSITIVE_OR_NEGATIVE_CHOOSER_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.POSITIVE_OR_NEGATIVE_MESSAGE;
import static br.com.sbk.sbking.core.MessageTypes.VALID_RULESET_MESSAGE;

import br.com.sbk.sbking.app.TableController;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.websockets.TableMessageDTO;
import br.com.sbk.sbking.networking.websockets.TableMessageDTO.Builder;

public class WebSocketTableMessageServerSender {

    private TableController tableController;

    public WebSocketTableMessageServerSender(TableController tableController) {
        this.tableController = tableController;
    }

    public void sendDealToTable(Deal deal, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(DEAL_MESSAGE)
                .withDeal(deal)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendFinishDealToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(FINISH_DEAL_MESSAGE)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendInitializeDealToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(INITIALIZE_DEAL_MESSAGE)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendInvalidRulesetToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(INVALID_RULESET_MESSAGE)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendValidRulesetToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(VALID_RULESET_MESSAGE)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendPositiveOrNegativeToTable(PositiveOrNegative positiveOrNegative, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(POSITIVE_OR_NEGATIVE_MESSAGE)
                .withContent(positiveOrNegative.toString().toUpperCase())
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendPositiveOrNegativeChooserToTable(Direction direction, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(POSITIVE_OR_NEGATIVE_CHOOSER_MESSAGE)
                .withDirection(direction)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendGameModeOrStrainChooserToTable(Direction direction, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage(GAME_MODE_OR_STRAIN_CHOOSER_MESSAGE)
                .withDirection(direction)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    private Builder createBuilderWithTable(Table table) {
        return new TableMessageDTO.Builder().withTableId(table.getId().toString());
    }

}
