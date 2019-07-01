package com.gencgirisimciler.saglikgozcusu.saglikgozcusu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.NavigationDrawerClasses.NavDrawerItem;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.NavigationDrawerClasses.NavDrawerListAdapter;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.android.ResultsActivity;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.utils.GeneralClasses;
import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.utils.MaddeClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

public class MainActivity extends AppCompatActivity {

    private final int TAKE_PICTURE = 0;
    private final int SELECT_FILE = 1;
    private String resultUrl = "result.txt";

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    public static int assetsFolderIndex = -1;
    public static int jsonWebServiceIndex = -1;
    public static ArrayList<String> mMaddeListesi ;
    HashMap<String,String> atemMadde=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }
    public void init() {

        new _WebServiceAsyncTask().execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMaddeListesi = getMaddeList("maddeler.json");

        assetsFolderIndex = mMaddeListesi.size();
        GeneralClasses.StatusBar statusBar = new GeneralClasses.StatusBar(this);
        statusBar.setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.colorPrimaryDark));

        View header = getLayoutInflater().inflate(R.layout.header, null);

        mTitle = mDrawerTitle = getTitle();

        // navMenuTitles=  EnumLanguage.names();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_small);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        for(String s : mMaddeListesi )
        {
            navDrawerItems.add(new NavDrawerItem(s, navMenuIcons.getResourceId(0, -1)));
        }
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.addHeaderView(header);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(R.string.app_name);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                mDrawerList.bringToFront();
                mDrawerLayout.requestLayout();
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    public void captureImageFromSdCard( View view ) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, SELECT_FILE);
    }

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Sağlık Gözcüsü");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

        return mediaFile;
    }

    public void captureImageFromCamera( View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        String imageFilePath = null;

        switch (requestCode) {
            case TAKE_PICTURE:
                imageFilePath = getOutputMediaFileUri().getPath();
                break;
            case SELECT_FILE: {
                Uri imageUri = data.getData();

                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cur = managedQuery(imageUri, projection, null, null, null);
                cur.moveToFirst();
                imageFilePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            break;
        }
        //Remove output file
        deleteFile(resultUrl);

        Intent results = new Intent( this, ResultsActivity.class);
        results.putExtra("IMAGE_PATH", imageFilePath);
        results.putExtra("RESULT_PATH", resultUrl);
        startActivity(results);
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position,view);
        }
    }

    private void displayView(int position,View view) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
//                fragment = new HomeFragment();
                break;

            default:
                com.rey.material.widget.CheckBox cb = (com.rey.material.widget.CheckBox)view.findViewById(R.id.check);
                TextView textView = (TextView)view.findViewById(R.id.title);
                if(NavDrawerListAdapter.tikliMiArray.get(position-1)) {
                    cb.setChecked(false);
                    NavDrawerListAdapter.tikliMiArray.set(position-1,false);
                    textView.setTextColor(Color.parseColor("#33999999"));
                }
                else
                {
                    cb.setChecked(true);
                    NavDrawerListAdapter.tikliMiArray.set(position-1,true);
                    textView.setTextColor(Color.parseColor("#23b4f5"));
                }
                break;
        }

