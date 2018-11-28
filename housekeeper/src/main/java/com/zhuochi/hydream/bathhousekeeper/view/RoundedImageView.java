package com.zhuochi.hydream.bathhousekeeper.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.zhuochi.hydream.bathhousekeeper.R;


/**
 * 带圆角的ImageView
 * 
 * @author gong
 *
 */
@SuppressWarnings("UnusedDeclaration")
public class RoundedImageView extends ImageView {

	public static final String TAG = "RoundedImageView";
	public static final int DEFAULT_RADIUS = 0;
	public static final int DEFAULT_BORDER_WIDTH = 0;
	private static final ScaleType[] SCALE_TYPES = { ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START,
			ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE };

	private int mCornerRadius = DEFAULT_RADIUS;
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;
	private ColorStateList mBorderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
	private boolean mOval = false;
	private boolean mRoundBackground = false;

	private int mResource;
	private Drawable mDrawable;
	private Drawable mBackgroundDrawable;

	private ScaleType mScaleType;

	public RoundedImageView(Context context) {
		super(context);
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

		int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
		if (index >= 0) {
			setScaleType(SCALE_TYPES[index]);
		} else {
			// 默认scaletype为FIT_CENTER
			setScaleType(ScaleType.CENTER_CROP);
		}
		
		mCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
		mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_border_width, -1);
		
		// 为radius和border过滤掉负值
		if (mCornerRadius < 0) {
			mCornerRadius = DEFAULT_RADIUS;
			//  默认的圆角(10)
			mCornerRadius=getResources().getDimensionPixelSize(R.dimen.x10);
		}
		if (mBorderWidth < 0) {
			mBorderWidth = DEFAULT_BORDER_WIDTH;
		}

		mBorderColor = a.getColorStateList(R.styleable.RoundedImageView_border_color);
		if (mBorderColor == null) {
			mBorderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
		}

		mRoundBackground = a.getBoolean(R.styleable.RoundedImageView_round_background, false);
		mOval = a.getBoolean(R.styleable.RoundedImageView_is_oval, false);

		updateDrawableAttrs();
		updateBackgroundDrawableAttrs();

		a.recycle();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	/**
	 * 返回当前的使用的scaletype
	 *
	 * @attr ref android.R.styleable#ImageView_scaleType
	 * @see ScaleType
	 */
	@Override
	public ScaleType getScaleType() {
		return mScaleType;
	}

	/**
	 * 
	 * 控制image的尺寸，来匹配imageview
	 *
	 * @param scaleType
	 *            The desired scaling mode.
	 * @attr ref android.R.styleable#ImageView_scaleType
	 */
	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType == null) {
			throw new NullPointerException();
		}

		if (mScaleType != scaleType) {
			mScaleType = scaleType;

			switch (scaleType) {
			case CENTER:
			case CENTER_CROP:
			case CENTER_INSIDE:
			case FIT_CENTER:
			case FIT_START:
			case FIT_END:
			case FIT_XY:
				super.setScaleType(ScaleType.FIT_XY);
				break;
			default:
				super.setScaleType(scaleType);
				break;
			}

			updateDrawableAttrs();
			updateBackgroundDrawableAttrs();
			invalidate();
		}
	}
