package ycagri.challenge.data.source.remote;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import retrofit2.Retrofit;
import rx.Scheduler;
import ycagri.challenge.interfaces.RetrofitApiInterface;

/**
 * Created by vayen01 on 13/10/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class VenueRemoteDataSourceTest {

    private static final String CLIENT_ID = "PWRC42LMLFLMEIPL05NKAQP31TG3I4XDZGPTAYSYJSBGFIGI";
    private static final String CLIENT_SECRET = "FT2E3K22SAYMPRWY0QARIQ0OKKVFOGVLGR1ZFBFPZ2CPVTVH";

    private VenueRemoteDataSource mRemoteDataSource;

    private Scheduler mScheduler;

    @Before
    public  void setup() {
        mRemoteDataSource = new VenueRemoteDataSource(new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/v2/")
                .build()
                .create(RetrofitApiInterface.class));
    }

    @Test
    public void getVenues() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date = year + (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : "" + day);

        mRemoteDataSource.getVenues("31,29", CLIENT_ID, CLIENT_SECRET, date);
    }
}