//        mDrawerList.setItemChecked(position, true);
//        mDrawerList.setSelection(position);
//        setTitle(navMenuTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
//        if (fragment != null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container, fragment).commit();
//
//            // update selected item and title, then close the drawer
//
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
///////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else if (id == R.id.action_yeni_kisi_ekle) {
            displayAlertDialogKullaniciEkle();
            return true;
        }

        return false;
    }

    public void displayAlertDialogKullaniciEkle() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_layout_madde_ekle,null);

        final com.rey.material.widget.EditText etMaddeEkle = (com.rey.material.widget.EditText) alertLayout.findViewById(R.id.eklenecekKullaniciEditText);

        AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);

        alert.setTitle("Yeni Madde Ekleyiniz");
        alert.setView(alertLayout);
        alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Madde Ekleme İşlemi İptal Edildi", Toast.LENGTH_SHORT).show();

            }
        });

        alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        //Toast.makeText(getBaseContext(), "Yeni IP 777: " + tvIP , Toast.LENGTH_SHORT).show();
        final AlertDialog dialog = alert.create();
        dialog.show();

        /**
         * AlertDialog.Builder yerine AlertDialog kullanılıp Positive Button'ı override ettik
         */
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(!etMaddeEkle.getText().toString().equals(""))
                {
                    navDrawerItems.add(new NavDrawerItem(etMaddeEkle.getText().toString(), navMenuIcons.getResourceId(0, -1)));
                    mMaddeListesi.add(etMaddeEkle.getText().toString());
                    adapter.notifyDataSetChanged();
                    NavDrawerListAdapter.tikliMiArray.add(true);
                    dialog.dismiss();
                }
                else
                    Toast.makeText(MainActivity.this, "Boş girdi yapılamamaktadır.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList<String> getMaddeList(String fileName)
    {
        JSONArray jsonArray = null;

        ArrayList<String> cList = new ArrayList<String>();

        try {

            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();

            String json = new String(data,"UTF-8");
            jsonArray= new JSONArray(json);

            if(jsonArray!=null){
                for(int i =0;i<jsonArray.length();i++)
                {
                    cList.add(jsonArray.getJSONObject(i).getString("Ingilizce"));
                }
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        return  cList;
    }

    // Web Servisimizdeki Namspace alanı
    private final String _Namspace      =   "http://tempuri.org/";
    // Web Servimizdeki Method ismi
    private final String _MethodName    =   "SG_WebService";
    // Namspace ile Method isminin birleşimi
    private final String _Action        =   "http://tempuri.org/SG_WebService";
    // Web Servisimizin Adresi
    private final String _Url           =   "http://rpdwebservice.azurewebsites.net/WebService1.asmx"; ///??
    private String _ResultValue         =   "";


    public static MaddeClass[] maddeArray ;
    public class _WebServiceAsyncTask extends AsyncTask<Void,Void,Void>
    {
        // Arkaplan işlemi başlamadan önce çalışacak olan fonksiyonumuz.
        protected void onPreExecute() {

        }
        protected Void doInBackground(Void... voids) {


            SoapObject request = new SoapObject(_Namspace, _MethodName);
            // Web Servisimize gönderilcek parametreleri ekliyoruz.
            //  request.addProperty("s1", _birinci_sayi.toString());
            //  request.addProperty("s2",_birinci_sayi.toString());



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.env ="http://schemas.xmlsoap.org/soap/envelope/";

            // WebServisimiz ASP.NET ile hazırlandığı için mutlaka TRUE değerini vermeliyiz.
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            AndroidHttpTransport aht = new AndroidHttpTransport(_Url);
            aht.debug=true;

            try {

                aht.call(_Action,envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                // Web servisimizden geri gelen sonucu değişkenimize aktarıyoruz.
                _ResultValue=response.toString();

                JSONArray JSONArrayForResult = new JSONArray(_ResultValue);
                maddeArray = new MaddeClass[JSONArrayForResult.length()];
                for (int i = 0; i < JSONArrayForResult.length(); i++) {
                    JSONObject maddeObject = JSONArrayForResult.getJSONObject(i);
                    maddeArray[i] = new MaddeClass(maddeObject.getString("MaddeAdi"),maddeObject.getString("MaddeAciklamasi"));
                }
            }catch (Exception e){

                _ResultValue=e.toString();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            // Sonucu yazdıracağımız TextView'ı tanımlıyoruz ve değerini veriyoruz.
            /*TextView textView =(TextView)findViewById(R.id.textView);
            if (textView != null) {
                textView.setText(_ResultValue);
            }*/

            for (MaddeClass aMaddeArray : maddeArray) {
                mMaddeListesi.add(aMaddeArray.getMaddeAdi());
                navDrawerItems.add(new NavDrawerItem(aMaddeArray.getMaddeAdi(), navMenuIcons.getResourceId(0, -1)));
                NavDrawerListAdapter.tikliMiArray.add(true);
            }
            jsonWebServiceIndex = mMaddeListesi.size();
            adapter.notifyDataSetChanged();

        }
    }
}
