package cc.sayaki.music.ui.playlist;

import android.view.View;
import android.widget.AdapterView;

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

    private PlayListCallback playListCallback;

    public PlayListAdapter(List<PlayList> data) {
        super(R.layout.item_play_list, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final PlayList playList) {
        baseViewHolder.setText(R.id.title_txt, playList.getName())
                .setText(R.id.summary_txt, playList.getItemCount() + "首歌");

        baseViewHolder.setOnItemClickListener(R.id.action_img, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (playListCallback != null) {
                    playListCallback.onAction(baseViewHolder.getView(R.id.action_img), position);
                }
            }
        });
    }

    public interface PlayListCallback {
        void onAction(View actionView, int position);

        void onAddPlayList();
    }
}
