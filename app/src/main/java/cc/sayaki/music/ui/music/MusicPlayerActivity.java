package cc.sayaki.music.ui.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.sayaki.music.R;
import cc.sayaki.music.RxBus;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.data.sp.MusicSp;
import cc.sayaki.music.event.PlayListNowEvent;
import cc.sayaki.music.event.PlaySongEvent;
import cc.sayaki.music.player.IPlayback;
import cc.sayaki.music.player.PlayMode;
import cc.sayaki.music.player.PlaybackService;
import cc.sayaki.music.ui.base.BaseActivity;
import cc.sayaki.music.ui.widget.SakuraLyrics;
import cc.sayaki.music.ui.widget.ShadowImageView;
import cc.sayaki.music.utils.AlbumUtil;
import cc.sayaki.music.utils.ScreenUtil;
import cc.sayaki.music.utils.TimeUtil;
import cc.sayaki.music.utils.UnitUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Author: sayaki
 * Date: 2017/6/7
 */
public class MusicPlayerActivity extends BaseActivity implements
        MusicPlayerContract.View, IPlayback.Callback {

    private static final String EXTRA_PLAY_LIST = "play_list";
    private static final String EXTRA_PLAY_INDEX = "play_index";

    private static final long UPDATE_PROGRESS_INTERVAL = 1000;

    @BindView(R.id.player_layout)
    ViewGroup playerLayout;
    @BindView(R.id.display_layout)
    ViewGroup displayLayout;
    @BindView(R.id.cover_img)
    ShadowImageView albumImg;
    @BindView(R.id.lrc_view)
    SakuraLyrics sakuraLyrics;
    @BindView(R.id.progress_txt)
    TextView progressTxt;
    @BindView(R.id.duration_txt)
    TextView durationTxt;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.play_mode_toggle_img)
    ImageView playModeImg;
    @BindView(R.id.play_last_img)
    ImageView playLastImg;
    @BindView(R.id.play_toggle_img)
    ImageView playToggleImg;
    @BindView(R.id.play_next_img)
    ImageView playNextImg;
    @BindView(R.id.favorite_img)
    ImageView favoriteImg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private IPlayback player;
    private PlayList playList;
    private int playIndex;

    private MusicPlayerContract.Presenter presenter;

    private Handler handler = new Handler();
    private Runnable progressCallback = new Runnable() {
        @Override
        public void run() {
            if (player.isPlaying()) {
                int progress = (int) (seekBar.getMax() * ((float) player.getProgress() / (float) getCurrentSongDuration()));
                updateProgressTextWithDuration(player.getProgress());
                if (progress >= 0 && progress <= seekBar.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar.setProgress(progress, true);
                    } else {
                        seekBar.setProgress(progress);
                    }
                    handler.postDelayed(progressCallback, UPDATE_PROGRESS_INTERVAL);
                }
            }
        }
    };

    public static void launch(Context context, PlayList playList, int playIndex) {
        Intent intent = new Intent();
        intent.setClass(context, MusicPlayerActivity.class);
        intent.putExtra(EXTRA_PLAY_LIST, playList);
        intent.putExtra(EXTRA_PLAY_INDEX, playIndex);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_music);
    }

    @Override
    protected void initData() {
        playList = getIntent().getParcelableExtra(EXTRA_PLAY_LIST);
        playIndex = getIntent().getIntExtra(EXTRA_PLAY_INDEX, 0);
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenUtil.getStatusBarHeight(this) + UnitUtil.dp2px(this, 48)));
            toolbar.setPadding(0, ScreenUtil.getStatusBarHeight(this), 0, 0);
            displayLayout.setPadding(0, ScreenUtil.getStatusBarHeight(this), 0, 0);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressTextWithProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(progressCallback);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getDuration(seekBar.getProgress()));
                if (player.isPlaying()) {
                    handler.removeCallbacks(progressCallback);
                    handler.post(progressCallback);
                }
            }
        });

        new MusicPlayerPresenter(this, MusicRepository.getInstance(), this).subscribe();
        startService(new Intent(this, PlaybackService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (player != null && player.isPlaying()) {
            handler.removeCallbacks(progressCallback);
            handler.post(progressCallback);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(progressCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @OnClick(R.id.play_toggle_img)
    public void onPlayToggleClick() {
        if (player == null) {
            return;
        }
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.play();
        }
    }

    @OnClick(R.id.play_last_img)
    public void onPlayLastClick() {
        if (player == null) {
            return;
        }
        player.playLast();
    }

    @OnClick(R.id.play_next_img)
    public void onPlayNextClick() {
        if (player == null) {
            return;
        }
        player.playNext();
    }

    @OnClick(R.id.play_mode_toggle_img)
    public void onPlayModeClick() {
        if (player == null) {
            return;
        }
        PlayMode currentMode = MusicSp.getPlayMode(this);
        PlayMode newMode = PlayMode.switchNextMode(currentMode);
        MusicSp.setPlayMode(this, newMode);
        player.setPlayMode(newMode);
        updatePlayMode(newMode);
    }

    @OnClick(R.id.favorite_img)
    public void onFavoriteClick() {
        if (player == null) {
            return;
        }
        Song song = player.getPlayingSong();
        if (song != null) {
            favoriteImg.setEnabled(false);
            presenter.setSongAsFavorite(song, !song.isFavorite());
        }
    }

    @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof PlaySongEvent) {
                            onPlaySongEvent((PlaySongEvent) o);
                        } else if (o instanceof PlayListNowEvent) {
                            onPlayListNowEvent((PlayListNowEvent) o);
                        }
                    }
                })
                .subscribe(RxBus.defaultSubscriber());
    }

    private void onPlaySongEvent(PlaySongEvent event) {
        Song song = event.song;
        playSong(song);
    }

    private void onPlayListNowEvent(PlayListNowEvent event) {
        PlayList playList = event.playList;
        int playIndex = event.playIndex;
        playSong(playList, playIndex);
    }

    private void playSong(Song song) {
        PlayList playList = new PlayList(song);
        playSong(playList, 0);
    }

    private void playSong(PlayList playList, int playIndex) {
        if (playList == null) {
            return;
        }
        playList.setPlayMode(MusicSp.getPlayMode(this));
        player.play(playList, playIndex);
        onSongUpdated(playList.getCurrentSong());
    }

    private void seekTo(int progress) {
        player.seekTo(progress);
    }

    private void updateProgressTextWithProgress(int progress) {
        int duration = getDuration(progress);
        progressTxt.setText(TimeUtil.formatDuration(duration));
    }

    private void updateProgressTextWithDuration(int duration) {
        progressTxt.setText(TimeUtil.formatDuration(duration));
    }

    private int getDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / seekBar.getMax()));
    }

    private int getCurrentSongDuration() {
        Song song = player.getPlayingSong();
        int duration = 0;
        if (song != null) {
            duration = song.getDuration();
        }
        return duration;
    }

    @Override
    public void setPresenter(MusicPlayerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void handleError(Throwable error) {

    }

    @Override
    public void onPlaybackServiceBound(PlaybackService service) {
        player = service;
        player.registerCallback(this);

        onPlayListNowEvent(new PlayListNowEvent(playList, playIndex));
    }

    @Override
    public void onPlaybackServiceUnbound() {
        player.unregisterCallback(this);
        player = null;
    }

    @Override
    public void onSongSetAsFavorite(@NonNull Song song) {
        favoriteImg.setEnabled(true);
        updateFavoriteToggle(song.isFavorite());
    }

    @Override
    public void onSongUpdated(@Nullable Song song) {
        if (song == null) {
            albumImg.cancelRotateAnimation();
            playToggleImg.setImageResource(R.drawable.ic_play);
            seekBar.setProgress(0);
            updateProgressTextWithProgress(0);
            seekTo(0);
            handler.removeCallbacks(progressCallback);
            return;
        }
        toolbar.setTitle(song.getDisplayName());
        toolbar.setSubtitle(song.getArtist());
        durationTxt.setText(TimeUtil.formatDuration(song.getDuration()));
        Bitmap bitmap = AlbumUtil.parseAlbum(song);
        if (bitmap == null) {
            albumImg.setImageResource(R.drawable.bg_placeholder_round);
        } else {
            albumImg.setImageBitmap(AlbumUtil.getCroppedBitmap(bitmap));
        }
        albumImg.pauseRotateAnimation();
        handler.removeCallbacks(progressCallback);
        if (player.isPlaying()) {
            albumImg.startRotateAnimation();
            handler.post(progressCallback);
            playToggleImg.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void updatePlayMode(PlayMode playMode) {
        if (playMode == null) {
            playMode = PlayMode.getDefault();
        }
        switch (playMode) {
            case LIST:
                playModeImg.setImageResource(R.drawable.ic_play_mode_list);
                break;
            case LOOP:
                playModeImg.setImageResource(R.drawable.ic_play_mode_loop);
                break;
            case SHUFFLE:
                playModeImg.setImageResource(R.drawable.ic_play_mode_shuffle);
                break;
            case SINGLE:
                playModeImg.setImageResource(R.drawable.ic_play_mode_single);
                break;
        }
    }

    @Override
    public void updatePlayToggle(boolean play) {
        playToggleImg.setImageResource(play ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override
    public void updateFavoriteToggle(boolean favorite) {
        favoriteImg.setImageResource(favorite ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    }

    @Override
    public void onSwitchLast(@Nullable Song last) {
        onSongUpdated(last);
    }

    @Override
    public void onSwitchNext(@Nullable Song next) {
        onSongUpdated(next);
    }

    @Override
    public void onComplete(@Nullable Song next) {
        onSongUpdated(next);
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        updatePlayToggle(isPlaying);
        if (isPlaying) {
            albumImg.resumeRotateAnimation();
            handler.removeCallbacks(progressCallback);
            handler.post(progressCallback);
        } else {
            albumImg.pauseRotateAnimation();
            handler.removeCallbacks(progressCallback);
        }
    }
}
