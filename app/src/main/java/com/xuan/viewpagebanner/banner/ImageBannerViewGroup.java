package com.xuan.viewpagebanner.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * com.xuan.viewpagebanner.banner
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author by xuan on 2018/5/9
 * @version [版本号, 2018/5/9]
 * @update by xuan on 2018/5/9
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ImageBannerViewGroup extends ViewGroup {
    private int childCount;
    private int childWidth;
    private int childHeight;

    private int x;// 第一次按下的x坐标
    private int index;//索引

    private Scroller scroller;

    private boolean isClick;//是否是点击事件或者是移动事件
    private ImageBannerInter listener;

    private ImageBannerPointInter pointListener;

    public void addImages(ArrayList<Integer> images) {
        for (int image : images) {
            ImageView view=new ImageView(getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(SystemConstantInter.screenWidth,ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setScaleType(ImageView.ScaleType.CENTER);
            view.setImageResource(image);
            addView(view);
        }
    }

    /**
     * 圆点指示器监听
     */
    public interface ImageBannerPointInter{
        void selectPoint(int position);
    }
    public void addImageBannerPointInter(ImageBannerPointInter pointListener){
        this.pointListener=pointListener;
    }

    /**
     * 点击监听
     */
    public interface ImageBannerInter{
        void onClick(int position);
    }

    public void addImageBannerInter(ImageBannerInter listener){
        this.listener=listener;
    }

    //自动轮播
    private Timer timer=new Timer();
    private TimerTask timerTask;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(++index>=childCount){//每次进来 让坐标+1
                        //如果是最后一张 那么从第一张重新滑动
                        index=0;
                    }
                    scrollTo(childWidth*index,0);
                    //绑定圆点指示器
                    pointListener.selectPoint(index);

                    break;
                default:
                    break;
            }
            return false;
        }
    });

    //设置轮播事件
    private boolean isAuto=true;
    private int delay=2000;
    private int period=2000;
    public void setAutoTime(int delay, int period){
        this.delay=delay;
        this.period=period;
    }

    private void startAuto(){
        isAuto=true;
    }
    private void stopAuto(){
        isAuto=false;
    }

    public ImageBannerViewGroup(Context context) {
        super(context);
        initMethod();
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMethod();
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMethod();
    }

    private void initMethod() {
        scroller=new Scroller(getContext());

        timerTask=new TimerTask() {
            @Override
            public void run() {
                if(isAuto){
                  handler.sendEmptyMessage(0);
                }
            }
        };
        timer.schedule(timerTask,delay,period);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            //滑动结束
            scrollTo(scroller.getCurrX(),0);
            //重绘
            invalidate();
        }
    }


    /**
     * 2布局
     *
     * @param changed ImageBannerViewGroup布局位置发生改变 true
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int leftMargin = 0;

            for (int i = 0; i < childCount; i++) {
                //对子布局进行布局
                View child = getChildAt(i);
                child.layout(leftMargin, 0, leftMargin + childWidth, childHeight);
                leftMargin += childWidth;
            }
        }
    }

    /**
     * 1测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量子视图的个数
        childCount = getChildCount();

        if (childCount == 0) {
            // 给ViewGroup赋值
            setMeasuredDimension(0, 0);
        } else {
            // 测量子视图的宽高
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            // 以第一个子视图的宽高为基准 宽=子宽*count 高=子高
            View child = getChildAt(0);
            childHeight = child.getMeasuredHeight();
            childWidth = child.getMeasuredWidth() ;

            // 根据子视图的宽高测量ViewGroup的宽高 重新赋值给ViewGroup
            setMeasuredDimension(childWidth * childCount, childHeight);
        }

    }

    /**
     * 返回值 true 自定义的ViewGroup处理事件 真正处理事件的方法是 onTouchEvent
     * 返回值 false ViewGroup不处理 向下传递
     *
     * @param ev
     *
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                //停止自动轮播
                stopAuto();

                isClick=true;

                if(scroller.isFinished()){
                    scroller.abortAnimation();//如果再一次按下 ，停止上一次按下的滑动过程
                }

                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE://移动
                isClick=false;

                int moveX = (int) event.getX();//移动之后停留的x坐标
                int distance = moveX - x;//移动的实际距离
                scrollBy(-distance, 0); //移动
                x = moveX; //重新记录
                break;
            case MotionEvent.ACTION_UP://抬起
                //开启轮播
                startAuto();

                int scrollX = getScrollX();
                index = (scrollX + childWidth / 2) / childWidth;
                if (index < 0) {
                    //说明此时滑动到最左边第一张图片
                    index = 0;
                } else if (index > childCount - 1) {
                    //滑动到了最右边一张图片
                    index = childCount - 1;
                }

                if(isClick){
                    //点击
                    if(listener!=null){
                        listener.onClick(index);
                    }
                }else{
                    //滑动到当前图片坐标 dx滑动的距离
                    int dx=index*childWidth-scrollX;
                    scroller.startScroll(scrollX,0,dx,0);
                    postInvalidate();
                    // scrollTo(index * childWidth, 0);

                    //绑定圆点指示器
                    pointListener.selectPoint(index);

                }


                break;
            default:
                break;
        }
        return true;//告诉ViewGroup的父容器 当前已经处理事件
    }
}
