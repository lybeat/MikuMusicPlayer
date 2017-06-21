package cc.sayaki.music.ui.local.folder;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import java.util.List;

import butterknife.BindView;
import cc.sayaki.music.R;
import cc.sayaki.music.RxBus;
import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.source.MusicRepository;
import cc.sayaki.music.event.PlayListCreatedEvent;
import cc.sayaki.music.ui.base.BaseFragment;
import cc.sayaki.music.ui.playlist.SongListActivity;

/**
 * Author: sayaki
 * Date: 2017/6/16
 */
public class FolderFragment extends BaseFragment implements FolderContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private FolderAdapter adapter;
    private int updateIndex, deleteIndex;

    FolderContract.Presenter presenter;

    public static FolderFragment newInstance() {
        return new FolderFragment();
    }

    protected View getView(LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return inflater.inflate(R.layout.fragment_added_folders, container ,false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        adapter = new FolderAdapter(getActivity(), null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Folder folder = adapter.getItem(i);
                SongListActivity.launchForFolder(getActivity(), folder);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });

        new FolderPresenter(MusicRepository.getInstance(), this).subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(FolderContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void handleError(Throwable error) {

    }

    @Override
    public void onFoldersLoaded(List<Folder> folders) {
        adapter.setNewData(folders);
    }

    @Override
    public void onFoldersAdded(List<Folder> folders) {
        int newItemCount = folders.size() - (adapter.getData() != null ? adapter.getData().size() : 0);
        adapter.setNewData(folders);
        if (newItemCount > 0) {
            String toast = getResources().getQuantityString(
                    R.plurals.mp_folders_created_formatter,
                    newItemCount,
                    newItemCount
            );
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFolderUpdated(Folder folder) {
        adapter.getData().set(updateIndex, folder);
        adapter.notifyItemChanged(updateIndex);
    }

    @Override
    public void onFolderDeleted(Folder folder) {
        adapter.getData().remove(deleteIndex);
        adapter.notifyItemRemoved(deleteIndex);
    }

    @Override
    public void onPlayListCreated(PlayList playList) {
        RxBus.getInstance().post(new PlayListCreatedEvent(playList));
        Toast.makeText(getActivity(), getString(R.string.mp_play_list_created, playList.getName()), Toast.LENGTH_SHORT).show();
    }
}
