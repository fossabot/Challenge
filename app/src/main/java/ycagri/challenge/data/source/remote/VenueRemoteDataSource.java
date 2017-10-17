package ycagri.challenge.data.source.remote;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ycagri.challenge.VenueDeserializer;
import ycagri.challenge.data.Venue;
import ycagri.challenge.data.source.VenueDataSource;
import ycagri.challenge.interfaces.RetrofitApiInterface;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by vayen01 on 10/10/2017.
 */
@Singleton
public class VenueRemoteDataSource implements VenueDataSource {

    @NonNull
    private final RetrofitApiInterface mRetrofit;

    @Inject
    public VenueRemoteDataSource(@NonNull Retrofit.Builder retrofitBuilder) {
        this.mRetrofit = checkNotNull(retrofitBuilder)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(new TypeToken<List<Venue>>() {
                }.getType(), new VenueDeserializer()).create()))
                .build().create(RetrofitApiInterface.class);
    }

    @NonNull
    @Override
    public Observable<List<Venue>> getVenues(String location, String clientId, String clientSecret, String date) {
        return mRetrofit.getVenues(location, clientId, clientSecret, date);
    }
}
