package com.globallogic.rss_reader.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.rss_reader.R;
import com.globallogic.rss_reader.model.Item;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego.rotondale on 10/05/2015.
 */
public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RSSHolder> {
    private List<Item> mList = new ArrayList<Item>();
    private OnItemClickListener onItemClickListener;

    @Override
    public RSSHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rss, parent, false);
        return new RSSHolder(v);
    }

    @Override
    public void onBindViewHolder(RSSHolder holder, int position) {
        Item item = mList.get(position);
        holder.load(item);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<Item> items) {
        mList.clear();
        mList.addAll(items);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(Item item);
    }

    public class RSSHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView vTitle;
        public final TextView vDescription;
        public final TextView vPubDate;
        public final ImageView vImage;
        private Item mItem;

        public RSSHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            vTitle = (TextView) view.findViewById(R.id.item_rss_title);
            vDescription = (TextView) view.findViewById(R.id.item_rss_description);
            vPubDate = (TextView) view.findViewById(R.id.item_rss_pub_date);
            vImage = (ImageView) view.findViewById(R.id.item_rss_image);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClickListener(mItem);
        }

        public void load(Item item) {
            this.mItem = item;

            vTitle.setText(item.title);
            vDescription.setText(Html.fromHtml(item.getDescription()));
            vPubDate.setText(item.getPubDate());
            if (item.content != null && !TextUtils.isEmpty(item.content.url)) {
                Ion.with(vImage).load(item.content.url);
                vImage.setContentDescription(item.content.title);
                vImage.setVisibility(View.VISIBLE);
            } else {
                vImage.setVisibility(View.GONE);
            }
        }
    }
}