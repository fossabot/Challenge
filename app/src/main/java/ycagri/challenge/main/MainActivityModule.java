package ycagri.challenge.main;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;
import ycagri.challenge.di.ActivityScoped;

/**
 * Created by vayen01 on 27/10/2017.
 */
@Module
public abstract class MainActivityModule {

    @ActivityScoped
    @Provides
    GoogleApiClient provideGoogleApiClient(@NonNull Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }
}
