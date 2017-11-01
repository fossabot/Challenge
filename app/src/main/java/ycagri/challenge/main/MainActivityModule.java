package ycagri.challenge.main;

import android.databinding.BaseObservable;
import android.os.Bundle;

import com.google.android.gms.location.LocationRequest;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import ycagri.challenge.di.ActivityScoped;
import ycagri.challenge.di.FragmentScoped;

/**
 * Created by vayen01 on 27/10/2017.
 */
@Module
public abstract class MainActivityModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MasterFragment masterFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DetailFragment detailFragment();

    @Binds
    abstract String provideVenueId(String venueId);

    @Binds
    abstract BaseObservable detailViewModel(DetailViewModel detailViewModel);

    @ActivityScoped
    @Binds
    abstract BaseObservable mainViewModule(MasterViewModel masterViewModel);

    @ActivityScoped
    @Provides
    static LocationRequest provideLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    @ActivityScoped
    @Binds
    abstract VenueSelectionNavigator provideVenueSelectionNavigator(MainActivity activity);
}
