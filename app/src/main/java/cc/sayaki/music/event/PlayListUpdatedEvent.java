package cc.sayaki.music.event;

import cc.sayaki.music.data.model.PlayList;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class PlayListUpdatedEvent {

    public PlayList playList;

    public PlayListUpdatedEvent(PlayList playList) {
        this.playList = playList;
    }
}
