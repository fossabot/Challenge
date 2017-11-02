package ycagri.challenge.main;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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

    public ObservableList<VenuePhoto> photos = new ObservableArrayList<>();

    @Inject
    public DetailViewModel(VenueRepository venueRepository) {
        this.mVenueRepository = checkNotNull(venueRepository);
    }

    public void setVenueId(String venueId) {
        mVenueId = venueId;
        getVenuePhotos();
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
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
