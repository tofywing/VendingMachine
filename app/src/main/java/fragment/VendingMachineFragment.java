package fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import adapter.ItemSlideAdapter;
import callback.InsertCoinDialogCallback;
import manager.DisplayAppearanceManager;
import model.Items;
import pillar_technology.vendingmachine.R;
import tools.FadeEffect;

import static java.lang.Thread.sleep;
import static pillar_technology.vendingmachine.VendingMachineActivity.TAG_SHARED_PREFERENCE;

/**
 * Created by Yee on 5/29/17.
 */

public class VendingMachineFragment extends Fragment {
    public static final String TAG = "VendingMachineFragment";
    public static final String TAG_ITEMS = "VendingMachineFragmentTransferredItems";
    public static final String TAG_INSERT_COIN_DIALOG = "VendingMachineFragmentInsertCoinDialog";

    Items mItems;
    Button mPayButton;
    Button mUseCreditButton;
    Button mCashOutButton;
    InsertCoinDialog mInsertCoinDialog;
    FragmentManager mFragmentManager;
    ItemSummaryFragment mItemSummaryFragment;
    TextView mMachineHint;
    TextView mUserCredit;
    public FadeEffect mEffect;
    RelativeLayout mRelativeLayout;
    Activity mActivity;
    float titleFontSize;
    float restFontSize;

