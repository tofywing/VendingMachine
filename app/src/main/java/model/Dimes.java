package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yee on 5/30/17.
 */

public class Dimes implements Parcelable{
    private int amount;
    private double val;
    boolean a;

    public Dimes() {
        amount = 0;
        val = 0.10;
    }

    protected Dimes(Parcel in) {
        amount = in.readInt();
        val = in.readDouble();
        a = in.readByte() != 0;
    }

    public static final Creator<Dimes> CREATOR = new Creator<Dimes>() {
        @Override
        public Dimes createFromParcel(Parcel in) {
            return new Dimes(in);
        }

        @Override
        public Dimes[] newArray(int size) {
            return new Dimes[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amount);
        dest.writeDouble(val);
        dest.writeByte((byte) (a ? 1 : 0));
    }
}
