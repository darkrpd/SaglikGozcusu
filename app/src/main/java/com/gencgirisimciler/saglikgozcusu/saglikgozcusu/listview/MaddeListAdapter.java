package com.gencgirisimciler.saglikgozcusu.saglikgozcusu.listview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.R;

import java.util.List;

public class MaddeListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MaddeListesi> mMaddelerListesi;
    private Activity mContext;

    public MaddeListAdapter(Activity activity, List<MaddeListesi> maddeListesi) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mMaddelerListesi = maddeListesi;
        mContext=activity;
    }

    @Override
    public int getCount() {
        return mMaddelerListesi.size();
    }

    @Override
    public MaddeListesi getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mMaddelerListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getItemName(int position)
    {
        return mMaddelerListesi.get(position).getIsim();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_layout_maddeler, null);
            holder = new ViewHolder();

            holder.textView = (TextView) convertView.findViewById(R.id.mainTextView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.simge);

            convertView.setTag(holder);

        }
        // view is recycling
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        MaddeListesi maddeListesi = mMaddelerListesi.get(position);

        holder.textView.setText(maddeListesi.getIsim());

        if(maddeListesi.isAktifMi())
            holder.imageView.setImageResource(R.drawable.check);
        else
            holder.imageView.setImageResource(R.drawable.delete);

        return convertView;
    }
    private static class ViewHolder {

        public TextView textView;
        public ImageView imageView;

    }

}

