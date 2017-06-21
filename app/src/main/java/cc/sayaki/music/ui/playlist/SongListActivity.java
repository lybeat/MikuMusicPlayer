package cc.sayaki.music.ui.playlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import butterknife.BindView;
import cc.sayaki.music.R;
import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.ui.base.BaseActivity;
import cc.sayaki.music.ui.music.MusicPlayerActivity;

/**
 * Author: sayaki
 * Date: 2017/6/12
 */
public class SongListActivity extends BaseActivity implements SongListContract.View {

    public static final String EXTRA_FOLDER = "extraFolder";
    public static final String EXTRA_PLAY_LIST = "extraPlayList";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private PlayList playList;
    private SongListAdapter adapter;

    private boolean isFolder;

    private SongListContract.Presenter presenter;

    public static void launchForPlayList(Context context, PlayList playList) {
        Intent intent = new Intent();
        intent.setClass(context, SongListActivity.class);
        intent.putExtra(EXTRA_PLAY_LIST, playList);
        context.startActivity(intent);
    }

    public static void launchForFolder(Context context, Folder folder) {
        Intent intent = new Intent();
        intent.setClass(context, SongListActivity.class);
        intent.putExtra(EXTRA_FOLDER, folder);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_play_list);
    }

    @Override
    protected void initData() {
        Folder folder = getIntent().getParcelableExtra(EXTRA_FOLDER);
        playList = getIntent().getParcelableExtra(EXTRA_PLAY_LIST);
        if (folder == null && playList == null) {
            return;
        }
        if (folder != null) {
            isFolder = true;
            playList = PlayList.fromFloder(folder);
        }
    }

    @Override
    protected void initView() {
        adapter = new SongListAdapter(this, playList.getSongs());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MusicPlayerActivity.launch(SongListActivity.this, playList, i);
            }
        });

        new SongListPresenter(MusicRepository.getInstance(), this).subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(SongListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void handleError(Throwable e) {

    }

    @Override
    public void onSongDeleted(Song song) {
    }
}
