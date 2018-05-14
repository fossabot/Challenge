package ycagri.challenge.main;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.VenueRepository;
import ycagri.challenge.di.ActivityScoped;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 30/10/2017.
 */

@ActivityScoped
public class MasterViewModel extends BaseObservable {

    private final VenueRepository mVenueRepository;

    private final LocationRequest mLocationRequest;

    public ObservableList<Venue> venues = new ObservableArrayList<>();

    @Inject
    MasterViewModel(VenueRepository venueRepository, LocationRequest locationRequest) {
        mVenueRepository = checkNotNull(venueRepository);
        mLocationRequest = checkNotNull(locationRequest);
    }

    void getUserLocation(Context context) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {

            try {
                task.getResult(ApiException.class);
                getLocation(context);
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                    (Activity) context,
                                    9);
                        } catch (IntentSender.SendIntentException ex) {
                            // Ignore the error.
                        } catch (ClassCastException ex) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void getLocation(Context context) {
        LocationServices.getFusedLocationProviderClient(context).getLastLocation()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        mVenueRepository.getVenues(task1.getResult().getLatitude(), task1.getResult().getLongitude())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(venues -> {
                                    this.venues.clear();
                                    this.venues.addAll(venues);
                                });
                    }
                });
    }
}
