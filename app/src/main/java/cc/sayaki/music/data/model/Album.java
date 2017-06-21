package cc.sayaki.music.data.model;

import com.google.gson.Gson;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class Album {

    private String name;
    private String url;
    private String img;

    public static Album objectFromData(String str) {
        return new Gson().fromJson(str, Album.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
