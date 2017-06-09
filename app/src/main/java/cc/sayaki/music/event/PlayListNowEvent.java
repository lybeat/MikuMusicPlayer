package cc.sayaki.music.event;

import cc.sayaki.music.data.model.PlayList;

/**
 * Author: sayaki
 * Date: 2017/6/8
 */
public class PlayListNowEvent {

    public PlayList playList;
    public int playIndex;

    public PlayListNowEvent(PlayList playList, int playIndex) {
        this.playList = playList;
        this.playIndex = playIndex;
    }
}
