package br.com.sbk.sbking.persistence;

import java.math.BigInteger;
import java.util.UUID;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.PavlicekNumber;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Access(AccessType.FIELD)
@Entity(name = "Board")
public class BoardEntity {

    @Id
    private UUID id;

    @Transient
    private BigInteger bigIntegerPavlicekNumber;

    @Access(AccessType.PROPERTY)
    @Column
    private String pavlicekNumber = null; // This field should never be used, only its accessors.

    @Transient
    private Board board;

    public BoardEntity() {
        this.id = UUID.randomUUID();
    }

    public BoardEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Syncs everything with the following priority:
     * 1. bigIntegerPavlicekNumber
     * 2. board
     */
    private void syncFields() {
        PavlicekNumber pavlicekNumberGenerator = new PavlicekNumber();
        if (this.bigIntegerPavlicekNumber != null) {
            this.board = pavlicekNumberGenerator.getBoardFromNumber(this.bigIntegerPavlicekNumber);
        } else if (this.board != null) {
            this.bigIntegerPavlicekNumber = pavlicekNumberGenerator.getNumberFromBoard(this.board);
        }
    }

    public String getPavlicekNumber() {
        if (this.bigIntegerPavlicekNumber != null) {
            return this.bigIntegerPavlicekNumber.toString();
        }
        return "";
    }

    public void setPavlicekNumber(String pavlicekNumber) {
        this.bigIntegerPavlicekNumber = new BigInteger(pavlicekNumber);
        this.board = null;
        this.syncFields();
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.bigIntegerPavlicekNumber = null;
        this.board = board;
        this.syncFields();
    }

}
