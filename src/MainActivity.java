package com.example.sloje;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.google.android.material.canvas.CanvasCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView iview = null;
    Button but;
    Button but2;
    Bitmap bitmap = null;
    int i = 0;
    boolean first_pick = true;
    double [] i1 = new double[2];
    double [] i2 = new double[2];
    List<Integer> sums = new ArrayList<>();
    Canvas canvas = null;


    SeekBar x, y;
    EditText x_n, y_n;
    RadioButton first_point, second_point;
    int last_x_g = 0, last_y_g = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

        iview = findViewById(R.id.imageView);
        but = findViewById(R.id.go);

        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        x_n = findViewById(R.id.x_n);
        y_n = findViewById(R.id.y_n);
        first_point = findViewById(R.id.first_point);
        second_point = findViewById(R.id.second_point);
        x.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(first_pick){
                    i1[0] = x.getProgress();
                    x_n.setText(String.valueOf(i1[0]));
                }else{
                    i2[0] = x.getProgress();
                    x_n.setText(String.valueOf(i2[0]));
                }
                last_x_g = x.getProgress();
                draw();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
         y.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(first_pick){
                    i1[1] = y.getProgress();
                    y_n.setText(String.valueOf(i1[1]));
                }else{
                    i2[1] = y.getProgress();
                    y_n.setText(String.valueOf(i2[1]));

                }
                last_y_g = y.getProgress();
                draw();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        first_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_pick = first_point.isChecked();
            }
        });

        second_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_pick = first_point.isChecked();
            }
        });

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean go = true;
                        int last_count = 0;
                        int last_last_count = 0;

                        int last_id = 0;
                        int last_last_id = 0;

                        int id = 0;

                        int y = 0;
                        while (go) {
                            double a = (i1[1] - i2[1]) / (i1[0] - i2[0]);
                            double b = i2[1] - (a * i2[0]);
                            int cc = 0;
                            sums.clear();
                            for (double x = 0; x < bitmap.getWidth(); x++) {
                                int px = bitmap.getPixel((int) x, y);
                                int red = Color.red(px);
                                int green = Color.green(px);
                                int blue = Color.blue(px);
                                int sum = red + green + blue;
                                cc++;
                                sums.add(sum);
                            }
                            int pick = sums.get(0);
                            int counter = 0;

                            List<Integer> min = new ArrayList<>();
                            List<Integer> max = new ArrayList<>();
                            max.add(101);
                            max.add(0);
                            boolean up = false;
                            boolean sl = false;
                            int middle_counter = 0;
                            int downCounter = 0;
                            int upCounter = 0;
                            int lastHigh = 0;
                            int lastDown = 0;
                            int last = 0;
                            boolean next_dont_add = false;
                            for (int x = 1; x < bitmap.getWidth(); x++) {
                                if(Math.abs(sums.get(x-1)-sums.get(x))>70){
                                    if(middle_counter>30){
                                        System.out.println("SRODEK");
                                    }
                                    if(middle_counter==0 && max.get(max.size()-1)!=0){
                                        middle_counter++;
                                    }else if(middle_counter>0 && max.get(max.size()-1)==0){
                                        middle_counter++;
                                    }else{
                                        middle_counter = 0;
                                    }
                                }
                                if (up) {
                                    if (pick <= sums.get(x)) {
                                        max.add(0);
                                        upCounter++;
                                    } else {

                                        if (pick - lastDown > 80) {
                                            counter++;
                                            max.add(pick);
                                        } else if (pick - lastDown <= 80 && upCounter == 1 && !next_dont_add) {
                                            counter++;
                                            max.add(pick);
                                        } else {
                                            max.add(0);
                                        }
                                        next_dont_add = false;
                                        downCounter++;
                                        lastHigh = pick;
                                        lastDown = 0;
                                        upCounter = 0;
                                        up = false;

                                        //}
                                    }
                                } else {
                                    if (pick > sums.get(x)) {
                                        downCounter = 12;
                                        next_dont_add = false;
                                        max.add(0);
                                    } else {
                                        if (downCounter == 1) {
                                            {
                                                counter--;
                                                max.remove(max.size() - 1);
                                                max.add(0);
                                            }
                                            if (lastHigh - pick < 80) {
                                                next_dont_add = true;
                                            } else {
                                                next_dont_add = false;
                                            }
                                        }
                                        max.add(0);
                                        min.add(x);
                                        up = true;
                                        downCounter = 0;
                                        upCounter++;
                                        lastDown = pick;
                                    }
                                }
                                last = pick;
                                pick = sums.get(x);
                            }


                            switch (y) {
                                case 0:
                                    last_last_count = counter;
                                    break;
                                case 1:
                                    last_count = counter;
                                    break;
                                default:
                                    if (last_last_count > last_count && last_count >= counter) {
                                        if(last_last_id<last_last_count) {
                                            id = y;
                                            last_last_id = last_last_count;

                                        }

                                    } else {

                                        last_last_count = last_count;
                                        last_count = counter;
                                    }
                            }
                            y += 1;

                            if (y >= bitmap.getHeight()) {
                                go = false;
                            }

                        }
                        dialog dialog = new dialog();

                        Bundle data = new Bundle();
                        data.putString("title", "Ilość słoji to :");
                        data.putString("text", String.valueOf(last_count));

                        dialog.setArguments(data);
                        dialog.show(getSupportFragmentManager(), "D");
                    }

                });
                thread.start();
