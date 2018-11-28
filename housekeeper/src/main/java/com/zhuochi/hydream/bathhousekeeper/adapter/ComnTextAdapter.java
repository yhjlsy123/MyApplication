package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListThridBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComnTextAdapter extends BaseAdapter {
    private List<SchoolListThridBean> school;
    private Context context;

    private List<SchoolListThridBean.OrgAreaBean> scArea;
    private List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean> scRoom;
    private List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition> scDevice;
    private ViewHolder viewHolder;
    private Boolean isLimit;

    public void setLimit(Boolean limit) {
        this.isLimit = limit;
        if (null != school && school.size() > 0) {
            SchoolListThridBean st = new SchoolListThridBean();
            st.setOrg_name("不限");
            school.add(0, st);
        }
        if (null != scArea && scArea.size() > 0) {
            SchoolListThridBean.OrgAreaBean so = new SchoolListThridBean.OrgAreaBean();
            so.setOrg_area_name("不限");
            scArea.add(0, so);
        }
        if (null != scRoom && scRoom.size() > 0) {
            SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean soo = new SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean();
            soo.setDevice_area_name("不限");
            scRoom.add(0, soo);
        }
        if (null != scDevice && scDevice.size() > 0) {
            SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition sb = new SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition();
            sb.setDevice_name("不限");
            scDevice.add(0, sb);
        }

    }


    public void setMapValue(Integer pos, Boolean value) {
        map.clear();
        map.put(pos, true);
    }

    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

    public void setScArea(List<SchoolListThridBean.OrgAreaBean> scArea) {
        if (null == this.scArea) {
            this.scArea = scArea;
        } else {
            this.scArea.clear();
            this.scArea.addAll(scArea);
        }

    }

    public ComnTextAdapter(Context context) {
        this.context = context;
    }


    public void setScRoom(List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean> scRoom) {
        if (null == this.scArea) {
            this.scRoom = scRoom;
        } else {
            this.scRoom.clear();
            this.scRoom.addAll(scRoom);
        }

    }

    public void setScDevice(List<SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition> scDevice) {
        if (null == this.scArea) {
            this.scDevice = scDevice;
        } else {
            this.scDevice.clear();
            this.scDevice.addAll(scDevice);
        }
    }

    public ComnTextAdapter(Context context, List<SchoolListThridBean> school) {
        this.school = school;
        this.context = context;
    }


    @Override
    public int getCount() {
        if (null != school) {
            return school.size();
        }
        if (null != scArea) {
            return scArea.size();
        }
        if (null != scRoom) {
            return scRoom.size();
        }
        if (null != scDevice) {
            return scDevice.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != school) {
            return school.get(position);
        }
        if (null != scArea) {
            return scArea.get(position);
        }
        if (null != scRoom) {
            return scRoom.get(position);
        }
        if (null != scDevice) {
            return scDevice.get(position);
        }


        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_text, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null != school) {
            viewHolder.areaName.setText(school.get(position).getOrg_name());
        }
        if (null != scArea) {
            viewHolder.areaName.setText(scArea.get(position).getOrg_area_name());
        }
        if (null != scRoom) {
            viewHolder.areaName.setText(scRoom.get(position).getDevice_area_name());
        }
        if (null != scDevice) {
            viewHolder.areaName.setText(scDevice.get(position).getDevice_name());
        }
        if (map.containsKey(position)) {
            viewHolder.arrow.setChecked(true);
        } else {
            viewHolder.arrow.setChecked(false);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.area_name)
        TextView areaName;
        @BindView(R.id.arrow)
        CheckBox arrow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
