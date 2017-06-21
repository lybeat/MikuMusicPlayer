package cc.sayaki.music.ui.playlist;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.sayaki.music.R;
import cc.sayaki.music.data.model.Song;

/**
 * Author: sayaki
 * Date: 2017/6/12
 */
public class SongListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private Context context;

    public SongListAdapter(Context context, List<Song> data) {
        super(R.layout.item_play_list, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Song song) {
        baseViewHolder.setText(R.id.title_txt, song.getDisplayName())
                .setText(R.id.summary_txt, song.getArtist());

        ImageView album = baseViewHolder.getView(R.id.album_img);
        Glide.with(context)
                .load(song.getAlbum())
                .into(album);
    }
}
