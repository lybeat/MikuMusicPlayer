package cc.sayaki.music.ui.album;

import java.util.List;

import cc.sayaki.music.data.model.Album;
import cc.sayaki.music.ui.base.BasePresenter;
import cc.sayaki.music.ui.base.BaseView;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public interface AlbumContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void handleError(Throwable error);

        void onAlbumLoaded(List<Album> alba);
    }

    interface Presenter extends BasePresenter {

        void loadLocalAlbum();

        void loadRemoteAlbum();
    }
}
