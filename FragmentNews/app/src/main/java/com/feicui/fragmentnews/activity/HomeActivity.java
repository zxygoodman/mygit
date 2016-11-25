package com.feicui.fragmentnews.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.feicui.fragmentnews.R;
import com.feicui.fragmentnews.adapter.LeftAdapter;
import com.feicui.fragmentnews.adapter.NewsViewPagerAdapter;
import com.feicui.fragmentnews.utils.ExampleUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

public class HomeActivity extends FragmentActivity implements View.OnClickListener{

    private TextView mInit;
    private TextView mStopPush;
    public static boolean isForeground = false;

    private MilitaryFragment militaryFragment;
    private ArrayList<Fragment> fragmentList;
    private FragmentManager fManager;
    private RadioButton rbTalk;
    private RadioButton rbMsg;
    private ViewPager viewPager;
    //侧滑
    private SlidingMenu sm;

    //动画图片
    private ImageView cursor;

    //动画图片偏移量
    private int position_one;
    private int position_two;

    //动画图片宽度
    private int bmpW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        registerMessageReceiver();  // used for receive msg


        initRadioButton();

        //初始化ImageView
        InitImageView();

        initFragment();

        initViewPager();

//        cutListView();

        initSlidingMenu();
        // 抽屉
        initDrawer();
    }

    private void initDrawer() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        final LinearLayout llRight = (LinearLayout) findViewById(R.id.ll_right);
        ViewGroup.LayoutParams layoutParams = llRight.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels * 1 / 2; //设置抽屉出现的宽度
        llRight.setLayoutParams(layoutParams);
    }

    private void initSlidingMenu() {
        // 创建SlidingMenu
        sm = new SlidingMenu(this);
        // 设置滑出方式
        sm.setMode(SlidingMenu.LEFT);
        // 引用布局
        sm.setMenu(R.layout.layout_slidingmenu);
        // 设置拖动方式
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        // 设置侧滑出的宽度
        sm.setBehindWidth(450);
        // 将布局添加到activity中
        sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sm.setBehindScrollScale(0.35F);

        ListView leftList = (ListView) findViewById(R.id.lv_left_list);
        ArrayList<String> list = new ArrayList<String>();
        list.add("新闻");
        list.add("收藏");
        list.add("本地");
        list.add("跟帖");
        list.add("图片");
        LeftAdapter leftAdapter = new LeftAdapter(this,list);
        //头布局
        View headerView = View.inflate(this,R.layout.layout_header_item,null);
        // 将头布局放入listView当中
        leftList.addHeaderView(headerView);
        leftList.setAdapter(leftAdapter);

    }

    private void initRadioButton() {
        rbTalk = (RadioButton) findViewById(R.id.rb_talk);
        rbMsg = (RadioButton) findViewById(R.id.rb_message);
        //添加点击事件
        rbTalk.setOnClickListener(new MyOnClickListener(0));
        rbMsg.setOnClickListener(new MyOnClickListener(1));
    }

    private void initViewPager() {

        viewPager = (ViewPager) findViewById(R.id.vPager);
        NewsViewPagerAdapter vpAdapter = new NewsViewPagerAdapter(fManager, fragmentList);
        viewPager.setAdapter(vpAdapter);
        //让ViewPager缓存2个页面
        viewPager.setOffscreenPageLimit(2);
        //设置默认打开第一页
        viewPager.setCurrentItem(0);
        //将顶部文字恢复默认值
        resetTextViewTextColor();
        rbTalk.setTextColor(getResources().getColor(R.color.main_top_tab_color2));
        //设置viewpager页面滑动监听事件
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        ImageView sliding = (ImageView) findViewById(R.id.iv_sliding);
        sliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件与SlidingMenu关联
                sm.toggle();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获取分辨率宽度
        int screenW = dm.widthPixels;

        bmpW = (screenW / 2);

        //设置动画图片宽度
        setBmpW(cursor, bmpW);

        //动画图片偏移量赋值
        position_one = (int) (screenW / 2.0);
        position_two = position_one * 2;

    }

    /**
     * 设置动画图片宽度
     *
     * @param mWidth
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }

    private void resetTextViewTextColor() {
        rbTalk.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        rbMsg.setTextColor(getResources().getColor(R.color.main_top_tab_color));
    }

    private void initFragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new MilitaryFragment());
        fragmentList.add(new SocialFragment());
        fManager = getSupportFragmentManager();
    }

//    private void cutListView() {
//
//        militaryFragment = new MilitaryFragment();
//        // 获取fragmentManager
//        FragmentManager fm = getSupportFragmentManager();
//        // 开启事物
//        FragmentTransaction fragTran = fm.beginTransaction();
//        // 替换布局
//        fragTran.replace(R.id.vPager, militaryFragment);
//        // 提交事物
//        fragTran.commit();
//
//        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // 获取fragmentManager
//                FragmentManager fm = getSupportFragmentManager();
//                // 开启事物
//                android.support.v4.app.FragmentTransaction fragTran = fm.beginTransaction();
//                switch (checkedId) {
//                    case R.id.rb_talk:
//                        if (militaryFragment == null) {
//                            militaryFragment = new MilitaryFragment();
//                        }
//                        // 替换布局
//                        fragTran.replace(R.id.vPager, militaryFragment);
//                        // 提交事物
//                        fragTran.commit();
//                        break;
//                    case R.id.rb_message:
//                        SocialFragment socialFragment = null;
//                        if (socialFragment == null) {
//                            socialFragment = new SocialFragment();
//                        }
//                        // 替换布局
//                        fragTran.replace(R.id.vPager, socialFragment);
//                        // 提交事物
//                        fragTran.commit();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0:
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                    resetTextViewTextColor();
                    rbTalk.setTextColor(getResources().getColor(R.color.main_top_tab_color2));
                    position = 1;
                    break;
                case 1:
                    animation = new TranslateAnimation(0, position_one, 0, 0);
                    resetTextViewTextColor();
                    rbMsg.setTextColor(getResources().getColor(R.color.main_top_tab_color2));
                    position = 0;
                    break;
                default:
                    break;
            }
            animation.setFillAfter(true);
            animation.setDuration(500);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initView(){

        mInit = (TextView)findViewById(R.id.init);
        mInit.setOnClickListener(this);

        mStopPush = (TextView)findViewById(R.id.stopPush);
        mStopPush.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init:
                init();
                break;
            case R.id.stopPush:
                JPushInterface.stopPush(getApplicationContext());
                break;
        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init(){
        JPushInterface.init(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.feicui.fragmentnews.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

}
