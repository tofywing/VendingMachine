package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Yee on 5/29/17.
 */

public class Items implements Parcelable {
    private ArrayList<Item> items;
    private double machineCredit;
    private double userCreditLeft;
    private double totalCost;
    private double userCreditInUsing;

    public Items() {
        items = new ArrayList<>();
        machineCredit = 0;
    }

    private Items(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
        machineCredit = in.readDouble();
        userCreditLeft = in.readDouble();
        totalCost = in.readDouble();
        userCreditInUsing = in.readDouble();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeDouble(machineCredit);
        dest.writeDouble(userCreditLeft);
        dest.writeDouble(totalCost);
        dest.writeDouble(userCreditInUsing);
    }

    public void reloadItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addMachineCredit(double machineCredit) {
        this.machineCredit += machineCredit;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public int getCount() {
        return items.size();
    }

    public Item get(int position) {
        return items.get(position);
    }

    public double getMachineCredit() {
        return machineCredit;
    }

    public double getUserCreditLeft() {
        return userCreditLeft;
    }

    public double getUserCreditInUsing() {
        return userCreditInUsing;
    }

    public void updateTotalCost() {
        double totalCost = 0;
        for (Item item : items) {
            totalCost += item.getPrice() * item.getUnpaid();
        }
        this.totalCost = totalCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void addUserCreditInUsing(double userCreditInUsing) {
        this.userCreditInUsing += userCreditInUsing;
    }

    public void addUserCreditLeft(double userCreditLeftCredit) {
        this.userCreditLeft += userCreditLeftCredit;
    }

    public void setMachineCredit(double machineCredit) {
        this.machineCredit = machineCredit;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setItem(int position, Item item) {
        items.set(position, item);
    }

    public void clearUnpaid() {
        for (Item item : items) {
            item.setUnpaid(0);
        }
    }

    public void setUserCreditLeft(double userCreditLeft) {
        this.userCreditLeft = userCreditLeft;
    }

    public void setUserCreditInUsing(double userCreditInUsing) {
        this.userCreditInUsing = userCreditInUsing;
    }

    public String getItemAmountSummary() {
        String result = "";
        int total = 0;
        for (Item item : items) {
            total += item.getAmount();
            result += item.getName() + ": " + item.getAmount() + ", ";
        }
        result += total + " in total";
        return result;
    }
}

