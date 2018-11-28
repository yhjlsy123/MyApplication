package com.zhuochi.hydream.bathhousekeeper.adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListSecondBean;
import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListThridBean;
import com.zhuochi.hydream.bathhousekeeper.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */
public class SchoolListSecondAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = SchoolListSecondAdapter.class.getSimpleName();
    private ExcuteListener mlistener;
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;
    public static final int request_type_1 = 1;
    public static final int request_type_2 = 2;
    public static final int request_type_3 = 3;
    public int request_type;

    public SchoolListSecondAdapter(List<MultiItemEntity> data, int request_type) {
        super(data);
        this.request_type = request_type;
        if (request_type == request_type_1) {
            addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
            addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
        } else if (request_type == request_type_2) {
            addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
            addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
            addItemType(TYPE_LEVEL_2, R.layout.item_expandable_lv2);
        } else if (request_type == request_type_3) {
            addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
            addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
            addItemType(TYPE_LEVEL_2, R.layout.item_expandable_lv2);
            addItemType(TYPE_LEVEL_3, R.layout.item_expandable_lv3);
        }
    }

    @Override
    protected void convert(final BaseViewHolder holder, MultiItemEntity item) {
        if (request_type == request_type_1) {
            switch (holder.getItemViewType()) {
                case TYPE_LEVEL_0:
                    final SchoolListSecondBean lv0 = (SchoolListSecondBean) item;
                    holder.setText(R.id.area_name, lv0.getOrg_name());

                    holder.getView(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = holder.getAdapterPosition();
                            Log.d(TAG, "Level 0 item pos: " + pos);
                            if (lv0.isExpanded()) {
                                collapse(pos);
                            } else {
                                expand(pos);
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                mlistener.setIds(lv0.getOrg_id(), 0, 0, "");
                                mlistener.setNames(lv0.getOrg_name(), "", "", "");
                            }
                        }
                    });
                    break;
                case TYPE_LEVEL_1:
                    holder.getView(R.id.arrow).setVisibility(View.GONE);
                    final SchoolListSecondBean.OrgAreaBean lv1 = (SchoolListSecondBean.OrgAreaBean) item;
                    holder.setText(R.id.area_name, lv1.getOrg_area_name());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getAdapterPosition();
                            if (mlistener != null) {
                                mlistener.setIds(lv1.getOrg_id(), lv1.getOrg_area_id(), 0, "");
                                mlistener.setNames("", lv1.getOrg_area_name(), "", "");
                            }
                        }
                    });
                    break;
            }
        } else if (request_type == request_type_2) {
            switch (holder.getItemViewType()) {
                case TYPE_LEVEL_0:
                    final SchoolListThridBean lv0 = (SchoolListThridBean) item;
                    holder.setText(R.id.area_name, lv0.getOrg_name());
                    holder.getView(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = holder.getAdapterPosition();
                            Log.d(TAG, "Level 0 item pos: " + pos);
                            Log.e("Level 0 item pos:", "onClick:" + pos);
                            if (lv0.isExpanded()) {
                                collapse(pos);
                            } else {
                                expand(pos);
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                mlistener.setIds(lv0.getOrg_id(), 0, 0, "");
                                mlistener.setNames(lv0.getOrg_name(), "", "", "");
                            }
                        }
                    });
                    break;
                case TYPE_LEVEL_1:
                    final SchoolListThridBean.OrgAreaBean lv1 = (SchoolListThridBean.OrgAreaBean) item;
                    holder.setText(R.id.area_name, lv1.getOrg_area_name());
                    holder.getView(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = holder.getAdapterPosition();
                            Log.d(TAG, "Level 1 item pos: " + pos);
                            Log.e("Level 1 item pos:", "onClick:" + pos);
                            if (lv1.isExpanded()) {
                                collapse(pos, false);
                            } else {
                                expand(pos, false);
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                mlistener.setIds(lv1.getOrg_id(), lv1.getOrg_area_id(), 0, "");
                                mlistener.setNames("", lv1.getOrg_area_name(), "", "");
                            }
                        }
                    });
                    break;
                case TYPE_LEVEL_2:
                    holder.getView(R.id.arrow).setVisibility(View.GONE);
                    final SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean lv2 = (SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean) item;
                    holder.setText(R.id.area_name, lv2.getDevice_area_name());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                mlistener.setIds(lv2.getOrg_id(), lv2.getOrg_area_id(), lv2.getDevice_area_id(), "");
                                mlistener.setNames("", "", lv2.getDevice_area_name(), "");
                            }
                        }
                    });
                    break;
            }
        } else if (request_type == request_type_3) {
            switch (holder.getItemViewType()) {
                case TYPE_LEVEL_0:
                    final SchoolListThridBean lv0 = (SchoolListThridBean) item;
                    holder.setText(R.id.area_name, lv0.getOrg_name());
                    holder.getView(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = holder.getAdapterPosition();
                            Log.d(TAG, "Level 0 item pos: " + pos);
                            Log.e("Level 0 item pos:", "onClick:" + pos);
                            if (lv0.isExpanded()) {
                                collapse(pos);
                            } else {
                                expand(pos);
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                mlistener.setIds(lv0.getOrg_id(), 0, 0, "");
                                mlistener.setNames(lv0.getOrg_name(), "", "", "");
                            }
                        }
                    });
                    break;
                case TYPE_LEVEL_1:
                    final SchoolListThridBean.OrgAreaBean lv1 = (SchoolListThridBean.OrgAreaBean) item;
                    holder.setText(R.id.area_name, lv1.getOrg_area_name());
                    holder.getView(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = holder.getAdapterPosition();
                            Log.d(TAG, "Level 1 item pos: " + pos);
                            Log.e("Level 1 item pos:", "onClick:" + pos);
                            if (lv1.isExpanded()) {
                                collapse(pos, false);
                            } else {
                                expand(pos, false);
                            }
                        }
                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                mlistener.setIds(lv1.getOrg_id(), lv1.getOrg_area_id(), 0, "");
                                mlistener.setNames("", lv1.getOrg_area_name(), "", "");
                            }
                        }
                    });
                    break;
                case TYPE_LEVEL_2:
                    final SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean lv2 = (SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean) item;
                    holder.setText(R.id.area_name, lv2.getDevice_area_name());
                    holder.getView(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = holder.getAdapterPosition();
                            Log.d(TAG, "Level 1 item pos: " + pos);
                            Log.e("Level 1 item pos:", "onClick:" + pos);
                            if (lv2.isExpanded()) {
                                collapse(pos, false);
                            } else {
                                expand(pos, false);
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                mlistener.setIds(lv2.getOrg_id(), lv2.getOrg_area_id(), 0, "");
                                mlistener.setNames("", lv2.getDevice_area_name(), "", "");
                            }
                        }
                    });
                    break;
                case TYPE_LEVEL_3:
                    holder.getView(R.id.arrow).setVisibility(View.GONE);
                    final SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition lv3 = (SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition) item;
                    holder.setText(R.id.area_name, lv3.getDevice_name());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mlistener != null) {
                                ToastUtils.show("测试");
//                                mlistener.setIds(lv3.getOrg_id(),lv3.getOrg_area_id(),lv3.getDevice_area_id(),0);
//                                mlistener.setNames("","",lv3.getDevice_name(),"");
                            }
                        }
                    });
                    break;
            }
        }
    }

    public interface ExcuteListener {
        void setIds(int org_id, int org_area_id, int boothroom_id, String boothroomPosition);

        void setNames(String org_name, String org_area_name, String boothroom_name, String device_key_name);
    }

    public void setExcuteListener(ExcuteListener listener) {
        mlistener = listener;
    }
}
