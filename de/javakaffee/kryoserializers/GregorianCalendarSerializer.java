package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class GregorianCalendarSerializer extends Serializer<GregorianCalendar> {
    private final Field _zoneField;

    public GregorianCalendarSerializer() {
        try {
            this._zoneField = Calendar.class.getDeclaredField("zone");
            this._zoneField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GregorianCalendar read(Kryo kryo, Input input, Class<GregorianCalendar> cls) {
        Calendar result = GregorianCalendar.getInstance();
        result.setTimeInMillis(input.readLong(true));
        result.setLenient(input.readBoolean());
        result.setFirstDayOfWeek(input.readInt(true));
        result.setMinimalDaysInFirstWeek(input.readInt(true));
        String timeZoneId = input.readString();
        if (!getTimeZone(result).getID().equals(timeZoneId)) {
            result.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        }
        return (GregorianCalendar) result;
    }

    public void write(Kryo kryo, Output output, GregorianCalendar calendar) {
        output.writeLong(calendar.getTimeInMillis(), true);
        output.writeBoolean(calendar.isLenient());
        output.writeInt(calendar.getFirstDayOfWeek(), true);
        output.writeInt(calendar.getMinimalDaysInFirstWeek(), true);
        output.writeString(getTimeZone(calendar).getID());
    }

    public GregorianCalendar copy(Kryo kryo, GregorianCalendar original) {
        return (GregorianCalendar) original.clone();
    }

    private TimeZone getTimeZone(Calendar obj) {
        try {
            return (TimeZone) this._zoneField.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
