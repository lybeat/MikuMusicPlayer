package cc.sayaki.music.event;

import cc.sayaki.music.data.model.Song;

/**
 * Author: sayaki
 * Date: 2017/6/8
 */
public class PlaySongEvent {

    public Song song;

    public PlaySongEvent(Song song) {
        this.song = song;
    }
}
