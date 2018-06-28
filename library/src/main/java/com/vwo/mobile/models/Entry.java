package com.vwo.mobile.models;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by aman on 16/09/17.
 */

@Keep
public abstract class Entry implements Serializable {
    public static final String TYPE_GOAL = "goal";
    public static final String TYPE_CAMPAIGN = "campaign";

    private String url;
    private int retryCount;

    public Entry(@NonNull String url) {
        this.url = url;
        retryCount = 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void incrementRetryCount() {
        ++retryCount;
    }

    /**
     * A unique key for the given entry.
     *
     * @return {@link String} the unique key
     */
    public abstract String getKey();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entry) {
            String entryUrl = ((Entry) obj).getUrl();
            return !TextUtils.isEmpty(entryUrl) && entryUrl.equals(this.url);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Url: %s\nRetryCount: %d\n", this.url, this.retryCount);
    }
}
