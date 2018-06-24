package com.trigma.tiktok.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.provider.Telephony.Sms;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.model.DocAcceptReject;
import com.trigma.tiktok.model.GeoLocationResponce;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.model.MessagesResponse;
import com.trigma.tiktok.model.NotificationCountPush;
import com.trigma.tiktok.realm.model.MessagesListObject;
import com.trigma.tiktok.realm.model.RealmControler;
import com.trigma.tiktok.retrofit.NetWorkingService;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmResults;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommonUtils {
    public static Typeface bold;
    public static Typeface light;
    public static Typeface reguler;
    public static Typeface sanslight;
    public static Typeface semibold;

    static class C13741 implements Observer<MessagesResponse> {
        C13741() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(MessagesResponse messagesResponse) {
            Log.e("messagesResponse", new Gson().toJson((Object) messagesResponse));
            if (messagesResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK) && messagesResponse.getUser().size() > 0) {
                for (int a = 0; a < messagesResponse.getUser().size(); a++) {
                    if (((MessageUserObject) messagesResponse.getUser().get(a)).getMessagedotshow() == 0) {
                        SharedPreff.showMessageCount(true);
                        NotificationCountPush notificationCountPush = new NotificationCountPush();
                        notificationCountPush.setShowCount(true);
                        EventBus.getDefault().post(notificationCountPush);
                        return;
                    }
                }
            }
        }
    }

    public static boolean isConnectedToInternet(Context act) {
        try {
            ConnectivityManager ConMgr = (ConnectivityManager) act.getSystemService("connectivity");
            if (ConMgr.getNetworkInfo(1).getState() == State.CONNECTED || ConMgr.getNetworkInfo(0).getState() == State.CONNECTED) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String capWordCase(String input) {
        String output = "";
        if (!(input == null || input.isEmpty())) {
            int count = 0;
            for (char c : input.toCharArray()) {
                if (count == 0) {
                    output = output + Character.toUpperCase(c);
                } else {
                    output = output + Character.toLowerCase(c);
                }
                count++;
            }
        }
        return output;
    }

    public static void textScrollable(TextView view) {
        view.setMovementMethod(new ScrollingMovementMethod());
    }

    public static void hideKeyboard(View v, Activity activity) {
        try {
            ((InputMethodManager) activity.getApplicationContext().getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closingKeyboard(Activity activity) {
        try {
            activity.getWindow().setSoftInputMode(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(2, 1);
    }

    public static String getLastTwoDigits(String phoneNumber) {
        try {
            return phoneNumber.substring(8, 10);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getConvertedPhoneNum(String phoneNumber) {
        StringBuilder sb = new StringBuilder();
        try {
            int phone_length = phoneNumber.length();
            if (phone_length > 2) {
                for (int g = 0; g < phone_length - 2; g++) {
                    sb.append("x");
                }
                sb.append(phoneNumber.substring(phone_length - 2));
            }
            phoneNumber = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    public static Double roundTwoDecimal(double d) {
        try {
            DecimalFormat twoDForm = new DecimalFormat("#.#");
            String.format("%.2f", new Object[]{Double.valueOf(d)});
            Log.e("normal" + d, "" + String.format("%.2f", new Object[]{Double.valueOf(d)}));
            return Double.valueOf(twoDForm.format(d));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Double.valueOf(d);
        }
    }

    public static void openLink(Activity context, String url) {
        try {
            Intent i = new Intent("android.intent.action.VIEW");
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Please check your Web Address or may be your device can not perform this action.", 0).show();
        }
    }

    public static void sendEmail(Activity activity, String data) {
        try {
            Intent emailIntent = new Intent("android.intent.action.SEND");
            emailIntent.setType("text/html");
            emailIntent.putExtra("android.intent.extra.EMAIL", new String[0]);
            emailIntent.putExtra("android.intent.extra.TEXT", Html.fromHtml(data));
            activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Can not perform this action on this device", 0).show();
        }
    }

    public static void sendEmailTo(Activity activity, String data, String email) {
        try {
            Intent emailIntent = new Intent("android.intent.action.SEND");
            emailIntent.setType("text/html");
            emailIntent.putExtra("android.intent.extra.EMAIL", new String[]{email});
            activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Can not perform this action on this device", 0).show();
        }
    }

    public static void sendViaSms(Context mContext, String data) {
        try {
            Intent sendIntent;
            if (VERSION.SDK_INT >= 19) {
                String defaultSmsPackageName = Sms.getDefaultSmsPackage(mContext);
                sendIntent = new Intent("android.intent.action.SEND");
                sendIntent.setType("text/plain");
                sendIntent.putExtra("android.intent.extra.TEXT", data);
                if (defaultSmsPackageName != null) {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                mContext.startActivity(sendIntent);
                return;
            }
            sendIntent = new Intent("android.intent.action.VIEW");
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", data);
            mContext.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Can not perform this action on this device", 0).show();
        }
    }

    public static Boolean validateEmail(String email) {
        return Boolean.valueOf(Pattern.compile(Patterns.EMAIL_ADDRESS.toString()).matcher(email).matches());
    }

    public static void call(Activity activity, String phoone) {
        try {
            Intent callIntent = new Intent("android.intent.action.CALL");
            callIntent.setData(Uri.parse("tel:" + phoone));
            activity.startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getArrayFromList(List<String> list) {
        String[] array = new String[list.size()];
        for (int j = 0; j < list.size(); j++) {
            array[j] = (String) list.get(j);
        }
        return array;
    }

    public static String[] getArrayFromList(ArrayList<String> list) {
        String[] array = new String[list.size()];
        for (int j = 0; j < list.size(); j++) {
            array[j] = (String) list.get(j);
        }
        return array;
    }

    public static String getCommaSepratedString(ArrayList<Integer> list) {
        if (list.size() <= 0) {
            return null;
        }
        StringBuilder commaSepValueBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            commaSepValueBuilder.append(list.get(i));
            if (i != list.size() - 1) {
                commaSepValueBuilder.append(", ");
            }
        }
        return commaSepValueBuilder.toString();
    }

    public static String getCommaSepratedStrings(ArrayList<String> list) {
        if (list.size() <= 0) {
            return null;
        }
        StringBuilder commaSepValueBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            commaSepValueBuilder.append((String) list.get(i));
            if (i != list.size() - 1) {
                commaSepValueBuilder.append(",");
            }
        }
        return commaSepValueBuilder.toString();
    }

    public static String getMonth(String dateMonth) {
        String dateM = "";
        if (dateMonth.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
            return "January";
        }
        if (dateMonth.equalsIgnoreCase("2")) {
            return "February";
        }
        if (dateMonth.equalsIgnoreCase("3")) {
            return "March";
        }
        if (dateMonth.equalsIgnoreCase("4")) {
            return "April";
        }
        if (dateMonth.equalsIgnoreCase("5")) {
            return "May";
        }
        if (dateMonth.equalsIgnoreCase("6")) {
            return "June";
        }
        if (dateMonth.equalsIgnoreCase("7")) {
            return "July";
        }
        if (dateMonth.equalsIgnoreCase("8")) {
            return "August";
        }
        if (dateMonth.equalsIgnoreCase("9")) {
            return "September";
        }
        if (dateMonth.equalsIgnoreCase("10")) {
            return "October";
        }
        if (dateMonth.equalsIgnoreCase("11")) {
            return "November";
        }
        if (dateMonth.equalsIgnoreCase("12")) {
            return "December";
        }
        return dateM;
    }

    public static String getMonthInteger(String dateMonth) {
        String dateM = "";
        if (dateMonth.equalsIgnoreCase("January")) {
            return AppEventsConstants.EVENT_PARAM_VALUE_YES;
        }
        if (dateMonth.equalsIgnoreCase("February")) {
            return "2";
        }
        if (dateMonth.equalsIgnoreCase("March")) {
            return "3";
        }
        if (dateMonth.equalsIgnoreCase("April")) {
            return "4";
        }
        if (dateMonth.equalsIgnoreCase("May")) {
            return "5";
        }
        if (dateMonth.equalsIgnoreCase("June")) {
            return "6";
        }
        if (dateMonth.equalsIgnoreCase("July")) {
            return "7";
        }
        if (dateMonth.equalsIgnoreCase("August")) {
            return "8";
        }
        if (dateMonth.equalsIgnoreCase("September")) {
            return "9";
        }
        if (dateMonth.equalsIgnoreCase("October")) {
            return "10";
        }
        if (dateMonth.equalsIgnoreCase("November")) {
            return "11";
        }
        if (dateMonth.equalsIgnoreCase("December")) {
            return "12";
        }
        return dateM;
    }

    public static Typeface opensansLightFont(Context appContext) {
        if (sanslight == null) {
            sanslight = Typeface.createFromAsset(appContext.getAssets(), "fonts/OpenSans-Light.ttf");
        }
        return sanslight;
    }

    public static int dpToPx(int dp, Context context) {
        return Math.round(((float) dp) * (context.getApplicationContext().getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    public static int pxToDp(int px, Context context) {
        return Math.round(((float) px) / (context.getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    public static GeoLocationResponce jsonParsorForGeo(JsonObject jsonObject) {
        String formatted_address = "";
        String title = "";
        Double lat = Double.valueOf(0.0d);
        Double lng = Double.valueOf(0.0d);
        GeoLocationResponce geoLocationResponce = new GeoLocationResponce();
        try {
            JSONObject mainOject = new JSONObject(jsonObject.toString());
            if (!mainOject.getString("status").equalsIgnoreCase(Constants.OK)) {
                geoLocationResponce = null;
            } else if (mainOject.has(Constants.RESULTS)) {
                JSONArray results = mainOject.getJSONArray(Constants.RESULTS);
                if (results.length() > 0) {
                    JSONObject resultObject = results.getJSONObject(0);
                    JSONObject location = resultObject.getJSONObject(Constants.GEOMETRY).getJSONObject("location");
                    lat = Double.valueOf(location.getDouble(Constants.LAT));
                    lng = Double.valueOf(location.getDouble(Constants.LNG));
                    formatted_address = resultObject.getString(Constants.FORMATTED_ADDRESS);
                    if (resultObject.has(Constants.ADDRESS_COMPONENTS)) {
                        JSONArray address_components = resultObject.getJSONArray(Constants.ADDRESS_COMPONENTS);
                        for (int k = 0; k < address_components.length(); k++) {
                            JSONObject innerObejct = address_components.getJSONObject(k);
                            if (innerObejct.has(Constants.TYPES)) {
                                JSONArray types = innerObejct.getJSONArray(Constants.TYPES);
                                if (0 < types.length()) {
                                    String val = types.getString(0);
                                    title = val.equalsIgnoreCase(Constants.ROUTE) ? innerObejct.getString(Constants.SHORT_NAME) : val.equalsIgnoreCase(Constants.ADMINISTRATIVE_AREA_LEVEL_2) ? innerObejct.getString(Constants.SHORT_NAME) : innerObejct.getString(Constants.SHORT_NAME);
                                }
                            }
                        }
                    }
                    geoLocationResponce.setAddress(formatted_address);
                    geoLocationResponce.setTitle(title);
                    geoLocationResponce.setLatitude(lat);
                    geoLocationResponce.setLongitude(lng);
                }
            } else {
                geoLocationResponce = null;
            }
            return geoLocationResponce;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDistance(LatLng source, LatLng destination) {
        Exception e;
        Log.e("source", "" + source);
        Log.e(Param.DESTINATION, "" + destination);
        Location mylocation = null;
        Location dest_location = null;
        try {
            if ((source.longitude == 0.0d && source.latitude == 0.0d) || (destination.longitude == 0.0d && destination.latitude == 0.0d)) {
                return "0 miles";
            }
            if (null == null) {
                Location mylocation2 = new Location("");
                try {
                    dest_location = new Location("");
                    mylocation = mylocation2;
                } catch (Exception e2) {
                    e = e2;
                    mylocation = mylocation2;
                    e.printStackTrace();
                    return "unknown";
                }
            }
            mylocation.setLongitude(source.longitude);
            mylocation.setLatitude(source.latitude);
            dest_location.setLongitude(destination.longitude);
            dest_location.setLatitude(destination.latitude);
            return "" + convertLength(roundTwoDecimal((double) mylocation.distanceTo(dest_location)).doubleValue(), Units.METRE, Units.MILE) + "\nmiles";
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return "unknown";
        }
    }

    public static double convertLength(double l, Units from, Units to) {
        if (from == to || l <= 0.0d) {
            return l;
        }
        double d = (l * from.length) * (1.0d / to.length);
        if (d < 1.0E-4d || d > 1000000.0d) {
            return Double.parseDouble(new DecimalFormat("0.00E00").format(d));
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(d));
    }

    public static String getLocalTime(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        String result = "";
        Date convertedDate = new Date();
        try {
            convertedDate = formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        SimpleDateFormat month = new SimpleDateFormat("MMMM");
        SimpleDateFormat date_ = new SimpleDateFormat("d");
        SimpleDateFormat hour = new SimpleDateFormat("hh");
        SimpleDateFormat mint = new SimpleDateFormat("mm");
        SimpleDateFormat am_pm = new SimpleDateFormat("aa");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String dayOfTheWeek = day.format(convertedDate);
        String time = hour.format(convertedDate) + ":" + mint.format(convertedDate) + " " + am_pm.format(convertedDate);
        String month_ = month.format(convertedDate);
        String date_shown_converted = date_.format(convertedDate);
        result = date_shown_converted + " " + month_ + " " + simpleDateFormat.format(convertedDate);
        return time;
    }

    public static Address getAddressObjectFromLocation(double latitude, double longitude, Activity context) {
        List<Address> gotAddresses = null;
        try {
            gotAddresses = new Geocoder(context).getFromLocation(latitude, longitude, 5);
            Log.e("gotAddresses", "" + new Gson().toJson((Object) gotAddresses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gotAddresses == null || gotAddresses.size() <= 0) {
            return null;
        }
        return (Address) gotAddresses.get(0);
    }

    public static void addEvent(Context context, long startMili, long endMili, int isDoc, String msg) {
        try {
            HashMap<String, Long> savedList = new HashMap();
            if (isDoc == 1) {
                savedList = SharedPreff.getDocLocalNotification();
            } else {
                savedList = SharedPreff.getPatLocalNotification();
            }
            if (savedList.containsKey("" + startMili)) {
                removeEvent(context, ((Long) savedList.get("" + startMili)).longValue());
                savedList.remove("" + startMili);
            }
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put("dtstart", Long.valueOf(startMili));
            values.put("dtend", Long.valueOf(endMili));
            values.put("title", context.getResources().getString(C1020R.string.tik_tok_doc_video_visit_with_you_doctor));
            values.put(ShareConstants.WEB_DIALOG_PARAM_DESCRIPTION, msg);
            values.put("calendar_id", Integer.valueOf(1));
            values.put("eventTimezone", Calendar.getInstance().getTimeZone().getID());
            Uri uri = cr.insert(Events.CONTENT_URI, values);
            Log.e("IDDDDDDDDDDD", "" + Long.parseLong(uri.getLastPathSegment()));
            savedList.put("" + startMili, Long.valueOf(Long.parseLong(uri.getLastPathSegment())));
            if (isDoc == 1) {
                SharedPreff.saveDOCLocalNotification(savedList);
            } else {
                SharedPreff.savePatLocalNotification(savedList);
            }
            Log.e("savedList", "" + savedList);
            setReminder(cr, Long.parseLong(uri.getLastPathSegment()), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setReminder(ContentResolver cr, long eventID, int timeBefore) {
        try {
            ContentValues values = new ContentValues();
            values.put("minutes", Integer.valueOf(timeBefore));
            values.put("event_id", Long.valueOf(eventID));
            values.put("method", Integer.valueOf(1));
            Uri uri = cr.insert(Reminders.CONTENT_URI, values);
            Cursor c = Reminders.query(cr, eventID, new String[]{"minutes"});
            if (c.moveToFirst()) {
                System.out.println("calendar" + c.getInt(c.getColumnIndex("minutes")));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeEvent(Context context, long id) {
        try {
            Uri eventsUri;
            ContentResolver cr = context.getContentResolver();
            if (VERSION.SDK_INT >= 8) {
                eventsUri = Uri.parse("content://com.android.calendar/events");
            } else {
                eventsUri = Uri.parse("content://calendar/events");
            }
            Log.i("Deleted", " " + cr.delete(ContentUris.withAppendedId(eventsUri, id), null, null) + " calendar entry.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getDateConverted(String data) {
        long mili = 0;
        try {
            mili = new SimpleDateFormat("d-MMMM-yyyy hh:mm:ss").parse(data).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mili;
    }

    public static String getStringValue(Map<String, String> data, String key) {
        if (data.containsKey(key)) {
            return (String) data.get(key);
        }
        return "";
    }

    public static int getIntValue(Map<String, String> data, String key) {
        if (data.containsKey(key)) {
            return Integer.parseInt((String) data.get(key));
        }
        return -1;
    }

    public static boolean getBoolValue(Map<String, String> data, String key) {
        if (data.containsKey(key)) {
            return Boolean.parseBoolean((String) data.get(key));
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Class getFlagClass(java.lang.String r4, int r5) {
        /*
        r2 = 1;
        r0 = 0;
        r1 = -1;
        if (r5 != r2) goto L_0x001f;
    L_0x0005:
        r2 = r4.hashCode();
        switch(r2) {
            case 3052376: goto L_0x0012;
            default: goto L_0x000c;
        };
    L_0x000c:
        switch(r1) {
            case 0: goto L_0x001c;
            default: goto L_0x000f;
        };
    L_0x000f:
        r0 = com.trigma.tiktok.activity.NotificationFromPush.class;
    L_0x0011:
        return r0;
    L_0x0012:
        r2 = "chat";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000c;
    L_0x001a:
        r1 = r0;
        goto L_0x000c;
    L_0x001c:
        r0 = com.trigma.tiktok.activity.HomeScreen.class;
        goto L_0x0011;
    L_0x001f:
        r3 = 3;
        if (r5 != r3) goto L_0x003c;
    L_0x0022:
        r2 = r4.hashCode();
        switch(r2) {
            case -33317017: goto L_0x002f;
            default: goto L_0x0029;
        };
    L_0x0029:
        switch(r1) {
            case 0: goto L_0x0039;
            default: goto L_0x002c;
        };
    L_0x002c:
        r0 = com.trigma.tiktok.activity.StaffHomeScreen.class;
        goto L_0x0011;
    L_0x002f:
        r2 = "Staff notification status";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x0029;
    L_0x0037:
        r1 = r0;
        goto L_0x0029;
    L_0x0039:
        r0 = com.trigma.tiktok.activity.StaffHomeScreen.class;
        goto L_0x0011;
    L_0x003c:
        r3 = r4.hashCode();
        switch(r3) {
            case 3052376: goto L_0x0054;
            case 112202875: goto L_0x004a;
            default: goto L_0x0043;
        };
    L_0x0043:
        r0 = r1;
    L_0x0044:
        switch(r0) {
            case 0: goto L_0x005e;
            case 1: goto L_0x0061;
            default: goto L_0x0047;
        };
    L_0x0047:
        r0 = com.trigma.tiktok.activity.NotificationFromPush.class;
        goto L_0x0011;
    L_0x004a:
        r2 = "video";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x0043;
    L_0x0053:
        goto L_0x0044;
    L_0x0054:
        r0 = "chat";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0043;
    L_0x005c:
        r0 = r2;
        goto L_0x0044;
    L_0x005e:
        r0 = com.trigma.tiktok.activity.VideoCallScreenChanged.class;
        goto L_0x0011;
    L_0x0061:
        r0 = com.trigma.tiktok.activity.patient.HomeScreenPatient.class;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trigma.tiktok.utils.CommonUtils.getFlagClass(java.lang.String, int):java.lang.Class");
    }

    public static String getAppointmentDateFormat(String time) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("d-MMMMM-yyyy");
            String str = null;
            try {
                return new SimpleDateFormat("MMMMM d, yyyy").format(inputFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
                return str;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return time;
        }
    }

    public static String phoneFormatter(String value) {
        String result = value;
        int lenghtTotal = value.length();
        if (lenghtTotal <= 0) {
            return result;
        }
        ArrayList<String> list = new ArrayList();
        for (int k = 0; k < lenghtTotal; k++) {
            list.add(String.valueOf(value.charAt(k)));
        }
        list.add(3, "-");
        StringBuilder sb = new StringBuilder();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            sb.append((String) it.next()).append("");
        }
        return sb.toString();
    }

    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return null;
    }

    public static boolean isFormatSupported(String file) {
        if (file.toString().endsWith(".jpg")) {
            return true;
        }
        if (file.toString().endsWith(".3gp")) {
            return true;
        }
        if (file.toString().endsWith(".mp4")) {
            return true;
        }
        if (file.toString().endsWith(".mkv")) {
            return true;
        }
        if (file.toString().endsWith(".png")) {
            return true;
        }
        if (file.toString().endsWith(".webm")) {
            return true;
        }
        if (file.toString().endsWith(".pdf")) {
            return true;
        }
        if (file.toString().endsWith(".jpeg")) {
            return true;
        }
        return false;
    }

    public static boolean isImage(String file) {
        if (file.toString().endsWith(".jpg")) {
            return true;
        }
        if (file.toString().endsWith(".png")) {
            return true;
        }
        if (file.toString().endsWith(".jpeg")) {
            return true;
        }
        return false;
    }

    public static boolean isMedia(String file) {
        if (file.toString().endsWith(".3gp")) {
            return true;
        }
        if (file.toString().endsWith(".mp4")) {
            return true;
        }
        if (file.toString().endsWith(".mkv")) {
            return true;
        }
        if (file.toString().endsWith(".webm")) {
            return true;
        }
        return false;
    }

    public static String getChatSubString(String file) {
        if (file == null) {
            return "";
        }
        String supported = file;
        if (file.contains(":-")) {
            supported = file.substring(file.indexOf(":-") + 2);
        } else if (file.contains(":")) {
            supported = file.substring(file.indexOf(":") + 2);
        }
        return supported.trim();
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy / MM / dd ").format(Calendar.getInstance().getTime());
    }

    public static int getWindowDimension(Activity activity, boolean requiredWidth) {
        int measuredWidth;
        int measuredHeight;
        Point size = new Point();
        WindowManager w = activity.getWindowManager();
        if (VERSION.SDK_INT >= 11) {
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
            measuredHeight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
            measuredHeight = d.getHeight();
        }
        Log.v("WindowDimension", "Width = " + measuredWidth + ", Height = " + measuredHeight);
        if (requiredWidth) {
            return measuredWidth;
        }
        return measuredHeight;
    }

    public static String getFirstNameAndLastName(String name, boolean isFirstName) {
        String result = name;
        String[] splited = name.split("\\s+");
        if (splited.length == 1) {
            if (isFirstName) {
                return splited[0];
            }
            return "";
        } else if (splited.length == 2) {
            if (isFirstName) {
                return splited[0];
            }
            return splited[1];
        } else if (isFirstName) {
            return splited[0];
        } else {
            return splited[1];
        }
    }

    public static void fetchMessageList(int isDoc, LoginDocResponce loginDocResponce, Context c, NetWorkingService netWorkingService) {
        if (loginDocResponce != null && isConnectedToInternet(c)) {
            netWorkingService.getAPI().getMessagesList(loginDocResponce.getData().get_id(), "" + isDoc).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C13741());
        }
    }

    public static void addingDataToDatabase(ArrayList<MessageUserObject> user, LoginDocResponce loginDocResponce) {
        RealmResults<MessagesListObject> results = RealmControler.getRealm().where(MessagesListObject.class).findAll();
        for (int q = 0; q < results.size(); q++) {
            boolean isMatched = false;
            for (int w = 0; w < user.size(); w++) {
                if (((MessagesListObject) results.get(q)).getUnique_id().equalsIgnoreCase(loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + ((MessageUserObject) user.get(w)).getId())) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                final MessagesListObject messagesListObject = (MessagesListObject) RealmControler.getRealm().where(MessagesListObject.class).equalTo("unique_id", ((MessagesListObject) results.get(q)).getUnique_id()).findFirst();
                if (messagesListObject != null) {
                    RealmControler.getRealm().executeTransaction(new Transaction() {
                        public void execute(Realm realm) {
                            messagesListObject.deleteFromRealm();
                        }
                    });
                }
            }
        }
        addingNewDataInList(user, loginDocResponce);
    }

    public static void addingNewDataInList(ArrayList<MessageUserObject> user, LoginDocResponce loginDocResponce) {
        RealmResults<MessagesListObject> results = RealmControler.getRealm().where(MessagesListObject.class).findAll();
        for (int q = 0; q < user.size(); q++) {
            boolean isMatched = false;
            for (int w = 0; w < results.size(); w++) {
                if ((loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + ((MessageUserObject) user.get(q)).getId()).equalsIgnoreCase(((MessagesListObject) results.get(w)).getUnique_id())) {
                    isMatched = true;
                    break;
                }
            }
            if (!isMatched) {
                final MessagesListObject u = new MessagesListObject();
                u.set_id(((MessageUserObject) user.get(q)).getId());
                u.setUnique_id(loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + ((MessageUserObject) user.get(q)).getId());
                u.setAddress(((MessageUserObject) user.get(q)).getUser().getAddress());
                u.setApiKey(((MessageUserObject) user.get(q)).getApiKey());
                u.setBio(((MessageUserObject) user.get(q)).getUser().getBio());
                u.setCity(((MessageUserObject) user.get(q)).getUser().getCity());
                u.setCode(((MessageUserObject) user.get(q)).getUser().getCode());
                u.setCount(((MessageUserObject) user.get(q)).getCount());
                u.setMobile(((MessageUserObject) user.get(q)).getUser().getMobile());
                u.setDeviceToken(((MessageUserObject) user.get(q)).getDeviceToken());
                u.setSessionData(((MessageUserObject) user.get(q)).getSessionData());
                u.setProfilePic(((MessageUserObject) user.get(q)).getProfilePic());
                u.setGroupId(((MessageUserObject) user.get(q)).getGroupId());
                u.setDOB(((MessageUserObject) user.get(q)).getDOB());
                u.setEmail(((MessageUserObject) user.get(q)).getUser().getEmail());
                u.setFirstName(((MessageUserObject) user.get(q)).getUser().getFirstName());
                u.setLastName(((MessageUserObject) user.get(q)).getUser().getLastName());
                u.setQualification(((MessageUserObject) user.get(q)).getUser().getQualification());
                u.setName(((MessageUserObject) user.get(q)).getName());
                u.setTokenData(((MessageUserObject) user.get(q)).getTokenData());
                u.setGender(((MessageUserObject) user.get(q)).getGender());
                RealmControler.getRealm().executeTransaction(new Transaction() {
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(u);
                    }
                });
            }
        }
        ArrayList<MessageUserObject> readList = new ArrayList();
        ArrayList<MessageUserObject> unReadList = new ArrayList();
        ArrayList<MessageUserObject> finalList = new ArrayList();
        for (int d = 0; d < user.size(); d++) {
            MessagesListObject messagesListObject = (MessagesListObject) RealmControler.getRealm().where(MessagesListObject.class).equalTo("unique_id", loginDocResponce.getData().get_id() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + ((MessageUserObject) user.get(d)).getId()).findFirst();
            if (messagesListObject == null) {
                unReadList.add(user.get(d));
            } else if (((MessageUserObject) user.get(d)).getCount() > messagesListObject.getChatList().size()) {
                readList.add(user.get(d));
            } else {
                unReadList.add(user.get(d));
            }
        }
        if (readList.size() > 0) {
            SharedPreff.showMessageCount(true);
            NotificationCountPush notificationCountPush = new NotificationCountPush();
            notificationCountPush.setShowCount(true);
            EventBus.getDefault().post(notificationCountPush);
        }
    }

    public static void chatMessageRead(final Context c, final NetWorkingService netWorkingService, final String userID, final String groupID) {
        if (isConnectedToInternet(c)) {
            netWorkingService.getAPI().chatMessageRead(userID, groupID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    CommonUtils.chatMessageRead(c, netWorkingService, userID, groupID);
                }

                public void onNext(DocAcceptReject messagesResponse) {
                    Log.e("chatMessageRead", new Gson().toJson((Object) messagesResponse));
                    if (!messagesResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    }
                }
            });
        }
    }

    public static void refreshingOpenTokToken(final Context c, final NetWorkingService netWorkingService, final String userID) {
        if (isConnectedToInternet(c)) {
            netWorkingService.getAPI().refreshingOpenTokToken(userID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DocAcceptReject>() {
                public void onCompleted() {
                }

                public void onError(Throwable e) {
                    CommonUtils.refreshingOpenTokToken(c, netWorkingService, userID);
                }

                public void onNext(DocAcceptReject messagesResponse) {
                    Log.e("refreshing", new Gson().toJson((Object) messagesResponse));
                    if (!messagesResponse.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                    }
                }
            });
        }
    }

    public static void copyText(String CopyText, Context c, String message) {
        if (VERSION.SDK_INT < 11) {
            ((ClipboardManager) c.getSystemService("clipboard")).setText(CopyText);
            Toast.makeText(c, message, 0).show();
            return;
        }
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) c.getSystemService("clipboard");
        ClipData clip = ClipData.newPlainText("Clip", CopyText);
        Toast.makeText(c, message, 0).show();
        clipboard.setPrimaryClip(clip);
    }
}
