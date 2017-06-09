package cc.sayaki.music.ui.playlist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.List;

import butterknife.BindView;
import cc.sayaki.music.R;
import cc.sayaki.music.RxBus;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.event.FavoriteChangeEvent;
import cc.sayaki.music.event.PlayListCreatedEvent;
import cc.sayaki.music.event.PlayListUpdatedEvent;
import cc.sayaki.music.ui.base.BaseActivity;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class PlayListActivity extends BaseActivity implements
        PlayListContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private PlayListAdapter adapter;
    private int editIndex, deleteIndex;

    private PlayListContract.Presenter presenter;

    protected void setContentView() {
        setContentView(R.layout.activity_play_list);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        adapter = new PlayListAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                // TODO: 2017/6/9 跳转的具体的播放列表
            }
        });

        new PlayListPresenter(MusicRepository.getInstance(), this).subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    // RxBus Events
    @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof PlayListCreatedEvent) {
                            onPlayListCreatedEvent((PlayListCreatedEvent) o);
                        } else if (o instanceof FavoriteChangeEvent) {
                            onFavoriteChangeEvent((FavoriteChangeEvent) o);
                        } else if (o instanceof PlayListUpdatedEvent) {
                            onPlayListUpdatedEvent((PlayListUpdatedEvent) o);
                        }
                    }
                })
                .subscribe(RxBus.defaultSubscriber());
    }

    private void onPlayListCreatedEvent(PlayListCreatedEvent event) {

    }

    private void onFavoriteChangeEvent(FavoriteChangeEvent event) {
        presenter.loadPlayLists();
    }

    private void onPlayListUpdatedEvent(PlayListUpdatedEvent event) {
        presenter.loadPlayLists();
    }

    @Override
    public void setPresenter(PlayListContract.Presenter presenter) {
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
    public void onPlayListLoaded(List<PlayList> playLists) {
        adapter.setNewData(playLists);
    }

    @Override
    public void onPlayListCreated(PlayList playList) {
        adapter.addData(playList);
    }

    @Override
    public void onPlayListEdited(PlayList playList) {
        adapter.getData().set(editIndex, playList);
        adapter.notifyItemChanged(editIndex);
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        adapter.remove(deleteIndex);
    }
}
