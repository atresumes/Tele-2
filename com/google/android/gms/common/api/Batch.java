package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult.zza;
import com.google.android.gms.internal.zzzx;
import java.util.ArrayList;
import java.util.List;

public final class Batch extends zzzx<BatchResult> {
    private boolean zzaxA;
    private boolean zzaxB;
    private final PendingResult<?>[] zzaxC;
    private int zzaxz;
    private final Object zzrN;

    class C06691 implements zza {
        final /* synthetic */ Batch zzaxD;

        C06691(Batch batch) {
            this.zzaxD = batch;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void zzx(com.google.android.gms.common.api.Status r6) {
            /*
            r5 = this;
            r0 = r5.zzaxD;
            r1 = r0.zzrN;
            monitor-enter(r1);
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r0 = r0.isCanceled();	 Catch:{ all -> 0x0039 }
            if (r0 == 0) goto L_0x0011;
        L_0x000f:
            monitor-exit(r1);	 Catch:{ all -> 0x0039 }
        L_0x0010:
            return;
        L_0x0011:
            r0 = r6.isCanceled();	 Catch:{ all -> 0x0039 }
            if (r0 == 0) goto L_0x003c;
        L_0x0017:
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r2 = 1;
            r0.zzaxB = r2;	 Catch:{ all -> 0x0039 }
        L_0x001d:
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r0.zzaxz = r0.zzaxz - 1;	 Catch:{ all -> 0x0039 }
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r0 = r0.zzaxz;	 Catch:{ all -> 0x0039 }
            if (r0 != 0) goto L_0x0037;
        L_0x002a:
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r0 = r0.zzaxB;	 Catch:{ all -> 0x0039 }
            if (r0 == 0) goto L_0x0049;
        L_0x0032:
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            super.cancel();	 Catch:{ all -> 0x0039 }
        L_0x0037:
            monitor-exit(r1);	 Catch:{ all -> 0x0039 }
            goto L_0x0010;
        L_0x0039:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0039 }
            throw r0;
        L_0x003c:
            r0 = r6.isSuccess();	 Catch:{ all -> 0x0039 }
            if (r0 != 0) goto L_0x001d;
        L_0x0042:
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r2 = 1;
            r0.zzaxA = r2;	 Catch:{ all -> 0x0039 }
            goto L_0x001d;
        L_0x0049:
            r0 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r0 = r0.zzaxA;	 Catch:{ all -> 0x0039 }
            if (r0 == 0) goto L_0x0069;
        L_0x0051:
            r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0039 }
            r2 = 13;
            r0.<init>(r2);	 Catch:{ all -> 0x0039 }
        L_0x0058:
            r2 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r3 = new com.google.android.gms.common.api.BatchResult;	 Catch:{ all -> 0x0039 }
            r4 = r5.zzaxD;	 Catch:{ all -> 0x0039 }
            r4 = r4.zzaxC;	 Catch:{ all -> 0x0039 }
            r3.<init>(r0, r4);	 Catch:{ all -> 0x0039 }
            r2.zzb(r3);	 Catch:{ all -> 0x0039 }
            goto L_0x0037;
        L_0x0069:
            r0 = com.google.android.gms.common.api.Status.zzayh;	 Catch:{ all -> 0x0039 }
            goto L_0x0058;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.Batch.1.zzx(com.google.android.gms.common.api.Status):void");
        }
    }

    public static final class Builder {
        private GoogleApiClient zzamy;
        private List<PendingResult<?>> zzaxE = new ArrayList();

        public Builder(GoogleApiClient googleApiClient) {
            this.zzamy = googleApiClient;
        }

        public <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken(this.zzaxE.size());
            this.zzaxE.add(pendingResult);
            return batchResultToken;
        }

        public Batch build() {
            return new Batch(this.zzaxE, this.zzamy);
        }
    }

    private Batch(List<PendingResult<?>> list, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.zzrN = new Object();
        this.zzaxz = list.size();
        this.zzaxC = new PendingResult[this.zzaxz];
        if (list.isEmpty()) {
            zzb(new BatchResult(Status.zzayh, this.zzaxC));
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            PendingResult pendingResult = (PendingResult) list.get(i);
            this.zzaxC[i] = pendingResult;
            pendingResult.zza(new C06691(this));
        }
    }

    public void cancel() {
        super.cancel();
        for (PendingResult cancel : this.zzaxC) {
            cancel.cancel();
        }
    }

    public BatchResult createFailedResult(Status status) {
        return new BatchResult(status, this.zzaxC);
    }

    public /* synthetic */ Result zzc(Status status) {
        return createFailedResult(status);
    }
}
