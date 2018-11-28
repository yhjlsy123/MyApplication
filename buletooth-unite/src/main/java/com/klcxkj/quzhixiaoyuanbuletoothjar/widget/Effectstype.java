package com.klcxkj.quzhixiaoyuanbuletoothjar.widget;


import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.BaseEffects;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.FadeIn;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.FlipH;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.FlipV;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.NewsPaper;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.SideFall;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.SlideLeft;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.SlideRight;
import com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.SlideTop;
import  com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.SlideBottom;
import  com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.Fall;
import  com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.RotateBottom;
import  com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.RotateLeft;
import  com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.Slit;
import  com.klcxkj.quzhixiaoyuanbuletoothjar.widget.effect.Shake;

/**
 * Created by lee on 2014/7/30.
 */
public enum Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
