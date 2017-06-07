package fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import callback.InsertCoinDialogCallback;
import pillar_technology.vendingmachine.R;

/**
 * Created by Yee on 5/29/17.
 */

public class InsertCoinDialog extends DialogFragment {
    public static final String TAG = "InsertCoinDialog";
    public static final String TAG_TOTAL_COST = "InsertCoinDialogTotalCost";

    TextView mTotalCost;
    TextView mTotalSpend;
    ImageButton mNickelsAdd;
    ImageButton mNickelsSub;
    TextView mNickelSpend;
    ImageButton mDimeAdd;
    ImageButton mDimeSub;
    TextView mDimeSpend;
    ImageButton mQuarterAdd;
    ImageButton mQuarterSub;
    ImageButton mCancelBtn;
    TextView mQuarterSpend;
    Button mPay;
    double totalCost;
    double totalSpend;
    int nickelSpend;
    int dimeSpend;
    int quarterSpend;
    InsertCoinDialogCallback mCallback;

    public static InsertCoinDialog newInstance(double totalCost) {
        Bundle args = new Bundle();
        args.putDouble(TAG_TOTAL_COST, totalCost);
        InsertCoinDialog fragment = new InsertCoinDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_insert_coin_dialog);
        totalCost = getArguments().getDouble(TAG_TOTAL_COST);
        totalSpend = 0;
        nickelSpend = 0;
        dimeSpend = 0;
        quarterSpend = 0;
        mTotalCost = (TextView) dialog.findViewById(R.id.dialog_total_cost);
        mTotalCost.setText(String.format(Locale.US, "TOTAL: $%.02f", totalCost));
        mTotalSpend = (TextView) dialog.findViewById(R.id.dialog_total_spend);
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend));
        mNickelSpend = (TextView) dialog.findViewById(R.id.nickel_count);
        mNickelSpend.setText(String.valueOf(nickelSpend));
        mNickelsAdd = (ImageButton) dialog.findViewById(R.id.nickels_up);
        mNickelsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickelSpend++;
                totalSpend += 0.05;
                mNickelSpend.setText(String.valueOf(nickelSpend));
                mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend));
            }
        });
        mNickelsSub = (ImageButton) dialog.findViewById(R.id.nickels_down);
        mNickelsSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickelSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_nickels), Snackbar.LENGTH_SHORT).show();
                } else {
                    nickelSpend--;
                    totalSpend -= 0.05;
                    mNickelSpend.setText(String.valueOf(nickelSpend));
                    mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend));
                }
            }
        });
        mDimeSpend = (TextView) dialog.findViewById(R.id.dime_count);
        mDimeSpend.setText(String.valueOf(dimeSpend));
        mDimeAdd = (ImageButton) dialog.findViewById(R.id.dime_up);
        mDimeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimeSpend++;
                totalSpend += 0.10;
                mDimeSpend.setText(String.valueOf(dimeSpend));
                mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend));
            }
        });
        mDimeSub = (ImageButton) dialog.findViewById(R.id.dime_down);
        mDimeSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dimeSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_dime), Snackbar.LENGTH_SHORT).show();
                } else {
                    dimeSpend--;
                    totalSpend -= 0.10;
                    mDimeSpend.setText(String.valueOf(dimeSpend));
                    mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend));
                }
            }
        });
        mQuarterSpend = (TextView) dialog.findViewById(R.id.quarter_count);
        mQuarterSpend.setText(String.valueOf(quarterSpend));
        mQuarterAdd = (ImageButton) dialog.findViewById(R.id.quarter_up);
        mQuarterAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quarterSpend++;
                totalSpend += 0.25;
                mQuarterSpend.setText(String.valueOf(quarterSpend));
                mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend));
            }
        });
        mQuarterSub = (ImageButton) dialog.findViewById(R.id.quarter_down);
        mQuarterSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quarterSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_quarter), Snackbar.LENGTH_SHORT).show();
                } else {
                    quarterSpend--;
                    totalSpend -= 0.25;
                    mQuarterSpend.setText(String.valueOf(quarterSpend));
                    mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend));
                }
            }
        });
        mPay = (Button) dialog.findViewById(R.id.btn_dialog_pay);
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onDialogCallback(true, totalSpend, totalSpend - totalCost);
                dismiss();
            }
        });
        mCancelBtn = (ImageButton) dialog.findViewById(R.id.btn_dialog_cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void setCallback(InsertCoinDialogCallback callback) {
        mCallback = callback;
    }
}
