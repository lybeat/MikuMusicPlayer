package cc.sayaki.music.ui.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import cc.sayaki.music.RxBus;
import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.data.sp.MusicSp;
import cc.sayaki.music.event.FavoriteChangeEvent;
import cc.sayaki.music.player.PlaybackService;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: sayaki
 * Date: 2017/6/8
 */
public class MusicPlayerPresenter implements MusicPlayerContract.Presenter {

    private Context context;
    private MusicPlayerContract.View view;
    private MusicRepository repository;
    private CompositeSubscription compositeSubscription;

    private PlaybackService playbackService;
    private boolean isServiceBound;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playbackService = ((PlaybackService.LocalBinder) service).getService();
            view.onPlaybackServiceBound(playbackService);
            view.onSongUpdated(playbackService.getPlayingSong());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playbackService = null;
            view.onPlaybackServiceUnbound();
        }
    };

    public MusicPlayerPresenter(Context context, MusicRepository repository, MusicPlayerContract.View view) {
        this.context = context;
        this.view = view;
        this.repository = repository;
        this.compositeSubscription = new CompositeSubscription();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        bindPlaybackService();
        retrieveLastPlayMode();

        if (playbackService != null && playbackService.isPlaying()) {
            view.onSongUpdated(playbackService.getPlayingSong());
        }
    }

    @Override
    public void unsubscribe() {
        unbindPlaybackService();
        context = null;
        view = null;
        compositeSubscription.clear();
    }

    @Override
    public void retrieveLastPlayMode() {
        view.updatePlayMode(MusicSp.getPlayMode(context));
    }

    @Override
    public void setSongAsFavorite(Song song, boolean favorite) {
        Subscription subscription = repository.setSongAsFavorite(song, favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Song>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(Song song) {
                        view.onSongSetAsFavorite(song);
                        RxBus.getInstance().post(new FavoriteChangeEvent(song));
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void bindPlaybackService() {
        Intent intent = new Intent(context, PlaybackService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        isServiceBound = true;
    }

    @Override
    public void unbindPlaybackService() {
        if (isServiceBound) {
            context.unbindService(connection);
            isServiceBound = false;
        }
    }
}
