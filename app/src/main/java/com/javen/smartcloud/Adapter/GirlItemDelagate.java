package com.javen.smartcloud.Adapter;

import android.widget.ImageView;

import com.javen.smartcloud.R;
import com.javen.smartcloud.entity.GankEntity;
import com.javen.smartcloud.widget.imageloader.Imageloader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by Javen on 16/8/13.
 * 显示图片
 */
public class GirlItemDelagate implements ItemViewDelegate<GankEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_new_girl_content;
    }

    @Override
    public boolean isForViewType(GankEntity item, int position) {
        return item.getType().equals("\u798f\u5229") ? true : false;
    }

    @Override
    public void convert(ViewHolder holder, GankEntity gankEntity, int position) {
        ImageView view = holder.getView(R.id.id_news_pic);
        Imageloader.getInstance(view.getContext()).setImage(gankEntity.getUrl(),view,R.drawable.default_item_picture);
    }
}
