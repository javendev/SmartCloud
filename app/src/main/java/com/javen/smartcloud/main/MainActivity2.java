package com.javen.smartcloud.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javen.smartcloud.R;
import com.javen.smartcloud.fragment.FragmentFour;
import com.javen.smartcloud.fragment.FragmentOne;
import com.javen.smartcloud.fragment.FragmentThree;
import com.javen.smartcloud.fragment.FragmentTwo;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity2 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.toolbar_title)
    TextView mToolbar_title;
    @BindView(R.id.tl_custom)
    Toolbar mToolbar;
    @BindView(R.id.lv_left_menu)
    ListView lvLeftMenu;
    @BindView(R.id.dl_left)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_ViewPager)
    ViewPager mainViewPager;
    @BindView(R.id.radio_joke)
    RadioButton radioJoke;
    @BindView(R.id.radio_girls)
    RadioButton radioGirls;
    @BindView(R.id.radio_news)
    RadioButton radioNews;
    @BindView(R.id.radio_about)
    RadioButton radioAbout;
    @BindView(R.id.main_tab_RadioGroup)
    RadioGroup mainTabRadioGroup;

    private ActionBarDrawerToggle mDrawerToggle;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;


    //类型为Fragment的动态数组
    private ArrayList<Fragment> fragmentList;
    Fragment oneFragment, twoFragment, threeFragment, fourFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolBar();//初始化toolbar
        initViews(); //获取控件
        initViewPager();//ViewPager初始化函数
    }

    private void initViews() {
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Logger.i("侧边栏打开");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Logger.i("侧边栏关闭");
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        mainTabRadioGroup.setOnCheckedChangeListener(this);
    }

    private void initViewPager() {
        fragmentList = new ArrayList<Fragment>();

        oneFragment = new FragmentOne();
        twoFragment = new FragmentTwo();
        threeFragment = new FragmentThree();
        fourFragment = new FragmentFour();

        //将各Fragment加入数组中
        fragmentList.add(oneFragment);
        fragmentList.add(twoFragment);
        fragmentList.add(threeFragment);
        fragmentList.add(fourFragment);

        //设置ViewPager的设配器
        mainViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        //当前为第一个页面
        mainViewPager.setCurrentItem(0);
        //ViewPager的页面改变监听器
        mainViewPager.addOnPageChangeListener(new MyViewPageListner());
    }

    private void initToolBar() {
        mToolbar.setTitle("标题");//设置Toolbar标题
        mToolbar_title.setText("自定义标题");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", 0).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", 0).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * 菜单项被点击时调用，也就是菜单项的监听方法。
         * 通过这几个方法，可以得知，对于Activity，同一时间只能显示和监听一个Menu 对象。
         * method stub
         */
        switch (item.getItemId()) {

            case R.id.action_share:
                Toast.makeText(MainActivity2.this, "action_share", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (oneFragment != null && !oneFragment.isHidden()) fragmentTransaction.hide(oneFragment);
        if (twoFragment != null && !twoFragment.isHidden()) fragmentTransaction.hide(twoFragment);
        if (threeFragment != null && !threeFragment.isHidden()) fragmentTransaction.hide(threeFragment);
        if (fourFragment != null && !fourFragment.isHidden()) fragmentTransaction.hide(fourFragment);
    }

    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;

        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    public class MyViewPageListner implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(fTransaction);
            //获取当前页面用于改变对应RadioButton的状态
            int current = mainViewPager.getCurrentItem();
            switch (current) {
                case 0:
                    mainTabRadioGroup.check(R.id.radio_joke);
                    if (oneFragment.isHidden())
                        fTransaction.show(oneFragment);
                    break;
                case 1:
                    mainTabRadioGroup.check(R.id.radio_girls);
                    if (twoFragment.isHidden())
                        fTransaction.show(twoFragment);
                    break;
                case 2:
                    mainTabRadioGroup.check(R.id.radio_news);
                    if (threeFragment.isHidden())
                        fTransaction.show(threeFragment);
                    break;
                case 3:
                    mainTabRadioGroup.check(R.id.radio_about);
                    if (fourFragment.isHidden())
                        fTransaction.show(fourFragment);
                    break;
            }
            fTransaction.commit();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //获取当前被选中的RadioButton的ID，用于改变ViewPager的当前页
        int current = 0;
        switch (checkedId) {
            case R.id.radio_joke:
                current = 0;
                break;
            case R.id.radio_girls:
                current = 1;
                break;
            case R.id.radio_news:
                current = 2;
                break;
            case R.id.radio_about:
                current = 3;
                break;
        }
        if (mainViewPager.getCurrentItem() != current) {
            mainViewPager.setCurrentItem(current);
        }
    }
}
