package cc.sayaki.music.ui.playlist;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.sayaki.music.R;
import cc.sayaki.music.data.model.PlayList;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class PlayListAdapter extends BaseQuickAdapter<PlayList, BaseViewHolder> {

    private Context context;

    public PlayListAdapter(Context context, List<PlayList> data) {
        super(R.layout.item_play_list, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, PlayList playList) {
        baseViewHolder.setText(R.id.title_txt, playList.getName())
                .setText(R.id.summary_txt, playList.getItemCount() + "首歌")
                .addOnClickListener(R.id.action_img);
        ImageView album = baseViewHolder.getView(R.id.album_img);
        Glide.with(context)
                .load(playList.getSongs().get(0).getAlbum())
                .into(album);
    }
}
