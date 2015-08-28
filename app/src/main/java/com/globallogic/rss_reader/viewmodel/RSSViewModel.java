package com.globallogic.rss_reader.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.globallogic.rss_reader.BuildConfig;
import com.globallogic.rss_reader.model.Item;
import com.globallogic.rss_reader.model.RSS;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.List;

/**
 * Created by Facundo Mengoni on 2015-08-27.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class RSSViewModel {
    private static final String TAG = RSSViewModel.class.getSimpleName();
    private final ICallback mCallback;

    private boolean mIgnoreCallback;

    public void onDestroy() {
        mIgnoreCallback = true;
    }

    public interface ICallback {
        void onError(Exception e);

        void onResponse(List<Item> e);
    }

    public RSSViewModel(Context context, ICallback callback) {
        this.mCallback = callback;
        Ion.with(context).load(BuildConfig.CLUB_FEED).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (TextUtils.isEmpty(result)) {
                    if (!mIgnoreCallback)
                        mCallback.onError(e);
                } else
                    onResponse(result);
            }
        });
    }

    private void onResponse(String response) {
        Serializer serializer = new Persister();
        try {
            RSS rss = serializer.read(RSS.class, response);
            if (!mIgnoreCallback)
                mCallback.onResponse(rss.getItems());
        } catch (Exception e) {
            if (e != null && !TextUtils.isEmpty(e.getMessage()))
                Log.e(TAG, e.getMessage());
            if (!mIgnoreCallback)
                mCallback.onError(e);
        }
    }
}