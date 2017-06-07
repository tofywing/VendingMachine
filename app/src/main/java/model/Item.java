package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Yee on 5/29/17.
 */

public class Item implements Parcelable {
    private int imageSrc;
    private String name;
    private int amount;
    private int unpaid;
    private double price;

    public Item(int imageSrc, String name, int amount, double price, int inStock, int unpaid) {
        this.imageSrc = imageSrc;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.unpaid = unpaid;
    }

    private Item(Parcel in) {
        imageSrc = in.readInt();
        name = in.readString();
        amount = in.readInt();
        price = in.readDouble();
        unpaid = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUnpaid(int purchased) {
        this.unpaid = purchased;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public int getUnpaid() {
        return unpaid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageSrc);
        dest.writeString(name);
        dest.writeInt(amount);
        dest.writeDouble(price);
        dest.writeInt(unpaid);
    }
}
