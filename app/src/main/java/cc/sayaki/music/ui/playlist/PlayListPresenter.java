package cc.sayaki.music.ui.playlist;

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
        compositeSubscription = new CompositeSubscription();
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadPlayLists();
    }

    @Override
    public void unsubscribe() {
        view = null;
        compositeSubscription.clear();
    }

    @Override
    public void loadPlayLists() {
        Subscription subscription = repository.playLists()
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
