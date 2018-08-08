package me.chrislewis.mentorship;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HomePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FavoritesMatchesFragment tab1 = new FavoritesMatchesFragment();
                return tab1;
            case 1:
                FavoritesPendingFragment tab2 = new FavoritesPendingFragment();
                return tab2;
            default:
                FavoritesMatchesFragment tabDefault = new FavoritesMatchesFragment();
                return tabDefault;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
}
