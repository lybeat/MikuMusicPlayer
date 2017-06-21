package cc.sayaki.music.ui.local;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.sayaki.music.R;
import cc.sayaki.music.ui.base.BaseActivity;
import cc.sayaki.music.ui.local.folder.FolderFragment;

/**
 * Author: sayaki
 * Date: 2017/6/16
 */
public class LocalActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private LocalTabAdapter adapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_local);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FolderFragment.newInstance());
        List<String> titles = new ArrayList<>();
        titles.add("目录");
        adapter = new LocalTabAdapter(fragments, titles, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
