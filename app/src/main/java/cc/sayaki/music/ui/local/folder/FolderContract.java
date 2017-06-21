package cc.sayaki.music.ui.local.folder;

import java.io.File;
import java.util.List;

import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.ui.base.BasePresenter;
import cc.sayaki.music.ui.base.BaseView;

/**
 * Author: sayaki
 * Date: 2017/6/16
 */
public interface FolderContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void handleError(Throwable error);

        void onFoldersLoaded(List<Folder> folders);

        void onFoldersAdded(List<Folder> folders);

        void onFolderUpdated(Folder folder);

        void onFolderDeleted(Folder folder);

        void onPlayListCreated(PlayList playList);
    }

    interface Presenter extends BasePresenter {

        void loadFolders();

        void addFolders(List<File> folders, List<Folder> existedFolders);

        void refreshFolders(Folder folder);

        void deleteFolders(Folder folder);

        void createPlayList(PlayList playList);

        void addFolderToPlayList(Folder folder, PlayList playList);
    }
}
