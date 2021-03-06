package com.example.flodingcell.Activities.Activities;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.flodingcell.R;

import java.util.concurrent.TimeUnit;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created on 2016/10/13.
 * Author Chico Chen
 */
public class PublishPopWindow extends PopupWindow implements View.OnClickListener {

    private View rootView;  //父元素节点view对象
    private RelativeLayout contentView;   //父元素
    private Activity mContext;  //


    public PublishPopWindow(Activity context) {
        this.mContext = context;
    }

    public void showMoreWindow(View anchor) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  //获得布局容器
        rootView = inflater.inflate(R.layout.dialog_publish, null);          //填充弹框布局
        int h = mContext.getWindowManager().getDefaultDisplay().getHeight();      //获得显示器高度
        int w = mContext.getWindowManager().getDefaultDisplay().getWidth();       //获得显示窗口的高度
        setContentView(rootView);
        this.setWidth(w);
        this.setHeight(h - ScreenUtils.getStatusHeight(mContext));

        contentView = (RelativeLayout) rootView.findViewById(R.id.contentView);
        LinearLayout close = (LinearLayout) rootView.findViewById(R.id.ll_close);
        close.setBackgroundColor(0xFFFFFFFF);
        close.setOnClickListener(this);
        showAnimation(contentView);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.translucence_with_white));  //设置背景
        setOutsideTouchable(true);
        setFocusable(true);  //布局可以获取焦点
        showAtLocation(anchor, Gravity.BOTTOM, 0, 0);   //在窗口底下显示
    }

    /**
     * 显示进入动画效果
     * @param layout
     */
    private void showAnimation(ViewGroup layout) {
        //遍历根试图下的一级子试图
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            //忽略关闭组件
            if (child.getId() == R.id.ll_close) {
                continue;
            }
            //设置所有一级子试图的点击事件
            child.setOnClickListener(this);
            child.setVisibility(View.INVISIBLE);
            //延迟显示每个子试图(主要动画就体现在这里)

                Observable.timer(i/2 * 210, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                child.setVisibility(View.VISIBLE);
                                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                                fadeAnim.setDuration(300);
                                KickBackAnimator kickAnimator = new KickBackAnimator();
                                kickAnimator.setDuration(150);
                                fadeAnim.setEvaluator(kickAnimator);
                                fadeAnim.start();
                            }
                        });

        }

    }

    /**
     * 关闭动画效果
     * @param layout
     */
    private void closeAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.ll_close) {
                continue;
            }
            Observable.timer((layout.getChildCount() - i - 1) * 30, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            child.setVisibility(View.VISIBLE);
                            ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
                            fadeAnim.setDuration(200);
                            KickBackAnimator kickAnimator = new KickBackAnimator();
                            kickAnimator.setDuration(100);
                            fadeAnim.setEvaluator(kickAnimator);
                            fadeAnim.start();
                            fadeAnim.addListener(new Animator.AnimatorListener() {

                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    child.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }
                            });
                        }
                    });


            if (child.getId() == R.id.video_window) {
                Observable.timer((layout.getChildCount() - i) * 30 + 80, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                dismiss();
                            }
                        });
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //匹配每一个父容器下的子标签
            case R.id.video_window:
            case R.id.photo_window:
            case R.id.weblink_window:
            case R.id.text_window:
            case R.id.rl_background:
//            case R.id.ll_link_des:
            case R.id.ll_close:
                if (isShowing()) {
                    closeAnimation(contentView);
                }
                break;
            case R.id.voice_window:
                if (isShowing()) {
                    closeAnimation(contentView);
                }
                break;
            default:
                break;
        }
    }
}