package com.example.busroute.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

class BaseViewModel extends ViewModel {
    private final CompositeDisposable disposables;
    private final String TAG = this.getClass().getSimpleName();

    BaseViewModel() {
        this.disposables = new CompositeDisposable();
    }

    void addDisposable(Disposable disposable) {
        this.disposables.add(disposable);
    }

    void removeDisposable(Disposable disposable) {
        if (disposable != null)
            this.disposables.remove(disposable);
    }

    void logError(String message, Throwable error) {
        Log.e(TAG, message + " throwable: " + error.getMessage());
    }
}
