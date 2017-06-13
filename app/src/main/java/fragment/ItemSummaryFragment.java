package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import adapter.ItemSummaryAdapter;
import adapter.ItemSummaryResultAdapter;
import model.Items;
import pillar_technology.vendingmachine.R;

/**
 * Created by Yee on 5/30/17.
 */

public class ItemSummaryFragment extends Fragment {
    public static final String TAG = "ItemSummaryFragment";
    public static final String TAG_ITEMS = "SummaryFragmentItemComplex";

    Items mItems;
    RecyclerView mSummaryItemsList;
    RecyclerView mSummaryResultsList;

    public static ItemSummaryFragment newInstance(Items items) {
        Bundle args = new Bundle();
        args.putParcelable(TAG_ITEMS, items);
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
        assert mItems != null;
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        mSummaryResultsList = (RecyclerView) view.findViewById(R.id.summary_result_container);
        mSummaryResultsList.setLayoutManager(layoutManager1);
        mSummaryResultsList.setHasFixedSize(true);
        setupResultsAdapter();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        mSummaryItemsList = (RecyclerView) view.findViewById(R.id.summary_list);
        mSummaryItemsList.setLayoutManager(layoutManager2);
        mSummaryItemsList.setHasFixedSize(true);
        setupItemsAdapter();
        return view;
    }

    private void setupItemsAdapter() {
        ItemSummaryAdapter adapter = new ItemSummaryAdapter(getActivity(), mItems);
        mSummaryItemsList.setAdapter(adapter);
    }

    private void setupResultsAdapter() {
        ItemSummaryResultAdapter adapter = new ItemSummaryResultAdapter(getActivity(), mItems);
        mSummaryResultsList.setAdapter(adapter);
    }
}
