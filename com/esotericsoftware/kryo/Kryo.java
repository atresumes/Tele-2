package com.esotericsoftware.kryo;

import com.esotericsoftware.kryo.factories.PseudoSerializerFactory;
import com.esotericsoftware.kryo.factories.ReflectionSerializerFactory;
import com.esotericsoftware.kryo.factories.SerializerFactory;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer.Closure;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.BooleanArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.ByteArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.CharArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.DoubleArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.FloatArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.IntArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.LongArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.ObjectArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.ShortArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers.StringArraySerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigDecimalSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BigIntegerSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.BooleanSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.ByteSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CalendarSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CharSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CharsetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.ClassSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptyListSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptyMapSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptySetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonListSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonMapSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonSetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CurrencySerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.DateSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.DoubleSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.EnumSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.EnumSetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.FloatSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.IntSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.KryoSerializableSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.LocaleSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.LongSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.ShortSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.StringBufferSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.StringBuilderSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.StringSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.TimeZoneSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.TreeMapSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.TreeSetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.URLSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.VoidSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.esotericsoftware.kryo.serializers.GenericsResolver;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.esotericsoftware.kryo.serializers.OptionalSerializers;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializerConfig;
import com.esotericsoftware.kryo.serializers.TimeSerializers;
import com.esotericsoftware.kryo.util.DefaultClassResolver;
import com.esotericsoftware.kryo.util.DefaultStreamFactory;
import com.esotericsoftware.kryo.util.IdentityMap;
import com.esotericsoftware.kryo.util.IntArray;
import com.esotericsoftware.kryo.util.MapReferenceResolver;
import com.esotericsoftware.kryo.util.ObjectMap;
import com.esotericsoftware.kryo.util.Util;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

public class Kryo {
    public static final byte NOT_NULL = (byte) 1;
    private static final int NO_REF = -2;
    public static final byte NULL = (byte) 0;
    private static final int REF = -1;
    private boolean autoReset;
    private ClassLoader classLoader;
    private final ClassResolver classResolver;
    private ObjectMap context;
    private int copyDepth;
    private boolean copyReferences;
    private boolean copyShallow;
    private SerializerFactory defaultSerializer;
    private final ArrayList<DefaultSerializerEntry> defaultSerializers;
    private int depth;
    private FieldSerializerConfig fieldSerializerConfig;
    private GenericsResolver genericsResolver;
    private ObjectMap graphContext;
    private final int lowPriorityDefaultSerializerCount;
    private int maxDepth;
    private Object needsCopyReference;
    private int nextRegisterID;
    private IdentityMap originalToCopy;
    private Object readObject;
    private final IntArray readReferenceIds;
    private ReferenceResolver referenceResolver;
    private boolean references;
    private boolean registrationRequired;
    private InstantiatorStrategy strategy;
    private StreamFactory streamFactory;
    private TaggedFieldSerializerConfig taggedFieldSerializerConfig;
    private volatile Thread thread;
    private boolean warnUnregisteredClasses;

    public static class DefaultInstantiatorStrategy implements InstantiatorStrategy {
        private InstantiatorStrategy fallbackStrategy;

        public DefaultInstantiatorStrategy(InstantiatorStrategy fallbackStrategy) {
            this.fallbackStrategy = fallbackStrategy;
        }

        public void setFallbackInstantiatorStrategy(InstantiatorStrategy fallbackStrategy) {
            this.fallbackStrategy = fallbackStrategy;
        }

        public InstantiatorStrategy getFallbackInstantiatorStrategy() {
            return this.fallbackStrategy;
        }

        public ObjectInstantiator newInstantiatorOf(final Class type) {
            Constructor ctor;
            boolean isNonStaticMemberClass = true;
            if (!Util.isAndroid) {
                if (type.getEnclosingClass() == null || !type.isMemberClass() || Modifier.isStatic(type.getModifiers())) {
                    isNonStaticMemberClass = false;
                }
                if (!isNonStaticMemberClass) {
                    try {
                        final ConstructorAccess access = ConstructorAccess.get(type);
                        return new ObjectInstantiator() {
                            public Object newInstance() {
                                try {
                                    return access.newInstance();
                                } catch (Exception ex) {
                                    throw new KryoException("Error constructing instance of class: " + Util.className(type), ex);
                                }
                            }
                        };
                    } catch (Exception e) {
                    }
                }
            }
            try {
                ctor = type.getConstructor((Class[]) null);
            } catch (Exception e2) {
                ctor = type.getDeclaredConstructor((Class[]) null);
                ctor.setAccessible(true);
            }
            final Constructor constructor = ctor;
            try {
                return new ObjectInstantiator() {
                    public Object newInstance() {
                        try {
                            return constructor.newInstance(new Object[0]);
                        } catch (Exception ex) {
                            throw new KryoException("Error constructing instance of class: " + Util.className(type), ex);
                        }
                    }
                };
            } catch (Exception e3) {
                if (this.fallbackStrategy != null) {
                    return this.fallbackStrategy.newInstantiatorOf(type);
                }
                if (!type.isMemberClass() || Modifier.isStatic(type.getModifiers())) {
                    throw new KryoException("Class cannot be created (missing no-arg constructor): " + Util.className(type));
                }
                throw new KryoException("Class cannot be created (non-static member class): " + Util.className(type));
            }
        }
    }

