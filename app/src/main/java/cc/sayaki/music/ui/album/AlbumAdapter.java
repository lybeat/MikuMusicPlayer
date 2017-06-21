package cc.sayaki.music.ui.album;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.labelview.LabelView;

import java.util.List;

import cc.sayaki.music.R;
import cc.sayaki.music.data.model.Album;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class AlbumAdapter extends BaseQuickAdapter<Album, BaseViewHolder> {

    public AlbumAdapter(List<Album> data) {
        super(R.layout.item_album, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Album album) {
        LabelView labelView = baseViewHolder.getView(R.id.label_view);
        labelView.setText(album.getName());
        ImageView coverImg = baseViewHolder.getView(R.id.cover_img);
        Glide.with(coverImg.getContext())
                .load(album.getImg())
                .placeholder(R.drawable.bg_placeholder)
                .into(coverImg);
    }
}
