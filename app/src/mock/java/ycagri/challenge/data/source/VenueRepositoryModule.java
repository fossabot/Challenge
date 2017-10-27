package ycagri.challenge.data.source;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ycagri.challenge.data.source.local.VenueLocalDataSource;
import ycagri.challenge.data.source.remote.VenueRemoteDataSource;

/**
 * This is used by Dagger to inject the required arguments into the {@link VenueRepository}.
 */
@Module
abstract public class VenueRepositoryModule {

    @Singleton
    @Binds
    @Local
    abstract LocalDataSource provideTasksLocalDataSource(VenueLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract VenueDataSource provideTasksRemoteDataSource(VenueRemoteDataSource dataSource);
}
