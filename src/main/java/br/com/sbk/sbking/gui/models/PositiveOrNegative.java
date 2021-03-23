package br.com.sbk.sbking.gui.models;

public class PositiveOrNegative {

    private Boolean positive = null;
    private Boolean negative = null;
    private boolean selected = false;

    public synchronized void setPositive() {
        this.positive = true;
        this.negative = false;
        this.selected = true;
    }

    public synchronized void setNegative() {
        this.positive = false;
        this.negative = true;
        this.selected = true;
    }

    public boolean isPositive() {
        if (this.positive == null)
            throw new RuntimeException("PositiveOrNegative not initialized");
        return this.positive;
    }

    public boolean isNegative() {
        if (this.negative == null)
            throw new RuntimeException("PositiveOrNegative not initialized");
        return this.negative;
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public String toString() {
        if (!this.isSelected()) {
            return "";
        } else if (this.isPositive()) {
            return "Positive";
        } else if (this.isNegative()) {
            return "Negative";
        }
        return "";
    }

}
