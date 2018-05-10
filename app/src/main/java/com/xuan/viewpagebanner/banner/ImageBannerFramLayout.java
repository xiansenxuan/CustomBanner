package com.xuan.viewpagebanner.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

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
public class ImageBannerFramLayout extends FrameLayout {
    private int MATCH_PARENT=FrameLayout.LayoutParams.MATCH_PARENT;
    private int WRAP_CONTENT= FrameLayout.LayoutParams.WRAP_CONTENT;

    private ImageBannerViewGroup imageBannerViewGroup;
    private ImageBannerPointLayout imageBannerPointLayout;

    private ArrayList<Integer> images;

    private int pointHeight=40;

    public ImageBannerFramLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ImageBannerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageBannerFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

    }


    public void addImageBannerViewGroup(ArrayList<Integer> images){
        this.images=images;

        imageBannerViewGroup =new ImageBannerViewGroup(getContext());
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT);
        imageBannerViewGroup.setLayoutParams(layoutParams);

        imageBannerViewGroup.addImages(images);
        imageBannerViewGroup.addImageBannerPointInter(new ImageBannerViewGroup.ImageBannerPointInter() {
            @Override
            public void selectPoint(int position) {
                imageBannerPointLayout.selectPoint(position);
            }
        });

        addView(imageBannerViewGroup);

    }

    public void addImageBannerViewGroupInter(ImageBannerViewGroup.ImageBannerInter inter){
        imageBannerViewGroup.addImageBannerInter(inter);
    }

    public void setAutoTime(int delay, int period) {
        imageBannerViewGroup.setAutoTime(delay, period);
    }

    public void addImageBannerPointLayout(){
        imageBannerPointLayout=new ImageBannerPointLayout(getContext());
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pointHeight);
        layoutParams.gravity= Gravity.BOTTOM;
        imageBannerPointLayout.setLayoutParams(layoutParams);

        imageBannerPointLayout.addPoints(images.size());
        //初始化选中第一个
        imageBannerPointLayout.selectPoint(0);

        addView(imageBannerPointLayout);

    }

}
