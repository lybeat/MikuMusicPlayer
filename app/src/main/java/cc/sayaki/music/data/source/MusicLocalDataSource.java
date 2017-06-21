package cc.sayaki.music.data.source;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.sayaki.music.data.model.Album;
import cc.sayaki.music.data.model.AlbumResp;
import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.data.net.client.DefaultRetrofit;
import cc.sayaki.music.data.net.service.MusicService;
import rx.Observable;
import rx.Subscriber;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class MusicLocalDataSource implements MusicContract {

    private static final String TAG = "MusicLocalDataSource";

    private Context context;
    private MusicService musicService;

    public MusicLocalDataSource(Context context) {
        this.context = context;
        this.musicService = new DefaultRetrofit().create().create(MusicService.class);
    }

    @Override
    public Observable<List<Album>> loadLocalAlba() {
        return null;
    }

    @Override
    public Observable<AlbumResp> loadRemoteAlba() {
        return musicService.loadRemoteAlbum("list");
    }

    @Override
    public Observable<List<PlayList>> loadLocalPlayLists() {
        return Observable.create(new Observable.OnSubscribe<List<PlayList>>() {
            @Override
            public void call(Subscriber<? super List<PlayList>> subscriber) {
                PlayList local = new PlayList();
                local.setName("本地音乐");
                local.setFavorite(false);
                local.setSongs(getLocalSongs());
                List<PlayList> playLists = new ArrayList<>();
                playLists.add(local);
                subscriber.onNext(playLists);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<PlayList>> loadRemotePlayLists() {
        return null;
    }

    private List<Song> getLocalSongs() {
        List<Song> songs = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setAlbum(getAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));

                songs.add(song);
            }
            cursor.close();
        }
        return songs;
    }

    private String getAlbum(String albumId) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cursor = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + albumId),
                projection, null, null, null);
        String albumArt = null;
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
                cursor.moveToNext();
                albumArt = cursor.getString(0);
            }
            cursor.close();
        }
        return albumArt;
    }

    @Override
    public List<PlayList> cachedPlayLists() {
        return null;
    }

    @Override
    public Observable<PlayList> create(final PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {
                Date now = new Date();
                playList.setCreatedAt(now);
                playList.setUpdatedAt(now);

                subscriber.onNext(playList);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<PlayList> update(final PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {

            }
        });
    }

    @Override
    public Observable<PlayList> delete(PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {

            }
        });
    }

    @Override
    public Observable<List<Folder>> folders() {
        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
            @Override
            public void call(Subscriber<? super List<Folder>> subscriber) {

            }
        });
    }

    @Override
    public Observable<Folder> create(Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {

            }
        });
    }

    @Override
    public Observable<List<Folder>> create(List<Folder> folders) {
        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
            @Override
            public void call(Subscriber<? super List<Folder>> subscriber) {

            }
        });
    }

    @Override
    public Observable<Folder> update(Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {

            }
        });
    }

    @Override
    public Observable<Folder> delete(Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {

            }
        });
    }

    @Override
    public Observable<List<Song>> loadRemoteSongs(String rid, String timestamp) {
        return musicService.loadRemoteSongs("radio2", rid, timestamp);
    }

    @Override
    public Observable<List<Song>> insert(List<Song> songs) {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {

            }
        });
    }

    @Override
    public Observable<Song> update(Song song) {
        return Observable.create(new Observable.OnSubscribe<Song>() {
            @Override
            public void call(Subscriber<? super Song> subscriber) {

            }
        });
    }

    @Override
    public Observable<Song> setSongAsFavorite(Song song, boolean favorite) {
        return Observable.create(new Observable.OnSubscribe<Song>() {
            @Override
            public void call(Subscriber<? super Song> subscriber) {

            }
        });
    }
}
