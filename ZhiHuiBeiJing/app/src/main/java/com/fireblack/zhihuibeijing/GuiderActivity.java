package com.fireblack.zhihuibeijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fireblack.zhihuibeijing.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 新手引导页
 */
public class GuiderActivity extends Activity {

    private static final int[] mImageIds = new int[] { R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3 };
    private ViewPager vpGuider;
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llPointGroup;//引导页小圆点
    private int mPointWidth;//圆点间距
    private View viewPoint;//小红点
    private Button btn_start;//开始体验按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        setContentView(R.layout.activity_guider);

        vpGuider = (ViewPager) findViewById(R.id.vp_guider);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        viewPoint = findViewById(R.id.view_red_point);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.putBoolean(GuiderActivity.this,"is_user_guider_showed",true);

                startActivity(new Intent(GuiderActivity.this, MainActivity.class));
                finish();
            }
        });

        initView();
        vpGuider.setAdapter(new GuiderAdapter());
        vpGuider.addOnPageChangeListener(new GuiderListen());
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mImageViewList = new ArrayList<ImageView>();

        //初始化引导页的3个界面
        for(int i =0; i < mImageIds.length; i++){
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(image);
        }

        //初始化小圆点
        for(int i=0; i<mImageIds.length;i++){
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);//默认圆点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,10);
            if(i>0) {
                params.leftMargin = 10;//设置圆点间隔
            }
            point.setLayoutParams(params);//设置圆点大小
            llPointGroup.addView(point);//将圆点添加给线性布局
        }

        // 获取视图树, 对layout结束事件进行监听
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointWidth = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
            }
        });

    }

    class GuiderAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }
    }

    /**
     * viewpage滑动监听器
     */
    class GuiderListen implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPoint.getLayoutParams();
            params.leftMargin = len;
            viewPoint.setLayoutParams(params);// 重新给小红点设置布局参数
        }

        @Override
        public void onPageSelected(int position) {
            if(position == mImageIds.length - 1){
                btn_start.setVisibility(View.VISIBLE);
            }else {
                btn_start.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
