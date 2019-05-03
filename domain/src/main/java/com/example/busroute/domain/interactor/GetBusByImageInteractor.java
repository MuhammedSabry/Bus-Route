package com.example.busroute.domain.interactor;

import com.example.busroute.domain.BusRepository;
import com.example.busroute.domain.interactor.usecase.BackgroundExecutionThread;
import com.example.busroute.domain.interactor.usecase.PostExecutionThread;
import com.example.busroute.domain.interactor.usecase.SingleUseCase;
import com.example.busroute.domain.model.Bus;
import com.example.busroute.domain.model.ImageBytes;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetBusByImageInteractor extends SingleUseCase<Bus, ImageBytes> {

    private final BusRepository busRepository;

    @Inject
    GetBusByImageInteractor(BackgroundExecutionThread backgroundExecutionThread, PostExecutionThread postExecutionThread, BusRepository busRepository) {
        super(backgroundExecutionThread, postExecutionThread);
        this.busRepository = busRepository;
    }

    @Override
    protected Single<Bus> interact(ImageBytes imageBytes) {
        return busRepository.getBusByImage(imageBytes)
                .firstOrError();
    }
}
