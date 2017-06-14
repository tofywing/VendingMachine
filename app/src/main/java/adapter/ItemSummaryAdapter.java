package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import manager.DisplayAppearanceManager;
import model.Item;
import model.Items;
import pillar_technology.vendingmachine.R;

/**
 * Created by Yee on 5/30/17.
 */

public class ItemSummaryAdapter extends RecyclerView.Adapter<ItemSummaryAdapter.ItemHolder> {
    private Context mContext;
    private Items mItems;
    private float titleFontSize;
    private float restFontSize;

    public ItemSummaryAdapter(Context context, Items items) {
        initialFontSize();
        mContext = context;
        mItems = items;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.summary_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        //First position of the items
        if (position == 0) {
            holder.mItemName.setText(R.string.summary_title_item);
            holder.mItemQty.setText(R.string.summary_title_qty);
            holder.mItemAmount.setText(R.string.summary_title_amount);
            holder.mItemPrice.setText(R.string.summary_title_price);
            //Last position of the items
        } else {
            Item item = mItems.get(position - 1);
            holder.mItemName.setText(item.getName());
            holder.mItemQty.setText(String.format(Locale.US, "%d", item.getUnpaid()));
            holder.mItemAmount.setText(String.format(Locale.US, "$%.02f", item.getUnpaid() * item.getPrice() / 100f));
            holder.mItemPrice.setText(String.format(Locale.US, "$%.02f", item.getPrice() / 100f));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.getCount() + 1;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView mItemQty;
        TextView mItemName;
        TextView mItemAmount;
        TextView mItemPrice;

        ItemHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mItemQty = (TextView) itemView.findViewById(R.id.item_count);
            mItemQty.setTextSize(restFontSize);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
            mItemName.setTextSize(restFontSize);
            mItemAmount = (TextView) itemView.findViewById(R.id.item_amount);
            mItemAmount.setTextSize(restFontSize);
            mItemPrice = (TextView) itemView.findViewById(R.id.item_price);
            mItemPrice.setTextSize(restFontSize);
        }
    }

    private void initialFontSize() {
        titleFontSize = DisplayAppearanceManager.titleFontSize;
        restFontSize = DisplayAppearanceManager.subFontSize;
    }
}
