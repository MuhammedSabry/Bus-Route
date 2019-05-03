package com.example.busroute.dagger;

import androidx.lifecycle.ViewModel;

import com.example.busroute.viewmodel.MainViewModel;
import com.example.busroute.viewmodel.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel provideLoginViewModel(MainViewModel loginViewModel);
}