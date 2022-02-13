package com.example.time.ui.Calander;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.time.DatabaseEvent;
import com.example.time.Mycalander;
import com.example.time.Myservice;
import com.example.time.R;
import com.example.time.Service_Block;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalanderFragment extends Fragment {

    int id = -1;
    int today = -1;
    int j = 0;
    TimePickerDialog mTimeSetListener;
    Date dt;
    int hourOfDays=0,minuteofday=0;
    CompactCalendarView compactCalendar;
    Dialog myDialog;
    Dialog myDialog1;
    Button event;
    EditText txtevent;
    TextView viewevent,date_click,txt_time;
    String txt="";
    TextView description, day, holiday,close;
    List<Event> ev1;
    List<Event> ev2;
    ArrayList<Event> db;
    Toolbar tools;
    Long date_selected;
    int id_date=-1;
    public static DatabaseEvent mydb;
    private SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault());
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calander_event, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.event){

            if(id_date==-1) {
                addEvent(date_selected);
            }else{
                if(ev1.get(id_date).getColor()==Color.BLACK){
                    DeleteEvent(ev1.get(id_date).getTimeInMillis(),ev1.get(id_date).getData().toString(),id_date);

                }else{
                    Toast.makeText(getContext(),"This is event Holiday  public ",Toast.LENGTH_LONG).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        myDialog = new Dialog(getContext());
        myDialog1=new Dialog(getContext());
        mydb=new DatabaseEvent(getContext());
        db=new ArrayList<>();
        long days = cal.getTimeInMillis();
        View root = inflater.inflate(R.layout.fragment_calander, container, false);
        holiday=root.findViewById(R.id.events);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(simpleDateFormat.format(cal.getTime()));
        compactCalendar = root.findViewById(R.id.calander);
        compactCalendar.setUseThreeLetterAbbreviation(true);

//-------------------------------------------------------------------------------------------------------------
        ev2=new ArrayList<>();
        ev1 = new ArrayList<>();
         db=mydb.selectall();


        for(int e=0;e<db.size();e++){

            ev2.add(new Event(Color.BLACK,db.get(e).getTimeInMillis(),db.get(e).getData()));
        }
        if(ev2.size()!=0){
            ev1.addAll(ev2);
            compactCalendar.addEvents(ev1);
        }

        //set a event
//----------------------------- event holiday static -----------------------------------------------------------

        ev1.add(new Event(Color.BLUE, 1577829600000l, "New Year's Day"));
        ev1.add(new Event(Color.BLUE, 1578261600000l, "Armenian Orthodox Christmas Day"));
        ev1.add(new Event(Color.BLUE, 1581199200000l, "St Maroun Day"));
        ev1.add(new Event(Color.BLUE, 1581631200000l, "Rafick Hariri Memorial Day"));
        ev1.add(new Event(Color.BLUE, 1585087200000l, "Feast of the Annunciation"));
        ev1.add(new Event(Color.BLUE, 1586466000000l, "Good Friday(Western Church)"));
        ev1.add(new Event(Color.BLUE, 1586638800000l, "Easter Sunday"));
        ev1.add(new Event(Color.BLUE, 1586725200000l, "Easter Monday"));
        ev1.add(new Event(Color.BLUE, 1587330000000l, "Orthodox Easter Monday"));
        ev1.add(new Event(Color.BLUE, 1588280400000l, "Labor Day"));
        ev1.add(new Event(Color.BLUE, 1590267600000l, "Eid Al-Fitr"));
        ev1.add(new Event(Color.BLUE, 1590354000000l, "Resistance and Liberation Day"));
        ev1.add(new Event(Color.BLUE, 1597438800000l, "Assumption of Mary"));
        ev1.add(new Event(Color.BLUE, 1597870800000l, "Hijri New Year"));
        ev1.add(new Event(Color.BLUE, 1598648400000l, "Ashoura"));
        ev1.add(new Event(Color.BLUE, 1603922400000l, "Birthday of Prophet Muhammmad"));
        ev1.add(new Event(Color.BLUE, 1605996000000l, "Independence Day"));
        ev1.add(new Event(Color.BLUE, 1608847200000l, "Christmas Day"));
        compactCalendar.addEvents(ev1);

////////// notification ----------------------------------------------------------------
        Calendar ca=Calendar.getInstance();

        for(int i=0;i<ev1.size();i++) {
            if(ev1.get(i).getTimeInMillis()>ca.getTimeInMillis()) {
                ca.setTimeInMillis(ev1.get(i).getTimeInMillis());
                Intent intent = new Intent(getActivity(), Mycalander.class);
                intent.putExtra("event",ev1.get(i).getData().toString());
                intent.putExtra("time",cal.getTimeInMillis());
//                long futureInMillis = SystemClock.elapsedRealtime() + delay;
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,ev1.get(i).getTimeInMillis(),pendingIntent);
                i=ev1.size();
             }
        }

//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------

        for (int i = 0; i < ev1.size(); i++) {
            if (days == (ev1.get(i).getTimeInMillis())) {
                today = i;

            }
        }
        if (today != -1) {

            if(ev1.get(today).getColor()==Color.BLACK){

                holiday.setText(ev1.get(today).getData().toString());

            }else {
                holiday.setText(ev1.get(today).getData().toString());
            }
            today = -1;

        } else {
            holiday.setText("No event");

        }


        //---------------------------------------------------------------------------------------------------


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getActivity().getApplicationContext();
                date_selected=dateClicked.getTime();

                for (int i = 0; i < ev1.size(); i++) {
                    Calendar temp=Calendar.getInstance();
                    temp.setTimeInMillis(ev1.get(i).getTimeInMillis());
                    temp.set(Calendar.HOUR_OF_DAY,0);
                    temp.set(Calendar.MINUTE,0);
                    temp.set(Calendar.SECOND,0);
                    if (dateClicked.getTime() == temp.getTimeInMillis()) {
                        id = i;
                    }
                }
                if (id != -1) {
                    if(ev1.get(id).getColor()==Color.BLACK){
                        holiday.setText(ev1.get(id).getData().toString());

                    }else {
                        holiday.setText(ev1.get(id).getData().toString());
                    }
                    id_date=id;
                    id = -1;

                } else {
                    Toast.makeText(context, "No event  ", Toast.LENGTH_LONG).show();
                    holiday.setText("No event");
                    id_date=-1;


                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(simpleDateFormat.format(firstDayOfNewMonth));
            }
        });


        return root;


    }

    public void DeleteEvent(final  Long Time, final String date, final int index){
        myDialog1.setContentView(R.layout.pop_delevent);
        myDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog1.show();
        close=myDialog1.findViewById(R.id.exit);
        event=myDialog1.findViewById(R.id.addevent);
        viewevent=myDialog1.findViewById(R.id.txtevent);
        viewevent.setText(date);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1.dismiss();
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydb.deleteData(""+Time);
                ev1.remove(index);
                myDialog1.dismiss();
                compactCalendar.removeAllEvents();
                compactCalendar.addEvents(ev1);


            }
        });

    }

    public void addEvent(final Long Time) {
        myDialog.setContentView(R.layout.pop_event);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        close=myDialog.findViewById(R.id.exit);
        event=myDialog.findViewById(R.id.addevent);
        txtevent=myDialog.findViewById(R.id.txtevent);
        txt_time=myDialog.findViewById(R.id.txt_time);
        date_click=myDialog.findViewById(R.id.date_click);

        date_click.setText(DateFormat.format("dd/MM/yyyy", new Date(date_selected)).toString());
        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTimeSetListener=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hourOfDays=hourOfDay;
                        minuteofday=minute;
                        txt_time.setText(" "+hourOfDay+" : "+minute);
                    }
                },0,0,true);
                mTimeSetListener.show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }


        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt=txtevent.getText().toString();
                if(!txt.equals("")){

                        Calendar date = Calendar.getInstance();
                        date.setTimeInMillis(Time);
                        date.set(Calendar.HOUR_OF_DAY,hourOfDays);
                        date.set(Calendar.MINUTE,minuteofday);

                    ev1.add(new Event(Color.BLACK, Time, txt));
                    compactCalendar.addEvents(ev1);
                    mydb.insertData(""+date.getTimeInMillis(),txt);
                    txt="";
                    myDialog.dismiss();

                }else{

                    Toast.makeText(getContext(),"Put a event ",Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    }


