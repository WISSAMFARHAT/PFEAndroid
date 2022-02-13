package com.example.time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class View_Guid extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout linearLayout;
    private TextView[] mDots;
    private Button preview;
    private  Button next;
    private  int mCurrentpage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_guid);
        viewPager=findViewById(R.id.View);
        linearLayout=findViewById(R.id.slide);
        preview=findViewById(R.id.button2);
        next=findViewById(R.id.button3);
        ViewAdapter viewAdapter=new ViewAdapter(this);
        viewPager.setAdapter(viewAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);




    }

    public void next(View view){
        if(mCurrentpage==2){
            Intent intent = new Intent(View_Guid.this, Main_page.class);
            startActivity(intent);
            finish();

        }else {
            mCurrentpage=mCurrentpage+1;
            addDotsIndicator(mCurrentpage);
            viewListener.onPageSelected(mCurrentpage);
            viewPager.setCurrentItem(mCurrentpage);
        }

    }
    public void preview(View v ){
        if(mCurrentpage==0){

        }else {
            mCurrentpage = mCurrentpage - 1;
            addDotsIndicator(mCurrentpage);
            viewListener.onPageSelected(mCurrentpage);
            viewPager.setCurrentItem(mCurrentpage);
        }
    }
    public void addDotsIndicator(int postion){
        mDots=new TextView[3];
        linearLayout.removeAllViews();
        for(int i =0;i < mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));
            linearLayout.addView(mDots[i]);

        }
        if(mDots.length > 0){
            mDots[postion].setTextColor(getResources().getColor(R.color.Blue));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentpage=position;

            if(position==0){
                next.setEnabled(true);
                preview.setEnabled(false);
                preview.setVisibility(View.INVISIBLE);
                next.setText("Next");
                preview.setText("");
            }else if(position==mDots.length-1){
            }else{
                next.setEnabled(true);
                preview.setEnabled(true);
                preview.setVisibility(View.VISIBLE);
                preview.setText("Preview");
                next.setText("Next");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
