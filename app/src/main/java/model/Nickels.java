package model;

import java.util.ArrayList;

/**
 * Created by Yee on 5/30/17.
 */

public class Nickels {

    private int amount;
    private double val;

    public Nickels() {
        amount = 0;
        val = 0.05;
    }

    void addNickel() {
        amount++;
    }

    void removeNickel() {
        amount--;
    }

    int getAmount() {
        return amount;
    }

    void SetAmount(double inputVal) {
        amount += (int) (inputVal / val);
    }
}
