package com.fireblack.zhihuibeijing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

/**
 * 闪屏页
 */
public class SplashActivity extends Activity {

    private RelativeLayout rlroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rlroot = (RelativeLayout) findViewById(R.id.rl_root);

        startAnim();
    }

    /**
     * 开启动画
     */
    private void startAnim() {

        AnimationSet set = new AnimationSet(false);
        //旋转动画
        RotateAnimation rotate = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);

        // 缩放动画
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(1000);// 动画时间
        scale.setFillAfter(true);// 保持动画状态

        // 渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);// 动画时间
        alpha.setFillAfter(true);// 保持动画状态

        //设置动画监听
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                JumpNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);

        rlroot.startAnimation(set);
    }

    /**
     * 跳转下一个页面
     */
    private void JumpNext() {
        startActivity(new Intent(SplashActivity.this,GuiderActivity.class));
        finish();
    }
}
