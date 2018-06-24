package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.minlog.Log;

public class TaggedFieldSerializerConfig extends FieldSerializerConfig {
    private boolean ignoreUnknownTags = false;

    public void setIgnoreUnknownTags(boolean ignoreUnknownTags) {
        this.ignoreUnknownTags = ignoreUnknownTags;
        if (Log.TRACE) {
            Log.trace("kryo.TaggedFieldSerializerConfig", "setIgnoreUnknownTags: " + ignoreUnknownTags);
        }
    }

    public boolean isIgnoreUnknownTags() {
        return this.ignoreUnknownTags;
    }

    protected TaggedFieldSerializerConfig clone() {
        return (TaggedFieldSerializerConfig) super.clone();
    }
}
