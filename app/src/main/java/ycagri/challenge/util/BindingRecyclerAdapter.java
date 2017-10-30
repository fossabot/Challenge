package ycagri.challenge.util;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Generic adapter for {@link RecyclerView} extends from {@link RecyclerView.Adapter}. Simply provides
 * item binding.
 *
 * @author ycagri
 * @since 30.10.2017
 */

public abstract class BindingRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mItems;

    public BindingRecyclerAdapter(List<T> items) {
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
    public int getItemCount() {
        return mItems.size();
    }
}
