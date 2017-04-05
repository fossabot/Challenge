package ycagri.challenge.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ycagri.challenge.ChallengeApplication;
import ycagri.challenge.R;
import ycagri.challenge.pojo.Venue;

/**
 * Displays detail information about selected venue. Consists of a {@link ViewPager} that displays
 * photos of the venue and a {@link GoogleMap} instance to show location of the venue.
 *
 * @author ycagri
 * @since 04.05.2017
 */
public class DetailFragment extends ChallengeFragment implements OnMapReadyCallback {
    // the fragment initialization parameters, e.g. ARG_VENUE
    private static final String ARG_VENUE = "venue";

    private Venue mVenue;

    private TextView mNoPhotosToShow;
    private VenuePhotosPagerAdapter mPhotosAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param venue Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Venue venue) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_VENUE, venue);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVenue = getArguments().getParcelable(ARG_VENUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager photosViewPager = (ViewPager) view.findViewById(R.id.vp_venue_photos);
        mProgressBar = (android.widget.ProgressBar) view.findViewById(R.id.progress_bar);
        mNoPhotosToShow = (TextView) view.findViewById(R.id.no_photos_to_show);

        if (mVenue.getPhotosList() == null) {
            mVenue.setPhotosList(new ArrayList<String>());
            mPhotosAdapter = new VenuePhotosPagerAdapter(getContext(), mVenue.getPhotosList());

            ChallengeApplication.getInstance().addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                    generateRequestUrl(mVenue.getId() + "/photos?"),
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    mProgressBar.setVisibility(View.GONE);
                    int resultCode = jsonObject.optJSONObject("meta").optInt("code");

                    if (resultCode == 200) {
                        JSONObject response = jsonObject.optJSONObject("response");
                        if (response != null) {
                            JSONArray items = response.optJSONObject("photos").optJSONArray("items");

                            if (items != null) {

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject photo = items.optJSONObject(i);

                                    if (photo != null) {
                                        String photoUrl = photo.optString("prefix") + photo.optInt("width")
                                                + "x" + photo.optInt("height") + photo.optString("suffix");
                                        mVenue.getPhotosList().add(photoUrl);
                                    }
                                }
                                if (mVenue.getPhotosList().isEmpty()) {
                                    mNoPhotosToShow.setVisibility(View.VISIBLE);
                                } else {
                                    mPhotosAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.http_error, Toast.LENGTH_LONG).show();
                        mNoPhotosToShow.setVisibility(View.VISIBLE);
                    }
                }
            }, mErrorListener
            ));
        } else {
            mProgressBar.setVisibility(View.GONE);
            mPhotosAdapter = new VenuePhotosPagerAdapter(getContext(), mVenue.getPhotosList());
        }

        photosViewPager.setAdapter(mPhotosAdapter);

        mListener.setToolbarTitle(mVenue.getName());

        SupportMapFragment mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_fragment_container, mapFragment)
                .commit();
    }

    @Override
    public void onDetach() {
        mListener.setToolbarTitle(getString(R.string.app_name));
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(mVenue.getLatitude(), mVenue.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(mVenue.getName()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private class VenuePhotosPagerAdapter extends PagerAdapter {

        private final LayoutInflater mInflater;
        private final ArrayList<String> mPhotos;

        VenuePhotosPagerAdapter(Context context, ArrayList<String> photos) {
            mInflater = LayoutInflater.from(context);
            mPhotos = photos;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mInflater.inflate(R.layout.item_detail_pager, container, false);
            NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.iv_pager_item);
            imageView.setImageUrl(mPhotos.get(position),
                    ChallengeApplication.getInstance().getImageLoader());

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((FrameLayout) object);
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
