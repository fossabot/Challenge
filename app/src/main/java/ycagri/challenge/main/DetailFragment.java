package ycagri.challenge.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import ycagri.challenge.R;
import ycagri.challenge.data.VenuePhoto;
import ycagri.challenge.databinding.FragmentDetailBinding;
import ycagri.challenge.databinding.ItemDetailPagerBinding;
import ycagri.challenge.util.BindingPagerAdapter;

/**
 * Displays detail information about selected venue. Consists of a {@link ViewPager} that displays
 * photos of the venue and a {@link GoogleMap} instance to show location of the venue.
 *
 * @author ycagri
 * @since 04.05.2017
 */
public class DetailFragment extends DaggerFragment implements OnMapReadyCallback {
    // the fragment initialization parameters, e.g. ARG_VENUE
    private static final String ARG_VENUE_ID = "venue_id";

    @Inject
    DetailViewModel mViewModel;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param venueId Foursquare id of the selected venue.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(String venueId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VENUE_ID, venueId);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return FragmentDetailBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentDetailBinding binding = DataBindingUtil.getBinding(view);

        binding.vpVenuePhotos.setAdapter(new VenuePhotosPagerAdapter(new ArrayList<>()));

        String venueId = "";
        if (getArguments() != null) {
            venueId = getArguments().getString(ARG_VENUE_ID);
        }
        mViewModel.setVenueId(venueId);
        binding.setViewModel(mViewModel);

        SupportMapFragment mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_fragment_container, mapFragment)
                .commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mViewModel.setMap(googleMap);
    }

    private class VenuePhotosPagerAdapter extends BindingPagerAdapter<VenuePhoto> {

        private VenuePhotosPagerAdapter(List<VenuePhoto> items) {
            super(items);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ItemDetailPagerBinding binding = ItemDetailPagerBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
            binding.setViewModel(new VenuePhotoItemBinding(mItems.get(position)));
            container.addView(binding.getRoot());
            return binding.getRoot();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((FrameLayout) object);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
