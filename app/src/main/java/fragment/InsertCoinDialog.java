package fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import callback.InsertCoinDialogCallback;
import manager.DisplayAppearanceManager;
import model.Items;
import pillar_technology.vendingmachine.R;

import static pillar_technology.vendingmachine.R.id.quarter_stealth_button;

/**
 * Created by Yee on 5/29/17.
 */

public class InsertCoinDialog extends DialogFragment {
    public static final String TAG = "InsertCoinDialog";
    public static final String TAG_ITEMS = "InsertCoinDialogTransferredData";

    TextView mTotalCost;
    TextView mTotalSpend;
    TextView mNickelTitle;
    ImageView mNickelImage;
    ImageButton mNickelsAdd;
    ImageButton mNickelsSub;
    TextView mNickelSpend;
    ImageView mNickelStealthButton;
    TextView mDimeTitle;
    ImageView mDimeImage;
    ImageButton mDimeAdd;
    ImageButton mDimeSub;
    TextView mDimeSpend;
    ImageButton mDimeStealButton;
    TextView mQuarterTitle;
    ImageView mQuarterImage;
    ImageButton mQuarterAdd;
    ImageButton mQuarterSub;
    TextView mQuarterSpend;
    ImageButton mQuarterStealthButton;
    ImageButton mCancelBtn;
    Button mPay;
    int totalSpend;
    int nickelSpend;
    int dimeSpend;
    int quarterSpend;
    Items mItems;
    InsertCoinDialogCallback mCallback;
    float dialogTitleSize;
    float dialogSubSize;
    float subFontSize;

