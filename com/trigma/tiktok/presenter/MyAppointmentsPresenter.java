package com.trigma.tiktok.presenter;

import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.model.Upcoming;

public interface MyAppointmentsPresenter extends BasePresenter {
    void appointmentRead();

    void fetchPast();

    void fetchUpcoming();

    void gotoDetailScreen(PatientPendingData patientPendingData);

    void gotoMakeVideoCall(Upcoming upcoming);

    void staffAppointmentRead();
}
