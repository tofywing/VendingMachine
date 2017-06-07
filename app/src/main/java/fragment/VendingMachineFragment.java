package fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.util.Locale;

import adapter.ItemSlideAdapter;
import callback.InsertCoinDialogCallback;
import model.Items;
import pillar_technology.vendingmachine.R;
import pillar_technology.vendingmachine.VendingMachineActivity;
import tools.FadeEffect;

import static java.lang.Thread.sleep;

/**
 * Created by Yee on 5/29/17.
 */

public class VendingMachineFragment extends Fragment {
    public static final String TAG = "VendingMachineFragment";
    public static final String TAG_TRANSFER = "VendingMachineFragmentTransferredData";
    public static final String TAG_INSERT_COIN_DIALOG = "VendingMachineFragmentInsertCoinDialog";

    Items mItems;
    Button mPayButton;
    Button mUseCreditButton;
    Button mCashOut;
    InsertCoinDialog mInsertCoinDialog;
    FragmentManager mFragmentManager;
    ItemSummaryFragment mItemSummaryFragment;
    TextView mMachineHint;
    TextView mUserCredit;
    public FadeEffect mEffect;
    RelativeLayout mRelativeLayout;
    Activity mActivity;
    boolean isUsingCredit;

    public static VendingMachineFragment newInstance(Items items) {
        Bundle args = new Bundle();
        args.putParcelable(TAG_TRANSFER, items);
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
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.top_panel_container);
        mItems = getArguments().getParcelable(TAG_TRANSFER);
        mFragmentManager = getChildFragmentManager();
        mUserCredit = (TextView) view.findViewById(R.id.text_credit);
        mUserCredit.setText(String.format(Locale.US, "Credit: $%.02f", mItems.getUserCreditLeft()));
        mUseCreditButton = (Button) view.findViewById(R.id.btn_use_credit);
        mUseCreditButton.setVisibility(mItems.getUserCreditInUsing() > 0 ? View.VISIBLE : View.INVISIBLE);
        mUseCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUsingCredit = true;
                double totalCost = mItems.getTotalCost();
                double userCredit = mItems.getUserCreditLeft();
                if (totalCost >= userCredit) {
                    mItems.setTotalCost(totalCost - userCredit);
                    mItems.setUserCreditInUsing(userCredit);
                    mItems.setUserCreditLeft(0);
                } else {
                    mItems.setTotalCost(0);
                    mItems.setUserCreditInUsing(userCredit);
                    mItems.setUserCreditLeft(userCredit - totalCost);

                }
                mUserCredit.setText(String.format(Locale.US, "Credit: $%.02f", mItems.getUserCreditLeft()));
                mUseCreditButton.setVisibility(View.INVISIBLE);
                if (mItems.getUserCreditLeft() == 0) {
                    mCashOut.setVisibility(View.INVISIBLE);
                }
                Log.d(TAG, mItems.getUserCreditInUsing() + "");
                createItemSummaryFragment();
            }
        });
        mPayButton = (Button) view.findViewById(R.id.btn_pay);
        mPayButton.setVisibility(mItems.getTotalCost() > 0 ? View.VISIBLE : View.INVISIBLE);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                isUsingCredit = false;
                if (mInsertCoinDialog != null && mInsertCoinDialog.isAdded()) {
                    mInsertCoinDialog.dismiss();
                }
                mInsertCoinDialog = InsertCoinDialog.newInstance(mItems.getTotalCost());
                mInsertCoinDialog.setCallback(new InsertCoinDialogCallback() {
                    @Override
                    public void onDialogCallback(boolean paid, final double spend, final double left) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        if (left == 0) {
                            builder.setMessage("Purchase item.");
                        } else if (left > 0) {
                            builder.setMessage(String.format(Locale.US, "Purchase item. $%.02f is left.", left));
                        } else {
                            builder.setMessage(String.format(Locale.US, "Purchase item. $%.02f is owned.", -left));
                        }
                        Log.d(TAG, "CreditInUsing" + mItems.getUserCreditInUsing());
                        builder.setCancelable(false);
                        builder.setPositiveButton(R.string.alert_btn_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                Handler handler = new Handler();
                                if (left >= 0) {
                                    mItems.clearUnpaid();
                                    mItems.addUserCreditLeft(left);
                                    mItems.addMachineCredit(spend);
                                    Log.d(TAG, mItems.getMachineCredit() + "");
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
                                    mItems.setTotalCost(-left);
                                    isUsingCredit = true;
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
                                                    mMachineHint.setText(getString(R.string.machine_hint_insert_coin));
                                                    mEffect = new FadeEffect();
                                                    mEffect.action(3, mMachineHint);
                                                }
                                            });
                                        }
                                    }
                                }, 3000);
                                mUserCredit.setText(String.format(Locale.US, "Credit: $%.02f", mItems
                                        .getUserCreditLeft()));
                                if (mItems.getUserCreditLeft() > 0) {
                                    mCashOut.setVisibility(View.VISIBLE);
                                }
                            }
                        }).show();
                    }
                });
                mInsertCoinDialog.show(mFragmentManager, TAG_INSERT_COIN_DIALOG);
            }
        });
        mMachineHint = (TextView) view.findViewById(R.id.text_payment_hint);
        mEffect = new FadeEffect();
        mEffect.action(3, mMachineHint);
        mCashOut = (Button) view.findViewById(R.id.btn_cash_out);
        mCashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.alert_cash_out, mItems.getUserCreditLeft()));
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.alert_btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mItems.setUserCreditLeft(0);
                        mUserCredit.setText(String.format(Locale.US, "Credit: $%.02f", mItems.getUserCreditLeft()));
                        mCashOut.setVisibility(View.INVISIBLE);
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
            mCashOut.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert mItems != null;
        generateUpdateViewPager(view);
        if (mItems.getTotalCost() > 0) {
            createItemSummaryFragment();
        }
    }

    private void createItemSummaryFragment() {
        mFragmentManager = getChildFragmentManager();
        mItemSummaryFragment = (ItemSummaryFragment) mFragmentManager.findFragmentById(R.id.item_summary_container);
        if (mItemSummaryFragment != null) {
            mFragmentManager.beginTransaction().remove(mItemSummaryFragment).commitAllowingStateLoss();
        }
        mItemSummaryFragment = ItemSummaryFragment.newInstance(mItems, isUsingCredit);
        mFragmentManager.beginTransaction().add(R.id.item_summary_container, mItemSummaryFragment).commit();
    }

    public void generateUpdateViewPager(View view) {
        ItemSlideAdapter adapter = new ItemSlideAdapter(getContext(), mItems, this, isUsingCredit);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.item_slide_container);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(10);
        viewPager.setAdapter(adapter);
    }

    private void dismissItemSummaryFragment() {
        mFragmentManager = getChildFragmentManager();
        mItemSummaryFragment = (ItemSummaryFragment) mFragmentManager.findFragmentById(R.id.item_summary_container);
        if (mItemSummaryFragment != null) {
            mFragmentManager.beginTransaction().remove(mItemSummaryFragment).commitAllowingStateLoss();
        }
    }
}
