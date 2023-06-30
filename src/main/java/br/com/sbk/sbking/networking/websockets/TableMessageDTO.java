package br.com.sbk.sbking.networking.websockets;

import br.com.sbk.sbking.core.Deal;

public class TableMessageDTO {

    private String message;
    private String tableId;
    private Deal deal;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

}
