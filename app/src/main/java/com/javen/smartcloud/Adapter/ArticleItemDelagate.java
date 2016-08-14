package com.javen.smartcloud.Adapter;

import com.javen.smartcloud.R;
import com.javen.smartcloud.entity.GankEntity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by Javen on 16/8/13.
 * IOS/Android/web
 */
public class ArticleItemDelagate implements ItemViewDelegate<GankEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_new_article_content;
    }

    @Override
    public boolean isForViewType(GankEntity item, int position) {
        String itemType = item.getType();
        boolean isItem=false;
        if (itemType.equals("\u524d\u7aef") || itemType.equalsIgnoreCase("android") ||
        itemType.equalsIgnoreCase("ios") || itemType.equals("\u4f11\u606f\u89c6\u9891")){
            isItem = true;
        }
        return isItem;
    }

    @Override
    public void convert(ViewHolder holder, GankEntity gankEntity, int position) {
        holder.setText(R.id.id_news_desc,gankEntity.getDesc());
        holder.setText(R.id.id_news_type,gankEntity.getType());
        holder.setText(R.id.id_news_updateTime,gankEntity.getPublishedAt());
        holder.setText(R.id.id_news_who,gankEntity.getWho());
    }
}
