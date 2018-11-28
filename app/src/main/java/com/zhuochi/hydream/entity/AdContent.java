package com.zhuochi.hydream.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AdContent implements Parcelable {


    /**
     * adEnabled : 1
     * adType : webPopup
     * adWidth : 100
     * adHeight : 60
     * adContent : http://lz.hydream.cn/portal/Article/index?id=12
     */

    private String adEnabled;
    private String adType;
    private String adWidth;
    private String adHeight;
    private String adContent;

    public String getAdEnabled() {
        return adEnabled;
    }

    public void setAdEnabled(String adEnabled) {
        this.adEnabled = adEnabled;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdWidth() {
        return adWidth;
    }

    public void setAdWidth(String adWidth) {
        this.adWidth = adWidth;
    }

    public String getAdHeight() {
        return adHeight;
    }

    public void setAdHeight(String adHeight) {
        this.adHeight = adHeight;
    }

    public String getAdContent() {
        return adContent;
    }

    public void setAdContent(String adContent) {
        this.adContent = adContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adEnabled);
        dest.writeString(this.adType);
        dest.writeString(this.adWidth);
        dest.writeString(this.adHeight);
        dest.writeString(this.adContent);
    }

    public AdContent() {
    }

    protected AdContent(Parcel in) {
        this.adEnabled = in.readString();
        this.adType = in.readString();
        this.adWidth = in.readString();
        this.adHeight = in.readString();
        this.adContent = in.readString();
    }

    public static final Parcelable.Creator<AdContent> CREATOR = new Parcelable.Creator<AdContent>() {
        @Override
        public AdContent createFromParcel(Parcel source) {
            return new AdContent(source);
        }

        @Override
        public AdContent[] newArray(int size) {
            return new AdContent[size];
        }
    };
}
