package de.javakaffee.kryoserializers.jodatime;

import com.esotericsoftware.kryo.io.Input;
import org.joda.time.Chronology;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;

enum IdentifiableChronology {
    ISO(null, ISOChronology.getInstance()),
    COPTIC("COPTIC", CopticChronology.getInstance()),
    ETHIOPIC("ETHIOPIC", EthiopicChronology.getInstance()),
    GREGORIAN("GREGORIAN", GregorianChronology.getInstance()),
    JULIAN("JULIAN", JulianChronology.getInstance()),
    ISLAMIC("ISLAMIC", IslamicChronology.getInstance()),
    BUDDHIST("BUDDHIST", BuddhistChronology.getInstance()),
    GJ("GJ", GJChronology.getInstance());
    
    private final Chronology _chronology;
    private final String _id;

    private IdentifiableChronology(String id, Chronology chronology) {
        this._id = id;
        this._chronology = chronology;
    }

    public String getId() {
        return this._id;
    }

    public static String getIdByChronology(Class<? extends Chronology> clazz) throws IllegalArgumentException {
        for (IdentifiableChronology item : values()) {
            if (clazz.equals(item._chronology.getClass())) {
                return item._id;
            }
        }
        throw new IllegalArgumentException("Chronology not supported: " + clazz.getSimpleName());
    }

    public static Chronology valueOfId(String id) throws IllegalArgumentException {
        if (id == null) {
            return ISO._chronology;
        }
        for (IdentifiableChronology item : values()) {
            if (id.equals(item._id)) {
                return item._chronology;
            }
        }
        throw new IllegalArgumentException("No chronology found for id " + id);
    }

    static Chronology readChronology(Input input) {
        String chronologyId = input.readString();
        if ("".equals(chronologyId)) {
            chronologyId = null;
        }
        return valueOfId(chronologyId);
    }

    static String getChronologyId(Chronology chronology) {
        return getIdByChronology(chronology.getClass());
    }
}
