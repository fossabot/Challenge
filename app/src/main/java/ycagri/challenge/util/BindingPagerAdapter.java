package ycagri.challenge.util;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 31/10/2017.
 */

public abstract class BindingPagerAdapter<T> extends PagerAdapter {

    protected List<T> mItems;

    public BindingPagerAdapter(List<T> items) {
        setList(items);
    }

    public void replaceData(List<T> items) {
        setList(items);
        notifyDataSetChanged();
    }

    private void setList(List<T> contents) {
        mItems = checkNotNull(contents);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
