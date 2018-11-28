package com.zhuochi.hydream.bathhousekeeper.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgTypeBean implements Parcelable {


    /**
     * id : 1
     * icon : default
     * type_name : 应用消息
     * type_no : 8
     * create_time : 1.527679017E9
     * status : 1
     */

    private int id;
    private String icon;
    private String type_name;
    private int type_no;
    private double create_time;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getType_no() {
        return type_no;
    }

    public void setType_no(int type_no) {
        this.type_no = type_no;
    }

    public double getCreate_time() {
        return create_time;
    }

    public void setCreate_time(double create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.icon);
        dest.writeString(this.type_name);
        dest.writeInt(this.type_no);
        dest.writeDouble(this.create_time);
        dest.writeInt(this.status);
    }

    public MsgTypeBean() {
    }

    protected MsgTypeBean(Parcel in) {
        this.id = in.readInt();
        this.icon = in.readString();
        this.type_name = in.readString();
        this.type_no = in.readInt();
        this.create_time = in.readDouble();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<MsgTypeBean> CREATOR = new Parcelable.Creator<MsgTypeBean>() {
        @Override
        public MsgTypeBean createFromParcel(Parcel source) {
            return new MsgTypeBean(source);
        }

        @Override
        public MsgTypeBean[] newArray(int size) {
            return new MsgTypeBean[size];
        }
    };
}
