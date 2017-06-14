package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import manager.DisplayAppearanceManager;
import model.Items;
import pillar_technology.vendingmachine.R;

import static fragment.VendingMachineFragment.TAG;

/**
 * Created by Yee on 6/12/17.
 */

public class ItemSummaryResultAdapter extends RecyclerView.Adapter<ItemSummaryResultAdapter.ResultHolder> {

    private Context mContext;
    private boolean mIsCreditUsed;
    private boolean mIsPaymentDue;
    private Map<Integer, String> resultsDataSet;
    private ArrayList<Integer> mKeys;
    private Items mItems;
    private float titleFontSize;
    private float restFontSize;

    public ItemSummaryResultAdapter(Context context, Items items) {
        initialFontSize();
        mContext = context;
        mIsCreditUsed = items.isCreditUsed();
        mIsPaymentDue = items.isPaymentDue();
        mItems = items;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.summary_result_item, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        int key = mKeys.get(position);
        holder.mResult.setText(String.format(Locale.US, "%s %s", mContext.getString(key), resultsDataSet.get(key)));
    }

    @Override
    public int getItemCount() {
        // credit & total & paid & owe
        if (mIsCreditUsed && mIsPaymentDue) {
            resultsDataSet = new LinkedHashMap<>();
            if (mItems != null) {
                resultsDataSet.put(R.string.summary_result_credit_use, String.format(Locale.US, "-$%.02f", mItems
                        .getCreditInUse() / 100f));
                resultsDataSet.put(R.string.summary_result_total_amount, String.format(Locale.US, "$%.02f", mItems
                        .getTotalAmount() / 100f));
                resultsDataSet.put(R.string.summary_result_total_payment, String.format(Locale.US, "-$%.02f", mItems
                        .getUserSpent() / 100f));
                resultsDataSet.put(R.string.summary_result_amount_due, String.format(Locale.US, "$%.02f", mItems
                        .getPaymentDue() / 100f));
                mKeys = new ArrayList<>(resultsDataSet.keySet());
            }
            return 4;
        }
        // credit & total
        else if (mIsCreditUsed) {
            resultsDataSet = new LinkedHashMap<>();
            if (mItems != null) {
                resultsDataSet.put(R.string.summary_result_credit_use, String.format(Locale.US, "-$%.02f", mItems
                        .getCreditInUse() / 100f));
                resultsDataSet.put(R.string.summary_result_total_amount, String.format(Locale.US, "$%.02f", mItems
                        .getTotalAmount() / 100f));
                mKeys = new ArrayList<>(resultsDataSet.keySet());
            }
            return 2;
        }
        // total & paid & owe
        else if (mIsPaymentDue) {
            resultsDataSet = new LinkedHashMap<>();
            if (mItems != null) {
                resultsDataSet.put(R.string.summary_result_total_amount, String.format(Locale.US, "$%.02f", mItems
                        .getTotalAmount() / 100f));
                resultsDataSet.put(R.string.summary_result_total_payment, String.format(Locale.US, "-$%.02f", mItems
                        .getUserSpent() / 100f));
                resultsDataSet.put(R.string.summary_result_amount_due, String.format(Locale.US, "$%.02f", mItems
                        .getPaymentDue() / 100f));
                mKeys = new ArrayList<>(resultsDataSet.keySet());
            }
            return 3;
        }
        // total
        else {
            resultsDataSet = new LinkedHashMap<>();
            if (mItems != null) {
                resultsDataSet.put(R.string.summary_result_total_amount, String.format(Locale.US, "$%.02f", mItems
                        .getTotalAmount() / 100f));
                mKeys = new ArrayList<>(resultsDataSet.keySet());
            }
            return 1;
        }
    }

    class ResultHolder extends RecyclerView.ViewHolder {

        TextView mResult;

        ResultHolder(View itemView) {
            super(itemView);
            mResult = (TextView) itemView.findViewById(R.id.summary_result_item);
            mResult.setTextSize(restFontSize);
        }
    }

    private void initialFontSize() {
        titleFontSize = DisplayAppearanceManager.titleFontSize;
        restFontSize = DisplayAppearanceManager.subFontSize;
    }
}
