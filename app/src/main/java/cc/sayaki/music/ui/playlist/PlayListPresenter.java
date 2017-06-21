package cc.sayaki.music.ui.playlist;

import android.util.Log;

import java.util.List;

import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.source.MusicRepository;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class PlayListPresenter implements PlayListContract.Presenter {

    private PlayListContract.View view;
    private MusicRepository repository;
    private CompositeSubscription compositeSubscription;

    public PlayListPresenter(MusicRepository repository, PlayListContract.View view) {
        this.repository = repository;
        this.view = view;
        this.compositeSubscription = new CompositeSubscription();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadLocalPlayLists();
//        loadRemotePlayLists();
    }

    @Override
    public void unsubscribe() {
        view = null;
        compositeSubscription.clear();
    }

    @Override
    public void loadLocalPlayLists() {
        Subscription subscription = repository.loadLocalPlayLists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PlayList>>() {
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
                    public void onNext(List<PlayList> playLists) {
                        view.onPlayListLoaded(playLists);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void loadRemotePlayLists() {
        Subscription subscription = repository.loadRemotePlayLists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PlayList>>() {
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
                        Log.i("PlayListPresenter", "@@@onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<PlayList> playLists) {
                        view.onPlayListLoaded(playLists);
                        Log.i("PlayListPresenter", "@@@onNext");
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void createPlayList(PlayList playList) {
        Subscription subscription = repository.create(playList)
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
                        view.onPlayListCreated(playList);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void editPlayList(PlayList playList) {
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
                        view.onPlayListEdited(playList);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void deletePlayList(PlayList playList) {
        Subscription subscription = repository.delete(playList)
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
                        view.onPlayListDeleted(playList);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
