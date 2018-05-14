package ycagri.challenge.data.source.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.observers.TestObserver;
import ycagri.challenge.data.CategoryIcon;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenueCategory;
import ycagri.challenge.data.VenueLocation;
import ycagri.challenge.data.VenuePhoto;
import ycagri.challenge.data.VenueStat;

/**
 * Unit tests are implemented for {@link VenueLocalDataSource}.
 *
 * @author ycagri
 * @since 24/10/2017
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class VenueLocalDataSourceTest {

    private static final String VENUE_ID = "4e0ecf88a8099e15260ccc1d";
    private static final String VENUE_NAME = "Çengelkoy gözleme";

    private static final String VENUE_ADDRESS = "";
    private static final String VENUE_CROSS_STREET = "";
    private static final double VENUE_LAT = 41.00031492962394;
    private static final double VENUE_LON = 29.000228592429632;
    private static final String VENUE_COUNTRY = "Türkiye";
    private static final String[] VENUE_FORMATTED_ADDRESS = new String[]{"Türkiye"};

    private static final int CHECK_IN_COUNT = 4;
    private static final int USER_COUNT = 4;
    private static final int TIP_COUNT = 0;

    private static final String ICON_PREFIX = "https://ss3.4sqi.net/img/categories_v2/food/bakery_";
    private static final String ICON_SUFFIX = ".png";

    private static final String CATEGORY_ID = "4bf58dd8d48988d16a941735";
    private static final String CATEGORY_NAME = "Fırın";

    private static final String PHOTO_ID = "51d47c78498ec8826c2dd6b3";
    private static final long PHOTO_CREATED_AT = 1372879992;
    private static final String PHOTO_PREFIX = "https://igx.4sqi.net/img/general/";
    private static final String PHOTO_SUFFIX = "/30584601_3QQLrh8uT5_YWo-98MO0utYul5PiRSxCNMImIDtztZg.jpg";
    private static final int PHOTO_WIDTH = 717;
    private static final int PHOTO_HEIGHT = 959;
    private static final String PHOTO_VISIBILITY = "public";

    private VenueLocalDataSource mLocalDataSource;

    private Venue mVenue;

    private List<VenuePhoto> mVenuePhotos;

    @Before
    public void setup() {
        mLocalDataSource = new VenueLocalDataSource(InstrumentationRegistry.getTargetContext(),
                ImmediateThinScheduler.INSTANCE);

        VenueLocation location = new VenueLocation(VENUE_ADDRESS, VENUE_CROSS_STREET,
                VENUE_LAT, VENUE_LON, VENUE_COUNTRY, VENUE_FORMATTED_ADDRESS);
        VenueStat stat = new VenueStat(CHECK_IN_COUNT, USER_COUNT, TIP_COUNT);
        CategoryIcon icon = new CategoryIcon(ICON_PREFIX, ICON_SUFFIX);
        VenueCategory category = new VenueCategory(CATEGORY_ID, CATEGORY_NAME, icon, true);
        mVenue = new Venue(VENUE_ID, VENUE_NAME, location, stat,
                new VenueCategory[]{category});

        VenuePhoto venuePhoto = new VenuePhoto(PHOTO_ID, PHOTO_CREATED_AT, PHOTO_PREFIX, PHOTO_SUFFIX,
                PHOTO_WIDTH, PHOTO_HEIGHT, PHOTO_VISIBILITY);
        mVenuePhotos = new ArrayList<>();
        mVenuePhotos.add(venuePhoto);
    }

    @After
    public void clear() {
        mLocalDataSource.clearDatabase();
    }

    @Test
    public void insertVenue() {
        List<Venue> venueList = new ArrayList<>();
        venueList.add(mVenue);

        mLocalDataSource.insertVenues(venueList);

        TestObserver<List<Venue>> testObserver = new TestObserver<>();
        mLocalDataSource.getVenues().subscribe(testObserver);
        testObserver.assertValue(venues -> venues.size() == 1);
        testObserver.assertValue(venues -> venues.get(0).equals(mVenue));
    }

    @Test
    public void getLocation() {
        List<Venue> venueList = new ArrayList<>();
        venueList.add(mVenue);

        mLocalDataSource.insertVenues(venueList);

        TestObserver<VenueLocation> testObserver = new TestObserver<>();
        mLocalDataSource.getVenueLocation(mVenue.getId()).subscribe(testObserver);
        testObserver.assertValue(mVenue.getLocation());
    }

    @Test
    public void getPhotos() {
        List<Venue> venueList = new ArrayList<>();
        venueList.add(mVenue);

        mLocalDataSource.insertVenues(venueList);
        mLocalDataSource.insertVenuePhotos(mVenuePhotos, mVenue.getId());

        TestObserver<List<VenuePhoto>> testObserver = new TestObserver<>();
        mLocalDataSource.getVenuePhotos(mVenue.getId()).subscribe(testObserver);
        testObserver.assertValue(photos -> photos.size() == 1);
    }

    @Test
    public void getPhotoCount() {
        TestObserver<Integer> countObserver = new TestObserver<>();
        mLocalDataSource.getPhotoCount(mVenue.getId()).subscribe(countObserver);
        countObserver.assertValue(0);

        List<Venue> venueList = new ArrayList<>();
        venueList.add(mVenue);

        mLocalDataSource.insertVenues(venueList);
        mLocalDataSource.insertVenuePhotos(mVenuePhotos, mVenue.getId());

        countObserver = new TestObserver<>();
        mLocalDataSource.getPhotoCount(mVenue.getId()).subscribe(countObserver);
        countObserver.assertValue(1);
    }
}
