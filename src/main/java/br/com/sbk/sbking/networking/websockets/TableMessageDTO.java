package br.com.sbk.sbking.networking.websockets;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class TableMessageDTO {

    private String message;
    private String tableId;
    private Deal deal;
    private String content;
    private Direction direction;

    public TableMessageDTO() {
    }

    private TableMessageDTO(String message, String tableId, Deal deal, String content,
            Direction direction) {
        this.message = message;
        this.tableId = tableId;
        this.deal = deal;
        this.content = content;
        this.direction = direction;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public static final class Builder {
        private String message;
        private String tableId;
        private Deal deal;
        private String content;
        private Direction direction;

        public Builder() {
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withTableId(String tableId) {
            this.tableId = tableId;
            return this;
        }

        public Builder withDeal(Deal deal) {
            this.deal = deal;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public TableMessageDTO build() {
            return new TableMessageDTO(this.message, this.tableId, this.deal, this.content, this.direction);
        }
    }
}
