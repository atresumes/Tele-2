package com.opentok.android;

import android.content.Context;

public class Subscriber extends SubscriberKit {

    public static class Builder extends com.opentok.android.SubscriberKit.Builder {
        public Builder(Context context, Stream stream) {
            super(context, stream);
        }

        public Builder renderer(BaseVideoRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public Subscriber build() {
            return new Subscriber(this.context, this.stream, this.renderer);
        }
    }

    protected Subscriber(Context context, Stream stream, BaseVideoRenderer renderer) {
        super(context, stream, renderer);
        if (renderer == null) {
            this.renderer = VideoRenderFactory.constructRenderer(context);
        }
    }

    @Deprecated
    public Subscriber(Context context, Stream stream) {
        this(context, stream, null);
    }
}
