package com.xuan.viewpagebanner.banner;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xuan.viewpagebanner.R;

/**
 * com.xuan.viewpagebanner.banner
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author by xuan on 2018/5/10
 * @version [版本号, 2018/5/10]
 * @update by xuan on 2018/5/10
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ImageBannerPointLayout extends LinearLayout {
    private int bgColor= Color.parseColor("#BDBDBD");
    private int alpha=100;

    public ImageBannerPointLayout(@NonNull Context context) {
        super(context);
        initPointLayout();
    }

    public ImageBannerPointLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageBannerPointLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPointLayout() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(bgColor);
        getBackground().setAlpha(alpha);
    }

    public void addPoints(int size) {
        for (int i = 0; i < size; i++) {
            ImageView view=new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5,0,5,0);
            view.setLayoutParams(layoutParams);
            view.setScaleType(ImageView.ScaleType.CENTER);
            view.setImageResource(R.drawable.point_normal);
            addView(view);
        }
    }

    public void selectPoint(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            ImageView view= (ImageView) getChildAt(i);
            if(i==position){
                view.setImageResource(R.drawable.point_selector);
            }else{
                view.setImageResource(R.drawable.point_normal);
            }
        }

    }
}
