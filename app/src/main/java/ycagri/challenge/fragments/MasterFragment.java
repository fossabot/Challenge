package ycagri.challenge.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ycagri.challenge.ChallengeApplication;
import ycagri.challenge.R;
import ycagri.challenge.pojo.Venue;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterFragment extends ChallengeFragment {

    private MasterListAdapter mMasterListAdapter;
    private ArrayList<Venue> mVenuesArray;

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
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_master, container, false);

        mVenuesArray = new ArrayList<>();
        mMasterListAdapter = new MasterListAdapter(mVenuesArray);

        RecyclerView masterList = (RecyclerView) parentView.findViewById(R.id.rv_master_list);
        masterList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        masterList.setAdapter(mMasterListAdapter);

        mProgressBar = (android.widget.ProgressBar) parentView.findViewById(R.id.progress_bar);

        Location location = mListener.getLastKnownLocation();
        double lat, lng;
        if (location == null) {
            lat = 41d;
            lng = 29d;
        } else {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
        ChallengeApplication.getInstance().addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                generateRequestUrl("search?ll=" + lat + "," + lng + "&"),
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

        return parentView;
    }

    class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.MasterListItemHolder> {

        private ArrayList<Venue> mVenueArray;

        MasterListAdapter(ArrayList<Venue> modelArray) {
            this.mVenueArray = modelArray;
        }

        @Override
        public MasterListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_master_list, parent,
                    false);

            return new MasterListItemHolder(v);
        }

        @Override
        public void onBindViewHolder(MasterListItemHolder holder, int position) {
            final Venue venue = mVenueArray.get(position);
            holder.mIconImage.setImageUrl(venue.getIconUrl(), ChallengeApplication.getInstance()
                    .getImageLoader());
            holder.mNameText.setText(venue.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.addFragment(DetailFragment.newInstance(venue));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mVenueArray.size();
        }

        class MasterListItemHolder extends RecyclerView.ViewHolder {

            private NetworkImageView mIconImage;
            private TextView mNameText;

            MasterListItemHolder(View itemView) {
                super(itemView);

                mIconImage = (NetworkImageView) itemView.findViewById(R.id.iv_icon);
                mNameText = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
    }
}
