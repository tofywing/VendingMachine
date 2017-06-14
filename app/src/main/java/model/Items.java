package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Yee on 5/29/17.
 */

public class Items implements Parcelable {
    public static final String TAG = "items";
    private ArrayList<Item> items;
    private int machineCredit;
    private int userCreditLeft;
    private int totalAmount;
    private int creditInUse;
    private int userSpent;
    private int paymentDue;
    private boolean isCreditUsed;
    private boolean isPaymentDue;

    public Items() {
        items = new ArrayList<>();
        machineCredit = 0;
    }

    private Items(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
        machineCredit = in.readInt();
        userCreditLeft = in.readInt();
        totalAmount = in.readInt();
        creditInUse = in.readInt();
        userSpent = in.readInt();
        paymentDue = in.readInt();
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
        dest.writeInt(machineCredit);
        dest.writeInt(userCreditLeft);
        dest.writeInt(totalAmount);
        dest.writeInt(creditInUse);
        dest.writeInt(userSpent);
        dest.writeInt(paymentDue);
        dest.writeByte((byte) (isCreditUsed ? 1 : 0));
        dest.writeByte((byte) (isPaymentDue ? 1 : 0));
    }

    public void reloadItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addMachineCredit(int machineCredit) {
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

    public int getMachineCredit() {
        return machineCredit;
    }

    public int getUserCreditLeft() {
        return userCreditLeft;
    }

    public int getCreditInUse() {
        return creditInUse;
    }

    public int getUserSpent() {
        return userSpent;
    }

    public int getPaymentDue() {
        return paymentDue;
    }

    public boolean isCreditUsed() {
        return isCreditUsed;
    }

    public boolean isPaymentDue() {
        return isPaymentDue;
    }

    public void updateTotalAmount() {
        int totalCost = 0;
        for (Item item : items) {
            totalCost += item.getPrice() * item.getUnpaid();
        }
        this.totalAmount = totalCost;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void addCreditInUse(int userCreditInUsing) {
        this.creditInUse += userCreditInUsing;
    }

    public void addUserCreditLeft(int userCreditLeftCredit) {
        this.userCreditLeft += userCreditLeftCredit;
    }

    public void setMachineCredit(int machineCredit) {
        this.machineCredit = machineCredit;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setUserSpent(int userSpent) {
        this.userSpent = userSpent;
    }

    public void addUserSpent(int userSpent) {
        this.userSpent += userSpent;
    }

    public void setItem(int position, Item item) {
        items.set(position, item);
    }

    public void clearUnpaid() {
        for (Item item : items) {
            item.setUnpaid(0);
        }
    }

    public void setUserCreditLeft(int userCreditLeft) {
        this.userCreditLeft = userCreditLeft;
    }

    public void setCreditInUse(int creditInUse) {
        this.creditInUse = creditInUse;
    }

    public void setPaymentDue(int paymentDue) {
        this.paymentDue = paymentDue;
    }

    public void setIsCreditUsed(boolean isCreditUsed) {
        this.isCreditUsed = isCreditUsed;
        Log.d(TAG, isCreditUsed + "");
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

