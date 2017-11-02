package ycagri.challenge.data.source.remote;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.observers.TestObserver;
import ycagri.challenge.data.CategoryIcon;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.VenueCategory;
import ycagri.challenge.data.VenueLocation;
import ycagri.challenge.data.VenueStat;
import ycagri.challenge.data.source.local.VenueLocalDataSource;

/**
 * Created by vayen01 on 24/10/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class VenueLocalDataSourceTest {

    private Scheduler mScheduler;

    private VenueLocalDataSource mLocalDataSource;

    @Before
    public void setup() {
        mLocalDataSource = new VenueLocalDataSource(InstrumentationRegistry.getTargetContext(),
                ImmediateThinScheduler.INSTANCE);
    }

    @After
    public void clear() {
        mLocalDataSource.clearDatabase();
    }

    @Test
    public void insertVenue() {
        List<Venue> venueList = new ArrayList<>();
        Venue venue = new Venue("venue_id_1", "Venue 1",
                new VenueLocation(
                        "Address",
                        "Cross Street",
                        41d, 29d,
                        "Country", new String[]{"Part 1", "Part 2"}),
                new VenueStat(100, 200, 300),
                new VenueCategory[]{
                        new VenueCategory("venue_category_1", "Category 1", new CategoryIcon("prefix", "suffix"), true)
                });
        venueList.add(venue);

        mLocalDataSource.insertVenues(venueList).subscribe();

        TestObserver<Venue> venueTestObserver = new TestObserver<>();
        mLocalDataSource.getVenueById(venue.getId()).subscribe(venueTestObserver);
        venueTestObserver.assertValue(venue);
    }
}
