package cc.sayaki.music.data.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

import cc.sayaki.music.player.PlayMode;

/**
 * Author: sayaki
 * Date: 2017/6/8
 */
public class MusicSp {

    private static final String SP_MUSIC = "sp_music";
    private static final String KEY_PLAY_MODE = "key_play_mode";

    public static void setPlayMode(Context context, PlayMode playMode) {
        SharedPreferences sp = context.getSharedPreferences(SP_MUSIC, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_PLAY_MODE, playMode.name());
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static PlayMode getPlayMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_MUSIC, Context.MODE_PRIVATE);
        String playModeName = sp.getString(KEY_PLAY_MODE, null);
        if (playModeName != null) {
            return PlayMode.valueOf(playModeName);
        }
        return PlayMode.getDefault();
    }
}