    public static VendingMachineFragment newInstance(Items items) {
        Bundle args = new Bundle();
        args.putParcelable(TAG_ITEMS, items);
        VendingMachineFragment fragment = new VendingMachineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_vending_machine, container, false);
        initialFontSize();
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.top_panel_container);
        mItems = getArguments().getParcelable(TAG_ITEMS);
        mFragmentManager = getChildFragmentManager();
        mUserCredit = (TextView) view.findViewById(R.id.text_credit);
        mUserCredit.setTextSize(restFontSize);
        mUserCredit.setText(String.format(Locale.US, "Store Credit: $%.02f", mItems.getUserCreditLeft()));
        mUseCreditButton = (Button) view.findViewById(R.id.btn_use_credit);
        mUseCreditButton.setTextSize(restFontSize);
        mUseCreditButton.setVisibility(mItems.getUserCreditLeft() > 0 && mItems.getTotalAmount() > 0 ? View.VISIBLE :
                View.INVISIBLE);
        mUseCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // is using credit true
                mItems.setIsCreditUsed(true);
                double totalCost = mItems.getTotalAmount();
                double userCreditLeft = mItems.getUserCreditLeft();
                if (totalCost >= userCreditLeft) {
                    //update total cost / unpaid / credit left
                    mItems.setTotalAmount(totalCost - userCreditLeft);
                    mItems.setCreditInUse(userCreditLeft);
                    mItems.setUserCreditLeft(0);
                } else {
                    //update total cost / credit in using / credit left
                    mItems.setTotalAmount(0);
                    mItems.setCreditInUse(totalCost);
                    mItems.setUserCreditLeft(userCreditLeft - totalCost);
                }
                mUserCredit.setText(String.format(Locale.US, "Store Credit: $%.02f", mItems.getUserCreditLeft()));
                mUseCreditButton.setVisibility(View.INVISIBLE);
                if (mItems.getUserCreditLeft() == 0) {
                    mCashOutButton.setVisibility(View.INVISIBLE);
                }
                createItemSummaryFragment();
            }
        });
        mPayButton = (Button) view.findViewById(R.id.btn_pay);
        mPayButton.setTextSize(restFontSize);
        mPayButton.setVisibility(mItems.getTotalAmount() > 0 ? View.VISIBLE : View.INVISIBLE);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mItems.setIsCreditUsed(false);
                if (mItems.getTotalAmount() == 0) {
                    //mIsPaymentDue = false;
                    mItems.setIsPaymentDue(false);
                    mItems.clearUnpaid();
                    mItems.updateTotalCost();
                    mItems.setCreditInUse(0);
                    mItems.setUserSpent(0);
                    dismissItemSummaryFragment();
                    mPayButton.setVisibility(View.INVISIBLE);
                    mUseCreditButton.setVisibility(View.INVISIBLE);
                    mEffect.interrupt();
                    mEffect = new FadeEffect();
                    mMachineHint.setText(getString(R.string.machine_hint_thank_you));
                    mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.azureBlue));
                    mEffect.action(1, mMachineHint);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Activity activity = getActivity();
                            if (activity != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mEffect.interrupt();
                                        mRelativeLayout.setBackgroundColor(ContextCompat.getColor
                                                (mActivity, R.color.pureWhite));
                                        mMachineHint.setText(mItems.getMachineCredit() == 0 ?
                                                getString(R.string.machine_hint_exact_change) : getString(R.string
                                                .machine_hint_insert_coin));
                                        mEffect = new FadeEffect();
                                        mEffect.action(3, mMachineHint);
                                    }
                                });
                            }
                        }
                    }, 3000);
                } else {
                    if (mInsertCoinDialog != null && mInsertCoinDialog.isAdded()) {
                        mInsertCoinDialog.dismiss();
                    }
                    mInsertCoinDialog = InsertCoinDialog.newInstance(mItems);
                    mInsertCoinDialog.setCallback(new InsertCoinDialogCallback() {
                        @Override
                        public void onDialogCallback(boolean paid, final double spent, final double left) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            if (left == 0) {
                                /*mIsCreditUsed = false;
                                mIsPaymentDue = false;*/
                                mItems.setIsCreditUsed(false);
                                mItems.setIsPaymentDue(false);
                                builder.setMessage(getString(R.string.alert_message_paid));
                            } else if (left > 0) {
                               /* mIsCreditUsed = false;
                                mIsPaymentDue = false;*/
                                mItems.setIsCreditUsed(false);
                                mItems.setIsPaymentDue(false);
                                builder.setMessage(getString(R.string.alert_message_over_paid, left));
                            } else {
                                //TODO: may not need to assign mISCreditUSed to true twice
                                /*mIsCreditUsed = true;
                                mIsPaymentDue = true;*/
                                mItems.setIsCreditUsed(true);
                                mItems.setIsPaymentDue(true);
                                builder.setMessage(getString(R.string.alert_message_due_payment, -left));
                            }
                            Log.d(TAG, "CreditInUsing" + mItems.getCreditInUse());
                            builder.setCancelable(false);
                            builder.setPositiveButton(R.string.alert_btn_confirm, new DialogInterface.OnClickListener
                                    () {

                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    Handler handler = new Handler();
                                    // payment finished
                                    // left >= 0, clearUnpaid, update total cost / add credit left / add machine
                                    // credit /
                                    // is using credit = false / set credit in using = 0
                                    if (left >= 0) {
                                        mItems.clearUnpaid();
                                        mItems.updateTotalCost();
                                        Log.d(TAG, "Total cost after pay: " + mItems.getTotalAmount());
                                        mItems.addUserCreditLeft(left);
                                        mItems.addMachineCredit(spent);
                                        Log.d(TAG, mItems.getMachineCredit() + "");
                                        mItems.setCreditInUse(0);
                                        mItems.setUserSpent(0);
                                        dismissItemSummaryFragment();
                                        mPayButton.setVisibility(View.INVISIBLE);
                                        mUseCreditButton.setVisibility(View.INVISIBLE);
                                        mEffect.interrupt();
                                        mEffect = new FadeEffect();
                                        dialog.dismiss();
                                        mMachineHint.setText(getString(R.string.machine_hint_thank_you));
                                        mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color
                                                .azureBlue));
                                        mEffect.action(1, mMachineHint);
                                    } else {
                                        /*payment not finished
                                        left < 0, set total cost / add machine credit
                                         mIsPaymentDue = true;
                                        set user spent money*/
                                        mItems.setIsPaymentDue(true);
                                        mItems.setUserSpent(spent);
                                        mItems.addMachineCredit(spent);
                                        mItems.setPaymentDue(-left);
                                        createItemSummaryFragment();
                                        handler = new Handler();
                                        mEffect.interrupt();
                                        mEffect = new FadeEffect();
                                        mMachineHint.setText(getString(R.string.machine_hint_money_required, -left));
                                        mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color
                                                .lightGreen));
                                        mEffect.action(1, mMachineHint);
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Activity activity = getActivity();
                                            if (activity != null) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mEffect.interrupt();
                                                        mRelativeLayout.setBackgroundColor(ContextCompat.getColor
                                                                (mActivity, R.color.pureWhite));
                                                        mMachineHint.setText(mItems.getMachineCredit() == 0 ?
                                                                getString(R.string.machine_hint_exact_change) :
                                                                getString(R.string.machine_hint_insert_coin));
                                                        mEffect = new FadeEffect();
                                                        mEffect.action(3, mMachineHint);
                                                    }
                                                });
                                            }
                                        }
                                    }, 3000);
                                    mUserCredit.setText(String.format(Locale.US, "Store Credit: $%.02f", mItems
                                            .getUserCreditLeft()));
                                    if (mItems.getUserCreditLeft() > 0) {
                                        mCashOutButton.setVisibility(View.VISIBLE);
                                    }
                                }
                            }).show();
                        }
                    });
                    mInsertCoinDialog.show(mFragmentManager, TAG_INSERT_COIN_DIALOG);
                }
            }
        });
        mMachineHint = (TextView) view.findViewById(R.id.text_payment_hint);
        mMachineHint.setTextSize(titleFontSize);
        mMachineHint.setText(mItems.getMachineCredit() == 0 ? getString(R.string.machine_hint_exact_change) :
                getString(R.string.machine_hint_insert_coin));
        mEffect = new FadeEffect();
        mEffect.action(3, mMachineHint);
        mCashOutButton = (Button) view.findViewById(R.id.btn_cash_out);
        mCashOutButton.setTextSize(restFontSize);
        mCashOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.alert_cash_out, mItems.getUserCreditLeft()));
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.alert_btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mItems.setUserCreditLeft(0);
                        mUserCredit.setText(String.format(Locale.US, "Store Credit: $%.02f", mItems.getUserCreditLeft
                                ()));
                        mCashOutButton.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.alert_btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        if (mItems.getUserCreditLeft() == 0) {
            mCashOutButton.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert mItems != null;
        generateUpdateViewPager(view);
        if (mItems.getTotalAmount() > 0) {
            createItemSummaryFragment();
        }
    }

    private void createItemSummaryFragment() {
        mFragmentManager = getChildFragmentManager();
        mItemSummaryFragment = (ItemSummaryFragment) mFragmentManager.findFragmentById(R.id.summary_container);
        if (mItemSummaryFragment != null) {
            mFragmentManager.beginTransaction().remove(mItemSummaryFragment).commitAllowingStateLoss();
        }
        mItemSummaryFragment = ItemSummaryFragment.newInstance(mItems);
        mFragmentManager.beginTransaction().add(R.id.summary_container, mItemSummaryFragment).commit();
    }

    public void generateUpdateViewPager(View view) {
        ItemSlideAdapter adapter = new ItemSlideAdapter(getContext(), mItems, this);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.item_slide_container);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(10);
        viewPager.setAdapter(adapter);
    }

    private void dismissItemSummaryFragment() {
        mFragmentManager = getChildFragmentManager();
        mItemSummaryFragment = (ItemSummaryFragment) mFragmentManager.findFragmentById(R.id.summary_container);
        if (mItemSummaryFragment != null) {
            mFragmentManager.beginTransaction().remove(mItemSummaryFragment).commitAllowingStateLoss();
        }
    }

    private void initialFontSize() {
        titleFontSize = DisplayAppearanceManager.titleFontSize;
        restFontSize = DisplayAppearanceManager.subFontSize;
        Log.d(TAG,titleFontSize + " " + restFontSize);
    }
}
