package com.javen.smartcloud.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.javen.smartcloud.R;
import com.javen.smartcloud.widget.imageloader.Imageloader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageTestActivity extends AppCompatActivity {

    @BindView(R.id.id_show_img)
    ImageView showImage;
    @BindView(R.id.view_need_offset)
    CoordinatorLayout mViewNeedOffset;


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);
        ButterKnife.bind(this);
        mContext = this;
//        showImage.setBackgroundResource(R.drawable.image_test);
        Imageloader.getInstance(this).setImage("http://ww3.sinaimg.cn/large/610dc034jw1f6qsn74e3yj20u011htc6.jpg", showImage, R.drawable.default_item_picture);
        setStatusBar();
    }


    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, mViewNeedOffset);
    }
}
