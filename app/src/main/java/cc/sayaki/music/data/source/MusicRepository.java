package cc.sayaki.music.data.source;

import java.util.ArrayList;
import java.util.List;

import cc.sayaki.music.MikuApplication;
import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import rx.Observable;
import rx.functions.Action1;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class MusicRepository implements MusicContract {

    private MusicLocalDataSource localDataSource;
    private List<PlayList> cachedPlayLists;

    private MusicRepository() {
        localDataSource = new MusicLocalDataSource(MikuApplication.getInstance());
    }

    public static MusicRepository getInstance() {
        return HolderClass.INSTANCE;
    }

    private static final class HolderClass {
        private static final MusicRepository INSTANCE = new MusicRepository();
    }

    @Override
    public Observable<List<PlayList>> playLists() {
        return localDataSource.playLists()
                .doOnNext(new Action1<List<PlayList>>() {
                    @Override
                    public void call(List<PlayList> playLists) {
                        cachedPlayLists = playLists;
                    }
                });
    }

    @Override
    public List<PlayList> cachedPlayLists() {
        if (cachedPlayLists == null) {
            return new ArrayList<>(0);
        }
        return cachedPlayLists;
    }

    @Override
    public Observable<PlayList> create(PlayList playList) {
        return localDataSource.create(playList);
    }

    @Override
    public Observable<PlayList> update(PlayList playList) {
        return localDataSource.update(playList);
    }

    @Override
    public Observable<PlayList> delete(PlayList playList) {
        return localDataSource.delete(playList);
    }

    @Override
    public Observable<List<Folder>> folders() {
        return localDataSource.folders();
    }

    @Override
    public Observable<Folder> create(Folder folder) {
        return localDataSource.create(folder);
    }

    @Override
    public Observable<List<Folder>> create(List<Folder> folders) {
        return localDataSource.create(folders);
    }

    @Override
    public Observable<Folder> update(Folder folder) {
        return localDataSource.update(folder);
    }

    @Override
    public Observable<Folder> delete(Folder folder) {
        return localDataSource.delete(folder);
    }

    @Override
    public Observable<List<Song>> insert(List<Song> songs) {
        return localDataSource.insert(songs);
    }

    @Override
    public Observable<Song> update(Song song) {
        return localDataSource.update(song);
    }

    @Override
    public Observable<Song> setSongAsFavorite(Song song, boolean favorite) {
        return localDataSource.setSongAsFavorite(song, favorite);
    }
}
