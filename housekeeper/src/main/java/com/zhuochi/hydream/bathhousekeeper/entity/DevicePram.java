package com.zhuochi.hydream.bathhousekeeper.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class DevicePram implements Parcelable {
    private int org_id;
    private int org_area_id;
    private int boothroom_id;
    private int requestCode;
    private String device_key;
    private String org_name;
    private String org_area_name;
    private String booth_name;
    private String device_name;
    private Class<?>  activity;

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public int getOrg_area_id() {
        return org_area_id;
    }

    public void setOrg_area_id(int org_area_id) {
        this.org_area_id = org_area_id;
    }

    public int getBoothroom_id() {
        return boothroom_id;
    }

    public void setBoothroom_id(int boothroom_id) {
        this.boothroom_id = boothroom_id;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_area_name() {
        return org_area_name;
    }

    public void setOrg_area_name(String org_area_name) {
        this.org_area_name = org_area_name;
    }

    public String getBooth_name() {
        return booth_name;
    }

    public void setBooth_name(String booth_name) {
        this.booth_name = booth_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public Class<?> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = activity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.org_id);
        dest.writeInt(this.org_area_id);
        dest.writeInt(this.boothroom_id);
        dest.writeInt(this.requestCode);
        dest.writeString(this.device_key);
        dest.writeString(this.org_name);
        dest.writeString(this.org_area_name);
        dest.writeString(this.booth_name);
        dest.writeString(this.device_name);
        dest.writeSerializable(this.activity);
    }

    public DevicePram() {
    }

    protected DevicePram(Parcel in) {
        this.org_id = in.readInt();
        this.org_area_id = in.readInt();
        this.boothroom_id = in.readInt();
        this.requestCode = in.readInt();
        this.device_key = in.readString();
        this.org_name = in.readString();
        this.org_area_name = in.readString();
        this.booth_name = in.readString();
        this.device_name = in.readString();
        this.activity = (Class<?>) in.readSerializable();
    }

    public static final Parcelable.Creator<DevicePram> CREATOR = new Parcelable.Creator<DevicePram>() {
        @Override
        public DevicePram createFromParcel(Parcel source) {
            return new DevicePram(source);
        }

        @Override
        public DevicePram[] newArray(int size) {
            return new DevicePram[size];
        }
    };
}
