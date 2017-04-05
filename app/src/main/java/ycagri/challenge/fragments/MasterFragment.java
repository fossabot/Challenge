package ycagri.challenge.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ycagri.challenge.ChallengeApplication;
import ycagri.challenge.R;
import ycagri.challenge.pojo.Venue;


/**
 * Demonstrates list of venues which are closest to user. Keeps all these venues in a {@link ListView}.
 *
 * @author ycagri
 * @since 04.05.2017
 */
public class MasterFragment extends ChallengeFragment {

    private static final String KEY_VENUES = "venues";
    private static final String KEY_SELECTED_INDEX = "selected_index";

    private ListView mVenuesLV;
    private MasterListAdapter mMasterListAdapter;
    private ArrayList<Venue> mVenuesArray = null;
    private int mCheckedItemPosition;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MasterFragment.
     */
    public static MasterFragment newInstance() {
        return new MasterFragment();
    }

    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCheckedItemPosition = AbsListView.INVALID_POSITION;
        if (savedInstanceState != null) {
            mCheckedItemPosition = savedInstanceState.getInt(KEY_SELECTED_INDEX, AbsListView.INVALID_POSITION);
            mVenuesArray = savedInstanceState.getParcelableArrayList(KEY_VENUES);
        } else {
            mVenuesArray = new ArrayList<>();
        }
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_master, container, false);

        mMasterListAdapter = new MasterListAdapter(getContext(), R.layout.item_master_list, R.id.tv_name, mVenuesArray);

        mVenuesLV = (ListView) parentView.findViewById(R.id.lv_master_list);
        mVenuesLV.setAdapter(mMasterListAdapter);
        mVenuesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mVenuesLV.getChoiceMode() == AbsListView.CHOICE_MODE_SINGLE)
                    mVenuesLV.setItemChecked(i, mVenuesLV.isItemChecked(i));
                mListener.addFragment(mVenuesArray.get(i));
            }
        });

        mProgressBar = (android.widget.ProgressBar) parentView.findViewById(R.id.progress_bar);

        if (!mVenuesArray.isEmpty()) {
            if (mCheckedItemPosition != AbsListView.INVALID_POSITION) {
                mVenuesLV.setItemChecked(mCheckedItemPosition, mVenuesLV.isItemChecked(mCheckedItemPosition));
            }
            mProgressBar.setVisibility(View.GONE);
        }

        return parentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mVenuesLV.getCheckedItemPosition() != AbsListView.INVALID_POSITION)
            outState.putInt(KEY_SELECTED_INDEX, mVenuesLV.getCheckedItemPosition());
        else if (mCheckedItemPosition != AbsListView.INVALID_POSITION)
            outState.putInt(KEY_SELECTED_INDEX, mCheckedItemPosition);
        outState.putParcelableArrayList(KEY_VENUES, mVenuesArray);
        super.onSaveInstanceState(outState);
    }

    public void updateLocation(Location location) {
        if (location != null) {
            retrieveVenues(location);
        }
    }

    private void retrieveVenues(Location location) {
        ChallengeApplication.getInstance().addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                generateRequestUrl("search?ll=" + location.getLatitude() + "," + location.getLongitude() + "&"),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mProgressBar.setVisibility(View.GONE);
                int resultCode = jsonObject.optJSONObject("meta").optInt("code");

                if (resultCode == 200) {
                    JSONObject response = jsonObject.optJSONObject("response");
                    if (response != null) {
                        JSONArray venues = response.optJSONArray("venues");

                        if (venues != null) {

                            for (int i = 0; i < venues.length(); i++) {
                                mVenuesArray.add(new Venue(venues.optJSONObject(i)));
                            }
                            mMasterListAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.http_error, Toast.LENGTH_LONG).show();
                }
            }
        }, mErrorListener
        ));
    }

    private class MasterListAdapter extends ArrayAdapter<Venue> {

        MasterListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Venue> objects) {
            super(context, resource, textViewResourceId, objects);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            final Venue venue = getItem(position);
            NetworkImageView iconIV = (NetworkImageView) view.findViewById(R.id.iv_icon);
            if (venue != null)
                iconIV.setImageUrl(venue.getIconUrl(), ChallengeApplication.getInstance()
                        .getImageLoader());

            if (view.isActivated())
                view.setBackgroundResource(R.color.primary_dark);
            else
                view.setBackgroundColor(Color.WHITE);

            return view;
        }
    }

}
