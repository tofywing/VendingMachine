package adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import model.Item;
import model.Items;
import fragment.ItemSummaryFragment;
import pillar_technology.vendingmachine.R;
import fragment.VendingMachineFragment;
import tools.FadeEffect;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by Yee on 5/29/17.
 */

public class ItemSlideAdapter extends PagerAdapter {
    public static final String TAG = "ItemSlideAdapter";

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private Items mItems;
    private FragmentManager mFragmentManager;
    private ItemSummaryFragment mItemSummaryFragment;
    private VendingMachineFragment mVendingMachineFragment;
    private TextView mMachineHint;
    private RelativeLayout mRelativeLayout;
    private Button mPayButton;
    private Button mUseCredit;
    private boolean mIsUsingCredit;

    public ItemSlideAdapter(Context context, Items items, VendingMachineFragment fragment, boolean isUsingCredit) {
        mContext = context;
        mVendingMachineFragment = fragment;
        mIsUsingCredit = isUsingCredit;
        if (mVendingMachineFragment != null) {
            View view = mVendingMachineFragment.getView();
            assert view != null;
            mMachineHint = (TextView) view.findViewById(R.id.text_payment_hint);
            mPayButton = (Button) view.findViewById(R.id.btn_pay);
            mUseCredit = (Button) view.findViewById(R.id.btn_use_credit);
            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.top_panel_container);
            mFragmentManager = fragment.getChildFragmentManager();
        }
        mItems = items;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mLayoutInflater.inflate(R.layout.slide_item, container, false);
        Item item = mItems.get(position);
        ImageView itemImage = (ImageView) view.findViewById(R.id.item_pic);
        itemImage.setImageResource(item.getImageSrc());
        ImageView addButton = (ImageView) view.findViewById(R.id.button_add_item);
        TextView itemPrice = (TextView) view.findViewById(R.id.text_price);
        itemPrice.setText(String.format(Locale.US, "Price: $%.02f", item.getPrice()));
        final TextView itemAmount = (TextView) view.findViewById(R.id.text_amount);
        itemAmount.setText(String.format(Locale.US, "Amount: %d", item.getAmount()));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsAddAction(position, v, itemAmount);
            }
        });
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsAddAction(position, v, itemAmount);
            }
        });
        ImageView deleteButton = (ImageView) view.findViewById(R.id.button_delete_item);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = mItems.get(position);
                int unpaid = item.getUnpaid();
                if (unpaid == 0) {
                    Snackbar.make(v, mContext.getString(R.string.item_empty, item.getName()), LENGTH_SHORT).show();
                } else {
                    if (unpaid > 0) {
                        item.setAmount(item.getAmount() + 1);
                        item.setUnpaid(--unpaid);
                        itemAmount.setText(String.format(Locale.US, "Amount: %d", item.getAmount()));
                        mItems.setItem(position, item);
                        mItems.updateTotalCost();
                        Log.d(TAG, mItems.getTotalCost() + "");
                        createItemSummaryFragment();
                    }
                    if (unpaid == 0) {
                        if (mItems.getTotalCost() == 0) {
                            if (mItemSummaryFragment != null) {
                                mFragmentManager.beginTransaction().remove(mItemSummaryFragment)
                                        .commitAllowingStateLoss();
                                Snackbar.make(v, mContext.getString(R.string.cart_empty), LENGTH_SHORT).show();
                            }
                            mPayButton.setVisibility(View.INVISIBLE);
                            mUseCredit.setVisibility(View.INVISIBLE);
                            if (mIsUsingCredit) {
                                mItems.addUserCreditLeft(mItems.getUserCreditInUsing());
                            }
                        }
                    }
                    Snackbar.make(v, mContext.getString(R.string.alert_delete, item.getName()), LENGTH_SHORT).show();
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void createItemSummaryFragment() {
        mItemSummaryFragment = (ItemSummaryFragment) mFragmentManager.findFragmentById(R.id.item_summary_container);
        if (mItemSummaryFragment != null) {
            mFragmentManager.beginTransaction().remove(mItemSummaryFragment).commitAllowingStateLoss();
        }
        mItemSummaryFragment = ItemSummaryFragment.newInstance(mItems, mIsUsingCredit);
        mFragmentManager.beginTransaction().add(R.id.item_summary_container, mItemSummaryFragment).commit();
    }

    private void itemsAddAction(int position, View v, TextView itemAmount) {
        Item item = mItems.get(position);
        if (item.getAmount() == 0) {
            Snackbar.make(v, mContext.getString(R.string.alter_add), LENGTH_SHORT).show();
            Handler handler = new Handler();
            mVendingMachineFragment.mEffect.interrupt();
            mVendingMachineFragment.mEffect = new FadeEffect();
            mMachineHint.setText(mContext.getString(R.string.machine_hint_sold_out));
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mVendingMachineFragment.getActivity(), R.color
                    .pureRed));
            mVendingMachineFragment.mEffect.action(1, mMachineHint);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Activity activity = mVendingMachineFragment.getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mVendingMachineFragment.mEffect.interrupt();
                                mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mVendingMachineFragment
                                        .getActivity(), R.color.pureWhite));
                                mMachineHint.setText(mItems.getMachineCredit() == 0 ? mContext.getString(R
                                        .string.machine_hint_exact_change) : mContext.getString(R.string
                                        .machine_hint_insert_coin));
                                mVendingMachineFragment.mEffect = new FadeEffect();
                                mVendingMachineFragment.mEffect.action(3, mMachineHint);
                            }
                        });
                    }
                }
            }, 3000);
        } else {
            mPayButton.setVisibility(View.VISIBLE);
            if (mItems.getUserCreditLeft() > 0) {
                mUseCredit.setVisibility(View.VISIBLE);
            }
            item.setAmount(item.getAmount() - 1);
            item.setUnpaid(item.getUnpaid() + 1);
            itemAmount.setText(String.format(Locale.US, "Amount: %d", item.getAmount()));
            mItems.updateTotalCost();
            createItemSummaryFragment();
        }
    }
}
