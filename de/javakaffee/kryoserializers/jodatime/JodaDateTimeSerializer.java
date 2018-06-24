package de.javakaffee.kryoserializers.jodatime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class JodaDateTimeSerializer extends Serializer<DateTime> {
    static final String CHRONOLOGY = "ch";
    static final String DATE_TIME = "dt";
    static final String MILLIS = "millis";
    static final String TIME_ZONE = "tz";

    public JodaDateTimeSerializer() {
        setImmutable(true);
    }

    public DateTime read(Kryo kryo, Input input, Class<DateTime> cls) {
        return new DateTime(input.readLong(true), IdentifiableChronology.readChronology(input).withZone(readTimeZone(input)));
    }

    public void write(Kryo kryo, Output output, DateTime obj) {
        output.writeLong(obj.getMillis(), true);
        String chronologyId = IdentifiableChronology.getChronologyId(obj.getChronology());
        if (chronologyId == null) {
            chronologyId = "";
        }
        output.writeString(chronologyId);
        output.writeString(obj.getZone().getID());
    }

    private DateTimeZone readTimeZone(Input input) {
        String tz = input.readString();
        if ("".equals(tz)) {
            return DateTimeZone.getDefault();
        }
        return DateTimeZone.forID(tz);
    }
}
