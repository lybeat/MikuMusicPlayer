package cc.sayaki.music.data.model;

/**
 * Author: lybeat
 * Date: 2016/8/16
 */
public class Lyrics {

    private long timestamp;
    private String lrc;

    public Lyrics() {}

    public Lyrics(long timestamp, String lrc) {
        this.timestamp = timestamp;
        this.lrc = lrc;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
}
