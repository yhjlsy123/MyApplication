package com.zhuochi.hydream.bathhousekeeper.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceEditBean implements Parcelable {


    /**
     * id : 14
     * show_name : 齐鲁医药6:30-23:30
     * start_time : 2018-09-15
     * end_time : 2022-09-30
     * daily_start_time : 06:30
     * daily_end_time : 23:30
     * remark :
     */

    private int id;
    private String show_name;
    private String start_time;
    private String end_time;
    private String daily_start_time;
    private String daily_end_time;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDaily_start_time() {
        return daily_start_time;
    }

    public void setDaily_start_time(String daily_start_time) {
        this.daily_start_time = daily_start_time;
    }

    public String getDaily_end_time() {
        return daily_end_time;
    }

    public void setDaily_end_time(String daily_end_time) {
        this.daily_end_time = daily_end_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.show_name);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeString(this.daily_start_time);
        dest.writeString(this.daily_end_time);
        dest.writeString(this.remark);
    }

    public DeviceEditBean() {
    }

    protected DeviceEditBean(Parcel in) {
        this.id = in.readInt();
        this.show_name = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.daily_start_time = in.readString();
        this.daily_end_time = in.readString();
        this.remark = in.readString();
    }

    public static final Parcelable.Creator<DeviceEditBean> CREATOR = new Parcelable.Creator<DeviceEditBean>() {
        @Override
        public DeviceEditBean createFromParcel(Parcel source) {
            return new DeviceEditBean(source);
        }

        @Override
        public DeviceEditBean[] newArray(int size) {
            return new DeviceEditBean[size];
        }
    };
}
