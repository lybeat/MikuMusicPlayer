package cc.sayaki.music.ui.playlist;

import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.ui.base.BasePresenter;
import cc.sayaki.music.ui.base.BaseView;

/**
 * Author: sayaki
 * Date: 2017/6/12
 */
public interface SongListContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void handleError(Throwable e);

        void onSongDeleted(Song song);
    }

    interface Presenter extends BasePresenter {

        void add(Song song, PlayList playList);

        void delete(Song song, PlayList playList);
    }
}
