package xyz.chiragtoprani.overlap;


    import android.Manifest;
    import android.app.Activity;
    import android.app.Dialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.database.Cursor;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.content.ContextCompat;
    import android.util.Log;
    import android.view.View;
    import android.webkit.WebView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import org.apache.http.NameValuePair;
    import org.apache.http.message.BasicNameValuePair;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Calendar;
    import java.util.List;

    import me.everything.providers.android.calendar.CalendarProvider;
    import me.everything.providers.android.calendar.Event;


public class ProfileActivity extends Activity {


    SharedPreferences pref;
    String token, grav, oldpasstxt, newpasstxt;
    WebView web;
    Button chgpass, chgpassfr, cancel, logout;
    Dialog dlg;
    EditText oldpass, newpass;
    List<NameValuePair> params;
    private final int REQUEST_CALENDAR = 0x001;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        display = (TextView) findViewById(R.id.display);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CALENDAR);

        } else {
            requestCalendarInformation();
        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCalendarInformation();
                } else {
                    display.setText("Permission Denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void requestCalendarInformation(){
//        CalendarProvider provider = new CalendarProvider(getApplicationContext());
//        List<Calendar> calendars = provider.getCalendars().getList();
//
//
//
//        //gets list of events:
//        for (int i = 0; i < calendars.size(); i ++ ) {
//            provider.getEvents(calendars.get(0).id);
//        }

//        Cursor cursor = getContentResolver().query(Uri.parse("content://calendar/calendars"), new String[]{ "_id",  "displayname" }, null, null, null);
//        cursor.moveToFirst();
//        String[] CalNames = new String[cursor.getCount()];
//
//        int[] CalIds = new int[cursor.getCount()];
//        for (int i = 0; i < CalNames.length; i++) {
//            CalIds[i] = cursor.getInt(0);
//            CalNames[i] = cursor.getString(1);
//            cursor.moveToNext();
//        }
//        cursor.close();

        ArrayList<String> nameOfEvent = new ArrayList<String>();
        ArrayList<String> startDates = new ArrayList<String>();
        ArrayList<String> endDates = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();

        Cursor cursor = getApplicationContext().getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            Log.v("NAME", "restart activity");
            if (cursor.getString(4) == null)
                Log.v("NAME", cursor.getString(1));
            else
                startDates.add(getDate(Long.parseLong(cursor.getString(4))));

//            endDates.add(getDate(Long.parseLong(cursor.getString(4).trim())));
//            Log.v("NAME", cursor.getString(1));
//            Log.v("NAME", "test " + Long.parseLong(cursor.getString(3)));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();

        }
        display.setText(Arrays.toString(CNames));
//        display.setText(":size: " + calendars.size() + " ... " + calendars.toString());
    }


        public static String getDate(long milliSeconds) {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        }
}


//ALL IN ON CREATE
//        web = (WebView)findViewById(R.id.webView);
//        chgpass = (Button)findViewById(R.id.chgbtn);
//        logout = (Button)findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor edit = pref.edit();
//                //Storing Data using SharedPreferences
//                edit.putString("token", "");
//                edit.commit();
//                Intent loginactivity = new Intent(ProfileActivity.this,LoginActivity.class);
//
//                startActivity(loginactivity);
//                finish();
//            }
//        });

//        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
//        token = pref.getString("token", "");
//        grav = pref.getString("grav", "");

//        web.getSettings().setUseWideViewPort(true);
//        web.getSettings().setLoadWithOverviewMode(true);
//        web.loadUrl(grav);
//
//        chgpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dlg = new Dialog(ProfileActivity.this);
//                dlg.setContentView(R.layout.chgpassword_frag);
//                dlg.setTitle("Change Password");
//                chgpassfr = (Button)dlg.findViewById(R.id.chgbtn);
//
//                chgpassfr.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        oldpass = (EditText)dlg.findViewById(R.id.oldpass);
//                        newpass = (EditText)dlg.findViewById(R.id.newpass);
//                        oldpasstxt = oldpass.getText().toString();
//                        newpasstxt = newpass.getText().toString();
//                        params = new ArrayList<NameValuePair>();
//                        params.add(new BasicNameValuePair("oldpass", oldpasstxt));
//                        params.add(new BasicNameValuePair("newpass", newpasstxt));
//                        params.add(new BasicNameValuePair("id", token));
//                        ServerRequest sr = new ServerRequest();
//                        //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
//                        JSONObject json = sr.getJSON("http://10.0.2.2:8080/api/chgpass",params);
//                        if(json != null){
//                            try{
//                                String jsonstr = json.getString("response");
//                                if(json.getBoolean("res")){
//
//                                    dlg.dismiss();
//                                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_SHORT).show();
//                                }else {
//                                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_SHORT).show();
//
//                                }
//                            }catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                });
//                cancel = (Button)dlg.findViewById(R.id.cancelbtn);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dlg.dismiss();
//                    }
//                });
//                dlg.show();
//            }
//        });



