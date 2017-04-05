package ycagri.challenge.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Calendar;

import ycagri.challenge.R;
import ycagri.challenge.interfaces.OnFragmentInteractionListener;

/**
 * Base fragment class for fragments used in the application.
 *
 * @author ycagri
 * @since 11.1.2015.
 */
public class ChallengeFragment extends Fragment {
    private static final String CLIENT_ID = "PWRC42LMLFLMEIPL05NKAQP31TG3I4XDZGPTAYSYJSBGFIGI";
    private static final String CLIENT_SECRET = "FT2E3K22SAYMPRWY0QARIQ0OKKVFOGVLGR1ZFBFPZ2CPVTVH";
    private static final String REQUEST_URL_BASE = "https://api.foursquare.com/v2/venues/";

    protected ProgressBar mProgressBar;

    protected OnFragmentInteractionListener mListener;

    protected final Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (mProgressBar != null)
                mProgressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), R.string.request_error, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnFragmentInteractionListener!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected String generateRequestUrl(String url) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date = year + (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : "" + day);

        return REQUEST_URL_BASE + url + "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=" + date;

    }
}