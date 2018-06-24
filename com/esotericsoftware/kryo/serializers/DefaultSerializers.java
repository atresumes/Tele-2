package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Util;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

public class DefaultSerializers {

    public static class BigDecimalSerializer extends Serializer<BigDecimal> {
        private final BigIntegerSerializer bigIntegerSerializer = new BigIntegerSerializer();

        public BigDecimalSerializer() {
            setAcceptsNull(true);
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, BigDecimal object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            BigDecimal value = object;
            if (value == BigDecimal.ZERO) {
                this.bigIntegerSerializer.write(kryo, output, BigInteger.ZERO);
                output.writeInt(0, false);
                return;
            }
            this.bigIntegerSerializer.write(kryo, output, value.unscaledValue());
            output.writeInt(value.scale(), false);
        }

        public BigDecimal read(Kryo kryo, Input input, Class<BigDecimal> type) {
            BigInteger unscaledValue = this.bigIntegerSerializer.read(kryo, input, BigInteger.class);
            if (unscaledValue == null) {
                return null;
            }
            int scale = input.readInt(false);
            if (type != BigDecimal.class && type != null) {
                try {
                    Constructor<BigDecimal> constructor = type.getConstructor(new Class[]{BigInteger.class, Integer.TYPE});
                    if (!constructor.isAccessible()) {
                        try {
                            constructor.setAccessible(true);
                        } catch (SecurityException e) {
                        }
                    }
                    return (BigDecimal) constructor.newInstance(new Object[]{unscaledValue, Integer.valueOf(scale)});
                } catch (Throwable ex) {
                    throw new KryoException(ex);
                }
            } else if (unscaledValue == BigInteger.ZERO && scale == 0) {
                return BigDecimal.ZERO;
            } else {
                return new BigDecimal(unscaledValue, scale);
            }
        }
    }

    public static class BigIntegerSerializer extends Serializer<BigInteger> {
        public BigIntegerSerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, BigInteger object) {
            if (object == null) {
                output.writeVarInt(0, true);
                return;
            }
            BigInteger value = object;
            if (value == BigInteger.ZERO) {
                output.writeVarInt(2, true);
                output.writeByte(0);
                return;
            }
            byte[] bytes = value.toByteArray();
            output.writeVarInt(bytes.length + 1, true);
            output.writeBytes(bytes);
        }

