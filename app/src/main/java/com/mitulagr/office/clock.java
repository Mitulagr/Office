package com.mitulagr.office;

import android.view.View;
import android.widget.TextView;

public class clock {

    int h=-1, m=-1;
    Boolean am = true;

    Boolean istime = false;

    clock(View view, TextView tv, String curr){

        TextView [] Ch = {(TextView) view.findViewById(R.id.Ch1),
                (TextView) view.findViewById(R.id.Ch2),
                (TextView) view.findViewById(R.id.Ch3),
                (TextView) view.findViewById(R.id.Ch4),
                (TextView) view.findViewById(R.id.Ch5),
                (TextView) view.findViewById(R.id.Ch6),
                (TextView) view.findViewById(R.id.Ch7),
                (TextView) view.findViewById(R.id.Ch8),
                (TextView) view.findViewById(R.id.Ch9),
                (TextView) view.findViewById(R.id.Ch10),
                (TextView) view.findViewById(R.id.Ch11),
                (TextView) view.findViewById(R.id.Ch12)};

        TextView [] Cm = {(TextView) view.findViewById(R.id.Cm00),
                (TextView) view.findViewById(R.id.Cm05),
                (TextView) view.findViewById(R.id.Cm10),
                (TextView) view.findViewById(R.id.Cm15),
                (TextView) view.findViewById(R.id.Cm20),
                (TextView) view.findViewById(R.id.Cm25),
                (TextView) view.findViewById(R.id.Cm30),
                (TextView) view.findViewById(R.id.Cm35),
                (TextView) view.findViewById(R.id.Cm40),
                (TextView) view.findViewById(R.id.Cm45),
                (TextView) view.findViewById(R.id.Cm50),
                (TextView) view.findViewById(R.id.Cm55)};

        TextView Creset = (TextView) view.findViewById(R.id.textView42);

        TextView Cam = (TextView) view.findViewById(R.id.Cam);
        TextView Cpm = (TextView) view.findViewById(R.id.Cpm);

        if(curr.length()>0){
            tv.setText(curr);
            tv.setVisibility(View.VISIBLE);
            istime = true;
            if(curr.charAt(1)==':'){
                h = Integer.parseInt(curr.substring(0,1))-1;
                m = Integer.parseInt(curr.substring(2,4))/5;
                if(curr.charAt(5)=='P') am = false;
            }
            else{
                h = Integer.parseInt(curr.substring(0,2))-1;
                m = Integer.parseInt(curr.substring(3,5))/5;
                if(curr.charAt(6)=='P') am = false;
            }
            Ch[h].setBackgroundResource(R.drawable.bagh);
            Cm[m].setBackgroundResource(R.drawable.bagm);
            if(am) Cam.setBackgroundResource(R.drawable.am);
            else Cpm.setBackgroundResource(R.drawable.am);
        }


        for(int i=0; i<12; i++){
            final int I = i;

            Ch[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!istime){
                        m=0;
                        am = true;
                        Cm[0].setBackgroundResource(R.drawable.bagm);
                        Cam.setBackgroundResource(R.drawable.am);
                    }
                    istime = true;
                    if(h!=-1){
                        Ch[h].setBackgroundResource(0);
                    }
                    Ch[I].setBackgroundResource(R.drawable.bagh);
                    h = I;
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(getTime());
                }
            });

            Cm[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!istime){
                        h=11;
                        am = true;
                        Ch[11].setBackgroundResource(R.drawable.bagh);
                        Cam.setBackgroundResource(R.drawable.am);
                    }
                    istime = true;
                    if(m!=-1){
                        Cm[m].setBackgroundResource(0);
                    }
                    Cm[I].setBackgroundResource(R.drawable.bagm);
                    m = I;
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(getTime());
                }
            });
        }

        Cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!istime){
                    h=11;
                    m=0;
                    Ch[11].setBackgroundResource(R.drawable.bagh);
                    Cm[0].setBackgroundResource(R.drawable.bagm);
                }
                istime = true;
                am = true;
                Cam.setBackgroundResource(R.drawable.am);
                Cpm.setBackgroundResource(R.drawable.pm);
                tv.setVisibility(View.VISIBLE);
                tv.setText(getTime());
            }
        });

        Cpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!istime){
                    h=11;
                    m=0;
                    Ch[11].setBackgroundResource(R.drawable.bagh);
                    Cm[0].setBackgroundResource(R.drawable.bagm);
                }
                istime = true;
                am = false;
                Cam.setBackgroundResource(R.drawable.pm);
                Cpm.setBackgroundResource(R.drawable.am);
                tv.setVisibility(View.VISIBLE);
                tv.setText(getTime());
            }
        });

        Creset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(istime){
                    Ch[h].setBackgroundResource(0);
                    Cm[m].setBackgroundResource(0);
                    Cam.setBackgroundResource(R.drawable.pm);
                    Cpm.setBackgroundResource(R.drawable.pm);
                }
                istime = false;
                tv.setText("");
                tv.setVisibility(View.GONE);
            }
        });



    }

    String getTime(){
        if(!istime) return "";
        String d = "PM";
        if(am) d = "AM";
        return (String.valueOf(h+1) + ":" + String.format("%02d ",m*5) + d);
    }

}
