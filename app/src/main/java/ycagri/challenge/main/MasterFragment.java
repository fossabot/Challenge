package ycagri.challenge.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

    private static final int RC_LOCATION_LISTENER = 100;

    @Inject
    MasterViewModel mViewModel;

    @Inject
    VenueSelectionNavigator mNavigator;

    @Inject
    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return FragmentMasterBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMasterBinding binding = DataBindingUtil.getBinding(view);

        if (binding != null) {
            binding.rvMasterList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.rvMasterList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            binding.rvMasterList.setHasFixedSize(true);
            binding.rvMasterList.setAdapter(new VenueAdapter(new ArrayList<>()));

            binding.setViewModel(mViewModel);
        }

        int rc = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (rc == PackageManager.PERMISSION_GRANTED)
            mViewModel.getUserLocation(getContext());
        else
            requestLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_LOCATION_LISTENER && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mViewModel.getUserLocation(getContext());
        }
    }

    private void requestLocationPermission() {
        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(permissions, RC_LOCATION_LISTENER);
        }
    }

    private class VenueAdapter extends BindingRecyclerAdapter<Venue, VenueViewHolder> {

        private VenueAdapter(List<Venue> items) {
            super(items);
        }

        @NonNull
        @Override
        public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VenueViewHolder(ItemMasterListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
            ItemMasterListBinding binding = DataBindingUtil.getBinding(holder.itemView);
            binding.setViewModel(new VenueItemBinding(mItems.get(position), mNavigator));
        }
    }

    private static final class VenueViewHolder extends RecyclerView.ViewHolder {

        private VenueViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
        }
    }

}
