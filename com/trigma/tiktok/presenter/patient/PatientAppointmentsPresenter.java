package com.trigma.tiktok.presenter.patient;

import com.trigma.tiktok.model.PatientModelUpcomingOuter;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.Upcoming;
import com.trigma.tiktok.presenter.BasePresenter;

public interface PatientAppointmentsPresenter extends BasePresenter {
    void appointmentRead();

    void checkingForDoctors();

    void fetchPast();

    void fetchUpcoming();

    void goToWaitingRoom(PatientModelUpcomingOuter patientModelUpcomingOuter);

    void gotoDetailScreen(PatientPendingData patientPendingData);

    void gotoMakeVideoCall(Upcoming upcoming);
}
