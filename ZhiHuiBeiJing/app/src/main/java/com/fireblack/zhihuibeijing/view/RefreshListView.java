package com.fireblack.zhihuibeijing.view;

import android.content.Context;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fireblack.zhihuibeijing.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的ListView
 * Created by ChengHao on 2016/7/21.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新


    private View mHeaderView;
    private int startY = -1;
    private int mHeaderViewHeight;
    private int mCurrentState = STATE_PULL_REFRESH;//当前状态
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar progress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private View mFooterView;
    private int mFooterViewHeight;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }


    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        progress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏头布局
        
        initArrowAnim();

        tvTime.setText("最后刷新时间" + getCurrentTime());
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        this.setOnScrollListener(this);
    }

    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        //箭头向上动画
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f
                                                    ,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        //箭头向下动画
        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,
                                                        Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                if(startY == -1){//确保startY有效
                    startY = (int) ev.getRawY();
                }
                if(mCurrentState == STATE_REFRESHING){//正在刷新是不处理
                    break;
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;//移动偏移量
                if(dy > 0 && getFirstVisiblePosition() == 0){// 只有下拉并且当前是第一个item,才允许下拉
                    int padding = dy - mHeaderViewHeight;
                    mHeaderView.setPadding(0,padding,0,0);

                    if(padding > 0 && mCurrentState != STATE_RELEASE_REFRESH){// 状态改为松开刷新
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    }else  if(padding < 0 && mCurrentState != STATE_PULL_REFRESH){// 改为下拉刷新状态
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                //重置
                startY = -1;
                if(mCurrentState == STATE_RELEASE_REFRESH){
                    mCurrentState = STATE_REFRESHING;
                    mHeaderView.setPadding(0,0,0,0);
                    refreshState();
                }else if(mCurrentState == STATE_PULL_REFRESH){
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrentState){
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新");
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);

                if(mListener != null){
                    mListener.OnRefresh();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 收起下拉刷新的控件
     */
    public void OnRefreshComplete(boolean success){
        if(isLoadingMore){//正在加载更多.....
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadingMore = false;
        }else {
            mCurrentState = STATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");
            ivArrow.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);

            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            if (success) {
                tvTime.setText("最后刷新时间" + getCurrentTime());
            }
        }
    }

    /**
     获取当前的时间* @return
     */
    public String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    OnRefreshListener mListener;
    public void setOnRefshListener(OnRefreshListener listener){
        mListener = listener;
    }

    public interface OnRefreshListener{
        public void OnRefresh();
        public void OnLoadMore();//加载下一页数据
    }

    private boolean isLoadingMore;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING){
            if(getLastVisiblePosition() == getCount() -1 && !isLoadingMore){
                Log.v("tag", "到底了");
                mFooterView.setPadding(0,0, 0, 0);
                setSelection(getCount() - 1);// 改变listview显示位置
                isLoadingMore = true;
                if(mListener != null){
                    mListener.OnLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    OnItemClickListener mItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mItemClickListener != null){
            mItemClickListener.onItemClick(parent,view,position - getHeaderViewsCount(),id);
        }
    }
}
