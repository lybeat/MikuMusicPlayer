package cc.sayaki.music.ui.album;

import cc.sayaki.music.data.model.AlbumResp;
import cc.sayaki.music.data.source.MusicRepository;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class AlbumPresenter implements AlbumContract.Presenter {

    private AlbumContract.View view;
    private MusicRepository repository;
    private CompositeSubscription compositeSubscription;

    public AlbumPresenter(MusicRepository repository, AlbumContract.View view) {
        this.repository = repository;
        this.view = view;
        this.compositeSubscription = new CompositeSubscription();
        this.view.setPresenter(this);
    }

    public void subscribe() {
        loadRemoteAlbum();
    }

    @Override
    public void unsubscribe() {
        view = null;
        compositeSubscription.clear();
    }

    @Override
    public void loadLocalAlbum() {

    }

    @Override
    public void loadRemoteAlbum() {
        Subscription subscription = repository.loadRemoteAlba()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AlbumResp>() {
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
                    public void onNext(AlbumResp albumResp) {
                        if (albumResp.isStatus()) {
                            view.onAlbumLoaded(albumResp.getAlba());
                        } else {
                            onError(new Throwable("获取专辑列表失败"));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
}
