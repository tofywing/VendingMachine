package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import model.Item;
import model.Items;
import pillar_technology.vendingmachine.R;

/**
 * Created by Yee on 5/30/17.
 */

public class ItemSummaryAdapter extends RecyclerView.Adapter<ItemSummaryAdapter.ItemHolder> {
    private Context mContext;
    private Items mItems;
    boolean mIsUsingCredit;

    public ItemSummaryAdapter(Context context, Items items, boolean useCredit) {
        mContext = context;
        mItems = items;
        mIsUsingCredit = useCredit;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.summary_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        //last position of the items
        if (position == mItems.getCount()) {
            holder.mItemName.setText(R.string.summary_user_credit);
            double creditInUsing = mItems.getUserCreditInUsing();
            holder.mItemCount.setText(String.format(Locale.US, "- $%.02f", creditInUsing));
        } else {
            Item item = mItems.get(position);
            holder.mItemName.setText(item.getName());
            holder.mItemCount.setText(String.format(Locale.US, "x %d", item.getUnpaid()));
        }
    }

    @Override
    public int getItemCount() {
        return mIsUsingCredit ? mItems.getCount() + 1 : mItems.getCount();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView mItemCount;
        TextView mItemName;

        ItemHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mItemCount = (TextView) itemView.findViewById(R.id.item_count);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
