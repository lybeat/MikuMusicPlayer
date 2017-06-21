package cc.sayaki.music.ui.local.folder;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cc.sayaki.music.RxBus;
import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.event.PlayListUpdatedEvent;
import cc.sayaki.music.utils.FileUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: sayaki
 * Date: 2017/6/16
 */
public class FolderPresenter implements FolderContract.Presenter {

    private FolderContract.View view;
    private MusicRepository repository;
    private CompositeSubscription compositeSubscription;

    public FolderPresenter(MusicRepository repository, FolderContract.View view) {
        this.repository = repository;
        this.view = view;
        this.compositeSubscription = new CompositeSubscription();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadFolders();
    }

    @Override
    public void unsubscribe() {
        view = null;
        compositeSubscription.clear();
    }

    @Override
    public void loadFolders() {
        Subscription subscription = repository.folders()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<Folder>>() {
                    @Override
                    public void call(List<Folder> folders) {
                        Collections.sort(folders, new Comparator<Folder>() {
                            @Override
                            public int compare(Folder o1, Folder o2) {
                                return o1.getName().compareToIgnoreCase(o2.getName());
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Folder>>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                    }

                    @Override
                    public void onNext(List<Folder> folders) {
                        view.onFoldersLoaded(folders);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void addFolders(List<File> folders, final List<Folder> existedFolders) {
        Subscription subscription = rx.Observable.from(folders)
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        for (Folder folder : existedFolders) {
                            if (file.getAbsolutePath().equals(folder.getPath())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .flatMap(new Func1<File, rx.Observable<Folder>>() {
                    @Override
                    public rx.Observable<Folder> call(File file) {
                        Folder folder = new Folder();
                        folder.setName(file.getName());
                        folder.setPath(file.getAbsolutePath());
                        List<Song> songs = FileUtils.musicFiles(file);
                        folder.setSongs(songs);
                        folder.setNumOfSongs(songs.size());
                        return rx.Observable.just(folder);
                    }
                })
                .toList()
                .flatMap(new Func1<List<Folder>, rx.Observable<List<Folder>>>() {
                    @Override
                    public rx.Observable<List<Folder>> call(List<Folder> folders) {
                        return repository.create(folders);
                    }
                })
                .doOnNext(new Action1<List<Folder>>() {
                    @Override
                    public void call(List<Folder> folders) {
                        Collections.sort(folders, new Comparator<Folder>() {
                            @Override
                            public int compare(Folder o1, Folder o2) {
                                return o1.getName().compareToIgnoreCase(o2.getName());
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Folder>>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(List<Folder> folders) {
                        view.onFoldersAdded(folders);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void refreshFolders(final Folder folder) {
        Subscription subscription = rx.Observable.just(FileUtils.musicFiles(new File(folder.getPath())))
                .flatMap(new Func1<List<Song>, rx.Observable<Folder>>() {
                    @Override
                    public rx.Observable<Folder> call(List<Song> songs) {
                        folder.setSongs(songs);
                        folder.setNumOfSongs(songs.size());
                        return repository.update(folder);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Folder>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(Folder folder) {
                        view.onFolderUpdated(folder);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void deleteFolders(Folder folder) {
        Subscription subscription = repository.delete(folder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Folder>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(Folder folder) {
                        view.onFolderDeleted(folder);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void createPlayList(PlayList playList) {
        Subscription subscription = repository.create(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlayList>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(PlayList playList) {
                        view.onPlayListCreated(playList);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void addFolderToPlayList(Folder folder, PlayList playList) {
        if (folder.getSongs().isEmpty()) {
            return;
        }

        if (playList.isFavorite()) {
            for (Song song : folder.getSongs()) {
                song.setFavorite(true);
            }
        }
        playList.addSongs(folder.getSongs(), 0);

        Subscription subscription = repository.update(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlayList>() {
                    @Override
                    public void onStart() {
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.handleError(e);
                    }

                    @Override
                    public void onNext(PlayList playList) {
                        RxBus.getInstance().post(new PlayListUpdatedEvent(playList));
                    }
                });
        compositeSubscription.add(subscription);
    }
}
