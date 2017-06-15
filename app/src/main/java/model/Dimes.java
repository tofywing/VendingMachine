package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yee on 5/30/17.
 */

public class Dimes {
    private int amount;
    private int val;
    boolean a;

    public Dimes() {
        amount = 0;
        val = 10;
    }

    protected Dimes(Parcel in) {
        amount = in.readInt();
        val = in.readInt();
        a = in.readByte() != 0;
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

    void SetAmount(int inputVal) {
        amount += inputVal / val;
    }
}
