package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Util;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class TimeSerializers {

    private static class DurationSerializer extends Serializer<Duration> {
        private DurationSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, Duration duration) {
            out.writeLong(duration.getSeconds());
            out.writeInt(duration.getNano(), true);
        }

        public Duration read(Kryo kryo, Input in, Class<Duration> cls) {
            return Duration.ofSeconds(in.readLong(), (long) in.readInt(true));
        }
    }

    private static class InstantSerializer extends Serializer<Instant> {
        private InstantSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, Instant instant) {
            out.writeLong(instant.getEpochSecond(), true);
            out.writeInt(instant.getNano(), true);
        }

        public Instant read(Kryo kryo, Input in, Class<Instant> cls) {
            return Instant.ofEpochSecond(in.readLong(true), (long) in.readInt(true));
        }
    }

    private static class LocalDateSerializer extends Serializer<LocalDate> {
        private LocalDateSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, LocalDate date) {
            write(out, date);
        }

        static void write(Output out, LocalDate date) {
            out.writeInt(date.getYear(), true);
            out.writeByte(date.getMonthValue());
            out.writeByte(date.getDayOfMonth());
        }

        public LocalDate read(Kryo kryo, Input in, Class<LocalDate> cls) {
            return read(in);
        }

        static LocalDate read(Input in) {
            return LocalDate.of(in.readInt(true), in.readByte(), in.readByte());
        }
    }

    private static class LocalDateTimeSerializer extends Serializer<LocalDateTime> {
        private LocalDateTimeSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, LocalDateTime dateTime) {
            LocalDateSerializer.write(out, dateTime.toLocalDate());
            LocalTimeSerializer.write(out, dateTime.toLocalTime());
        }

        public LocalDateTime read(Kryo kryo, Input in, Class<LocalDateTime> cls) {
            return LocalDateTime.of(LocalDateSerializer.read(in), LocalTimeSerializer.read(in));
        }
    }

    private static class LocalTimeSerializer extends Serializer<LocalTime> {
        private LocalTimeSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, LocalTime time) {
            write(out, time);
        }

        static void write(Output out, LocalTime time) {
            if (time.getNano() != 0) {
                out.writeByte(time.getHour());
                out.writeByte(time.getMinute());
                out.writeByte(time.getSecond());
                out.writeInt(time.getNano(), true);
            } else if (time.getSecond() != 0) {
                out.writeByte(time.getHour());
                out.writeByte(time.getMinute());
                out.writeByte(time.getSecond() ^ -1);
            } else if (time.getMinute() == 0) {
                out.writeByte(time.getHour() ^ -1);
            } else {
                out.writeByte(time.getHour());
                out.writeByte(time.getMinute() ^ -1);
            }
        }

        public LocalTime read(Kryo kryo, Input in, Class<LocalTime> cls) {
            return read(in);
        }

        static LocalTime read(Input in) {
            int hour = in.readByte();
            int minute = 0;
            int second = 0;
            int nano = 0;
            if (hour < 0) {
                hour ^= -1;
            } else {
                minute = in.readByte();
                if (minute < 0) {
                    minute ^= -1;
                } else {
                    second = in.readByte();
                    if (second < 0) {
                        second ^= -1;
                    } else {
                        nano = in.readInt(true);
                    }
                }
            }
            return LocalTime.of(hour, minute, second, nano);
        }
    }

    private static class MonthDaySerializer extends Serializer<MonthDay> {
        private MonthDaySerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, MonthDay obj) {
            out.writeByte(obj.getMonthValue());
            out.writeByte(obj.getDayOfMonth());
        }

        public MonthDay read(Kryo kryo, Input in, Class<MonthDay> cls) {
            return MonthDay.of(in.readByte(), in.readByte());
        }
    }

    private static class OffsetDateTimeSerializer extends Serializer<OffsetDateTime> {
        private OffsetDateTimeSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, OffsetDateTime obj) {
            LocalDateSerializer.write(out, obj.toLocalDate());
            LocalTimeSerializer.write(out, obj.toLocalTime());
            ZoneOffsetSerializer.write(out, obj.getOffset());
        }

        public OffsetDateTime read(Kryo kryo, Input in, Class<OffsetDateTime> cls) {
            return OffsetDateTime.of(LocalDateSerializer.read(in), LocalTimeSerializer.read(in), ZoneOffsetSerializer.read(in));
        }
    }

    private static class OffsetTimeSerializer extends Serializer<OffsetTime> {
        private OffsetTimeSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, OffsetTime obj) {
            LocalTimeSerializer.write(out, obj.toLocalTime());
            ZoneOffsetSerializer.write(out, obj.getOffset());
        }

        public OffsetTime read(Kryo kryo, Input in, Class<OffsetTime> cls) {
            return OffsetTime.of(LocalTimeSerializer.read(in), ZoneOffsetSerializer.read(in));
        }
    }

    private static class PeriodSerializer extends Serializer<Period> {
        private PeriodSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, Period obj) {
            out.writeInt(obj.getYears(), true);
            out.writeInt(obj.getMonths(), true);
            out.writeInt(obj.getDays(), true);
        }

        public Period read(Kryo kryo, Input in, Class<Period> cls) {
            return Period.of(in.readInt(true), in.readInt(true), in.readInt(true));
        }
    }

    private static class YearMonthSerializer extends Serializer<YearMonth> {
        private YearMonthSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, YearMonth obj) {
            out.writeInt(obj.getYear(), true);
            out.writeByte(obj.getMonthValue());
        }

        public YearMonth read(Kryo kryo, Input in, Class<YearMonth> cls) {
            return YearMonth.of(in.readInt(true), in.readByte());
        }
    }

    private static class YearSerializer extends Serializer<Year> {
        private YearSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, Year obj) {
            out.writeInt(obj.getValue(), true);
        }

        public Year read(Kryo kryo, Input in, Class<Year> cls) {
            return Year.of(in.readInt(true));
        }
    }

    private static class ZoneIdSerializer extends Serializer<ZoneId> {
        private ZoneIdSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, ZoneId obj) {
            write(out, obj);
        }

        static void write(Output out, ZoneId obj) {
            out.writeString(obj.getId());
        }

        public ZoneId read(Kryo kryo, Input in, Class<ZoneId> cls) {
            return read(in);
        }

        static ZoneId read(Input in) {
            return ZoneId.of(in.readString());
        }
    }

    private static class ZoneOffsetSerializer extends Serializer<ZoneOffset> {
        private ZoneOffsetSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, ZoneOffset obj) {
            write(out, obj);
        }

        static void write(Output out, ZoneOffset obj) {
            int offsetByte;
            int offsetSecs = obj.getTotalSeconds();
            if (offsetSecs % 900 == 0) {
                offsetByte = offsetSecs / 900;
            } else {
                offsetByte = 127;
            }
            out.writeByte(offsetByte);
            if (offsetByte == 127) {
                out.writeInt(offsetSecs);
            }
        }

        public ZoneOffset read(Kryo kryo, Input in, Class<ZoneOffset> cls) {
            return read(in);
        }

        static ZoneOffset read(Input in) {
            int offsetByte = in.readByte();
            return offsetByte == 127 ? ZoneOffset.ofTotalSeconds(in.readInt()) : ZoneOffset.ofTotalSeconds(offsetByte * 900);
        }
    }

    private static class ZonedDateTimeSerializer extends Serializer<ZonedDateTime> {
        private ZonedDateTimeSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output out, ZonedDateTime obj) {
            LocalDateSerializer.write(out, obj.toLocalDate());
            LocalTimeSerializer.write(out, obj.toLocalTime());
            ZoneIdSerializer.write(out, obj.getZone());
        }

        public ZonedDateTime read(Kryo kryo, Input in, Class<ZonedDateTime> cls) {
            return ZonedDateTime.of(LocalDateSerializer.read(in), LocalTimeSerializer.read(in), ZoneIdSerializer.read(in));
        }
    }

    public static void addDefaultSerializers(Kryo kryo) {
        if (Util.isClassAvailable("java.time.Duration")) {
            kryo.addDefaultSerializer(Duration.class, new DurationSerializer());
        }
        if (Util.isClassAvailable("java.time.Instant")) {
            kryo.addDefaultSerializer(Instant.class, new InstantSerializer());
        }
        if (Util.isClassAvailable("java.time.LocalDate")) {
            kryo.addDefaultSerializer(LocalDate.class, new LocalDateSerializer());
        }
        if (Util.isClassAvailable("java.time.LocalTime")) {
            kryo.addDefaultSerializer(LocalTime.class, new LocalTimeSerializer());
        }
        if (Util.isClassAvailable("java.time.LocalDateTime")) {
            kryo.addDefaultSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        }
        if (Util.isClassAvailable("java.time.ZoneOffset")) {
            kryo.addDefaultSerializer(ZoneOffset.class, new ZoneOffsetSerializer());
        }
        if (Util.isClassAvailable("java.time.ZoneId")) {
            kryo.addDefaultSerializer(ZoneId.class, new ZoneIdSerializer());
        }
        if (Util.isClassAvailable("java.time.OffsetTime")) {
            kryo.addDefaultSerializer(OffsetTime.class, new OffsetTimeSerializer());
        }
        if (Util.isClassAvailable("java.time.OffsetDateTime")) {
            kryo.addDefaultSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        }
        if (Util.isClassAvailable("java.time.ZonedDateTime")) {
            kryo.addDefaultSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        }
        if (Util.isClassAvailable("java.time.Year")) {
            kryo.addDefaultSerializer(Year.class, new YearSerializer());
        }
        if (Util.isClassAvailable("java.time.YearMonth")) {
            kryo.addDefaultSerializer(YearMonth.class, new YearMonthSerializer());
        }
        if (Util.isClassAvailable("java.time.MonthDay")) {
            kryo.addDefaultSerializer(MonthDay.class, new MonthDaySerializer());
        }
        if (Util.isClassAvailable("java.time.Period")) {
            kryo.addDefaultSerializer(Period.class, new PeriodSerializer());
        }
    }
}
