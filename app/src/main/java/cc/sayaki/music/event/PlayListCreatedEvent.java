package cc.sayaki.music.event;

import cc.sayaki.music.data.model.PlayList;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class PlayListCreatedEvent {

    public PlayList playList;

    public PlayListCreatedEvent(PlayList playList) {
        this.playList = playList;
    }
}
