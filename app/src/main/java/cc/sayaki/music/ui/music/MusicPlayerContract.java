package cc.sayaki.music.ui.music;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.player.PlayMode;
import cc.sayaki.music.player.PlaybackService;
import cc.sayaki.music.ui.base.BasePresenter;
import cc.sayaki.music.ui.base.BaseView;

/**
 * Author: sayaki
 * Date: 2017/6/7
 */
public interface MusicPlayerContract {

    interface View extends BaseView<Presenter> {

        void handleError(Throwable error);

        void onPlaybackServiceBound(PlaybackService service);

        void onPlaybackServiceUnbound();

        void onSongFavorite(@NonNull Song song);

        void onSongUpdated(@Nullable Song song);

        void updatePlayMode(PlayMode playMode);

        void updatePlayToggle(boolean play);

        void updateFavoriteToggle(boolean favorite);
    }

    interface Presenter extends BasePresenter {

        void retrieveLastPlayMode();

        void setSongAsFavorite(Song song, boolean favorite);

        void bindPlaybackService();

        void unbindPlaybackService();
    }
}
