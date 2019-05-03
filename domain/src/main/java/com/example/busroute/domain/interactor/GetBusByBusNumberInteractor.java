package com.example.busroute.domain.interactor;

import com.example.busroute.domain.BusRepository;
import com.example.busroute.domain.interactor.usecase.BackgroundExecutionThread;
import com.example.busroute.domain.interactor.usecase.PostExecutionThread;
import com.example.busroute.domain.interactor.usecase.SingleUseCase;
import com.example.busroute.domain.model.Bus;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetBusByBusNumberInteractor extends SingleUseCase<Bus, Integer> {

    private final BusRepository busRepository;

    @Inject
    GetBusByBusNumberInteractor(BackgroundExecutionThread backgroundExecutionThread, PostExecutionThread postExecutionThread, BusRepository busRepository) {
        super(backgroundExecutionThread, postExecutionThread);
        this.busRepository = busRepository;
    }

    @Override
    protected Single<Bus> interact(Integer busNumber) {
        return busRepository.getBusByBusNumber(busNumber)
                .firstOrError();
    }
}
