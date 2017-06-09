package cc.sayaki.music.event;

import cc.sayaki.music.data.model.Song;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class FavoriteChangeEvent {

    public Song song;

    public FavoriteChangeEvent(Song song) {
        this.song = song;
    }
}
