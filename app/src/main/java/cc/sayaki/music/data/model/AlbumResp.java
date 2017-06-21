package cc.sayaki.music.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class AlbumResp {

    private boolean status;
    @SerializedName("list")
    private List<Album> alba;

    public static AlbumResp objectFromData(String str) {
        return new Gson().fromJson(str, AlbumResp.class);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Album> getAlba() {
        return alba;
    }

    public void setAlba(List<Album> alba) {
        this.alba = alba;
    }
}
