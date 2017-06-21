package cc.sayaki.music.ui.playlist;

import cc.sayaki.music.RxBus;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.event.PlayListUpdatedEvent;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: sayaki
 * Date: 2017/6/12
 */
public class SongListPresenter implements SongListContract.Presenter {

    private SongListContract.View view;
    private MusicRepository repository;
    private CompositeSubscription compositeSubscription;

    public SongListPresenter(MusicRepository repository, SongListContract.View view) {
        this.view = view;
        this.repository = repository;
        this.compositeSubscription = new CompositeSubscription();
        this.view.setPresenter(this);
    }

    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        view = null;
        compositeSubscription.clear();
    }

    @Override
    public void add(Song song, PlayList playList) {
        if (playList.isFavorite()) {
            song.setFavorite(true);
        }
        playList.addSong(song, 0);
        Subscription subscription = repository.update(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlayList>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(PlayList playList) {
                        RxBus.getInstance().post(new PlayListUpdatedEvent(playList));
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void delete(final Song song, PlayList playList) {
        playList.removeSong(song);
        Subscription subscription = repository.update(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlayList>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(PlayList playList) {
                        view.onSongDeleted(song);
                        RxBus.getInstance().post(new PlayListUpdatedEvent(playList));
                    }
                });
        compositeSubscription.add(subscription);
    }
}
