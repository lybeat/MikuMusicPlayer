package cc.sayaki.music.ui.playlist;

import java.util.List;

import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.ui.base.BasePresenter;
import cc.sayaki.music.ui.base.BaseView;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public interface PlayListContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void handleError(Throwable error);

        void onPlayListLoaded(List<PlayList> playLists);

        void onPlayListCreated(PlayList playList);

        void onPlayListEdited(PlayList playList);

        void onPlayListDeleted(PlayList playList);
    }

    interface Presenter extends BasePresenter {

        void loadPlayLists();

        void createPlayList(PlayList playList);

        void editPlayList(PlayList playList);

        void deletePlayList(PlayList playList);
    }
}
