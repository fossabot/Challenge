package ycagri.challenge.data.source.remote;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.observers.TestObserver;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenuePhoto;

/**
 * Unit tests are implemented for {@link VenueRemoteDataSource}.
 *
 * @author ycagri
 * @since 13/10/2017
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class VenueRemoteDataSourceTest {

    private static final String PHOTO_ID = "51d47c3b498e0edecfc55df9";
    private static final int PHOTO_WIDTH = 717;
    private static final int PHOTO_HEIGHT = 959;

    private VenueRemoteDataSource mRemoteDataSource;

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
        TestObserver<List<Venue>> o = new TestObserver<>();
        mRemoteDataSource.getVenues(31d, 29d)
                .subscribe(o);

        o.assertValue(venues12 -> venues12.size() == 3);
        o.assertValue(venues1 -> venues1.get(0).getName().equals("Coffe Shop Alsahel"));
    }

    @Test
    public void getPhotos() {
        TestObserver<List<VenuePhoto>> o = new TestObserver<>();
        mRemoteDataSource.getVenuePhotos(PHOTO_ID)
                .subscribe(o);

        o.assertValue(venuePhotos -> venuePhotos.size() == 1);
        o.assertValue(venuePhotos -> venuePhotos.get(0).getWidth() == PHOTO_WIDTH &&
                venuePhotos.get(0).getHeight() == PHOTO_HEIGHT);
    }
}
