package cc.sayaki.music.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cc.sayaki.music.player.PlayMode;

/**
 * Author: sayaki
 * Date: 2017/6/6
 */
public class PlayList implements Parcelable {

    public static final int NO_POSITION = -1;

    private int id;
    private String name;
    private int numOfSongs;
    private boolean favorite;
    private Date createdAt;
    private Date updatedAt;
    private List<Song> songs = new ArrayList<>();
    private int playingIndex = -1;
    private PlayMode playMode = PlayMode.LOOP;

    public PlayList() {
    }

    public PlayList(Song song) {
        songs.add(song);
        numOfSongs = 1;
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

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public int getPlayingIndex() {
        return playingIndex;
    }

    public void setPlayingIndex(int playingIndex) {
        this.playingIndex = playingIndex;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.numOfSongs);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeList(this.songs);
        dest.writeInt(this.playingIndex);
        dest.writeInt(this.playMode == null ? -1 : this.playMode.ordinal());
    }

    protected PlayList(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.numOfSongs = in.readInt();
        this.favorite = in.readByte() != 0;
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.songs = new ArrayList<>();
        in.readList(this.songs, Song.class.getClassLoader());
        this.playingIndex = in.readInt();
        int tmpPlayMode = in.readInt();
        this.playMode = tmpPlayMode == -1 ? null : PlayMode.values()[tmpPlayMode];
    }

    public static final Parcelable.Creator<PlayList> CREATOR = new Parcelable.Creator<PlayList>() {
        @Override
        public PlayList createFromParcel(Parcel source) {
            return new PlayList(source);
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[size];
        }
    };

    public int getItemCount() {
        return songs != null ? songs.size() : 0;
    }

    public void addSong(@Nullable Song song) {
        if (song == null) {
            return;
        }
        songs.add(song);
        numOfSongs = songs.size();
    }

    public void addSong(@Nullable Song song, int index) {
        if (song == null) {
            return;
        }
        songs.add(index, song);
        numOfSongs = songs.size();
    }

    public void addSongs(@Nullable List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return;
        }
        this.songs.addAll(songs);
        this.numOfSongs = this.songs.size();
    }

    public void addSongs(@Nullable List<Song> songs, int index) {
        if (songs == null || songs.isEmpty()) {
            return;
        }
        this.songs.addAll(index, songs);
        this.numOfSongs = this.songs.size();
    }

    public boolean removeSong(Song song) {
        if (song == null) {
            return false;
        }
        int index = songs.indexOf(song);
        if (index != -1) {
            if (songs.remove(index) != null) {
                numOfSongs = songs.size();
                return true;
            }
        } else {
            for (Iterator<Song> iterator = songs.iterator(); iterator.hasNext(); ) {
                Song item = iterator.next();
                if (song.getPath().equals(item.getPath())) {
                    iterator.remove();
                    numOfSongs = songs.size();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean prepare() {
        if (songs.isEmpty()) {
            return false;
        }
        if (playingIndex == NO_POSITION) {
            playingIndex = 0;
        }
        return true;
    }

    public Song getCurrentSong() {
        if (playingIndex != NO_POSITION) {
            return songs.get(playingIndex);
        }
        return null;
    }

    public boolean hasLast() {
        return songs != null && songs.size() != 0;
    }

    public Song last() {
        switch (playMode) {
            case LOOP:
            case LIST:
            case SINGLE:
                int newIndex = playingIndex - 1;
                if (newIndex < 0) {
                    newIndex = songs.size() - 1;
                }
                playingIndex = newIndex;
                break;
            case SHUFFLE:
                playingIndex = randomPlayIndex();
                break;
        }
        return songs.get(playingIndex);
    }

    public boolean hasNext(boolean fromComplete) {
        if (songs.isEmpty()) {
            return false;
        }
        if (fromComplete) {
            if (playMode == PlayMode.LIST && playingIndex + 1 >= songs.size()) {
                return false;
            }
        }
        return true;
    }

    public Song next() {
        switch (playMode) {
            case LOOP:
            case LIST:
            case SINGLE:
                int newIndex = playingIndex + 1;
                if (newIndex >= songs.size()) {
                    newIndex = 0;
                }
                playingIndex = newIndex;
                break;
            case SHUFFLE:
                playingIndex = randomPlayIndex();
                break;
        }
        return songs.get(playingIndex);
    }

    private int randomPlayIndex() {
        int randomIndex = new Random().nextInt(songs.size());
        if (songs.size() > 1 && randomIndex == playingIndex) {
            randomPlayIndex();
        }
        return randomIndex;
    }

    public static PlayList fromFloder(@NonNull Folder folder) {
        PlayList playList = new PlayList();
        playList.setName(folder.getName());
        playList.setSongs(folder.getSongs());
        playList.setNumOfSongs(folder.getNumOfSongs());
        return playList;
    }
}
