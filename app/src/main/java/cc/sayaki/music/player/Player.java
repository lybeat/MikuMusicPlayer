package cc.sayaki.music.player;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;

/**
 * Author: sayaki
 * Date: 2017/6/6
 */
public class Player implements IPlayback, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private PlayList playList;
    // 默认两个回调接口，Service和UI
    private List<Callback> callbacks = new ArrayList<>(2);

    private boolean isPaused;

    private static final class HolderClass {
        private static final Player INSTANCE = new Player();
    }

    public static Player getInstance() {
        return HolderClass.INSTANCE;
    }

    private Player() {
        mediaPlayer = new MediaPlayer();
        playList = new PlayList();
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void setPlayList(PlayList list) {
        if (list == null) {
            list = new PlayList();
        }
        playList = list;
    }

    @Override
    public boolean play() {
        if (isPaused) {
            mediaPlayer.start();
            notifyPlayStatusChanged(true);
            return true;
        }
        if (playList.prepare()) {
            Song song = playList.getCurrentSong();
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(song.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                notifyPlayStatusChanged(true);
            } catch (IOException e) {
                e.printStackTrace();
                notifyPlayStatusChanged(false);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean play(PlayList list) {
        if (list == null) {
            return false;
        }
        isPaused = false;
        setPlayList(list);
        return play();
    }

    @Override
    public boolean play(PlayList list, int startIndex) {
        if (playList == null) {
            return false;
        }
        isPaused = false;
        list.setPlayingIndex(startIndex);
        setPlayList(list);
        return play();
    }

    @Override
    public boolean play(Song song) {
        if (song == null) {
            return false;
        }
        isPaused = false;
        playList.getSongs().clear();
        playList.getSongs().add(song);
        return play();
    }

    @Override
    public boolean playLast() {
        isPaused = false;
        boolean hasLast = playList.hasLast();
        if (hasLast) {
            Song last = playList.last();
            if (play()) {
                notifyPlayLast(last);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean playNext() {
        isPaused = false;
        boolean hasNext = playList.hasNext(false);
        if (hasNext) {
            Song next = playList.next();
            if (play()) {
                notifyPlayNext(next);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
            notifyPlayStatusChanged(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public Song getPlayingSong() {
        return playList.getCurrentSong();
    }

    @Override
    public boolean seekTo(int progress) {
        if (playList.getSongs().isEmpty()) {
            return false;
        }
        Song song = playList.getCurrentSong();
        if (song != null) {
            if (song.getDuration() <= progress) {
                onCompletion(mediaPlayer);
            } else {
                mediaPlayer.seekTo(progress);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        playList.setPlayMode(playMode);
    }

    @Override
    public void releasePlayer() {
        playList = null;
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void registerCallback(Callback callback) {
        callbacks.add(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        callbacks.remove(callback);
    }

    @Override
    public void removeCallbacks() {
        callbacks.clear();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Song next = null;
        PlayMode playMode = playList.getPlayMode();
        if (playMode == PlayMode.LIST && playList.getPlayingIndex() == playList.getNumOfSongs() - 1) {
            // In the end of the list
            // Do nothing, just deliver the callback
        } else if (playMode == PlayMode.SINGLE) {
            next = playList.getCurrentSong();
            play();
        } else {
            boolean hasNext = playList.hasNext(true);
            if (hasNext) {
                next = playList.next();
                play();
            }
        }
        notifyComplete(next);
    }

    private void notifyPlayStatusChanged(boolean isPlaying) {
        for (Callback callback : callbacks) {
            callback.onPlayStatusChanged(isPlaying);
        }
    }

    private void notifyPlayLast(Song song) {
        for (Callback callback : callbacks) {
            callback.onSwitchLast(song);
        }
    }

    private void notifyPlayNext(Song song) {
        for (Callback callback : callbacks) {
            callback.onSwitchNext(song);
        }
    }

    private void notifyComplete(Song song) {
        for (Callback callback : callbacks) {
            callback.onComplete(song);
        }
    }
}
