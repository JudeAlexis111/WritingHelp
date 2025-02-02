package com.hackathon.writinghelp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Debug;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


public class PaintView extends View {

    public static int BRUSH_SIZE = 20;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    public boolean cc = false;
    private Path mPath;
    private Path path = new Path();
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    public String string;
    MainActivity mainActivity = new MainActivity();

    //private TextView text = (TextView) findViewById(R.id.textView);

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        Log.e("helloJello", "alive");

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

    public void emboss() {
        emboss = true;
        blur = false;
    }

    public void blur() {
        emboss = false;
        blur = true;
    }

    public void WrongClear() {
        //backgroundColor = DEFAULT_BG_COLOR;
        //paths.clear();
        //mCanvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.RED);
        mCanvas.drawPath(path,mPaint);

        (new Handler()).postDelayed(this::clear, 100);

        //path.reset();
        //invalidate();
        //mainActivity.handleText(this.getContext(), "");

    }

    public void RightClear() {
        //backgroundColor = DEFAULT_BG_COLOR;
        //paths.clear();
        //mCanvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.GREEN);
        mCanvas.drawPath(path,mPaint);

        (new Handler()).postDelayed(this::clear, 100);

        //path.reset();
        //invalidate();
        //mainActivity.handleText(this.getContext(), "");

    }

    public  void clear(){
        path.reset();
        invalidate();
        //mainActivity.handleText(this.getContext(), "");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        //mCanvas.drawColor(backgroundColor);
        mCanvas.drawColor(Color.WHITE);

        for (FingerPath fp : paths) {
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(8f);
            mPaint.setMaskFilter(null);

            if (fp.emboss)
                mPaint.setMaskFilter(mEmboss);
            else if (fp.blur)
                mPaint.setMaskFilter(mBlur);
        }

        if(mainActivity.equiped){
            mPaint.setColor(Color.BLUE);
        }
        else if(mainActivity.equiped1){
            mPaint.setColor(Color.GREEN);
        }
        else if(mainActivity.equiped2){
            mPaint.setColor(Color.parseColor("#f0d530"));
        }
        else if(mainActivity.equiped3){
            mPaint.setColor(Color.parseColor("#ffa501"));
        }
        else if(mainActivity.equiped4){
            mPaint.setColor(Color.RED);
        }
        else{
            mPaint.setColor(Color.BLACK);
        }

        mPaint.setStrokeWidth(45f);

        //canvas.drawPath(path, mPaint);
        mCanvas.drawPath(path,mPaint);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();


    }

    private void processTextRecognitionResult(FirebaseVisionText texts){
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();

        if(blocks.size() == 0){
            //Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
            Log.e("thing","text not found");
            string = "Text Not Found";
            //mainActivity.onUpdate("Text Not Found");
            mainActivity.handleText(this.getContext(), "");
            return;
        }

        Log.e("thing","text found");

        for (int i = 0; i < blocks.size(); i++){
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++){
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for(int k = 0; k < elements.size(); k++){
                    Log.e("thing", elements.get(k).getText());
                    string = elements.get(k).getText();
                    mainActivity.handleText(this.getContext(), elements.get(k).getText());
                    //mainActivity.onUpdate(elements.get(k).getText());
                }
            }
        }

    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE :
                path.lineTo(x, y);
                break;
            default:
                Log.e("thing","up");

                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mBitmap);
                FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        processTextRecognitionResult(firebaseVisionText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
                return false;
        }
        postInvalidate();
        return false;
    }
}