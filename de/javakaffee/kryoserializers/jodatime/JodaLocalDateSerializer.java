package de.javakaffee.kryoserializers.jodatime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.LocalDate;

public class JodaLocalDateSerializer extends Serializer<LocalDate> {
    public JodaLocalDateSerializer() {
        setImmutable(true);
    }

    public LocalDate read(Kryo kryo, Input input, Class<LocalDate> cls) {
        int packedYearMonthDay = input.readInt(true);
        return new LocalDate(packedYearMonthDay / 416, (packedYearMonthDay % 416) / 32, packedYearMonthDay % 32, IdentifiableChronology.readChronology(input));
    }

    public void write(Kryo kryo, Output output, LocalDate localDate) {
        output.writeInt((((localDate.getYear() * 13) * 32) + (localDate.getMonthOfYear() * 32)) + localDate.getDayOfMonth(), true);
        String chronologyId = IdentifiableChronology.getChronologyId(localDate.getChronology());
        if (chronologyId == null) {
            chronologyId = "";
        }
        output.writeString(chronologyId);
    }
}