//                System.out.println("SUMA");
//                System.out.println(i1[0] +" "+ i1[1]);
//                System.out.println(i2[0]+" "+i2[1]);
//                if(i2[0]<i1[0]){
//                    double i1x = i1[0];
//                    double i1y = i1[1];
//
//                    i1[0]=i2[0];
//                    i1[1]=i2[1];
//                    i2[0]=i1x;
//                    i2[1]=i1y;
//                }
//
//                double a = (i1[1]-i2[1])/(i1[0]-i2[0]);
//                double b = i2[1]-(a*i2[0]);
//                int cc = 0;
//                for(double x = i1[0]; x <= i2[0]; x++){
//                    int px = bitmap.getPixel((int)x,          (int)(x*a+b));
//                    int red = Color.red(px);
//                    int green = Color.green(px);
//                    int blue = Color.blue(px);
//                    int sum = red+green+blue;
//                    cc++;
//                    sums.add(sum);
//                }
//                int pick = sums.get(0);
//                int counter = 0;
//
//                List<Integer> min = new ArrayList<>();
//                List<Integer> max = new ArrayList<>();
//                max.add(101);
//                max.add(0);
//                boolean up = false;
//                int downCounter = 0;
//                int upCounter = 0;
//                int lastHigh = 0;
//                int lastDown = 0;
//                int last = 0;
//                boolean next_dont_add = false;
//                for(int x = 1; x < sums.size(); x++){
//
//                    if(up){
//                        if(pick<=sums.get(x)){
//                            //System.out.println("POINT "+0);
//                            max.add(0);
//                            upCounter++;
//                        }else{
//
//                            if(pick-lastDown>80) {
//                                counter++;
//                                max.add(pick);
//                            }else if(pick-lastDown<=80 && upCounter==1 && !next_dont_add){
//                                counter++;
//                                max.add(pick);
//                            }else{
//                                max.add(0);
//                            }
//                            next_dont_add=false;
//                            downCounter++;
//                            lastHigh = pick;
//                            lastDown=0;
//                            upCounter=0;
//                            up = false;
//
//                                //}
//                        }
//                    }else{
//                        if(pick>sums.get(x)){
//                            downCounter=12;
//                            next_dont_add=false;
//                            max.add(0);
//                        }else{
//                            if(downCounter==1){
////                                System.out.println("REMOVING");
////                                System.out.println(lastHigh<=sums.get(x));
////                                System.out.println(lastHigh+" "+sums.get(x));
//                                if(lastHigh-pick<80 /*&& lastHigh<=sums.get(x)*/){
//                                    counter--;
//                                    max.remove(max.size()-1);
//                                    max.add(0);
//                                } if(lastHigh-pick<80 ){
//                                        next_dont_add = true;
//                                }else{
//                                    next_dont_add = false;
//                                }
//                            }
//                            max.add(0);
//                            min.add(x);
//                            up = true;
//                            downCounter=0;
//                            upCounter++;
//
//                            lastDown = pick;
//                            //System.out.println("LAST DOWN " +lastDown);
//                            //System.out.println("IF "+(pick-lastDown>80));
//                            //System.out.println("X "+(x-1));
//                        }
//                    }
//                    last = pick;
//                    pick=sums.get(x);
//                }
//                System.out.println("Ilość słoji to : ");
//                System.out.println(counter);
//                System.out.println("FF");
//                //System.out.println(min.size());
//                System.out.println(max.size());
//                for (int d: max){
//                    System.out.println("HH "+d);
//                }
//                //System.out.println("Średnia :"+srUPD+" "+srDOWND);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("DATA");
        System.out.println(data);
        if(data!=null){

            iview.setImageURI((Uri)data.getData());
            bitmap = ((BitmapDrawable)iview.getDrawable()).getBitmap();
            x.setMax(bitmap.getWidth());
            y.setMax(bitmap.getHeight());
            Bitmap copy = bitmap.copy(Bitmap.Config.RGB_565, true);
            canvas = new Canvas(copy);
            Paint paint = new Paint();
            paint.setStrokeWidth(9);
            paint.setColor(Color.BLACK);
            canvas.drawLine(0,0,400, 400, paint);

            iview.setImageBitmap(copy);

        }
    }

    public void draw(){
        Bitmap copy = bitmap.copy(Bitmap.Config.RGB_565, true);
        canvas = new Canvas(copy);
        Paint paint = new Paint();
        paint.setStrokeWidth(9);
        paint.setColor(Color.GREEN);
        canvas.drawPoint(last_x_g,last_y_g, paint);

        iview.setImageBitmap(copy);
    }
    public void draw1(int y){
        Bitmap copy = bitmap.copy(Bitmap.Config.RGB_565, true);
        canvas = new Canvas(copy);
        Paint paint = new Paint();
        paint.setStrokeWidth(9);
        paint.setColor(Color.GREEN);
        canvas.drawPoint(10,y, paint);

        iview.setImageBitmap(copy);
    }
}