//	public void setScale(float r){
//		this.r=r;
//		Rect bounds = mDrawable.getBounds();
//		float w=bounds.right-bounds.left;
//		int chazhi=(int)(w*(r-1)/2+0.5);
//		bounds.right=bounds.right+chazhi;
//		bounds.left=bounds.left+chazhi;
//		bounds.top=bounds.top+chazhi;
//		bounds.bottom=bounds.bottom+chazhi;
//		mDrawable.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
//		updateDrawableAttrs();
//	}
//	private float r;
//	public float getScale(){
//		return r;
//	}
	@Override
	public void setImageDrawable(Drawable drawable) {
		mResource = 0;
		mDrawable = RoundedDrawable.fromDrawable(this,drawable);
		updateDrawableAttrs();
		super.setImageDrawable(mDrawable);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		mResource = 0;
		mDrawable = RoundedDrawable.fromBitmap(bm);
		updateDrawableAttrs();
		super.setImageDrawable(mDrawable);
	}
	
	@Override
	public void setImageResource(int resId) {
		if (mResource != resId) {
			mResource = resId;
			mDrawable = resolveResource();
			updateDrawableAttrs();
			super.setImageDrawable(mDrawable);
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		setImageDrawable(getDrawable());
	}

	private Drawable resolveResource() {
		Resources rsrc = getResources();
		if (rsrc == null) {
			return null;
		}

		Drawable d = null;

		if (mResource != 0) {
			try {
				d = rsrc.getDrawable(mResource);
			} catch (Exception e) {
				Log.w(TAG, "Unable to find resource: " + mResource, e);
				// Don't try again.
				mResource = 0;
			}
		}
		return RoundedDrawable.fromDrawable(this,d);
	}

	@Override
	public void setBackground(Drawable background) {
		setBackgroundDrawable(background);
	}

	private void updateDrawableAttrs() {
		updateAttrs(mDrawable, false);
	}

	private void updateBackgroundDrawableAttrs() {
		updateAttrs(mBackgroundDrawable, true);
	}
	/**
	 * 有前景色
	 */
	private boolean hasForebg;
	/**
	 * 有前景色
	 */
	public void setHasForebg(boolean has){
		hasForebg=has;
	}
	private void updateAttrs(Drawable drawable, boolean background) {
		if (drawable == null) {
			return;
		}

		if (drawable instanceof RoundedDrawable) {
			((RoundedDrawable) drawable).setScaleType(mScaleType)
					.setCornerRadius(background && !mRoundBackground ? 0 : mCornerRadius)
					.setBorderWidth(background && !mRoundBackground ? 0 : mBorderWidth).setBorderColors(mBorderColor)
					.setOval(mOval).setHasForebg(hasForebg);
		} else if (drawable instanceof LayerDrawable) {
			// loop through layers to and set drawable attrs
			LayerDrawable ld = ((LayerDrawable) drawable);
			int layers = ld.getNumberOfLayers();
			for (int i = 0; i < layers; i++) {
				updateAttrs(ld.getDrawable(i), background);
			}
		}
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		mBackgroundDrawable = RoundedDrawable.fromDrawable(this,background);
		updateBackgroundDrawableAttrs();
		super.setBackgroundDrawable(mBackgroundDrawable);
	}

	public int getCornerRadius() {
		return mCornerRadius;
	}

	public void setCornerRadius(int radius) {
		if (mCornerRadius == radius) {
			return;
		}

		mCornerRadius = radius;
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs();
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int width) {
		if (mBorderWidth == width) {
			return;
		}

		mBorderWidth = width;
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs();
		invalidate();
	}

	public int getBorderColor() {
		return mBorderColor.getDefaultColor();
	}

	public void setBorderColor(int color) {
		setBorderColors(ColorStateList.valueOf(color));
	}

	public ColorStateList getBorderColors() {
		return mBorderColor;
	}

	public void setBorderColors(ColorStateList colors) {
		if (mBorderColor.equals(colors)) {
			return;
		}

		mBorderColor = (colors != null) ? colors : ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs();
		if (mBorderWidth > 0) {
			invalidate();
		}
	}

	public boolean isOval() {
		return mOval;
	}

	public void setOval(boolean oval) {
		mOval = oval;
		updateDrawableAttrs();
		updateBackgroundDrawableAttrs();
		invalidate();
	}

	public boolean isRoundBackground() {
		return mRoundBackground;
	}

	public void setRoundBackground(boolean roundBackground) {
		if (mRoundBackground == roundBackground) {
			return;
		}

		mRoundBackground = roundBackground;
		updateBackgroundDrawableAttrs();
		invalidate();
	}
//	public class UpdateListener implements AnimatorUpdateListener{
//		
//		public UpdateListener() {
//		}
//
//		@Override
//		public void onAnimationUpdate(ValueAnimator arg0) {
//			Float value = (Float) arg0.getAnimatedValue();
//			setScale(value);
//		}
//	}
}
