package com.javen.smartcloud.Adapter;

import android.content.Context;

import com.javen.smartcloud.entity.GankEntity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by Javen on 16/8/13.
 */
public class NewsAdapter extends MultiItemTypeAdapter<GankEntity> {
    public NewsAdapter(Context context, List<GankEntity> datas) {
        super(context, datas);

        addItemViewDelegate(new GirlItemDelagate());
        addItemViewDelegate(new ArticleItemDelagate());
    }
}
