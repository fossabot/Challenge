package ycagri.challenge.data.source.remote;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ycagri.challenge.data.VenuePhoto;
import ycagri.challenge.util.PhotoDeserializer;
import ycagri.challenge.util.VenueDeserializer;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.VenueDataSource;
import ycagri.challenge.interfaces.RetrofitApiInterface;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 10/10/2017.
 */
@Singleton
public class VenueRemoteDataSource implements VenueDataSource {

    private static final String CLIENT_ID = "PWRC42LMLFLMEIPL05NKAQP31TG3I4XDZGPTAYSYJSBGFIGI";
    private static final String CLIENT_SECRET = "FT2E3K22SAYMPRWY0QARIQ0OKKVFOGVLGR1ZFBFPZ2CPVTVH";

    @NonNull
    private final RetrofitApiInterface mRetrofit;

    @Inject
    public VenueRemoteDataSource(@NonNull Retrofit.Builder retrofitBuilder) {
        this.mRetrofit = checkNotNull(retrofitBuilder)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(new TypeToken<List<Venue>>() {
                        }.getType(), new VenueDeserializer())
                        .registerTypeAdapter(new TypeToken<List<VenuePhoto>>() {
                        }.getType(), new PhotoDeserializer())
                        .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(RetrofitApiInterface.class);
    }

    @NonNull
    @Override
    public Observable<List<Venue>> getVenues(double latitude, double longitude) {
        return mRetrofit.getVenues(latitude + "," + longitude, CLIENT_ID, CLIENT_SECRET, getDate());
    }

    @Override
    public Observable<List<VenuePhoto>> getVenuePhotos(String venueId) {
        return mRetrofit.getVenuePhotos(venueId, CLIENT_ID, CLIENT_SECRET, getDate());
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year + (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : "" + day);
    }
}