        public BigInteger read(Kryo kryo, Input input, Class<BigInteger> type) {
            int length = input.readVarInt(true);
            if (length == 0) {
                return null;
            }
            byte[] bytes = input.readBytes(length - 1);
            if (type == BigInteger.class || type == null) {
                if (length == 2) {
                    switch (bytes[0]) {
                        case (byte) 0:
                            return BigInteger.ZERO;
                        case (byte) 1:
                            return BigInteger.ONE;
                        case (byte) 10:
                            return BigInteger.TEN;
                    }
                }
                return new BigInteger(bytes);
            }
            try {
                Constructor<BigInteger> constructor = type.getConstructor(new Class[]{byte[].class});
                if (!constructor.isAccessible()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (SecurityException e) {
                    }
                }
                return (BigInteger) constructor.newInstance(new Object[]{bytes});
            } catch (Throwable ex) {
                throw new KryoException(ex);
            }
        }
    }

    public static class BooleanSerializer extends Serializer<Boolean> {
        public BooleanSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Boolean object) {
            output.writeBoolean(object.booleanValue());
        }

        public Boolean read(Kryo kryo, Input input, Class<Boolean> cls) {
            return Boolean.valueOf(input.readBoolean());
        }
    }

    public static class ByteSerializer extends Serializer<Byte> {
        public ByteSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Byte object) {
            output.writeByte(object.byteValue());
        }

        public Byte read(Kryo kryo, Input input, Class<Byte> cls) {
            return Byte.valueOf(input.readByte());
        }
    }

    public static class CalendarSerializer extends Serializer<Calendar> {
        private static final long DEFAULT_GREGORIAN_CUTOVER = -12219292800000L;
        TimeZoneSerializer timeZoneSerializer = new TimeZoneSerializer();

        public void write(Kryo kryo, Output output, Calendar object) {
            this.timeZoneSerializer.write(kryo, output, object.getTimeZone());
            output.writeLong(object.getTimeInMillis(), true);
            output.writeBoolean(object.isLenient());
            output.writeInt(object.getFirstDayOfWeek(), true);
            output.writeInt(object.getMinimalDaysInFirstWeek(), true);
            if (object instanceof GregorianCalendar) {
                output.writeLong(((GregorianCalendar) object).getGregorianChange().getTime(), false);
            } else {
                output.writeLong(DEFAULT_GREGORIAN_CUTOVER, false);
            }
        }

        public Calendar read(Kryo kryo, Input input, Class<Calendar> cls) {
            Calendar result = Calendar.getInstance(this.timeZoneSerializer.read(kryo, input, TimeZone.class));
            result.setTimeInMillis(input.readLong(true));
            result.setLenient(input.readBoolean());
            result.setFirstDayOfWeek(input.readInt(true));
            result.setMinimalDaysInFirstWeek(input.readInt(true));
            long gregorianChange = input.readLong(false);
            if (gregorianChange != DEFAULT_GREGORIAN_CUTOVER && (result instanceof GregorianCalendar)) {
                ((GregorianCalendar) result).setGregorianChange(new Date(gregorianChange));
            }
            return result;
        }

        public Calendar copy(Kryo kryo, Calendar original) {
            return (Calendar) original.clone();
        }
    }

    public static class CharSerializer extends Serializer<Character> {
        public CharSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Character object) {
            output.writeChar(object.charValue());
        }

        public Character read(Kryo kryo, Input input, Class<Character> cls) {
            return Character.valueOf(input.readChar());
        }
    }

    public static class CharsetSerializer extends Serializer<Charset> {
        public CharsetSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Charset object) {
            output.writeString(object.name());
        }

        public Charset read(Kryo kryo, Input input, Class<Charset> cls) {
            return Charset.forName(input.readString());
        }
    }

    public static class ClassSerializer extends Serializer<Class> {
        public ClassSerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, Class object) {
            kryo.writeClass(output, object);
            int i = (object == null || !object.isPrimitive()) ? 0 : 1;
            output.writeByte(i);
        }

        public Class read(Kryo kryo, Input input, Class<Class> cls) {
            Registration registration = kryo.readClass(input);
            int isPrimitive = input.read();
            Class typ = registration != null ? registration.getType() : null;
            return (typ == null || !typ.isPrimitive() || isPrimitive == 1) ? typ : Util.getWrapperClass(typ);
        }
    }

    public static class CollectionsEmptyListSerializer extends Serializer {
        public CollectionsEmptyListSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Object object) {
        }

        public Object read(Kryo kryo, Input input, Class type) {
            return Collections.EMPTY_LIST;
        }
    }

    public static class CollectionsEmptyMapSerializer extends Serializer {
        public CollectionsEmptyMapSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Object object) {
        }

        public Object read(Kryo kryo, Input input, Class type) {
            return Collections.EMPTY_MAP;
        }
    }

    public static class CollectionsEmptySetSerializer extends Serializer {
        public CollectionsEmptySetSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Object object) {
        }

        public Object read(Kryo kryo, Input input, Class type) {
            return Collections.EMPTY_SET;
        }
    }

    public static class CollectionsSingletonListSerializer extends Serializer<List> {
        public CollectionsSingletonListSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, List object) {
            kryo.writeClassAndObject(output, object.get(0));
        }

        public List read(Kryo kryo, Input input, Class type) {
            return Collections.singletonList(kryo.readClassAndObject(input));
        }
    }

    public static class CollectionsSingletonMapSerializer extends Serializer<Map> {
        public CollectionsSingletonMapSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Map object) {
            Entry entry = (Entry) object.entrySet().iterator().next();
            kryo.writeClassAndObject(output, entry.getKey());
            kryo.writeClassAndObject(output, entry.getValue());
        }

        public Map read(Kryo kryo, Input input, Class type) {
            return Collections.singletonMap(kryo.readClassAndObject(input), kryo.readClassAndObject(input));
        }
    }

    public static class CollectionsSingletonSetSerializer extends Serializer<Set> {
        public CollectionsSingletonSetSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Set object) {
            kryo.writeClassAndObject(output, object.iterator().next());
        }

        public Set read(Kryo kryo, Input input, Class type) {
            return Collections.singleton(kryo.readClassAndObject(input));
        }
    }

    public static class CurrencySerializer extends Serializer<Currency> {
        public CurrencySerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, Currency object) {
            output.writeString(object == null ? null : object.getCurrencyCode());
        }

        public Currency read(Kryo kryo, Input input, Class<Currency> cls) {
            String currencyCode = input.readString();
            if (currencyCode == null) {
                return null;
            }
            return Currency.getInstance(currencyCode);
        }
    }

    public static class DateSerializer extends Serializer<Date> {
        private Date create(Kryo kryo, Class<? extends Date> type, long time) throws KryoException {
            if (type == Date.class || type == null) {
                return new Date(time);
            }
            if (type == Timestamp.class) {
                return new Timestamp(time);
            }
            if (type == java.sql.Date.class) {
                return new java.sql.Date(time);
            }
            if (type == Time.class) {
                return new Time(time);
            }
            try {
                Constructor<? extends Date> constructor = type.getConstructor(new Class[]{Long.TYPE});
                if (!constructor.isAccessible()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (SecurityException e) {
                    }
                }
                return (Date) constructor.newInstance(new Object[]{Long.valueOf(time)});
            } catch (Exception e2) {
                Date d = (Date) kryo.newInstance(type);
                d.setTime(time);
                return d;
            }
        }

        public void write(Kryo kryo, Output output, Date object) {
            output.writeLong(object.getTime(), true);
        }

        public Date read(Kryo kryo, Input input, Class<Date> type) {
            return create(kryo, type, input.readLong(true));
        }

        public Date copy(Kryo kryo, Date original) {
            return create(kryo, original.getClass(), original.getTime());
        }
    }

    public static class DoubleSerializer extends Serializer<Double> {
        public DoubleSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Double object) {
            output.writeDouble(object.doubleValue());
        }

        public Double read(Kryo kryo, Input input, Class<Double> cls) {
            return Double.valueOf(input.readDouble());
        }
    }

    public static class EnumSerializer extends Serializer<Enum> {
        private Object[] enumConstants;

        public EnumSerializer(Class<? extends Enum> type) {
            setImmutable(true);
            setAcceptsNull(true);
            this.enumConstants = type.getEnumConstants();
            if (this.enumConstants == null) {
                throw new IllegalArgumentException("The type must be an enum: " + type);
            }
        }

        public void write(Kryo kryo, Output output, Enum object) {
            if (object == null) {
                output.writeVarInt(0, true);
            } else {
                output.writeVarInt(object.ordinal() + 1, true);
            }
        }

        public Enum read(Kryo kryo, Input input, Class<Enum> type) {
            int ordinal = input.readVarInt(true);
            if (ordinal == 0) {
                return null;
            }
            ordinal--;
            if (ordinal >= 0 && ordinal <= this.enumConstants.length - 1) {
                return (Enum) this.enumConstants[ordinal];
            }
            throw new KryoException("Invalid ordinal for enum \"" + type.getName() + "\": " + ordinal);
        }
    }

    public static class EnumSetSerializer extends Serializer<EnumSet> {
        public void write(Kryo kryo, Output output, EnumSet object) {
            Serializer serializer;
            if (object.isEmpty()) {
                EnumSet tmp = EnumSet.complementOf(object);
                if (tmp.isEmpty()) {
                    throw new KryoException("An EnumSet must have a defined Enum to be serialized.");
                }
                serializer = kryo.writeClass(output, tmp.iterator().next().getClass()).getSerializer();
            } else {
                serializer = kryo.writeClass(output, object.iterator().next().getClass()).getSerializer();
            }
            output.writeInt(object.size(), true);
            Iterator it = object.iterator();
            while (it.hasNext()) {
                serializer.write(kryo, output, it.next());
            }
        }

        public EnumSet read(Kryo kryo, Input input, Class<EnumSet> cls) {
            Registration registration = kryo.readClass(input);
            EnumSet object = EnumSet.noneOf(registration.getType());
            Serializer serializer = registration.getSerializer();
            int length = input.readInt(true);
            for (int i = 0; i < length; i++) {
                object.add(serializer.read(kryo, input, null));
            }
            return object;
        }

        public EnumSet copy(Kryo kryo, EnumSet original) {
            return EnumSet.copyOf(original);
        }
    }

    public static class FloatSerializer extends Serializer<Float> {
        public FloatSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Float object) {
            output.writeFloat(object.floatValue());
        }

        public Float read(Kryo kryo, Input input, Class<Float> cls) {
            return Float.valueOf(input.readFloat());
        }
    }

    public static class IntSerializer extends Serializer<Integer> {
        public IntSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Integer object) {
            output.writeInt(object.intValue(), false);
        }

        public Integer read(Kryo kryo, Input input, Class<Integer> cls) {
            return Integer.valueOf(input.readInt(false));
        }
    }

    public static class KryoSerializableSerializer extends Serializer<KryoSerializable> {
        public void write(Kryo kryo, Output output, KryoSerializable object) {
            object.write(kryo, output);
        }

        public KryoSerializable read(Kryo kryo, Input input, Class<KryoSerializable> type) {
            KryoSerializable object = (KryoSerializable) kryo.newInstance(type);
            kryo.reference(object);
            object.read(kryo, input);
            return object;
        }
    }

    public static class LocaleSerializer extends Serializer<Locale> {
        public static final Locale SPAIN = new Locale("es", "ES", "");
        public static final Locale SPANISH = new Locale("es", "", "");

        public LocaleSerializer() {
            setImmutable(true);
        }

        protected Locale create(String language, String country, String variant) {
            Locale defaultLocale = Locale.getDefault();
            if (isSameLocale(defaultLocale, language, country, variant)) {
                return defaultLocale;
            }
            if (defaultLocale != Locale.US && isSameLocale(Locale.US, language, country, variant)) {
                return Locale.US;
            }
            if (isSameLocale(Locale.ENGLISH, language, country, variant)) {
                return Locale.ENGLISH;
            }
            if (isSameLocale(Locale.GERMAN, language, country, variant)) {
                return Locale.GERMAN;
            }
            if (isSameLocale(SPANISH, language, country, variant)) {
                return SPANISH;
            }
            if (isSameLocale(Locale.FRENCH, language, country, variant)) {
                return Locale.FRENCH;
            }
            if (isSameLocale(Locale.ITALIAN, language, country, variant)) {
                return Locale.ITALIAN;
            }
            if (isSameLocale(Locale.JAPANESE, language, country, variant)) {
                return Locale.JAPANESE;
            }
            if (isSameLocale(Locale.KOREAN, language, country, variant)) {
                return Locale.KOREAN;
            }
            if (isSameLocale(Locale.SIMPLIFIED_CHINESE, language, country, variant)) {
                return Locale.SIMPLIFIED_CHINESE;
            }
            if (isSameLocale(Locale.CHINESE, language, country, variant)) {
                return Locale.CHINESE;
            }
            if (isSameLocale(Locale.TRADITIONAL_CHINESE, language, country, variant)) {
                return Locale.TRADITIONAL_CHINESE;
            }
            if (isSameLocale(Locale.UK, language, country, variant)) {
                return Locale.UK;
            }
            if (isSameLocale(Locale.GERMANY, language, country, variant)) {
                return Locale.GERMANY;
            }
            if (isSameLocale(SPAIN, language, country, variant)) {
                return SPAIN;
            }
            if (isSameLocale(Locale.FRANCE, language, country, variant)) {
                return Locale.FRANCE;
            }
            if (isSameLocale(Locale.ITALY, language, country, variant)) {
                return Locale.ITALY;
            }
            if (isSameLocale(Locale.JAPAN, language, country, variant)) {
                return Locale.JAPAN;
            }
            if (isSameLocale(Locale.KOREA, language, country, variant)) {
                return Locale.KOREA;
            }
            if (isSameLocale(Locale.CANADA, language, country, variant)) {
                return Locale.CANADA;
            }
            if (isSameLocale(Locale.CANADA_FRENCH, language, country, variant)) {
                return Locale.CANADA_FRENCH;
            }
            return new Locale(language, country, variant);
        }

        public void write(Kryo kryo, Output output, Locale l) {
            output.writeAscii(l.getLanguage());
            output.writeAscii(l.getCountry());
            output.writeString(l.getVariant());
        }

        public Locale read(Kryo kryo, Input input, Class<Locale> cls) {
            return create(input.readString(), input.readString(), input.readString());
        }

        protected static boolean isSameLocale(Locale locale, String language, String country, String variant) {
            try {
                return locale.getLanguage().equals(language) && locale.getCountry().equals(country) && locale.getVariant().equals(variant);
            } catch (NullPointerException e) {
                return false;
            }
        }
    }

    public static class LongSerializer extends Serializer<Long> {
        public LongSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Long object) {
            output.writeLong(object.longValue(), false);
        }

        public Long read(Kryo kryo, Input input, Class<Long> cls) {
            return Long.valueOf(input.readLong(false));
        }
    }

    public static class ShortSerializer extends Serializer<Short> {
        public ShortSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Short object) {
            output.writeShort(object.shortValue());
        }

        public Short read(Kryo kryo, Input input, Class<Short> cls) {
            return Short.valueOf(input.readShort());
        }
    }

    public static class StringBufferSerializer extends Serializer<StringBuffer> {
        public StringBufferSerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, StringBuffer object) {
            output.writeString((CharSequence) object);
        }

        public StringBuffer read(Kryo kryo, Input input, Class<StringBuffer> cls) {
            String value = input.readString();
            if (value == null) {
                return null;
            }
            return new StringBuffer(value);
        }

        public StringBuffer copy(Kryo kryo, StringBuffer original) {
            return new StringBuffer(original);
        }
    }

    public static class StringBuilderSerializer extends Serializer<StringBuilder> {
        public StringBuilderSerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, StringBuilder object) {
            output.writeString((CharSequence) object);
        }

        public StringBuilder read(Kryo kryo, Input input, Class<StringBuilder> cls) {
            return input.readStringBuilder();
        }

        public StringBuilder copy(Kryo kryo, StringBuilder original) {
            return new StringBuilder(original);
        }
    }

    public static class StringSerializer extends Serializer<String> {
        public StringSerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, String object) {
            output.writeString(object);
        }

        public String read(Kryo kryo, Input input, Class<String> cls) {
            return input.readString();
        }
    }

    public static class TimeZoneSerializer extends Serializer<TimeZone> {
        public TimeZoneSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, TimeZone object) {
            output.writeString(object.getID());
        }

        public TimeZone read(Kryo kryo, Input input, Class<TimeZone> cls) {
            return TimeZone.getTimeZone(input.readString());
        }
    }

    public static class TreeMapSerializer extends MapSerializer {
        public void write(Kryo kryo, Output output, Map map) {
            kryo.writeClassAndObject(output, ((TreeMap) map).comparator());
            super.write(kryo, output, map);
        }

        protected Map create(Kryo kryo, Input input, Class<Map> type) {
            return createTreeMap(type, (Comparator) kryo.readClassAndObject(input));
        }

        protected Map createCopy(Kryo kryo, Map original) {
            return createTreeMap(original.getClass(), ((TreeMap) original).comparator());
        }

        private TreeMap createTreeMap(Class<? extends Map> type, Comparator comparator) {
            if (type == TreeMap.class || type == null) {
                return new TreeMap(comparator);
            }
            try {
                Constructor constructor = type.getConstructor(new Class[]{Comparator.class});
                if (!constructor.isAccessible()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (SecurityException e) {
                    }
                }
                return (TreeMap) constructor.newInstance(new Object[]{comparator});
            } catch (Throwable ex) {
                throw new KryoException(ex);
            }
        }
    }

    public static class TreeSetSerializer extends CollectionSerializer {
        public void write(Kryo kryo, Output output, Collection collection) {
            kryo.writeClassAndObject(output, ((TreeSet) collection).comparator());
            super.write(kryo, output, collection);
        }

        protected TreeSet create(Kryo kryo, Input input, Class<Collection> type) {
            return createTreeSet(type, (Comparator) kryo.readClassAndObject(input));
        }

        protected TreeSet createCopy(Kryo kryo, Collection original) {
            return createTreeSet(original.getClass(), ((TreeSet) original).comparator());
        }

        private TreeSet createTreeSet(Class<? extends Collection> type, Comparator comparator) {
            if (type == TreeSet.class || type == null) {
                return new TreeSet(comparator);
            }
            try {
                Constructor constructor = type.getConstructor(new Class[]{Comparator.class});
                if (!constructor.isAccessible()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (SecurityException e) {
                    }
                }
                return (TreeSet) constructor.newInstance(new Object[]{comparator});
            } catch (Throwable ex) {
                throw new KryoException(ex);
            }
        }
    }

    public static class URLSerializer extends Serializer<URL> {
        public URLSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, URL object) {
            output.writeString(object.toExternalForm());
        }

        public URL read(Kryo kryo, Input input, Class<URL> cls) {
            try {
                return new URL(input.readString());
            } catch (Throwable e) {
                throw new KryoException(e);
            }
        }
    }

    public static class VoidSerializer extends Serializer {
        public VoidSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Object object) {
        }

        public Object read(Kryo kryo, Input input, Class type) {
            return null;
        }
    }
}
