package cc.sayaki.music.ui.local.folder;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.sayaki.music.R;
import cc.sayaki.music.data.model.Folder;

/**
 * Author: sayaki
 * Date: 2017/6/16
 */
public class FolderAdapter extends BaseQuickAdapter<Folder, BaseViewHolder> {

    private Context context;

    public FolderAdapter(Context context, List<Folder> data) {
        super(R.layout.item_added_folders, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Folder folder) {
        baseViewHolder.setText(R.id.name_txt, folder.getName())
                .setText(R.id.info_txt, context.getResources().getString(
                        R.string.mp_local_files_folder_list_item_info_formatter,
                        folder.getNumOfSongs(), folder.getPath()));
    }
}
