package ycagri.challenge.util;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vayen01 on 30/10/2017.
 */

public class BindingUtils {

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T, VH extends RecyclerView.ViewHolder> void setItems(RecyclerView listView, List<T> items) {
        BindingRecyclerAdapter<T, VH> adapter = (BindingRecyclerAdapter) listView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <T> void setItems(ViewPager viewPager, List<T> items) {
        BindingPagerAdapter<T> adapter = (BindingPagerAdapter) viewPager.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }

    @BindingAdapter("url")
    public static void setUrl(ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }
}
