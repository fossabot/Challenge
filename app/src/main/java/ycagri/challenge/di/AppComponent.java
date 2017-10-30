package ycagri.challenge.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;
import io.reactivex.Scheduler;
import retrofit2.Retrofit;
import ycagri.challenge.ChallengeApplication;
import ycagri.challenge.data.source.VenueRepositoryModule;

/**
 * This is a Dagger component. Refer to {@link ChallengeApplication} for the list of Dagger components
 * used in this application.
 * <p>
 * Even though Dagger allows annotating a {@link Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in {@link
 * ChallengeApplication}.
 * {@link AndroidSupportInjectionModule} is the module from Dagger.Android that helps with the generation
 * and location of subcomponents.
 */
@Singleton
@Component(modules = {ApplicationModule.class,
        VenueRepositoryModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(ChallengeApplication application);


    @Override
    void inject(DaggerApplication instance);

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        @BindsInstance
        AppComponent.Builder scheduler(Scheduler scheduler);

        @BindsInstance
        AppComponent.Builder retrofit(Retrofit.Builder retrofitBuilder);

        AppComponent build();
    }
}
