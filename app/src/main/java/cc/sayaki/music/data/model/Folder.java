package cc.sayaki.music.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: sayaki
 * Date: 2017/6/7
 */
public class Folder implements Parcelable {

    private int id;
    private String name;
    private String path;
    private int numOfSongs;
    private List<Song> songs = new ArrayList<>();
    private Date createAt;

    public Folder() {
    }

    public Folder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeInt(this.numOfSongs);
        dest.writeList(this.songs);
        dest.writeLong(this.createAt != null ? this.createAt.getTime() : -1);
    }

    protected Folder(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.path = in.readString();
        this.numOfSongs = in.readInt();
        this.songs = new ArrayList<>();
        in.readList(this.songs, Song.class.getClassLoader());
        long tmpCreateAt = in.readLong();
        this.createAt = tmpCreateAt == -1 ? null : new Date(tmpCreateAt);
    }

    public static final Parcelable.Creator<Folder> CREATOR = new Parcelable.Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };
}