    static final class DefaultSerializerEntry {
        final SerializerFactory serializerFactory;
        final Class type;

        DefaultSerializerEntry(Class type, SerializerFactory serializerFactory) {
            this.type = type;
            this.serializerFactory = serializerFactory;
        }
    }

    public <T> T copyShallow(T r6) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r5 = this;
        r4 = 1;
        r3 = 0;
        if (r6 != 0) goto L_0x0006;
    L_0x0004:
        r1 = 0;
    L_0x0005:
        return r1;
    L_0x0006:
        r2 = r5.copyDepth;
        r2 = r2 + 1;
        r5.copyDepth = r2;
        r5.copyShallow = r4;
        r2 = r5.originalToCopy;	 Catch:{ all -> 0x0073 }
        if (r2 != 0) goto L_0x0019;	 Catch:{ all -> 0x0073 }
    L_0x0012:
        r2 = new com.esotericsoftware.kryo.util.IdentityMap;	 Catch:{ all -> 0x0073 }
        r2.<init>();	 Catch:{ all -> 0x0073 }
        r5.originalToCopy = r2;	 Catch:{ all -> 0x0073 }
    L_0x0019:
        r2 = r5.originalToCopy;	 Catch:{ all -> 0x0073 }
        r1 = r2.get(r6);	 Catch:{ all -> 0x0073 }
        if (r1 == 0) goto L_0x002f;
    L_0x0021:
        r5.copyShallow = r3;
        r2 = r5.copyDepth;
        r2 = r2 + -1;
        r5.copyDepth = r2;
        if (r2 != 0) goto L_0x0005;
    L_0x002b:
        r5.reset();
        goto L_0x0005;
    L_0x002f:
        r2 = r5.copyReferences;	 Catch:{ all -> 0x0073 }
        if (r2 == 0) goto L_0x0035;	 Catch:{ all -> 0x0073 }
    L_0x0033:
        r5.needsCopyReference = r6;	 Catch:{ all -> 0x0073 }
    L_0x0035:
        r2 = r6 instanceof com.esotericsoftware.kryo.KryoCopyable;	 Catch:{ all -> 0x0073 }
        if (r2 == 0) goto L_0x0066;	 Catch:{ all -> 0x0073 }
    L_0x0039:
        r6 = (com.esotericsoftware.kryo.KryoCopyable) r6;	 Catch:{ all -> 0x0073 }
        r0 = r6.copy(r5);	 Catch:{ all -> 0x0073 }
    L_0x003f:
        r2 = r5.needsCopyReference;	 Catch:{ all -> 0x0073 }
        if (r2 == 0) goto L_0x0046;	 Catch:{ all -> 0x0073 }
    L_0x0043:
        r5.reference(r0);	 Catch:{ all -> 0x0073 }
    L_0x0046:
        r2 = com.esotericsoftware.minlog.Log.TRACE;	 Catch:{ all -> 0x0073 }
        if (r2 != 0) goto L_0x0052;	 Catch:{ all -> 0x0073 }
    L_0x004a:
        r2 = com.esotericsoftware.minlog.Log.DEBUG;	 Catch:{ all -> 0x0073 }
        if (r2 == 0) goto L_0x0057;	 Catch:{ all -> 0x0073 }
    L_0x004e:
        r2 = r5.copyDepth;	 Catch:{ all -> 0x0073 }
        if (r2 != r4) goto L_0x0057;	 Catch:{ all -> 0x0073 }
    L_0x0052:
        r2 = "Shallow copy";	 Catch:{ all -> 0x0073 }
        com.esotericsoftware.kryo.util.Util.log(r2, r0);	 Catch:{ all -> 0x0073 }
    L_0x0057:
        r5.copyShallow = r3;
        r2 = r5.copyDepth;
        r2 = r2 + -1;
        r5.copyDepth = r2;
        if (r2 != 0) goto L_0x0064;
    L_0x0061:
        r5.reset();
    L_0x0064:
        r1 = r0;
        goto L_0x0005;
    L_0x0066:
        r2 = r6.getClass();	 Catch:{ all -> 0x0073 }
        r2 = r5.getSerializer(r2);	 Catch:{ all -> 0x0073 }
        r0 = r2.copy(r5, r6);	 Catch:{ all -> 0x0073 }
        goto L_0x003f;
    L_0x0073:
        r2 = move-exception;
        r5.copyShallow = r3;
        r3 = r5.copyDepth;
        r3 = r3 + -1;
        r5.copyDepth = r3;
        if (r3 != 0) goto L_0x0081;
    L_0x007e:
        r5.reset();
    L_0x0081:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryo.Kryo.copyShallow(java.lang.Object):T");
    }

    public <T> T copyShallow(T r6, com.esotericsoftware.kryo.Serializer r7) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r5 = this;
        r4 = 1;
        r3 = 0;
        if (r6 != 0) goto L_0x0006;
    L_0x0004:
        r1 = 0;
    L_0x0005:
        return r1;
    L_0x0006:
        r2 = r5.copyDepth;
        r2 = r2 + 1;
        r5.copyDepth = r2;
        r5.copyShallow = r4;
        r2 = r5.originalToCopy;	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0019;	 Catch:{ all -> 0x006b }
    L_0x0012:
        r2 = new com.esotericsoftware.kryo.util.IdentityMap;	 Catch:{ all -> 0x006b }
        r2.<init>();	 Catch:{ all -> 0x006b }
        r5.originalToCopy = r2;	 Catch:{ all -> 0x006b }
    L_0x0019:
        r2 = r5.originalToCopy;	 Catch:{ all -> 0x006b }
        r1 = r2.get(r6);	 Catch:{ all -> 0x006b }
        if (r1 == 0) goto L_0x002f;
    L_0x0021:
        r5.copyShallow = r3;
        r2 = r5.copyDepth;
        r2 = r2 + -1;
        r5.copyDepth = r2;
        if (r2 != 0) goto L_0x0005;
    L_0x002b:
        r5.reset();
        goto L_0x0005;
    L_0x002f:
        r2 = r5.copyReferences;	 Catch:{ all -> 0x006b }
        if (r2 == 0) goto L_0x0035;	 Catch:{ all -> 0x006b }
    L_0x0033:
        r5.needsCopyReference = r6;	 Catch:{ all -> 0x006b }
    L_0x0035:
        r2 = r6 instanceof com.esotericsoftware.kryo.KryoCopyable;	 Catch:{ all -> 0x006b }
        if (r2 == 0) goto L_0x0066;	 Catch:{ all -> 0x006b }
    L_0x0039:
        r6 = (com.esotericsoftware.kryo.KryoCopyable) r6;	 Catch:{ all -> 0x006b }
        r0 = r6.copy(r5);	 Catch:{ all -> 0x006b }
    L_0x003f:
        r2 = r5.needsCopyReference;	 Catch:{ all -> 0x006b }
        if (r2 == 0) goto L_0x0046;	 Catch:{ all -> 0x006b }
    L_0x0043:
        r5.reference(r0);	 Catch:{ all -> 0x006b }
    L_0x0046:
        r2 = com.esotericsoftware.minlog.Log.TRACE;	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0052;	 Catch:{ all -> 0x006b }
    L_0x004a:
        r2 = com.esotericsoftware.minlog.Log.DEBUG;	 Catch:{ all -> 0x006b }
        if (r2 == 0) goto L_0x0057;	 Catch:{ all -> 0x006b }
    L_0x004e:
        r2 = r5.copyDepth;	 Catch:{ all -> 0x006b }
        if (r2 != r4) goto L_0x0057;	 Catch:{ all -> 0x006b }
    L_0x0052:
        r2 = "Shallow copy";	 Catch:{ all -> 0x006b }
        com.esotericsoftware.kryo.util.Util.log(r2, r0);	 Catch:{ all -> 0x006b }
    L_0x0057:
        r5.copyShallow = r3;
        r2 = r5.copyDepth;
        r2 = r2 + -1;
        r5.copyDepth = r2;
        if (r2 != 0) goto L_0x0064;
    L_0x0061:
        r5.reset();
    L_0x0064:
        r1 = r0;
        goto L_0x0005;
    L_0x0066:
        r0 = r7.copy(r5, r6);	 Catch:{ all -> 0x006b }
        goto L_0x003f;
    L_0x006b:
        r2 = move-exception;
        r5.copyShallow = r3;
        r3 = r5.copyDepth;
        r3 = r3 + -1;
        r5.copyDepth = r3;
        if (r3 != 0) goto L_0x0079;
    L_0x0076:
        r5.reset();
    L_0x0079:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryo.Kryo.copyShallow(java.lang.Object, com.esotericsoftware.kryo.Serializer):T");
    }

    public Kryo() {
        this(new DefaultClassResolver(), new MapReferenceResolver(), new DefaultStreamFactory());
    }

    public Kryo(ReferenceResolver referenceResolver) {
        this(new DefaultClassResolver(), referenceResolver, new DefaultStreamFactory());
    }

    public Kryo(ClassResolver classResolver, ReferenceResolver referenceResolver) {
        this(classResolver, referenceResolver, new DefaultStreamFactory());
    }

    public Kryo(ClassResolver classResolver, ReferenceResolver referenceResolver, StreamFactory streamFactory) {
        this.defaultSerializer = new ReflectionSerializerFactory(FieldSerializer.class);
        this.defaultSerializers = new ArrayList(33);
        this.classLoader = getClass().getClassLoader();
        this.strategy = new DefaultInstantiatorStrategy();
        this.maxDepth = Integer.MAX_VALUE;
        this.autoReset = true;
        this.readReferenceIds = new IntArray(0);
        this.copyReferences = true;
        this.genericsResolver = new GenericsResolver();
        this.fieldSerializerConfig = new FieldSerializerConfig();
        this.taggedFieldSerializerConfig = new TaggedFieldSerializerConfig();
        if (classResolver == null) {
            throw new IllegalArgumentException("classResolver cannot be null.");
        }
        this.classResolver = classResolver;
        classResolver.setKryo(this);
        this.streamFactory = streamFactory;
        streamFactory.setKryo(this);
        this.referenceResolver = referenceResolver;
        if (referenceResolver != null) {
            referenceResolver.setKryo(this);
            this.references = true;
        }
        addDefaultSerializer(byte[].class, ByteArraySerializer.class);
        addDefaultSerializer(char[].class, CharArraySerializer.class);
        addDefaultSerializer(short[].class, ShortArraySerializer.class);
        addDefaultSerializer(int[].class, IntArraySerializer.class);
        addDefaultSerializer(long[].class, LongArraySerializer.class);
        addDefaultSerializer(float[].class, FloatArraySerializer.class);
        addDefaultSerializer(double[].class, DoubleArraySerializer.class);
        addDefaultSerializer(boolean[].class, BooleanArraySerializer.class);
        addDefaultSerializer(String[].class, StringArraySerializer.class);
        addDefaultSerializer(Object[].class, ObjectArraySerializer.class);
        addDefaultSerializer(KryoSerializable.class, KryoSerializableSerializer.class);
        addDefaultSerializer(BigInteger.class, BigIntegerSerializer.class);
        addDefaultSerializer(BigDecimal.class, BigDecimalSerializer.class);
        addDefaultSerializer(Class.class, ClassSerializer.class);
        addDefaultSerializer(Date.class, DateSerializer.class);
        addDefaultSerializer(Enum.class, EnumSerializer.class);
        addDefaultSerializer(EnumSet.class, EnumSetSerializer.class);
        addDefaultSerializer(Currency.class, CurrencySerializer.class);
        addDefaultSerializer(StringBuffer.class, StringBufferSerializer.class);
        addDefaultSerializer(StringBuilder.class, StringBuilderSerializer.class);
        addDefaultSerializer(Collections.EMPTY_LIST.getClass(), CollectionsEmptyListSerializer.class);
        addDefaultSerializer(Collections.EMPTY_MAP.getClass(), CollectionsEmptyMapSerializer.class);
        addDefaultSerializer(Collections.EMPTY_SET.getClass(), CollectionsEmptySetSerializer.class);
        addDefaultSerializer(Collections.singletonList(null).getClass(), CollectionsSingletonListSerializer.class);
        addDefaultSerializer(Collections.singletonMap(null, null).getClass(), CollectionsSingletonMapSerializer.class);
        addDefaultSerializer(Collections.singleton(null).getClass(), CollectionsSingletonSetSerializer.class);
        addDefaultSerializer(TreeSet.class, TreeSetSerializer.class);
        addDefaultSerializer(Collection.class, CollectionSerializer.class);
        addDefaultSerializer(TreeMap.class, TreeMapSerializer.class);
        addDefaultSerializer(Map.class, MapSerializer.class);
        addDefaultSerializer(TimeZone.class, TimeZoneSerializer.class);
        addDefaultSerializer(Calendar.class, CalendarSerializer.class);
        addDefaultSerializer(Locale.class, LocaleSerializer.class);
        addDefaultSerializer(Charset.class, CharsetSerializer.class);
        addDefaultSerializer(URL.class, URLSerializer.class);
        OptionalSerializers.addDefaultSerializers(this);
        TimeSerializers.addDefaultSerializers(this);
        this.lowPriorityDefaultSerializerCount = this.defaultSerializers.size();
        register(Integer.TYPE, new IntSerializer());
        register(String.class, new StringSerializer());
        register(Float.TYPE, new FloatSerializer());
        register(Boolean.TYPE, new BooleanSerializer());
        register(Byte.TYPE, new ByteSerializer());
        register(Character.TYPE, new CharSerializer());
        register(Short.TYPE, new ShortSerializer());
        register(Long.TYPE, new LongSerializer());
        register(Double.TYPE, new DoubleSerializer());
        register(Void.TYPE, new VoidSerializer());
    }

    public void setDefaultSerializer(SerializerFactory serializer) {
        if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        }
        this.defaultSerializer = serializer;
    }

    public void setDefaultSerializer(Class<? extends Serializer> serializer) {
        if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        }
        this.defaultSerializer = new ReflectionSerializerFactory(serializer);
    }

    public void addDefaultSerializer(Class type, Serializer serializer) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            this.defaultSerializers.add(this.defaultSerializers.size() - this.lowPriorityDefaultSerializerCount, new DefaultSerializerEntry(type, new PseudoSerializerFactory(serializer)));
        }
    }

    public void addDefaultSerializer(Class type, SerializerFactory serializerFactory) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializerFactory == null) {
            throw new IllegalArgumentException("serializerFactory cannot be null.");
        } else {
            this.defaultSerializers.add(this.defaultSerializers.size() - this.lowPriorityDefaultSerializerCount, new DefaultSerializerEntry(type, serializerFactory));
        }
    }

    public void addDefaultSerializer(Class type, Class<? extends Serializer> serializerClass) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializerClass == null) {
            throw new IllegalArgumentException("serializerClass cannot be null.");
        } else {
            this.defaultSerializers.add(this.defaultSerializers.size() - this.lowPriorityDefaultSerializerCount, new DefaultSerializerEntry(type, new ReflectionSerializerFactory(serializerClass)));
        }
    }

    public Serializer getDefaultSerializer(Class type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        Serializer serializerForAnnotation = getDefaultSerializerForAnnotatedType(type);
        if (serializerForAnnotation != null) {
            return serializerForAnnotation;
        }
        int n = this.defaultSerializers.size();
        for (int i = 0; i < n; i++) {
            DefaultSerializerEntry entry = (DefaultSerializerEntry) this.defaultSerializers.get(i);
            if (entry.type.isAssignableFrom(type)) {
                return entry.serializerFactory.makeSerializer(this, type);
            }
        }
        return newDefaultSerializer(type);
    }

    protected Serializer getDefaultSerializerForAnnotatedType(Class type) {
        if (type.isAnnotationPresent(DefaultSerializer.class)) {
            return ReflectionSerializerFactory.makeSerializer(this, ((DefaultSerializer) type.getAnnotation(DefaultSerializer.class)).value(), type);
        }
        return null;
    }

    protected Serializer newDefaultSerializer(Class type) {
        return this.defaultSerializer.makeSerializer(this, type);
    }

    public Registration register(Class type) {
        Registration registration = this.classResolver.getRegistration(type);
        return registration != null ? registration : register(type, getDefaultSerializer(type));
    }

    public Registration register(Class type, int id) {
        Registration registration = this.classResolver.getRegistration(type);
        return registration != null ? registration : register(type, getDefaultSerializer(type), id);
    }

    public Registration register(Class type, Serializer serializer) {
        Registration registration = this.classResolver.getRegistration(type);
        if (registration == null) {
            return this.classResolver.register(new Registration(type, serializer, getNextRegistrationId()));
        }
        registration.setSerializer(serializer);
        return registration;
    }

    public Registration register(Class type, Serializer serializer, int id) {
        if (id >= 0) {
            return register(new Registration(type, serializer, id));
        }
        throw new IllegalArgumentException("id must be >= 0: " + id);
    }

    public Registration register(Registration registration) {
        int id = registration.getId();
        if (id < 0) {
            throw new IllegalArgumentException("id must be > 0: " + id);
        }
        Registration existing = getRegistration(registration.getId());
        if (!(!Log.DEBUG || existing == null || existing.getType() == registration.getType())) {
            Log.debug("An existing registration with a different type already uses ID: " + registration.getId() + "\nExisting registration: " + existing + "\nis now overwritten with: " + registration);
        }
        return this.classResolver.register(registration);
    }

    public int getNextRegistrationId() {
        while (this.nextRegisterID != -2) {
            if (this.classResolver.getRegistration(this.nextRegisterID) == null) {
                return this.nextRegisterID;
            }
            this.nextRegisterID++;
        }
        throw new KryoException("No registration IDs are available.");
    }

    public Registration getRegistration(Class type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        Registration registration = this.classResolver.getRegistration(type);
        if (registration != null) {
            return registration;
        }
        if (Proxy.isProxyClass(type)) {
            registration = getRegistration(InvocationHandler.class);
        } else if (!type.isEnum() && Enum.class.isAssignableFrom(type)) {
            registration = getRegistration(type.getEnclosingClass());
        } else if (EnumSet.class.isAssignableFrom(type)) {
            registration = this.classResolver.getRegistration(EnumSet.class);
        } else if (isClosure(type)) {
            registration = this.classResolver.getRegistration(Closure.class);
        }
        if (registration != null) {
            return registration;
        }
        if (this.registrationRequired) {
            throw new IllegalArgumentException(unregisteredClassMessage(type));
        }
        if (this.warnUnregisteredClasses) {
            Log.warn(unregisteredClassMessage(type));
        }
        return this.classResolver.registerImplicit(type);
    }

    protected String unregisteredClassMessage(Class type) {
        return "Class is not registered: " + Util.className(type) + "\nNote: To register this class use: kryo.register(" + Util.className(type) + ".class);";
    }

    public Registration getRegistration(int classID) {
        return this.classResolver.getRegistration(classID);
    }

    public Serializer getSerializer(Class type) {
        return getRegistration(type).getSerializer();
    }

    public Registration writeClass(Output output, Class type) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        }
        try {
            Registration writeClass = this.classResolver.writeClass(output, type);
            return writeClass;
        } finally {
            if (this.depth == 0 && this.autoReset) {
                reset();
            }
        }
    }

    public void writeObject(Output output, Object object) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        } else if (object == null) {
            throw new IllegalArgumentException("object cannot be null.");
        } else {
            beginObject();
            try {
                if (this.references && writeReferenceOrNull(output, object, false)) {
                    getRegistration(object.getClass()).getSerializer().setGenerics(this, null);
                    return;
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Write", object);
                }
                getRegistration(object.getClass()).getSerializer().write(this, output, object);
                int i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public void writeObject(Output output, Object object, Serializer serializer) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        } else if (object == null) {
            throw new IllegalArgumentException("object cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            try {
                if (this.references && writeReferenceOrNull(output, object, false)) {
                    serializer.setGenerics(this, null);
                    return;
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Write", object);
                }
                serializer.write(this, output, object);
                int i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public void writeObjectOrNull(Output output, Object object, Class type) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        }
        beginObject();
        try {
            int i;
            Serializer serializer = getRegistration(type).getSerializer();
            if (this.references) {
                if (writeReferenceOrNull(output, object, true)) {
                    serializer.setGenerics(this, null);
                    return;
                }
            } else if (!serializer.getAcceptsNull()) {
                if (object == null) {
                    if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                        Util.log("Write", object);
                    }
                    output.writeByte((byte) 0);
                    i = this.depth - 1;
                    this.depth = i;
                    if (i == 0 && this.autoReset) {
                        reset();
                        return;
                    }
                    return;
                }
                output.writeByte((byte) 1);
            }
            if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                Util.log("Write", object);
            }
            serializer.write(this, output, object);
            i = this.depth - 1;
            this.depth = i;
            if (i == 0 && this.autoReset) {
                reset();
            }
        } finally {
            int i2 = this.depth - 1;
            this.depth = i2;
            if (i2 == 0 && this.autoReset) {
                reset();
            }
        }
    }

    public void writeObjectOrNull(Output output, Object object, Serializer serializer) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            try {
                int i;
                if (this.references) {
                    if (writeReferenceOrNull(output, object, true)) {
                        serializer.setGenerics(this, null);
                        return;
                    }
                } else if (!serializer.getAcceptsNull()) {
                    if (object == null) {
                        if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                            Util.log("Write", null);
                        }
                        output.writeByte((byte) 0);
                        i = this.depth - 1;
                        this.depth = i;
                        if (i == 0 && this.autoReset) {
                            reset();
                            return;
                        }
                        return;
                    }
                    output.writeByte((byte) 1);
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Write", object);
                }
                serializer.write(this, output, object);
                i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public void writeClassAndObject(Output output, Object object) {
        if (output == null) {
            throw new IllegalArgumentException("output cannot be null.");
        }
        beginObject();
        if (object == null) {
            try {
                writeClass(output, null);
            } finally {
                int i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            }
        } else {
            Registration registration = writeClass(output, object.getClass());
            int i2;
            if (this.references && writeReferenceOrNull(output, object, false)) {
                registration.getSerializer().setGenerics(this, null);
                i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                    return;
                }
                return;
            }
            if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                Util.log("Write", object);
            }
            registration.getSerializer().write(this, output, object);
            i2 = this.depth - 1;
            this.depth = i2;
            if (i2 == 0 && this.autoReset) {
                reset();
            }
        }
    }

    boolean writeReferenceOrNull(Output output, Object object, boolean mayBeNull) {
        if (object == null) {
            if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                Util.log("Write", null);
            }
            output.writeVarInt(0, true);
            return true;
        } else if (this.referenceResolver.useReferences(object.getClass())) {
            int id = this.referenceResolver.getWrittenId(object);
            if (id != -1) {
                if (Log.DEBUG) {
                    Log.debug("kryo", "Write object reference " + id + ": " + Util.string(object));
                }
                output.writeVarInt(id + 2, true);
                return true;
            }
            id = this.referenceResolver.addWrittenObject(object);
            output.writeVarInt(1, true);
            if (Log.TRACE) {
                Log.trace("kryo", "Write initial object reference " + id + ": " + Util.string(object));
            }
            return false;
        } else {
            if (mayBeNull) {
                output.writeVarInt(1, true);
            }
            return false;
        }
    }

    public Registration readClass(Input input) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        }
        try {
            Registration readClass = this.classResolver.readClass(input);
            return readClass;
        } finally {
            if (this.depth == 0 && this.autoReset) {
                reset();
            }
        }
    }

    public <T> T readObject(Input input, Class<T> type) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else {
            beginObject();
            try {
                T t;
                if (this.references) {
                    int stackSize = readReferenceOrNull(input, type, false);
                    if (stackSize == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = getRegistration((Class) type).getSerializer().read(this, input, type);
                    if (stackSize == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else {
                    t = getRegistration((Class) type).getSerializer().read(this, input, type);
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Read", t);
                }
                int i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public <T> T readObject(Input input, Class<T> type, Serializer serializer) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            try {
                T t;
                if (this.references) {
                    int stackSize = readReferenceOrNull(input, type, false);
                    if (stackSize == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = serializer.read(this, input, type);
                    if (stackSize == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else {
                    t = serializer.read(this, input, type);
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Read", t);
                }
                int i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public <T> T readObjectOrNull(Input input, Class<T> type) {
        T t = null;
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else {
            beginObject();
            try {
                int i;
                if (this.references) {
                    int stackSize = readReferenceOrNull(input, type, true);
                    if (stackSize == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = getRegistration((Class) type).getSerializer().read(this, input, type);
                    if (stackSize == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else {
                    Serializer serializer = getRegistration((Class) type).getSerializer();
                    if (serializer.getAcceptsNull() || input.readByte() != (byte) 0) {
                        t = serializer.read(this, input, type);
                    } else {
                        if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                            Util.log("Read", null);
                        }
                        i = this.depth - 1;
                        this.depth = i;
                        if (i == 0 && this.autoReset) {
                            reset();
                        }
                        return t;
                    }
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Read", t);
                }
                i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public <T> T readObjectOrNull(Input input, Class<T> type, Serializer serializer) {
        T t = null;
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        } else if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            beginObject();
            try {
                int i;
                if (this.references) {
                    int stackSize = readReferenceOrNull(input, type, true);
                    if (stackSize == -1) {
                        t = this.readObject;
                        return t;
                    }
                    t = serializer.read(this, input, type);
                    if (stackSize == this.readReferenceIds.size) {
                        reference(t);
                    }
                } else if (serializer.getAcceptsNull() || input.readByte() != (byte) 0) {
                    t = serializer.read(this, input, type);
                } else {
                    if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                        Util.log("Read", null);
                    }
                    i = this.depth - 1;
                    this.depth = i;
                    if (i == 0 && this.autoReset) {
                        reset();
                    }
                    return t;
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Read", t);
                }
                i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
                return t;
            } finally {
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0 && this.autoReset) {
                    reset();
                }
            }
        }
    }

    public Object readClassAndObject(Input input) {
        Object obj = null;
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        }
        beginObject();
        try {
            Registration registration = readClass(input);
            if (registration != null) {
                int i;
                Class type = registration.getType();
                if (this.references) {
                    registration.getSerializer().setGenerics(this, null);
                    int stackSize = readReferenceOrNull(input, type, false);
                    if (stackSize == -1) {
                        obj = this.readObject;
                        i = this.depth - 1;
                        this.depth = i;
                        if (i == 0 && this.autoReset) {
                            reset();
                        }
                    } else {
                        obj = registration.getSerializer().read(this, input, type);
                        if (stackSize == this.readReferenceIds.size) {
                            reference(obj);
                        }
                    }
                } else {
                    obj = registration.getSerializer().read(this, input, type);
                }
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Read", obj);
                }
                i = this.depth - 1;
                this.depth = i;
                if (i == 0 && this.autoReset) {
                    reset();
                }
            }
            return obj;
        } finally {
            int i2 = this.depth - 1;
            this.depth = i2;
            if (i2 == 0 && this.autoReset) {
                reset();
            }
        }
    }

    int readReferenceOrNull(Input input, Class type, boolean mayBeNull) {
        int id;
        if (type.isPrimitive()) {
            type = Util.getWrapperClass(type);
        }
        boolean referencesSupported = this.referenceResolver.useReferences(type);
        if (mayBeNull) {
            id = input.readVarInt(true);
            if (id == 0) {
                if (Log.TRACE || (Log.DEBUG && this.depth == 1)) {
                    Util.log("Read", null);
                }
                this.readObject = null;
                return -1;
            } else if (!referencesSupported) {
                this.readReferenceIds.add(-2);
                return this.readReferenceIds.size;
            }
        } else if (referencesSupported) {
            id = input.readVarInt(true);
        } else {
            this.readReferenceIds.add(-2);
            return this.readReferenceIds.size;
        }
        if (id == 1) {
            id = this.referenceResolver.nextReadId(type);
            if (Log.TRACE) {
                Log.trace("kryo", "Read initial object reference " + id + ": " + Util.className(type));
            }
            this.readReferenceIds.add(id);
            return this.readReferenceIds.size;
        }
        id -= 2;
        this.readObject = this.referenceResolver.getReadObject(type, id);
        if (!Log.DEBUG) {
            return -1;
        }
        Log.debug("kryo", "Read object reference " + id + ": " + Util.string(this.readObject));
        return -1;
    }

    public void reference(Object object) {
        if (this.copyDepth > 0) {
            if (this.needsCopyReference == null) {
                return;
            }
            if (object == null) {
                throw new IllegalArgumentException("object cannot be null.");
            }
            this.originalToCopy.put(this.needsCopyReference, object);
            this.needsCopyReference = null;
        } else if (this.references && object != null) {
            int id = this.readReferenceIds.pop();
            if (id != -2) {
                this.referenceResolver.setReadObject(id, object);
            }
        }
    }

    public void reset() {
        this.depth = 0;
        if (this.graphContext != null) {
            this.graphContext.clear();
        }
        this.classResolver.reset();
        if (this.references) {
            this.referenceResolver.reset();
            this.readObject = null;
        }
        this.copyDepth = 0;
        if (this.originalToCopy != null) {
            this.originalToCopy.clear(2048);
        }
        if (Log.TRACE) {
            Log.trace("kryo", "Object graph complete.");
        }
    }

    public <T> T copy(T object) {
        if (object == null) {
            return null;
        }
        if (this.copyShallow) {
            return object;
        }
        this.copyDepth++;
        try {
            if (this.originalToCopy == null) {
                this.originalToCopy = new IdentityMap();
            }
            T existingCopy = this.originalToCopy.get(object);
            if (existingCopy != null) {
                return existingCopy;
            }
            T copy;
            if (this.copyReferences) {
                this.needsCopyReference = object;
            }
            if (object instanceof KryoCopyable) {
                copy = ((KryoCopyable) object).copy(this);
            } else {
                copy = getSerializer(object.getClass()).copy(this, object);
            }
            if (this.needsCopyReference != null) {
                reference(copy);
            }
            if (Log.TRACE || (Log.DEBUG && this.copyDepth == 1)) {
                Util.log("Copy", copy);
            }
            int i = this.copyDepth - 1;
            this.copyDepth = i;
            if (i == 0) {
                reset();
            }
            return copy;
        } finally {
            int i2 = this.copyDepth - 1;
            this.copyDepth = i2;
            if (i2 == 0) {
                reset();
            }
        }
    }

    public <T> T copy(T object, Serializer serializer) {
        if (object == null) {
            return null;
        }
        if (this.copyShallow) {
            return object;
        }
        this.copyDepth++;
        try {
            if (this.originalToCopy == null) {
                this.originalToCopy = new IdentityMap();
            }
            T existingCopy = this.originalToCopy.get(object);
            if (existingCopy != null) {
                return existingCopy;
            }
            T copy;
            if (this.copyReferences) {
                this.needsCopyReference = object;
            }
            if (object instanceof KryoCopyable) {
                copy = ((KryoCopyable) object).copy(this);
            } else {
                copy = serializer.copy(this, object);
            }
            if (this.needsCopyReference != null) {
                reference(copy);
            }
            if (Log.TRACE || (Log.DEBUG && this.copyDepth == 1)) {
                Util.log("Copy", copy);
            }
            int i = this.copyDepth - 1;
            this.copyDepth = i;
            if (i == 0) {
                reset();
            }
            return copy;
        } finally {
            int i2 = this.copyDepth - 1;
            this.copyDepth = i2;
            if (i2 == 0) {
                reset();
            }
        }
    }

    private void beginObject() {
        if (Log.DEBUG) {
            if (this.depth == 0) {
                this.thread = Thread.currentThread();
            } else if (this.thread != Thread.currentThread()) {
                throw new ConcurrentModificationException("Kryo must not be accessed concurrently by multiple threads.");
            }
        }
        if (this.depth == this.maxDepth) {
            throw new KryoException("Max depth exceeded: " + this.depth);
        }
        this.depth++;
    }

    public ClassResolver getClassResolver() {
        return this.classResolver;
    }

    public ReferenceResolver getReferenceResolver() {
        return this.referenceResolver;
    }

    public void setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader cannot be null.");
        }
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
        if (Log.TRACE) {
            Log.trace("kryo", "Registration required: " + registrationRequired);
        }
    }

    public boolean isRegistrationRequired() {
        return this.registrationRequired;
    }

    public void setWarnUnregisteredClasses(boolean warnUnregisteredClasses) {
        this.warnUnregisteredClasses = warnUnregisteredClasses;
        if (Log.TRACE) {
            Log.trace("kryo", "Warn unregistered classes: " + warnUnregisteredClasses);
        }
    }

    public boolean isWarnUnregisteredClasses() {
        return this.warnUnregisteredClasses;
    }

    public boolean setReferences(boolean references) {
        if (references == this.references) {
            return references;
        }
        this.references = references;
        if (references && this.referenceResolver == null) {
            this.referenceResolver = new MapReferenceResolver();
        }
        if (Log.TRACE) {
            Log.trace("kryo", "References: " + references);
        }
        return !references;
    }

    public void setCopyReferences(boolean copyReferences) {
        this.copyReferences = copyReferences;
    }

    public FieldSerializerConfig getFieldSerializerConfig() {
        return this.fieldSerializerConfig;
    }

    public TaggedFieldSerializerConfig getTaggedFieldSerializerConfig() {
        return this.taggedFieldSerializerConfig;
    }

    public void setReferenceResolver(ReferenceResolver referenceResolver) {
        if (referenceResolver == null) {
            throw new IllegalArgumentException("referenceResolver cannot be null.");
        }
        this.references = true;
        this.referenceResolver = referenceResolver;
        if (Log.TRACE) {
            Log.trace("kryo", "Reference resolver: " + referenceResolver.getClass().getName());
        }
    }

    public boolean getReferences() {
        return this.references;
    }

    public void setInstantiatorStrategy(InstantiatorStrategy strategy) {
        this.strategy = strategy;
    }

    public InstantiatorStrategy getInstantiatorStrategy() {
        return this.strategy;
    }

    protected ObjectInstantiator newInstantiator(Class type) {
        return this.strategy.newInstantiatorOf(type);
    }

    public <T> T newInstance(Class<T> type) {
        Registration registration = getRegistration((Class) type);
        ObjectInstantiator instantiator = registration.getInstantiator();
        if (instantiator == null) {
            instantiator = newInstantiator(type);
            registration.setInstantiator(instantiator);
        }
        return instantiator.newInstance();
    }

    public ObjectMap getContext() {
        if (this.context == null) {
            this.context = new ObjectMap();
        }
        return this.context;
    }

    public ObjectMap getGraphContext() {
        if (this.graphContext == null) {
            this.graphContext = new ObjectMap();
        }
        return this.graphContext;
    }

    public int getDepth() {
        return this.depth;
    }

    public IdentityMap getOriginalToCopyMap() {
        return this.originalToCopy;
    }

    public void setAutoReset(boolean autoReset) {
        this.autoReset = autoReset;
    }

    public void setMaxDepth(int maxDepth) {
        if (maxDepth <= 0) {
            throw new IllegalArgumentException("maxDepth must be > 0.");
        }
        this.maxDepth = maxDepth;
    }

    public boolean isFinal(Class type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (type.isArray()) {
            return Modifier.isFinal(Util.getElementClass(type).getModifiers());
        } else {
            return Modifier.isFinal(type.getModifiers());
        }
    }

    protected boolean isClosure(Class type) {
        if (type != null) {
            return type.getName().indexOf(47) >= 0;
        } else {
            throw new IllegalArgumentException("type cannot be null.");
        }
    }

    public GenericsResolver getGenericsResolver() {
        return this.genericsResolver;
    }

    public StreamFactory getStreamFactory() {
        return this.streamFactory;
    }

    public void setStreamFactory(StreamFactory streamFactory) {
        this.streamFactory = streamFactory;
    }

    @Deprecated
    public void setAsmEnabled(boolean flag) {
        this.fieldSerializerConfig.setUseAsm(flag);
    }

    @Deprecated
    public boolean getAsmEnabled() {
        return this.fieldSerializerConfig.isUseAsm();
    }
}
