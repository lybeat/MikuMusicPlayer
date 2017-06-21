package cc.sayaki.music.ui.album;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import cc.sayaki.music.R;
import cc.sayaki.music.data.model.Album;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.ui.base.BaseActivity;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class AlbumActivity extends BaseActivity implements AlbumContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AlbumAdapter adapter;

    private AlbumContract.Presenter presenter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_play_list);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        adapter = new AlbumAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });

        new AlbumPresenter(MusicRepository.getInstance(), this).subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(AlbumContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void handleError(Throwable error) {

    }

    @Override
    public void onAlbumLoaded(List<Album> alba) {
        adapter.setNewData(alba);
    }
}
