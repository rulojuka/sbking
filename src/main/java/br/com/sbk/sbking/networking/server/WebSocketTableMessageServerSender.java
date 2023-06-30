package br.com.sbk.sbking.networking.server;

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
                .withMessage("deal")
                .withDeal(deal)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendFinishDealToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage("finishDeal")
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendInitializeDealToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage("initializeDeal")
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendInvalidRulesetToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage("invalidRuleset")
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendValidRulesetToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage("validRuleset")
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendPositiveOrNegativeToTable(PositiveOrNegative positiveOrNegative, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage("positiveOrNegative")
                .withContent(positiveOrNegative.toString().toUpperCase())
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendPositiveOrNegativeChooserToTable(Direction direction, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table)
                .withMessage("positiveOrNegativeChooser")
                .withDirection(direction)
                .build();
        this.tableController.sendMessage(tableDealDTO);
    }

    private Builder createBuilderWithTable(Table table) {
        return new TableMessageDTO.Builder().withTableId(table.getId().toString());
    }

}
