package ycagri.challenge.main;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import ycagri.challenge.data.Venue;
import ycagri.challenge.databinding.FragmentMasterBinding;
import ycagri.challenge.databinding.ItemMasterListBinding;
import ycagri.challenge.di.ActivityScoped;
import ycagri.challenge.util.BindingRecyclerAdapter;


/**
 * Demonstrates list of venues which are closest to user. Keeps all these venues in a {@link ListView}.
 *
 * @author ycagri
 * @since 04.05.2017
 */

@ActivityScoped
public class MasterFragment extends DaggerFragment {

    private static final String KEY_VENUES = "venues";
    private static final String KEY_SELECTED_INDEX = "selected_index";

    @Inject
    MasterViewModel mViewModel;

    @Inject
    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return FragmentMasterBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMasterBinding binding = DataBindingUtil.getBinding(view);

        binding.rvMasterList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvMasterList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.rvMasterList.setHasFixedSize(true);
        binding.rvMasterList.setAdapter(new VenueAdapter(new ArrayList<>()));

        binding.setViewModel(mViewModel);
        mViewModel.getUserLocation(getContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //if (mVenuesLV.getCheckedItemPosition() != AbsListView.INVALID_POSITION)
        //    outState.putInt(KEY_SELECTED_INDEX, mVenuesLV.getCheckedItemPosition());
        //else if (mCheckedItemPosition != AbsListView.INVALID_POSITION)
        //    outState.putInt(KEY_SELECTED_INDEX, mCheckedItemPosition);
        //outState.putParcelableArrayList(KEY_VENUES, mVenuesArray);
        super.onSaveInstanceState(outState);
    }

    private class VenueAdapter extends BindingRecyclerAdapter<Venue, VenueViewHolder> {

        private VenueAdapter(List<Venue> items) {
            super(items);
        }

        @Override
        public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VenueViewHolder(ItemMasterListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(VenueViewHolder holder, int position) {
            ItemMasterListBinding binding = DataBindingUtil.getBinding(holder.itemView);
            binding.setViewModel(new VenueItemBinding(mItems.get(position)));
        }
    }

    private static final class VenueViewHolder extends RecyclerView.ViewHolder {

        private VenueViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
        }
    }

}
