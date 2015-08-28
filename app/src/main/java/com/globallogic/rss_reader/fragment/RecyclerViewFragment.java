package com.globallogic.rss_reader.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.globallogic.rss_reader.R;
import com.globallogic.rss_reader.adapter.RssAdapter;
import com.globallogic.rss_reader.decoration.DividerItemDecoration;
import com.globallogic.rss_reader.interfaces.IItemClick;
import com.globallogic.rss_reader.model.Item;
import com.globallogic.rss_reader.viewmodel.RSSViewModel;

import java.util.List;

/**
 * Created by diego.rotondale on 12/05/2015.
 */
public class RecyclerViewFragment extends Fragment implements RssAdapter.OnItemClickListener, RSSViewModel.ICallback {
    private static final String TAG = RecyclerViewFragment.class.getSimpleName();

    private RecyclerView mRssRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RssAdapter mAdapter;

    private View vProgress;
    private IItemClick mCallback;

    private RSSViewModel mRSSViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view, container, false);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new RssAdapter();
        mAdapter.setOnItemClickListener(this);

        mRssRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_rss);
        mRssRecyclerView.setLayoutManager(mLayoutManager);
        mRssRecyclerView.setAdapter(mAdapter);

        mRssRecyclerView.setHasFixedSize(true);
        mRssRecyclerView.addItemDecoration(new DividerItemDecoration(1, Color.BLACK));
        mRssRecyclerView.setItemAnimator(new DefaultItemAnimator());

        vProgress = view.findViewById(R.id.recycle_view_progress);

        mRSSViewModel = new RSSViewModel(getActivity(), this);
        return view;
    }

    @Override
    public void onError(final Exception error) {
        Toast.makeText(RecyclerViewFragment.this.getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        vProgress.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(final List<Item> items) {
        mAdapter.setList(items);
        vProgress.setVisibility(View.GONE);
    }

    @Override
    public void onItemClickListener(Item item) {
        mCallback.openItem(item.link);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mCallback = (IItemClick) activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRSSViewModel.onDestroy();
    }
}