    public static InsertCoinDialog newInstance(Items items) {
        Bundle args = new Bundle();
        args.putParcelable(TAG_ITEMS, items);
        InsertCoinDialog fragment = new InsertCoinDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initialFontSize();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_insert_coin_dialog);
        mItems = getArguments().getParcelable(TAG_ITEMS);
        totalSpend = 0;
        nickelSpend = 0;
        dimeSpend = 0;
        quarterSpend = 0;
        mTotalCost = (TextView) dialog.findViewById(R.id.dialog_total_cost);
        mTotalCost.setTextSize(dialogTitleSize);
        mTotalCost.setText(String.format(Locale.US, "TOTAL: $%.02f", mItems.isPaymentDue() ? mItems
                .getPaymentDue() / 100f : mItems.getTotalAmount() / 100f));
        mTotalSpend = (TextView) dialog.findViewById(R.id.dialog_total_spend);
        mTotalSpend.setTextSize(dialogTitleSize);
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend / 100f));
        mNickelTitle = (TextView) dialog.findViewById(R.id.nickels);
        mNickelTitle.setTextSize(dialogSubSize);
        mNickelSpend = (TextView) dialog.findViewById(R.id.nickel_count);
        mNickelSpend.setTextSize(dialogSubSize);
        mNickelSpend.setText(String.valueOf(nickelSpend));
        mNickelsAdd = (ImageButton) dialog.findViewById(R.id.nickels_up);
        mNickelsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNickelAction();
            }
        });
        mNickelImage = (ImageView) dialog.findViewById(R.id.nickel_pic);
        mNickelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNickelAction();
            }
        });
        mNickelsSub = (ImageButton) dialog.findViewById(R.id.nickels_down);
        mNickelsSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickelSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_nickels), Snackbar.LENGTH_SHORT).show();
                } else {
                    deleteNickelAction();
                }
            }
        });
        mNickelStealthButton = (ImageView) dialog.findViewById(R.id.nickel_stealth_button);
        mNickelStealthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickelSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_nickels), Snackbar.LENGTH_SHORT).show();
                } else {
                    deleteNickelAction();
                }
            }
        });
        mDimeTitle = (TextView) dialog.findViewById(R.id.dime);
        mDimeTitle.setTextSize(dialogSubSize);
        mDimeSpend = (TextView) dialog.findViewById(R.id.dime_count);
        mDimeSpend.setTextSize(dialogSubSize);
        mDimeSpend.setText(String.valueOf(dimeSpend));
        mDimeAdd = (ImageButton) dialog.findViewById(R.id.dime_up);
        mDimeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDimeAction();
            }
        });
        mDimeImage = (ImageView) dialog.findViewById(R.id.dime_pic);
        mDimeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDimeAction();
            }
        });
        mDimeSub = (ImageButton) dialog.findViewById(R.id.dime_down);
        mDimeSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dimeSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_dime), Snackbar.LENGTH_SHORT).show();
                } else {
                    deleteDimeAction();
                }
            }
        });
        mDimeStealButton = (ImageButton) dialog.findViewById(R.id.dime_stealth_button);
        mDimeStealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dimeSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_dime), Snackbar.LENGTH_SHORT).show();
                } else {
                    deleteDimeAction();
                }
            }
        });
        mQuarterTitle = (TextView) dialog.findViewById(R.id.quarter);
        mQuarterTitle.setTextSize(dialogSubSize);
        mQuarterSpend = (TextView) dialog.findViewById(R.id.quarter_count);
        mQuarterSpend.setTextSize(dialogSubSize);
        mQuarterSpend.setText(String.valueOf(quarterSpend));
        mQuarterAdd = (ImageButton) dialog.findViewById(R.id.quarter_up);
        mQuarterAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuarterAction();
            }
        });
        mQuarterImage = (ImageView) dialog.findViewById(R.id.quarter_pic);
        mQuarterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuarterAction();
            }
        });
        mQuarterSub = (ImageButton) dialog.findViewById(R.id.quarter_down);
        mQuarterSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quarterSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_quarter), Snackbar.LENGTH_SHORT).show();
                } else {
                    deleteQuarterAction();
                }
            }
        });
        mQuarterStealthButton = (ImageButton) dialog.findViewById(quarter_stealth_button);
        mQuarterStealthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quarterSpend == 0) {
                    Snackbar.make(v, v.getContext().getString(R.string.alert_quarter), Snackbar.LENGTH_SHORT).show();
                } else {
                    deleteQuarterAction();
                }
            }
        });
        mPay = (Button) dialog.findViewById(R.id.btn_dialog_pay);
        mPay.setTextSize(subFontSize);
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payChecking()) {
                    Log.d(TAG, "paid: " + totalSpend + " left: " + (totalSpend - (mItems.isPaymentDue() ? mItems
                            .getPaymentDue() : mItems.getTotalAmount())));
                    mCallback.onDialogCallback(true, totalSpend, totalSpend - (mItems.isPaymentDue() ? mItems
                            .getPaymentDue() : mItems.getTotalAmount()));
                    dismiss();
                }
            }
        });
        mCancelBtn = (ImageButton) dialog.findViewById(R.id.btn_dialog_cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Log.d(TAG, mTotalCost.getTextSize() + " " + mNickelSpend.getTextSize());
        return dialog;
    }

    public void setCallback(InsertCoinDialogCallback callback) {
        mCallback = callback;
    }

    private boolean payChecking() {
        int different = totalSpend - mItems.getTotalAmount();
        boolean noProblem = different <= mItems.getMachineCredit();
        if (!noProblem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.alert_exact_change, mItems.getMachineCredit() / 100f));
            builder.setPositiveButton(R.string.alert_btn_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
        return noProblem;
    }

    private void deleteChecking() {

    }

    private void initialFontSize() {
        dialogTitleSize = DisplayAppearanceManager.dialogFragmentTitle;
        dialogSubSize = DisplayAppearanceManager.dialogFragmentSub;
        subFontSize = DisplayAppearanceManager.subFontSize;
    }

    private void addNickelAction() {
        nickelSpend++;
        totalSpend += 5;
        Log.d(TAG, "nickel add: " + 5 + " total spend: " + totalSpend);
        mNickelSpend.setText(String.valueOf(nickelSpend));
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend / 100f));
    }

    private void addDimeAction() {
        dimeSpend++;
        totalSpend += 10;
        Log.d(TAG, "dime add: " + 10 + " total spend: " + totalSpend);
        mDimeSpend.setText(String.valueOf(dimeSpend));
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend / 100f));
    }

    private void addQuarterAction() {
        quarterSpend++;
        totalSpend += 25;
        Log.d(TAG, "quarter add: " + 25 + " total spend: " + totalSpend);
        mQuarterSpend.setText(String.valueOf(quarterSpend));
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend / 100f));
    }

    private void deleteNickelAction() {
        nickelSpend--;
        totalSpend -= 5;
        Log.d(TAG, "nickel delete: " + 5 + " total spend: " + totalSpend);
        mNickelSpend.setText(String.valueOf(nickelSpend));
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend / 100f));
    }

    private void deleteDimeAction() {
        dimeSpend--;
        totalSpend -= 10;
        Log.d(TAG, "dime delete: " + 10 + " total spend: " + totalSpend);
        mDimeSpend.setText(String.valueOf(dimeSpend));
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend / 100f));
    }

    private void deleteQuarterAction() {
        quarterSpend--;
        totalSpend -= 25;
        Log.d(TAG, "quarter delete: " + 25 + " total spend: " + totalSpend);
        mQuarterSpend.setText(String.valueOf(quarterSpend));
        mTotalSpend.setText(String.format(Locale.US, "SPEND: $%.02f", totalSpend / 100f));
    }
}
