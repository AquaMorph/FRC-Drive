package com.aquamorph.frcdrive;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Joy1Adapter extends BaseAdapter {

    private Context context;
    List<Integer> pictureList = new ArrayList<Integer>();

    public Joy1Adapter(Context c) {
        context = c;
        for (int i = 0; i < 8; i++) {
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (pictureList.size());
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pictureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ImageView myimage = new ImageView(context);
        myimage.setImageResource(pictureList.get(position));
        myimage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        myimage.setLayoutParams(new GridView.LayoutParams(70, 70));
        return myimage;
    }

}