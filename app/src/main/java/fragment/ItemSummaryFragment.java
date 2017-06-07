package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import adapter.ItemSummaryAdapter;
import model.Items;
import pillar_technology.vendingmachine.R;

/**
 * Created by Yee on 5/30/17.
 */

public class ItemSummaryFragment extends Fragment {
    public static final String TAG = "ItemSummaryFragment";
    public static final String TAG_ITEMS = "SummaryFragmentItemComplex";
    public static final String TAG_IS_USING_CREDIT = "SummaryFragmentIsUsingCredit";

    Items mItems;
    RecyclerView mRecyclerView;
    TextView mTotalCost;
    boolean mIsUsingCredit;

    public static ItemSummaryFragment newInstance(Items items, boolean isUsingCredit) {
        Bundle args = new Bundle();
        args.putParcelable(TAG_ITEMS, items);
        args.putBoolean(TAG_IS_USING_CREDIT, isUsingCredit);
        ItemSummaryFragment fragment = new ItemSummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_summary, container, false);
        mItems = getArguments().getParcelable(TAG_ITEMS);
        mIsUsingCredit = getArguments().getBoolean(TAG_IS_USING_CREDIT);
        assert mItems != null;
        double totalCost = mItems.getTotalCost();
        mTotalCost = (TextView) view.findViewById(R.id.total_cost);
        mTotalCost.setText(String.format(Locale.US, "Total: $%.02f", totalCost));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.item_summary_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        ItemSummaryAdapter adapter = new ItemSummaryAdapter(getActivity(), mItems, mIsUsingCredit);
        mRecyclerView.setAdapter(adapter);
    }
}
