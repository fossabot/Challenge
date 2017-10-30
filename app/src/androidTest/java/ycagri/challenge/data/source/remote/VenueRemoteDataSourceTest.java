package ycagri.challenge.data.source.remote;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import io.reactivex.observers.TestObserver;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import rx.Scheduler;
import ycagri.challenge.data.Venue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by vayen01 on 13/10/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class VenueRemoteDataSourceTest {

    private VenueRemoteDataSource mRemoteDataSource;

    private Scheduler mScheduler;

    @Before
    public void setup() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        mRemoteDataSource = new VenueRemoteDataSource(new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build()));
    }

    @Test
    public void getVenues() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date = year + (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : "" + day);

        TestObserver<List<Venue>> o = new TestObserver<>();
        mRemoteDataSource.getVenues("31,29", date)
                .subscribe(o);
        List<List<Venue>> venues = o.values();
        assertThat(venues, hasSize(1));
        assertThat(venues.get(0), hasSize(3));
        assertThat(venues.get(0).get(0).getName(), equalTo("Coffe Shop Alsahel"));
    }
}
