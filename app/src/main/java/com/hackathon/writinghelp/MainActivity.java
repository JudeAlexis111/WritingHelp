package com.hackathon.writinghelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    private Button button;
    private ImageButton imageButton;
    private Button shop;
    public TextView textView;

    public List words = new ArrayList<>(Arrays.asList("W", "C", "L", "S", "V", "Z", "M"));
    public List strokes = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1));
    public int stroke = 1;
    public int arrayPlace = 0;
    public long score;

    public Boolean owned = false;
    public static Boolean equiped = false;

    public Boolean owned1 = false;
    public static Boolean equiped1 = false;

    public Boolean owned2 = false;
    public static Boolean equiped2 = false;

    public Boolean owned3 = false;
    public static Boolean equiped3 = false;

    public Boolean owned4 = false;
    public static Boolean equiped4 = false;

    public Boolean owned5 = false;
    public static Boolean equiped5 = false;

    public boolean musicOn = true;
    public static boolean effectOn = true;

    public MediaPlayer rightSound;
    public MediaPlayer wrongSound;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String userId = fAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PaintView paintView = new PaintView(this);
        //setContentView(paintView);
        setContentView(R.layout.activity_main);
        paintView = (PaintView) findViewById(R.id.paintView);
        //button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        //textView = (TextView) findViewById(R.id.textView);

        //fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();
        //userId = fAuth.getCurrentUser().getUid();
        imageButton = (ImageButton) findViewById(R.id.imageButton3);
        shop = (Button) findViewById(R.id.button2);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                score = (documentSnapshot.getLong("creditNum"));
                ((TextView) findViewById(R.id.textView3)).setText("Credits: "+ Long.toString(score));
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);


        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.shop_popup, null);

                Button close = (Button) mView.findViewById(R.id.button10);

                Button buyOne = (Button) mView.findViewById(R.id.button8);
                Button buyTwo = (Button) mView.findViewById(R.id.button19);
                Button buyThree = (Button) mView.findViewById(R.id.button29);
                Button buyFour = (Button) mView.findViewById(R.id.button40);
                Button buyFive = (Button) mView.findViewById(R.id.button9);
                DocumentReference  mDocumentReference = fStore.collection("users").document(userId);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                mDocumentReference = fStore.collection("users").document(userId);
                mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.e("value", String.valueOf(documentSnapshot.getBoolean("bluePen")));

                        if(documentSnapshot.getBoolean("bluePen") && equiped){
                            buyOne.setText("Dequip");
                            owned = true;
                        }

                        else if(documentSnapshot.getBoolean("bluePen")){
                            buyOne.setText("Equip");
                            owned = true;
                        }
                        else{
                            buyOne.setText("25");
                            owned = false;
                        }
                    }
                });

                mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.e("value", String.valueOf(documentSnapshot.getBoolean("greenPen")));

                        if(documentSnapshot.getBoolean("greenPen") && equiped1){
                            buyTwo.setText("Dequip");
                            owned1 = true;
                        }

                        else if(documentSnapshot.getBoolean("greenPen")){
                            buyTwo.setText("Equip");
                            owned1 = true;
                        }
                        else{
                            buyTwo.setText("50");
                            owned1 = false;
                        }
                    }
                });

                mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.e("value", String.valueOf(documentSnapshot.getBoolean("yellowPen")));

                        if(documentSnapshot.getBoolean("yellowPen") && equiped2){
                            buyThree.setText("Dequip");
                            owned2 = true;
                        }

                        else if(documentSnapshot.getBoolean("yellowPen")){
                            buyThree.setText("Equip");
                            owned2 = true;
                        }
                        else{
                            buyThree.setText("100");
                            owned2 = false;
                        }
                    }
                });

                mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.e("value", String.valueOf(documentSnapshot.getBoolean("orangePen")));

                        if(documentSnapshot.getBoolean("orangePen") && equiped3){
                            buyFour.setText("Dequip");
                            owned3 = true;
                        }

                        else if(documentSnapshot.getBoolean("orangePen")){
                            buyFour.setText("Equip");
                            owned3 = true;
                        }
                        else{
                            buyFour.setText("200");
                            owned3 = false;
                        }
                    }
                });

                mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.e("value", String.valueOf(documentSnapshot.getBoolean("redPen")));

                        if(documentSnapshot.getBoolean("redPen") && equiped4){
                            buyFive.setText("Dequip");
                            owned4 = true;
                        }

                        else if(documentSnapshot.getBoolean("redPen")){
                            buyFive.setText("Equip");
                            owned4 = true;
                        }
                        else{
                            buyFive.setText("400");
                            owned4 = false;
                        }
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buyOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference mDocumentReference = fStore.collection("users").document(userId);
                        mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (owned && equiped) {
                                    buyOne.setText("Equip");
                                    equiped = false;
                                }

                                else if(owned){
                                    buyOne.setText("Dequip");

                                    equiped = true;
                                    equiped1 = false;
                                    equiped2 = false;
                                    equiped3 = false;
                                    equiped4 = false;
                                    equiped5 = false;

                                    if(owned1){
                                        buyTwo.setText("Equip");
                                    } else{
                                        buyTwo.setText("50");
                                    }
                                    if(owned2){
                                        buyThree.setText("Equip");
                                    } else{
                                        buyThree.setText("100");
                                    }
                                    if(owned3){
                                        buyFour.setText("Equip");
                                    } else{
                                        buyFour.setText("200");
                                    }
                                    if(owned4){
                                        buyFive.setText("Equip");
                                    } else{
                                        buyFive.setText("400");
                                    }
                                }

                                else {
                                    Log.e("value", String.valueOf(documentSnapshot.getLong("creditNum")));

                                    if (documentSnapshot.getLong("creditNum") >= 25) {
                                        //Log.e("thing","inside");
                                        DocumentReference documentReference = fStore.collection("users").document(userId);

                                        Map<String, Object> scoreValue = new HashMap<>();
                                        scoreValue.put("creditNum", ( documentSnapshot.getLong("creditNum") - 25 ));
                                        scoreValue.put("bluePen", true);
                                        owned = true;
                                        buyOne.setText("Equip");

                                        documentReference.update(scoreValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Nothing
                                            }
                                        });

                                    }
                                }
                            }
                        });
                    }
                });

                buyTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference mDocumentReference = fStore.collection("users").document(userId);
                        mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (owned1 && equiped1) {
                                    buyTwo.setText("Equip");
                                    equiped1 = false;
                                }

                                else if(owned1){
                                    buyTwo.setText("Dequip");

                                    equiped1 = true;

                                    equiped = false;
                                    equiped2 = false;
                                    equiped3 = false;
                                    equiped4 = false;
                                    equiped5 = false;

                                    if(owned){
                                        buyOne.setText("Equip");
                                    } else{
                                        buyOne.setText("25");
                                    }
                                    if(owned2){
                                        buyThree.setText("Equip");
                                    } else{
                                        buyThree.setText("50");
                                    }
                                    if(owned3){
                                        buyFour.setText("Equip");
                                    } else{
                                        buyFour.setText("100");
                                    }
                                    if(owned4){
                                        buyFive.setText("Equip");
                                    } else{
                                        buyFive.setText("200");
                                    }
                                }

                                else {
                                    Log.e("value", String.valueOf(documentSnapshot.getLong("creditNum")));

                                    if (documentSnapshot.getLong("creditNum") >= 50) {
                                        //Log.e("thing","inside");
                                        DocumentReference documentReference = fStore.collection("users").document(userId);

                                        Map<String, Object> scoreValue = new HashMap<>();
                                        scoreValue.put("creditNum", ( documentSnapshot.getLong("creditNum") - 50 ));
                                        scoreValue.put("greenPen", true);
                                        owned = true;
                                        buyTwo.setText("Equip");

                                        documentReference.update(scoreValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Nothing
                                            }
                                        });

                                    }
                                }
                            }
                        });
                    }
                });



                dialog.show();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.pause_popup, null);

                Button quit = (Button) mView.findViewById(R.id.button3);
                Button resume = (Button) mView.findViewById(R.id.button4);
                CheckBox checkBox = (CheckBox) mView.findViewById(R.id.checkBox2);
                CheckBox checkBoxTwo = (CheckBox) mView.findViewById(R.id.checkBox3);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                if(musicOn){
                    checkBox.setChecked(true);
                }

                else{
                    checkBox.setChecked(false);
                }


                if(effectOn){
                    checkBoxTwo.setChecked(true);
                }

                else{
                    checkBoxTwo.setChecked(false);
                }

                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Menu.class));
                    }
                });

                resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkBox.isChecked()){
                            musicOn = true;
                        }

                        else{
                            musicOn = false;
                        }
                    }
                });

                checkBoxTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("check", String.valueOf(effectOn));
                        if(checkBoxTwo.isChecked()){
                            effectOn = true;
                        }

                        else{
                            effectOn = false;
                        }
                    }
                });


                dialog.show();
            }
        });


        //button.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        paintView.clear();
        //    }
        //});

        //handleText();


        textView.setText("W");
    }


    public void handleText(Context context, String string){

        if (rightSound != null) {
            rightSound.stop();
            rightSound.release();
            rightSound = null;
        }

        if (wrongSound != null) {
            wrongSound.stop();
            wrongSound.release();
            wrongSound = null;
        }

        rightSound = MediaPlayer.create(context, R.raw.right);
        wrongSound = MediaPlayer.create(context, R.raw.wrong);

        //words.add("W");
        //words.add("C");
        //words.add("L");
        //words.add("S");
        //words.add("V");
        //words.add("Z");
        //words.add("M");

        words = new ArrayList<>(Arrays.asList("W", "C", "L", "S", "V", "Z", "M"));
        strokes = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1));

        ((TextView) ((Activity)context).findViewById(R.id.textView)).setText((String) words.get(arrayPlace));

        if(string.equals((String) words.get(arrayPlace))){
            //Log.e("thing","correct");

            if((words.size() -1) <= arrayPlace){
                arrayPlace = 0;
            }
            else{
                arrayPlace = arrayPlace + 1;
            }

            ((TextView) ((Activity)context).findViewById(R.id.textView)).setText((String) words.get(arrayPlace));
            ((PaintView) ((Activity)context).findViewById(R.id.paintView)).RightClear();

            //score = score + 1;
            DocumentReference documentReference = fStore.collection("users").document(userId);

            Map<String,Object> scoreValue = new HashMap<>();
            scoreValue.put("creditNum", (Integer.parseInt((((TextView) ((Activity)context).findViewById(R.id.textView3)).getText().toString()).replaceAll("[^\\d.]", "")) + 1));

            documentReference.update(scoreValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Nothing
                }
            });

            //Log.e("sound", String.valueOf(effectOn));

            if(MainActivity.effectOn){
                rightSound.start();
            }
        }
        else{
            ((PaintView) ((Activity)context).findViewById(R.id.paintView)).WrongClear();
            string = "";
            //paintView.clear();

            //Log.e("sound", String.valueOf(effectOn));

            if(MainActivity.effectOn){
                wrongSound.start();
            }
        }

        //TextView txtView = (( Activity)MainActivity).findViewById()
        //Log.e("this", Long.toString(score));
        //((TextView) ((Activity)context).findViewById(R.id.textView3)).setText("Credits: "+ Long.toString(score));
    }

}
