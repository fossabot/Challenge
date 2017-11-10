package ycagri.challenge.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ycagri.challenge.BR;
import ycagri.challenge.data.VenueLocation;
import ycagri.challenge.data.VenuePhoto;
import ycagri.challenge.data.source.VenueRepository;
import ycagri.challenge.di.ActivityScoped;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 31/10/2017.
 */

@ActivityScoped
public class DetailViewModel extends BaseObservable {

    private final VenueRepository mVenueRepository;

    private String mVenueId;

    private GoogleMap mMap;

    private LatLng mLatestLatLng;

    public ObservableList<VenuePhoto> photos = new ObservableArrayList<>();

    @Inject
    public DetailViewModel(VenueRepository venueRepository) {
        this.mVenueRepository = checkNotNull(venueRepository);
    }

    public void setVenueId(String venueId) {
        photos.clear();
        mVenueId = venueId;
        getVenuePhotos();
        getVenueLocation();
    }

    public void setMap(GoogleMap map) {
        mMap = map;
        if(mLatestLatLng!=null){
            updateMap();
        }
    }

    @Bindable
    public int getNoPhotoVisibility() {
        return photos.isEmpty() ? View.VISIBLE : View.GONE;
    }

    private void getVenuePhotos() {
        mVenueRepository.getVenuePhotos(mVenueId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<VenuePhoto>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<VenuePhoto> venuePhotos) {
                        photos.clear();
                        photos.addAll(venuePhotos);
                        notifyPropertyChanged(BR.noPhotoVisibility);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getVenueLocation() {
        mVenueRepository.getVenueLocation(mVenueId).subscribeOn(Schedulers.io())
                .subscribe(new Observer<VenueLocation>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VenueLocation location) {
                        if (mMap != null) {
                            mLatestLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            updateMap();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateMap(){
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(mLatestLatLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatestLatLng, 14));
    }
}
