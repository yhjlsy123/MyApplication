package com.zhuochi.hydream.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuochi.hydream.config.Constants;
import com.zhuochi.hydream.dialog.ErroInforDialog;
import com.zhuochi.hydream.dialog.TipDialog;
import com.zhuochi.hydream.entity.HomeShowerListEntity;
import com.zhuochi.hydream.utils.Common;
import com.zhuochi.hydream.utils.SPUtils;
import com.zhuochi.hydream.view.BathRoomView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zhuochi.hydream.fragment.OriginFragment.IsImg_load;

/**
 * 浴室浴头列表
 * Created by and on 2016/11/5.
 */

public class ShowerListAdapter extends BaseAdapter {
    //    private List<ShowerList.Shower> mShowerList;
    private List<HomeShowerListEntity> mShowerList;
    private Context mContext;

    public ShowerListAdapter(List<HomeShowerListEntity> shower, Context context) {
        mShowerList = new ArrayList<>();
        mShowerList.addAll(shower);
        Log.d("cxc_load", shower.toString());

        mContext = context;
    }

    private String TAG = "ShowerListAdapter";

    @Override
    public int getCount() {
        return mShowerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<HomeShowerListEntity> shower) {
        mShowerList.clear();
        mShowerList.addAll(shower);
        notifyDataSetChanged();
    }

    /**
     * 标记选中的位置
     */
    private String selectID;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BathRoomView bathRoomView;
        if (convertView != null) {
            if (!IsImg_load) {
                convertView = null;
                bathRoomView = new BathRoomView(parent.getContext());
            } else {
                bathRoomView = (BathRoomView) convertView;
            }
        } else {
            bathRoomView = new BathRoomView(parent.getContext());
        }
        final HomeShowerListEntity shower = mShowerList.get(position);

        if (!(TextUtils.isEmpty(mShowerList.get(position).getDevice_item_flag())) && TextUtils.equals("default", mShowerList.get(position).getDevice_item_flag())) {
            if (TextUtils.equals("ready", shower.getDevice_status())) {
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    if (listener != null)
                        listener.change(shower.getDevice_key(),  shower.getDevice_type_key());
                    bathRoomView.setStatus(mContext, BathRoomView.CHECKED);//选中
                } else {
                    bathRoomView.setStatus(mContext, BathRoomView.USABLE);//可用
                }
            } else if (TextUtils.equals("running", shower.getDevice_status())) {
                //使用
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    selectID = "";
                    if (listener != null)
                        listener.change("",  shower.getDevice_type_key());
                }
                bathRoomView.setStatus(mContext, BathRoomView.USING);
            } else if (TextUtils.equals("reserved", shower.getDevice_status())) {
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    selectID = "";
                    if (listener != null)
                        listener.change("",  shower.getDevice_type_key());
                }
                bathRoomView.setStatus(mContext, BathRoomView.RESERVED_NO);
            } else if (TextUtils.equals("junked", shower.getDevice_status())) {
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    selectID = "";
                    if (listener != null)
                        listener.change("",  shower.getDevice_type_key());
                }
                bathRoomView.setStatus(mContext, BathRoomView.JUNKED_NO);
            }

        } else if (!(TextUtils.isEmpty(mShowerList.get(position).getDevice_item_flag())) && TextUtils.equals("warmwater", mShowerList.get(position).getDevice_item_flag())) {
            if (TextUtils.equals("ready", shower.getDevice_status())) {
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    if (listener != null)
                        listener.change(shower.getDevice_key(), null);
                    bathRoomView.setWarmStatus(mContext, BathRoomView.CHECKED);//选中
                } else {
                    bathRoomView.setWarmStatus(mContext, BathRoomView.USABLE);//可用
                }
            } else if (TextUtils.equals("running", shower.getDevice_status())) {
                //使用
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    selectID = "";
                    if (listener != null)
                        listener.change("",  shower.getDevice_type_key());
                }
                bathRoomView.setWarmStatus(mContext, BathRoomView.USING);
            } else if (TextUtils.equals("reserved", shower.getDevice_status())) {
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    selectID = "";
                    if (listener != null)
                        listener.change("",  shower.getDevice_type_key());
                }
                bathRoomView.setWarmStatus(mContext, BathRoomView.RESERVED_NO);
            } else if (TextUtils.equals("junked", shower.getDevice_status())) {
                if (TextUtils.equals(selectID, shower.getDevice_key())) {
                    selectID = "";
                    if (listener != null)
                        listener.change("",  shower.getDevice_type_key());
                }
                bathRoomView.setWarmStatus(mContext, BathRoomView.JUNKED_NO);
            }

        }

        bathRoomView.setText(shower.getDevice_number());
        bathRoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals("reserved", shower.getDevice_status()))
                    return;
                if (TextUtils.equals("ready", shower.getDevice_status())) {
                    if (TextUtils.equals(selectID, shower.getDevice_key())) {
                        selectID = "";
                        if (listener != null)
                            listener.change("", shower.getDevice_type_key());
                    } else {
                        selectID = shower.getDevice_key();
                    }
                    notifyDataSetChanged();
                }
            }
        });
        bathRoomView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (SPUtils.getInt(Constants.USER_OAUTH, -1) == 2) {
                    Map<String, Object> dd = JSON.parseObject(JSON.toJSONString(shower));
                    ErroInforDialog dialog = new ErroInforDialog(mContext, dd);
                    dialog.show();
                }
                return false;
            }
        });
        return bathRoomView;
    }

    private ItemClickListener listener;

    /**
     * 设置点击条目后的回调
     *
     * @param listener
     */
    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 获取选中条目的数据
     */
    public interface ItemClickListener {
        void change(String item, String deviceTypeKey);
    }
}
