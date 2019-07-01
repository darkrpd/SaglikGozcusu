package com.gencgirisimciler.saglikgozcusu.saglikgozcusu;

import android.app.SearchManager;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarSearchHandler;
import com.blunderer.materialdesignlibrary.listeners.OnSearchListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Bdeppy on 24.05.2016.
 */
public class HikayeActivity extends com.blunderer.materialdesignlibrary.activities.Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hikaye);

        Intent ResultsActivityGelenIntent = getIntent();
        String toolbarTitleName =ResultsActivityGelenIntent.getStringExtra("MaddeAdi");

        final TextView sonucTextView = (TextView)findViewById(R.id.hikayeActivitySonucTextView);
        int indexOf = ResultsActivityGelenIntent.getIntExtra("MaddeIndex",-1);

      /*  if(indexOf!=-1) {
            assert sonucTextView != null;
        }
        else {
            assert sonucTextView != null;
            sonucTextView.setText("\tAradığınız madde bulunmamaktadır, internet üzerinden aratma yapmak için tıklayınız...");
        }*/
        assert sonucTextView != null;
        sonucTextView.setText(String.format("\t%s", getMaddeIndex("maddeler.json", indexOf)));


        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        if(indexOf < MainActivity.assetsFolderIndex)
         imageView.setImageResource(R.drawable.microsd);
//        else if(indexOf>=MainActivity.assetsFolderIndex && indexOf < MainActivity.jsonWebServiceIndex)
//            imageView.setImageResource(R.drawable.cloud_connected);
        else if(indexOf >= MainActivity.assetsFolderIndex && indexOf< MainActivity.jsonWebServiceIndex) {
            imageView.setImageResource(R.drawable.cloud_connected);
            sonucTextView.setText(MainActivity.maddeArray[indexOf-MainActivity.assetsFolderIndex].getMaddeAciklamasi());
        }
        else
        {
            imageView.setImageResource(R.drawable.cloud_connected);
        }

        sonucTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sonucTextView.getText().toString().startsWith("\tAradığınız")) {
                    searchTheText();
                }
            }
        });

        sonucTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                registerForContextMenu(sonucTextView);
                return false;
            }
        });

        getSupportActionBar().setTitle(toolbarTitleName);
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarSearchHandler(this, new OnSearchListener() {

            @Override
            public void onSearched(String text) {
                Toast.makeText(getApplicationContext(),
                        "Aratılıyor :  \"" + text + "\"", Toast.LENGTH_SHORT).show();

                searchTheText();
            }

        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hikaye;
    }

    public void searchTheText ()
    {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
        // catch event that there's no activity to handle intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(HikayeActivity.this, "Uygun değil", Toast.LENGTH_LONG).show();
        }
    }
    private String getMaddeIndex(String fileName,int position)
    {
        String sonuc="" ;
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

                sonuc= jsonArray.getJSONObject(position).getString("Turkce");
             /*   for(int i =0;i<jsonArray.length();i++)
                {
                    cList.add(jsonArray.getJSONObject(i).getString("Turkce"));
                }*/
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        if(sonuc.equals(""))
            sonuc="Aradığınız madde bulunmamaktadır, internet üzerinden aratma yapmak için tıklayınız...";

        return sonuc;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //user has long pressed your TextView
        menu.add(0, v.getId(), 0, "Kopyala");

        //cast the received View to TextView so that you can get its text
        TextView yourTextView = (TextView)findViewById(R.id.hikayeActivitySonucTextView);

        //place your TextView's text in clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(yourTextView.getText());
        Toast.makeText(HikayeActivity.this, "Panoya Kopyalandı", Toast.LENGTH_SHORT).show();
    }
}
