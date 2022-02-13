package com.example.time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private  Integer[] images={R.drawable.category_app,R.drawable.trak_app,R.drawable.statice_app ,};
    private  String[] head={"Set categories goals and keep track of your progress","Check your phone stats and fight your online time wasting ","See the analytics for all your time use and balance your life "};
    private  Integer[] imagess={R.drawable.image1,R.drawable.images2,R.drawable.images3};



    public ViewAdapter(Context context){
        this.context=context;
                                     }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.view,container,false);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageView5);
        TextView header=view.findViewById(R.id.header);
        ImageView imageView1=(ImageView)view.findViewById(R.id.imageView6);
        imageView.setImageResource(images[position]);
        imageView1.setImageResource(imagess[position]);
        header.setText(head[position]);
        ViewPager vp=(ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager) container;
        View view=(View) object;
        vp.removeView(view);
    }
}
