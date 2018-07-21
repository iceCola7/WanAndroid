package q.rorbin.verticaltablayout.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * Created by chqiu on 2017/1/16.
 */

public class TabFragmentManager {
    private FragmentManager mManager;
    private int mContainerResid;
    private List<Fragment> mFragments;
    private VerticalTabLayout mTabLayout;
    private VerticalTabLayout.OnTabSelectedListener mListener;

    public TabFragmentManager(FragmentManager manager, List<Fragment> fragments, VerticalTabLayout tabLayout) {
        this.mManager = manager;
        this.mFragments = fragments;
        this.mTabLayout = tabLayout;
        mListener = new OnFragmentTabSelectedListener();
        mTabLayout.addOnTabSelectedListener(mListener);
    }

    public TabFragmentManager(FragmentManager manager, int containerResid, List<Fragment> fragments, VerticalTabLayout tabLayout) {
        this(manager, fragments, tabLayout);
        this.mContainerResid = containerResid;
        changeFragment();
    }

    public void changeFragment() {
        FragmentTransaction ft = mManager.beginTransaction();
        int position = mTabLayout.getSelectedTabPosition();
        List<Fragment> addedFragments = mManager.getFragments();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if ((addedFragments == null || !addedFragments.contains(fragment)) && mContainerResid != 0) {
                ft.add(mContainerResid, fragment);
            }
            if ((mFragments.size() > position && i == position)
                    || (mFragments.size() <= position && i == mFragments.size() - 1)) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }
        ft.commit();
        mManager.executePendingTransactions();
    }

    public void detach() {
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment fragment : mFragments) {
            ft.remove(fragment);
        }
        ft.commit();
        mManager.executePendingTransactions();
        mManager = null;
        mFragments = null;
        mTabLayout.removeOnTabSelectedListener(mListener);
        mListener = null;
        mTabLayout = null;
    }


    private class OnFragmentTabSelectedListener implements VerticalTabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabView tab, int position) {
            changeFragment();
        }

        @Override
        public void onTabReselected(TabView tab, int position) {

        }
    }
}
