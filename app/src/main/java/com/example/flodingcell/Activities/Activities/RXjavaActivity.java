package com.example.flodingcell.Activities.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flodingcell.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
/*
* RXJAVA  测试类
*
*
* */

public class RXjavaActivity extends AppCompatActivity {
    public   final String TAG="RXJAVA";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
       /* Observable o =Observable.from(new String[]{"1","2","3"});
        Observable observable =Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Log.i(TAG,"被观察者被使用了");
            }
        });
        Subscriber subscriber =new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                String str =(String)o;
                Log.i(TAG,str);
            }
        };
        o.subscribe(subscriber);*/
        final SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");


       for(int i=0;i<10;i++)
       Observable.timer(i, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
           @Override
           public void call(Long aLong) {
               Log.i(TAG, "call: "+simpleDateFormat.format(new Date(System.currentTimeMillis())));
           }
       });
    }
}
