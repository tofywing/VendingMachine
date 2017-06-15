package model;

import java.util.ArrayList;

/**
 * Created by Yee on 5/30/17.
 */

public class Nickels {

    private int amount;
    private int val;

    public Nickels() {
        amount = 0;
        val = 5;
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

    void SetAmount(int inputVal) {
        amount += inputVal / val;
    }
}
