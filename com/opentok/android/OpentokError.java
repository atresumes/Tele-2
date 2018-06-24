package com.opentok.android;

import android.support.v4.view.PointerIconCompat;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.proxy.AuthApiStatusCodes;
import com.google.android.gms.common.ConnectionResult;

public class OpentokError {
    protected ErrorCode errorCode;
    protected Domain errorDomain;
    protected String errorMessage;

    public enum Domain {
        SessionErrorDomain,
        PublisherErrorDomain,
        SubscriberErrorDomain
    }

    public enum ErrorCode {
        UnknownError(-1),
        AuthorizationFailure(PointerIconCompat.TYPE_WAIT),
        InvalidSessionId(1005),
        ConnectionFailed(PointerIconCompat.TYPE_CELL),
        NoMessagingServer(1503),
        ConnectionRefused(1023),
        SessionStateFailed(PointerIconCompat.TYPE_GRAB),
        P2PSessionMaxParticipants(1403),
        SessionConnectionTimeout(PointerIconCompat.TYPE_GRABBING),
        SessionInternalError(CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE),
        SessionInvalidSignalType(1461),
        SessionSignalDataTooLong(1413),
        SessionSignalTypeTooLong(1414),
        ConnectionDropped(1022),
        SessionDisconnected(PointerIconCompat.TYPE_ALIAS),
        PublisherInternalError(CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE),
        PublisherWebRTCError(1610),
        PublisherUnableToPublish(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED),
        PublisherUnexpectedPeerConnectionDisconnection(1710),
        PublisherCannotAccessCamera(1650),
        PublisherCameraAccessDenied(1670),
        ConnectionTimedOut(1542),
        SubscriberSessionDisconnected(1541),
        SubscriberWebRTCError(1600),
        SubscriberServerCannotFindStream(1604),
        SubscriberStreamLimitExceeded(1605),
        SubscriberInternalError(CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE),
        UnknownPublisherInstance(2003),
        UnknownSubscriberInstance(2004),
        SessionNullOrInvalidParameter(PointerIconCompat.TYPE_COPY),
        VideoCaptureFailed(AuthApiStatusCodes.AUTH_API_INVALID_CREDENTIALS),
        CameraFailed(3010),
        VideoRenderFailed(4000),
        SessionSubscriberNotFound(1112),
        SessionPublisherNotFound(1113),
        PublisherTimeout(1541),
        SessionBlockedCountry(1026),
        SessionConnectionLimitExceeded(1027),
        SessionUnexpectedGetSessionInfoResponse(2001),
        SessionIllegalState(PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW);
        
        private int code;

        private ErrorCode(int code) {
            this.code = code;
        }

        public int getErrorCode() {
            return this.code;
        }

        public static ErrorCode fromTypeCode(int id) {
            for (ErrorCode code : values()) {
                if (code.getErrorCode() == id) {
                    return code;
                }
            }
            return UnknownError;
        }
    }

    public OpentokError(Domain errorDomain, int errorCode, String msg) {
        if (msg == null) {
            msg = "(null description)";
        }
        this.errorMessage = msg;
        this.errorDomain = errorDomain;
        this.errorCode = ErrorCode.fromTypeCode(errorCode);
    }

    public Domain getErrorDomain() {
        return this.errorDomain;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.errorMessage;
    }
}
