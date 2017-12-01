package ycagri.challenge.data.source;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.observers.TestObserver;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.local.LocalDataSource;
import ycagri.challenge.data.source.remote.RemoteDataSource;
import ycagri.challenge.util.SharedPreferenceManager;

/**
 * Created by vayen01 on 10/11/2017.
 */

public class VenueRepositoryTest {

    private static final double LATITUDE = 41d;
    private static final double LONGITUDE = 29d;

    private VenueRepository mVenueRepository;

    @Mock
    private RemoteDataSource mRemoteDataSource;

    @Mock
    private LocalDataSource mLocalDataSource;

    @Mock
    private SharedPreferenceManager mSharedPrefManager;

    private TestObserver<List<Venue>> mTestVenueSubscriber = new TestObserver<>();

    @Before
    public void setupTasksRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mVenueRepository = new VenueRepository(mRemoteDataSource, mLocalDataSource, mSharedPrefManager);
    }

    @Test
    public void getVenues() {
        mVenueRepository.getVenues(LATITUDE, LONGITUDE).subscribe(mTestVenueSubscriber);
    }
}
