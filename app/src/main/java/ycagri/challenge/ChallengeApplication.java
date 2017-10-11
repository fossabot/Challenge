package ycagri.challenge;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import retrofit2.Retrofit;
import rx.schedulers.Schedulers;
import ycagri.challenge.data.source.VenueRepository;
import ycagri.challenge.di.AppComponent;
import ycagri.challenge.di.DaggerAppComponent;

/**
 *
 * @author ycagri
 * @since 10.1.2015.
 */
public class ChallengeApplication extends DaggerApplication {

    private static final String TAG = ChallengeApplication.class.getSimpleName();

    @Inject
    VenueRepository mVenueRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .application(this)
                .scheduler(Schedulers.io())
                .retrofit(new Retrofit.Builder().baseUrl("https://api.foursquare.com/v2/venues/").build())
                .build();
        appComponent.inject(this);
        return appComponent;
    }
}
