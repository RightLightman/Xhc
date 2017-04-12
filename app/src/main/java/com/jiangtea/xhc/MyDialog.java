package com.jiangtea.xhc;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by zhuqiguang on 17/2/23.
 */
public class MyDialog extends DialogFragment {
    public String[] mImages;//图片地址
    private ArrayList<View> pageViews;//图片容器
    private int mPosition;//vp當前角碼
    private boolean isDragging;
    public static MyDialog instance = null;
    private boolean mIsCancel;//点击四周是否取消dialog
    private boolean mIsTransparent;//设置背景四周是否透明

    public MyDialog() {
    }

    public static MyDialog getInstance() {
        if (instance == null) {
            instance = new MyDialog();
        }
        return instance;
    }

    /**
     * 设置图片地址url
     */
    public MyDialog setImages(String[] images) {
        mImages = images;
        return this;
    }

    public MyDialog show(android.app.FragmentManager fragmentManager) {
        if (instance != null) {
            instance.show(fragmentManager,"homepage");
        }
        return this;
    }

    /**
     * 点击四周是否取消dialog,默认取消
     *
     * @param isCancel
     * @return
     */
    public MyDialog setCanceledOnTouchOutside(boolean isCancel) {
        mIsCancel = isCancel;
        return this;
    }

    /**
     * 设置背景四周是否透明,调用时需要放到show方法后面
     *
     * @param isTransparent
     * @return
     */
    public MyDialog setOutsideIsTransparent(boolean isTransparent) {
        mIsTransparent = isTransparent;
        return this;
    }

    public MyDialog dissmiss() {
        getDialog().dismiss();
        return this;
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageViews = new ArrayList<>();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mIsCancel) {
            getDialog().setCanceledOnTouchOutside(mIsCancel);
        }
        View view = inflater.inflate(R.layout.fragment_dialog, container);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        for (String image : mImages) {
            View inflate = inflater.inflate(R.layout.guide_pager_four, null);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_item_guide_img);
            Glide.with(getActivity()).load(image).into(imageView);
            pageViews.add(imageView);
        }
        viewPager.setAdapter(new HomePagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING://拖动
                        isDragging = true;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING://安放到了目标页
                        isDragging = false;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE://拖动结束
                        if (mPosition == pageViews.size() - 1 && isDragging) {
//                            getDialog().dismiss();
                        }
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsTransparent) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            window.setAttributes(windowParams);
        }
    }

    class HomePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageViews.get(position));
            return pageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pageViews.get(position));
        }
    }
}
