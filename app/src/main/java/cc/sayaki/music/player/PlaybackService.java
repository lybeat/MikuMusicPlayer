package cc.sayaki.music.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import cc.sayaki.music.ui.main.MainActivity;
import cc.sayaki.music.R;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;

/**
 * Author: sayaki
 * Date: 2017/6/7
 */
public class PlaybackService extends Service implements IPlayback, IPlayback.Callback {

    private static final String ACTION_PLAY_TOGGLE = "cc.sayaki.music.ACTION_PLAY_TOGGLE";
    private static final String ACTION_PLAY_LAST = "cc.sayaki.music.ACTION_LAST";
    private static final String ACTION_PLAY_NEXT = "cc.sayaki.music.ACTION_NEXT";
    private static final String ACTION_STOP_SERVICE = "cc.sayaki.music.ACTION_STOP_SERVICE";

    private static final int NOTIFICATION_ID = 1;

    private RemoteViews bigRemoteViews, smallRemoteViews;
    private Player player;

    private final Binder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = Player.getInstance();
        player.registerCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY_TOGGLE.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
            } else if (ACTION_PLAY_LAST.equals(action)) {
                playLast();
            } else if (ACTION_PLAY_NEXT.equals(action)) {
                playNext();
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (isPlaying()) {
                    pause();
                }
                stopForeground(true);
                unregisterCallback(this);
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        unregisterCallback(this);
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    @Override
    public void setPlayList(PlayList list) {
        player.setPlayList(list);
    }

    @Override
    public boolean play() {
        return player.play();
    }

    @Override
    public boolean play(PlayList list) {
        return player.play(list);
    }

    @Override
    public boolean play(PlayList list, int startIndex) {
        return player.play(list, startIndex);
    }

    @Override
    public boolean play(Song song) {
        return player.play(song);
    }

    @Override
    public boolean playLast() {
        return player.playLast();
    }

    @Override
    public boolean playNext() {
        return player.playNext();
    }

    @Override
    public boolean pause() {
        return player.pause();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getProgress() {
        return player.getProgress();
    }

    @Override
    public Song getPlayingSong() {
        return player.getPlayingSong();
    }

    @Override
    public boolean seekTo(int progress) {
        return player.seekTo(progress);
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        player.setPlayMode(playMode);
    }

    @Override
    public void registerCallback(Callback callback) {
        player.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        player.unregisterCallback(callback);
    }

    @Override
    public void removeCallbacks() {
        player.removeCallbacks();
    }

    @Override
    public void releasePlayer() {
        player.releasePlayer();
        super.onDestroy();
    }

    @Override
    public void onSwitchLast(@Nullable Song last) {
        showNotification();
    }

    @Override
    public void onSwitchNext(@Nullable Song next) {
        showNotification();
    }

    @Override
    public void onComplete(@Nullable Song next) {
        showNotification();
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        showNotification();
    }

    private void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .setCustomContentView(getSmallRemoteViews())
                .setCustomBigContentView(getBigRemoteViews())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private RemoteViews getSmallRemoteViews() {
        if (smallRemoteViews == null) {
            smallRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_music_small);
            setUpRemoteViews(smallRemoteViews);
        }
        updateRemoteViews(smallRemoteViews);
        return smallRemoteViews;
    }

    private RemoteViews getBigRemoteViews() {
        if (bigRemoteViews == null) {
            bigRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_music_big);
            setUpRemoteViews(bigRemoteViews);
        }
        updateRemoteViews(bigRemoteViews);
        return bigRemoteViews;
    }

    private void setUpRemoteViews(RemoteViews remoteViews) {
        remoteViews.setImageViewResource(R.id.close_img, R.drawable.ic_remote_view_close);
        remoteViews.setImageViewResource(R.id.play_last_img, R.drawable.ic_remote_view_play_last);
        remoteViews.setImageViewResource(R.id.play_next_img, R.drawable.ic_remote_view_play_next);

        remoteViews.setOnClickPendingIntent(R.id.play_toggle_img, getPendingIntent(ACTION_PLAY_TOGGLE));
        remoteViews.setOnClickPendingIntent(R.id.play_last_img, getPendingIntent(ACTION_PLAY_LAST));
        remoteViews.setOnClickPendingIntent(R.id.play_next_img, getPendingIntent(ACTION_PLAY_NEXT));
        remoteViews.setOnClickPendingIntent(R.id.close_img, getPendingIntent(ACTION_STOP_SERVICE));
    }

    private void updateRemoteViews(RemoteViews remoteViews) {
        Song song = player.getPlayingSong();
        if (song != null) {
            remoteViews.setTextViewText(R.id.name_txt, song.getDisplayName());
            remoteViews.setTextViewText(R.id.artist_txt, song.getArtist());
        }
        remoteViews.setImageViewResource(R.id.play_toggle_img, isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);
    }
}
