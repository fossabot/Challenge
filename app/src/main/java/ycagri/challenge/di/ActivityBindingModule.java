package ycagri.challenge.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ycagri.challenge.main.MainActivity;
import ycagri.challenge.main.MainActivityModule;

/**
 * Created by vayen01 on 27/10/2017.
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();

}
