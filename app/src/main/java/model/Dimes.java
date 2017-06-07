package model;

/**
 * Created by Yee on 5/30/17.
 */

public class Dimes {
    private int amount;
    private double val;

    public Dimes() {
        amount = 0;
        val = 0.10;
    }

    void addDime() {
        amount++;
    }

    void removeDime() {
        amount--;
    }

    int getAmount() {
        return amount;
    }

    void SetAmount(double inputVal) {
        amount += (int) (inputVal / val);
    }
}
