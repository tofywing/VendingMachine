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
    private double totalAmount;
    private double creditInUse;
    private double userSpent;
    private double paymentDue;
    private boolean isCreditUsed;
    private boolean isPaymentDue;

    public Items() {
        items = new ArrayList<>();
        machineCredit = 0;
    }

    private Items(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
        machineCredit = in.readDouble();
        userCreditLeft = in.readDouble();
        totalAmount = in.readDouble();
        creditInUse = in.readDouble();
        userSpent = in.readDouble();
        paymentDue = in.readDouble();
        isCreditUsed = in.readByte() != 0;
        isPaymentDue = in.readByte() != 0;
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
        dest.writeDouble(totalAmount);
        dest.writeDouble(creditInUse);
        dest.writeDouble(userSpent);
        dest.writeDouble(paymentDue);
        dest.writeByte((byte) (isCreditUsed ? 1 : 0));
        dest.writeByte((byte) (isPaymentDue ? 1 : 0));
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

    public double getCreditInUse() {
        return creditInUse;
    }

    public double getUserSpent() {
        return userSpent;
    }

    public double getPaymentDue() {
        return paymentDue;
    }

    public boolean isCreditUsed() {
        return isCreditUsed;
    }

    public boolean isPaymentDue() {
        return isPaymentDue;
    }

    public void updateTotalCost() {
        double totalCost = 0;
        for (Item item : items) {
            totalCost += item.getPrice() * item.getUnpaid();
        }
        this.totalAmount = totalCost;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addCreditInUse(double userCreditInUsing) {
        this.creditInUse += userCreditInUsing;
    }

    public void addUserCreditLeft(double userCreditLeftCredit) {
        this.userCreditLeft += userCreditLeftCredit;
    }

    public void setMachineCredit(double machineCredit) {
        this.machineCredit = machineCredit;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setUserSpent(double userSpent) {
        this.userSpent = userSpent;
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

    public void setCreditInUse(double creditInUse) {
        this.creditInUse = creditInUse;
    }

    public void setPaymentDue(double paymentDue) {
        this.paymentDue = paymentDue;
    }

    public void setIsCreditUsed(boolean isCreditUsed) {
        this.isCreditUsed = isCreditUsed;
    }

    public void setIsPaymentDue(boolean isPaymentDue) {
        this.isPaymentDue = isPaymentDue;
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

