package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.NotNull;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializerUnsafeUtil.Factory;
import com.esotericsoftware.kryo.util.IntArray;
import com.esotericsoftware.kryo.util.ObjectMap;
import com.esotericsoftware.kryo.util.Util;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.reflectasm.FieldAccess;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class FieldSerializer<T> extends Serializer<T> implements Comparator<CachedField> {
    static CachedFieldFactory asmFieldFactory;
    static CachedFieldFactory objectFieldFactory;
    static Method sortFieldsByOffsetMethod;
    static boolean unsafeAvailable = true;
    static CachedFieldFactory unsafeFieldFactory;
    static Class<?> unsafeUtilClass;
    Object access;
    private FieldSerializerAnnotationsUtil annotationsUtil;
    final Class componentType;
    protected final FieldSerializerConfig config;
    private CachedField[] fields;
    private Class[] generics;
    private Generics genericsScope;
    private FieldSerializerGenericsUtil genericsUtil;
    private boolean hasObjectFields;
    final Kryo kryo;
    protected HashSet<CachedField> removedFields;
    private CachedField[] transientFields;
    final Class type;
    final TypeVariable[] typeParameters;
    private FieldSerializerUnsafeUtil unsafeUtil;
    private boolean useMemRegions;
    private boolean varIntsEnabled;

    public static abstract class CachedField<X> {
        FieldAccess access;
        int accessIndex = -1;
        boolean canBeNull;
        Field field;
        long offset = -1;
        Serializer serializer;
        Class valueClass;
        boolean varIntsEnabled = true;

        public abstract void copy(Object obj, Object obj2);

        public abstract void read(Input input, Object obj);

        public abstract void write(Output output, Object obj);

        public void setClass(Class valueClass) {
            this.valueClass = valueClass;
            this.serializer = null;
        }

        public void setClass(Class valueClass, Serializer serializer) {
            this.valueClass = valueClass;
            this.serializer = serializer;
        }

        public void setSerializer(Serializer serializer) {
            this.serializer = serializer;
        }

        public Serializer getSerializer() {
            return this.serializer;
        }

        public void setCanBeNull(boolean canBeNull) {
            this.canBeNull = canBeNull;
        }

        public Field getField() {
            return this.field;
        }

        public String toString() {
            return this.field.getName();
        }
    }

    public interface CachedFieldFactory {
        CachedField createCachedField(Class cls, Field field, FieldSerializer fieldSerializer);
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Bind {
        Class<? extends Serializer> value();
    }

    public interface CachedFieldNameStrategy {
        public static final CachedFieldNameStrategy DEFAULT = new C04731();
        public static final CachedFieldNameStrategy EXTENDED = new C04742();

        static class C04731 implements CachedFieldNameStrategy {
            C04731() {
            }

            public String getName(CachedField cachedField) {
                return cachedField.field.getName();
            }
        }

        static class C04742 implements CachedFieldNameStrategy {
            C04742() {
            }

            public String getName(CachedField cachedField) {
                return cachedField.field.getDeclaringClass().getSimpleName() + "." + cachedField.field.getName();
            }
        }

        String getName(CachedField cachedField);
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Optional {
        String value();
    }

    static {
        try {
            unsafeUtilClass = FieldSerializer.class.getClassLoader().loadClass("com.esotericsoftware.kryo.util.UnsafeUtil");
            Method unsafeMethod = unsafeUtilClass.getMethod("unsafe", new Class[0]);
            sortFieldsByOffsetMethod = unsafeUtilClass.getMethod("sortFieldsByOffset", new Class[]{List.class});
            if (unsafeMethod.invoke(null, new Object[0]) != null) {
            }
        } catch (Throwable th) {
            if (Log.TRACE) {
                Log.trace("kryo", "sun.misc.Unsafe is unavailable.");
            }
        }
    }

    public FieldSerializer(Kryo kryo, Class type) {
        this(kryo, type, null);
    }

    public FieldSerializer(Kryo kryo, Class type, Class[] generics) {
        this(kryo, type, generics, kryo.getFieldSerializerConfig().clone());
    }

    protected FieldSerializer(Kryo kryo, Class type, Class[] generics, FieldSerializerConfig config) {
        this.fields = new CachedField[0];
        this.transientFields = new CachedField[0];
        this.removedFields = new HashSet();
        this.useMemRegions = false;
        this.hasObjectFields = false;
        this.varIntsEnabled = true;
        if (Log.TRACE) {
            Log.trace("kryo", "Optimize ints: " + this.varIntsEnabled);
        }
        this.config = config;
        this.kryo = kryo;
        this.type = type;
        this.generics = generics;
        this.typeParameters = type.getTypeParameters();
        if (this.typeParameters == null || this.typeParameters.length == 0) {
            this.componentType = type.getComponentType();
        } else {
            this.componentType = null;
        }
        this.genericsUtil = new FieldSerializerGenericsUtil(this);
        this.unsafeUtil = Factory.getInstance(this);
        this.annotationsUtil = new FieldSerializerAnnotationsUtil(this);
        rebuildCachedFields();
    }

    protected void rebuildCachedFields() {
        rebuildCachedFields(false);
    }

    protected void rebuildCachedFields(boolean minorRebuild) {
        if (Log.TRACE && this.generics != null) {
            Log.trace("kryo", "Generic type parameters: " + Arrays.toString(this.generics));
        }
        if (this.type.isInterface()) {
            this.fields = new CachedField[0];
            return;
        }
        List<Field> validFields;
        List<Field> validTransientFields;
        this.hasObjectFields = false;
        if (this.config.isOptimizedGenerics()) {
            this.genericsScope = this.genericsUtil.buildGenericsScope(this.type, this.generics);
            if (this.genericsScope != null) {
                this.kryo.getGenericsResolver().pushScope(this.type, this.genericsScope);
            }
        }
        IntArray useAsm = new IntArray();
        if (minorRebuild) {
            validFields = buildValidFieldsFromCachedFields(this.fields, useAsm);
            validTransientFields = buildValidFieldsFromCachedFields(this.transientFields, useAsm);
        } else {
            List<Field> allFields = new ArrayList();
            for (Class nextClass = this.type; nextClass != Object.class; nextClass = nextClass.getSuperclass()) {
                Field[] declaredFields = nextClass.getDeclaredFields();
                if (declaredFields != null) {
                    for (Field f : declaredFields) {
                        if (!Modifier.isStatic(f.getModifiers())) {
                            allFields.add(f);
                        }
                    }
                }
            }
            ObjectMap context = this.kryo.getContext();
            if (this.useMemRegions && !this.config.isUseAsm() && unsafeAvailable) {
                try {
                    allFields = Arrays.asList((Field[]) sortFieldsByOffsetMethod.invoke(null, new Object[]{allFields}));
                } catch (Exception e) {
                    throw new RuntimeException("Cannot invoke UnsafeUtil.sortFieldsByOffset()", e);
                }
            }
            validFields = buildValidFields(false, allFields, context, useAsm);
            validTransientFields = buildValidFields(true, allFields, context, useAsm);
            if (this.config.isUseAsm() && !Util.isAndroid && Modifier.isPublic(this.type.getModifiers()) && useAsm.indexOf(1) != -1) {
                try {
                    this.access = FieldAccess.get(this.type);
                } catch (RuntimeException e2) {
                }
            }
        }
        List<CachedField> cachedFields = new ArrayList(validFields.size());
        List<CachedField> cachedTransientFields = new ArrayList(validTransientFields.size());
        createCachedFields(useAsm, validFields, cachedFields, 0);
        createCachedFields(useAsm, validTransientFields, cachedTransientFields, validFields.size());
        Collections.sort(cachedFields, this);
        this.fields = (CachedField[]) cachedFields.toArray(new CachedField[cachedFields.size()]);
        Collections.sort(cachedTransientFields, this);
        this.transientFields = (CachedField[]) cachedTransientFields.toArray(new CachedField[cachedTransientFields.size()]);
        initializeCachedFields();
        if (this.genericsScope != null) {
            this.kryo.getGenericsResolver().popScope();
        }
        if (!minorRebuild) {
            Iterator it = this.removedFields.iterator();
            while (it.hasNext()) {
                removeField((CachedField) it.next());
            }
        }
        this.annotationsUtil.processAnnotatedFields(this);
    }

    private List<Field> buildValidFieldsFromCachedFields(CachedField[] cachedFields, IntArray useAsm) {
        ArrayList<Field> fields = new ArrayList(cachedFields.length);
        for (CachedField f : cachedFields) {
            int i;
            fields.add(f.field);
            if (f.accessIndex > -1) {
                i = 1;
            } else {
                i = 0;
            }
            useAsm.add(i);
        }
        return fields;
    }

    private List<Field> buildValidFields(boolean transientFields, List<Field> allFields, ObjectMap context, IntArray useAsm) {
        List<Field> result = new ArrayList(allFields.size());
        int n = allFields.size();
        for (int i = 0; i < n; i++) {
            Field field = (Field) allFields.get(i);
            int modifiers = field.getModifiers();
            if (!(Modifier.isTransient(modifiers) != transientFields || Modifier.isStatic(modifiers) || (field.isSynthetic() && this.config.isIgnoreSyntheticFields()))) {
                if (!field.isAccessible()) {
                    if (this.config.isSetFieldsAsAccessible()) {
                        try {
                            field.setAccessible(true);
                        } catch (AccessControlException e) {
                        }
                    }
                }
                Optional optional = (Optional) field.getAnnotation(Optional.class);
                if (optional == null || context.containsKey(optional.value())) {
                    result.add(field);
                    int i2 = (!Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers) && Modifier.isPublic(field.getType().getModifiers())) ? 1 : 0;
                    useAsm.add(i2);
                }
            }
        }
        return result;
    }

    private void createCachedFields(IntArray useAsm, List<Field> validFields, List<CachedField> cachedFields, int baseIndex) {
        if (this.config.isUseAsm() || !this.useMemRegions) {
            int i = 0;
            int n = validFields.size();
            while (i < n) {
                Field field = (Field) validFields.get(i);
                int accessIndex = -1;
                if (this.access != null && useAsm.get(baseIndex + i) == 1) {
                    accessIndex = ((FieldAccess) this.access).getIndex(field.getName());
                }
                cachedFields.add(newCachedField(field, cachedFields.size(), accessIndex));
                i++;
            }
            return;
        }
        this.unsafeUtil.createUnsafeCacheFieldsAndRegions(validFields, cachedFields, baseIndex, useAsm);
    }

    public void setGenerics(Kryo kryo, Class[] generics) {
        if (this.config.isOptimizedGenerics()) {
            this.generics = generics;
            if (this.typeParameters != null && this.typeParameters.length > 0) {
                rebuildCachedFields(true);
            }
        }
    }

    public Class[] getGenerics() {
        return this.generics;
    }

    protected void initializeCachedFields() {
    }

    CachedField newCachedField(Field field, int fieldIndex, int accessIndex) {
        Type fieldGenericType;
        CachedField cachedField;
        Class[] fieldClass = new Class[]{field.getType()};
        if (this.config.isOptimizedGenerics()) {
            fieldGenericType = field.getGenericType();
        } else {
            fieldGenericType = null;
        }
        if (!this.config.isOptimizedGenerics() || fieldGenericType == fieldClass[0]) {
            if (Log.TRACE) {
                Log.trace("kryo", "Field " + field.getName() + ": " + fieldClass[0]);
            }
            cachedField = newMatchingCachedField(field, accessIndex, fieldClass[0], fieldGenericType, null);
        } else {
            cachedField = this.genericsUtil.newCachedFieldOfGenericType(field, accessIndex, fieldClass, fieldGenericType);
        }
        if (cachedField instanceof ObjectField) {
            this.hasObjectFields = true;
        }
        cachedField.field = field;
        cachedField.varIntsEnabled = this.varIntsEnabled;
        if (!this.config.isUseAsm()) {
            cachedField.offset = this.unsafeUtil.getObjectFieldOffset(field);
        }
        cachedField.access = (FieldAccess) this.access;
        cachedField.accessIndex = accessIndex;
        boolean z = (!this.config.isFieldsCanBeNull() || fieldClass[0].isPrimitive() || field.isAnnotationPresent(NotNull.class)) ? false : true;
        cachedField.canBeNull = z;
        if (this.kryo.isFinal(fieldClass[0]) || this.config.isFixedFieldTypes()) {
            cachedField.valueClass = fieldClass[0];
        }
        return cachedField;
    }

    CachedField newMatchingCachedField(Field field, int accessIndex, Class fieldClass, Type fieldGenericType, Class[] fieldGenerics) {
        if (accessIndex != -1) {
            return getAsmFieldFactory().createCachedField(fieldClass, field, this);
        }
        if (!this.config.isUseAsm()) {
            return getUnsafeFieldFactory().createCachedField(fieldClass, field, this);
        }
        CachedField cachedField = getObjectFieldFactory().createCachedField(fieldClass, field, this);
        if (!this.config.isOptimizedGenerics()) {
            return cachedField;
        }
        if (fieldGenerics != null) {
            ((ObjectField) cachedField).generics = fieldGenerics;
            return cachedField;
        } else if (fieldGenericType == null) {
            return cachedField;
        } else {
            Class[] cachedFieldGenerics = FieldSerializerGenericsUtil.getGenerics(fieldGenericType, this.kryo);
            ((ObjectField) cachedField).generics = cachedFieldGenerics;
            if (!Log.TRACE) {
                return cachedField;
            }
            Log.trace("kryo", "Field generics: " + Arrays.toString(cachedFieldGenerics));
            return cachedField;
        }
    }

    private CachedFieldFactory getAsmFieldFactory() {
        if (asmFieldFactory == null) {
            asmFieldFactory = new AsmCachedFieldFactory();
        }
        return asmFieldFactory;
    }

    private CachedFieldFactory getObjectFieldFactory() {
        if (objectFieldFactory == null) {
            objectFieldFactory = new ObjectCachedFieldFactory();
        }
        return objectFieldFactory;
    }

    private CachedFieldFactory getUnsafeFieldFactory() {
        if (unsafeFieldFactory == null) {
            try {
                unsafeFieldFactory = (CachedFieldFactory) getClass().getClassLoader().loadClass("com.esotericsoftware.kryo.serializers.UnsafeCachedFieldFactory").newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Cannot create UnsafeFieldFactory", e);
            }
        }
        return unsafeFieldFactory;
    }

    public int compare(CachedField o1, CachedField o2) {
        return getCachedFieldName(o1).compareTo(getCachedFieldName(o2));
    }

    public void setFieldsCanBeNull(boolean fieldsCanBeNull) {
        this.config.setFieldsCanBeNull(fieldsCanBeNull);
        rebuildCachedFields();
    }

    public void setFieldsAsAccessible(boolean setFieldsAsAccessible) {
        this.config.setFieldsAsAccessible(setFieldsAsAccessible);
        rebuildCachedFields();
    }

    public void setIgnoreSyntheticFields(boolean ignoreSyntheticFields) {
        this.config.setIgnoreSyntheticFields(ignoreSyntheticFields);
        rebuildCachedFields();
    }

    public void setFixedFieldTypes(boolean fixedFieldTypes) {
        this.config.setFixedFieldTypes(fixedFieldTypes);
        rebuildCachedFields();
    }

    public void setUseAsm(boolean setUseAsm) {
        this.config.setUseAsm(setUseAsm);
        rebuildCachedFields();
    }

    public void setCopyTransient(boolean setCopyTransient) {
        this.config.setCopyTransient(setCopyTransient);
    }

    public void setSerializeTransient(boolean setSerializeTransient) {
        this.config.setSerializeTransient(setSerializeTransient);
    }

    public void setOptimizedGenerics(boolean setOptimizedGenerics) {
        this.config.setOptimizedGenerics(setOptimizedGenerics);
        rebuildCachedFields();
    }

    public void write(Kryo kryo, Output output, T object) {
        if (Log.TRACE) {
            Log.trace("kryo", "FieldSerializer.write fields of class: " + object.getClass().getName());
        }
        if (this.config.isOptimizedGenerics()) {
            if (!(this.typeParameters == null || this.generics == null)) {
                rebuildCachedFields();
            }
            if (this.genericsScope != null) {
                kryo.getGenericsResolver().pushScope(this.type, this.genericsScope);
            }
        }
        for (CachedField write : this.fields) {
            write.write(output, object);
        }
        if (this.config.isSerializeTransient()) {
            for (CachedField write2 : this.transientFields) {
                write2.write(output, object);
            }
        }
        if (this.config.isOptimizedGenerics() && this.genericsScope != null) {
            kryo.getGenericsResolver().popScope();
        }
    }

    public T read(Kryo kryo, Input input, Class<T> type) {
        try {
            if (this.config.isOptimizedGenerics()) {
                if (!(this.typeParameters == null || this.generics == null)) {
                    rebuildCachedFields();
                }
                if (this.genericsScope != null) {
                    kryo.getGenericsResolver().pushScope(type, this.genericsScope);
                }
            }
            T object = create(kryo, input, type);
            kryo.reference(object);
            for (CachedField read : this.fields) {
                read.read(input, object);
            }
            if (this.config.isSerializeTransient()) {
                for (CachedField read2 : this.transientFields) {
                    read2.read(input, object);
                }
            }
            if (!(!this.config.isOptimizedGenerics() || this.genericsScope == null || kryo.getGenericsResolver() == null)) {
                kryo.getGenericsResolver().popScope();
            }
            return object;
        } catch (Throwable th) {
            if (!(!this.config.isOptimizedGenerics() || this.genericsScope == null || kryo.getGenericsResolver() == null)) {
                kryo.getGenericsResolver().popScope();
            }
        }
    }

    protected T create(Kryo kryo, Input input, Class<T> type) {
        return kryo.newInstance(type);
    }

    public CachedField getField(String fieldName) {
        for (CachedField cachedField : this.fields) {
            if (getCachedFieldName(cachedField).equals(fieldName)) {
                return cachedField;
            }
        }
        throw new IllegalArgumentException("Field \"" + fieldName + "\" not found on class: " + this.type.getName());
    }

    protected String getCachedFieldName(CachedField cachedField) {
        return this.config.getCachedFieldNameStrategy().getName(cachedField);
    }

    public void removeField(String fieldName) {
        int i;
        for (i = 0; i < this.fields.length; i++) {
            CachedField cachedField = this.fields[i];
            if (getCachedFieldName(cachedField).equals(fieldName)) {
                CachedField[] newFields = new CachedField[(this.fields.length - 1)];
                System.arraycopy(this.fields, 0, newFields, 0, i);
                System.arraycopy(this.fields, i + 1, newFields, i, newFields.length - i);
                this.fields = newFields;
                this.removedFields.add(cachedField);
                return;
            }
        }
        for (i = 0; i < this.transientFields.length; i++) {
            cachedField = this.transientFields[i];
            if (getCachedFieldName(cachedField).equals(fieldName)) {
                newFields = new CachedField[(this.transientFields.length - 1)];
                System.arraycopy(this.transientFields, 0, newFields, 0, i);
                System.arraycopy(this.transientFields, i + 1, newFields, i, newFields.length - i);
                this.transientFields = newFields;
                this.removedFields.add(cachedField);
                return;
            }
        }
        throw new IllegalArgumentException("Field \"" + fieldName + "\" not found on class: " + this.type.getName());
    }

    public void removeField(CachedField removeField) {
        int i;
        for (i = 0; i < this.fields.length; i++) {
            CachedField cachedField = this.fields[i];
            if (cachedField == removeField) {
                CachedField[] newFields = new CachedField[(this.fields.length - 1)];
                System.arraycopy(this.fields, 0, newFields, 0, i);
                System.arraycopy(this.fields, i + 1, newFields, i, newFields.length - i);
                this.fields = newFields;
                this.removedFields.add(cachedField);
                return;
            }
        }
        for (i = 0; i < this.transientFields.length; i++) {
            cachedField = this.transientFields[i];
            if (cachedField == removeField) {
                newFields = new CachedField[(this.transientFields.length - 1)];
                System.arraycopy(this.transientFields, 0, newFields, 0, i);
                System.arraycopy(this.transientFields, i + 1, newFields, i, newFields.length - i);
                this.transientFields = newFields;
                this.removedFields.add(cachedField);
                return;
            }
        }
        throw new IllegalArgumentException("Field \"" + removeField + "\" not found on class: " + this.type.getName());
    }

    public CachedField[] getFields() {
        return this.fields;
    }

    public CachedField[] getTransientFields() {
        return this.transientFields;
    }

    public Class getType() {
        return this.type;
    }

    public Kryo getKryo() {
        return this.kryo;
    }

    public boolean getUseAsmEnabled() {
        return this.config.isUseAsm();
    }

    public boolean getUseMemRegions() {
        return this.useMemRegions;
    }

    public boolean getCopyTransient() {
        return this.config.isCopyTransient();
    }

    public boolean getSerializeTransient() {
        return this.config.isSerializeTransient();
    }

    protected T createCopy(Kryo kryo, T original) {
        return kryo.newInstance(original.getClass());
    }

    public T copy(Kryo kryo, T original) {
        T copy = createCopy(kryo, original);
        kryo.reference(copy);
        if (this.config.isCopyTransient()) {
            for (CachedField copy2 : this.transientFields) {
                copy2.copy(original, copy);
            }
        }
        for (CachedField copy22 : this.fields) {
            copy22.copy(original, copy);
        }
        return copy;
    }

    final Generics getGenericsScope() {
        return this.genericsScope;
    }
}
