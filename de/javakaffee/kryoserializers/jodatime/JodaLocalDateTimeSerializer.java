package de.javakaffee.kryoserializers.jodatime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import fr.quentinklein.slt.TrackerSettings;
import org.joda.time.LocalDateTime;

public class JodaLocalDateTimeSerializer extends Serializer<LocalDateTime> {
    public JodaLocalDateTimeSerializer() {
        setImmutable(true);
    }

    public LocalDateTime read(Kryo kryo, Input input, Class<LocalDateTime> cls) {
        long packedLocalDateTime = input.readLong(true);
        int packedYearMonthDay = (int) (packedLocalDateTime / 86400000);
        int millisOfDay = (int) (packedLocalDateTime % 86400000);
        return new LocalDateTime(packedYearMonthDay / 416, (packedYearMonthDay % 416) / 32, packedYearMonthDay % 32, millisOfDay / 3600000, (millisOfDay % 3600000) / TrackerSettings.DEFAULT_TIMEOUT, (millisOfDay % TrackerSettings.DEFAULT_TIMEOUT) / 1000, millisOfDay % 1000, IdentifiableChronology.readChronology(input));
    }

    public void write(Kryo kryo, Output output, LocalDateTime localDateTime) {
        output.writeLong((((long) ((((localDateTime.getYear() * 13) * 32) + (localDateTime.getMonthOfYear() * 32)) + localDateTime.getDayOfMonth())) * 86400000) + ((long) localDateTime.getMillisOfDay()), true);
        String chronologyId = IdentifiableChronology.getChronologyId(localDateTime.getChronology());
        if (chronologyId == null) {
            chronologyId = "";
        }
        output.writeString(chronologyId);
    }
}
