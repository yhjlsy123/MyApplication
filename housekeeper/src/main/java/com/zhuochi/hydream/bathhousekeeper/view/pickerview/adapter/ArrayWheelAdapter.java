package com.zhuochi.hydream.bathhousekeeper.view.pickerview.adapter;

import com.zhuochi.hydream.bathhousekeeper.bean.SchoolListThridBean;
import com.zhuochi.hydream.bathhousekeeper.view.datapicker.WheelAdapter;

import java.util.List;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {
	

	// items
	private List<T> items;

	/**
	 * Constructor
	 * @param items the items
	 */
	public ArrayWheelAdapter(List<T> items) {
		this.items = items;

	}
	
	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < items.size()) {
			if (items.get(index) instanceof SchoolListThridBean.OrgAreaBean )
			return ((SchoolListThridBean.OrgAreaBean)((SchoolListThridBean.OrgAreaBean) items.get(index))).getOrg_area_name();
			if (items.get(index) instanceof SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean)
				return ((SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean)(SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean) items.get(index)).getDevice_area_name();
			if (items.get(index) instanceof SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition)
				return ((SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition)((SchoolListThridBean.OrgAreaBean.OrgAreaBathroomBean.BathPosition) items.get(index))).getDevice_name();

		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public int indexOf(Object o){
		return items.indexOf(o);
	}

}
