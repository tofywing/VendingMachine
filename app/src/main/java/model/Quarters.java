package model;

/**
 * Created by Yee on 5/30/17.
 */

public class Quarters {
    private int amount;
    private double val;

    public Quarters() {
        amount = 0;
        val = 0.25;
    }

    void addQuarter() {
        amount++;
    }

    void removeQuarter() {
        amount--;
    }

    int getAmount() {
        return amount;
    }

    void SetAmount(double inputVal) {
        amount += (int) (inputVal / val);
    }
}