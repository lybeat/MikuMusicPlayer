package cc.sayaki.music.data.source;

import java.util.List;

import cc.sayaki.music.data.model.Album;
import cc.sayaki.music.data.model.AlbumResp;
import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import rx.Observable;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public interface MusicContract {

    // Album
    Observable<List<Album>> loadLocalAlba();

    Observable<AlbumResp> loadRemoteAlba();

    // PlayList
    Observable<List<PlayList>> loadLocalPlayLists();

    Observable<List<PlayList>> loadRemotePlayLists();

    List<PlayList> cachedPlayLists();

    Observable<PlayList> create(PlayList playList);

    Observable<PlayList> update(PlayList playList);

    Observable<PlayList> delete(PlayList playList);

    // Folder
    Observable<List<Folder>> folders();

    Observable<Folder> create(Folder folder);

    Observable<List<Folder>> create(List<Folder> folders);

    Observable<Folder> update(Folder folder);

    Observable<Folder> delete(Folder folder);

    // Song
    Observable<List<Song>> loadRemoteSongs(String rid, String timestamp);

    Observable<List<Song>> insert(List<Song> songs);

    Observable<Song> update(Song song);

    Observable<Song> setSongAsFavorite(Song song, boolean favorite);
}
