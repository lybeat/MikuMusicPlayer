package cc.sayaki.music.data.net.service;

import java.util.List;

import cc.sayaki.music.data.model.AlbumResp;
import cc.sayaki.music.data.model.Song;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author: sayaki
 * Date: 2017/6/16
 */
public interface MusicService {

    @GET("fm/")
    Observable<AlbumResp> loadRemoteAlbum(@Query("a") String a);

    @GET("fm/")
    Observable<List<Song>> loadRemoteSongs(@Query("a") String a, @Query("rid") String rid, @Query("t") String timestamp);
}
