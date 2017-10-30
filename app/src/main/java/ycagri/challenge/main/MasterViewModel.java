package ycagri.challenge.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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
        int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (rc == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(context).getLastLocation()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            String location = task1.getResult().getLatitude() + "," + task1.getResult().getLongitude();
                            Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH) + 1;
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            String date = year + (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : "" + day);

                            mVenueRepository.getVenues(location, date)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Observer<List<Venue>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(List<Venue> v) {
                                            venues.clear();
                                            venues.addAll(v);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    });
        }
    }
}
