package hu.ciwsduino.ultrasonic;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thecy on 2016. 04. 05..
 */
public class UltrasonicView extends View {

    public final static int MAX_ELEM = 120; //120°
    public final static int MAX_TAV = 500; //500cm
    public final static int SZOG_LEPTEK = 30;
    public final static int TAV_LEPTEK = 50;

    public final static int szel_margo=100;
    public final static int mag_margo=130;
    public final static int betu_mag_offset=70;
    public final static int betu_szel_offset=60;

    public final static int haromszogUtvonal_view=10;

    Paint mireFest, mireFest2, mireFest3;
    List<Float> adatok;
    List<Point> pontok;
    int aktSzog=(180-MAX_ELEM)/2;
    int elsoSzog = aktSzog;
    int hatsoSzog=aktSzog;
    Path utvonal, haromszogUtvonal;

    private void beallitas(){
        mireFest=new Paint();
        mireFest2=new Paint();
        mireFest3=new Paint();
        //háttérszínt majd XML-ben állítunk
        mireFest.setAntiAlias(true);
        mireFest2.setAntiAlias(true);
        mireFest2.setStrokeJoin(Paint.Join.ROUND);
        mireFest3.setAntiAlias(true);
        //kezdeti feltöltés 0-ákkal
        adatok=new ArrayList<Float>(MAX_ELEM);
        for (int i=0;i<MAX_ELEM;i++){
            adatok.add(0f);
        }
        pontok=new ArrayList<Point>(MAX_ELEM);
        for (int i=0;i<MAX_ELEM;i++){
            pontok.add(new Point(0, 0));
        }
        utvonal=new Path();
        haromszogUtvonal=new Path();
    }

    public UltrasonicView(Context context) {
        super(context);
        beallitas();
    }

    public UltrasonicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        beallitas();
    }

    public UltrasonicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        beallitas();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UltrasonicView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        beallitas();
    }

    public void UjErtek(int szog, float tav, int irany){
        aktSzog=szog;
        adatok.set(szog, tav);

        int kulonbseg=0;

        if(irany==1) {
            if (szog <= haromszogUtvonal_view / 2) {
                hatsoSzog=0;
                elsoSzog=aktSzog+haromszogUtvonal_view/2;
            } else if (szog>= MAX_ELEM-haromszogUtvonal_view /2){
                elsoSzog=MAX_ELEM;
                //hatsoSzog=aktSzog-haromszogUtvonal_view/2;
                hatsoSzog=aktSzog-(MAX_ELEM-aktSzog);
            } else {
                elsoSzog=aktSzog+haromszogUtvonal_view/2;
                hatsoSzog=aktSzog-haromszogUtvonal_view/2;
            }
        } else {
            if (szog >= MAX_ELEM - haromszogUtvonal_view / 2) {
                hatsoSzog=MAX_ELEM;
                elsoSzog=aktSzog-haromszogUtvonal_view/2;
            } else  if (szog <= haromszogUtvonal_view / 2) {
                elsoSzog=0;
                //hatsoSzog=aktSzog+haromszogUtvonal_view/2;
                hatsoSzog=aktSzog+(0+aktSzog);
            } else {
                elsoSzog=aktSzog-haromszogUtvonal_view/2;
                hatsoSzog=aktSzog+haromszogUtvonal_view/2;
            }
        }
        //jelezzük, hogy új értékkor újra kell majd rajzoltatni
        //invalidate();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //olyanokat mérünk csak itt, ami folyton változik
        int szel=getWidth()-szel_margo;
        int mag=getHeight()-mag_margo;

        int kezdSzog=180+((180-MAX_ELEM)/2);
        int vegSzog=kezdSzog+MAX_ELEM;

        int scaledSize = getResources().getDimensionPixelSize(R.dimen.font_size);
        mireFest.setTextSize(scaledSize);
        mireFest.setTextAlign(Paint.Align.CENTER);
        mireFest.setStyle(Paint.Style.STROKE);

        //körívek
        for (int tav=0;tav<MAX_TAV;tav+=TAV_LEPTEK){ //500cm : 0..500cm
            //mireFest.setColor(Color.GREEN);
            mireFest.setStrokeWidth(7f);
            mireFest.setColor(ContextCompat.getColor(getContext(),R.color.dark_green));
            int param1 = (szel/2)*tav/MAX_TAV;
            int param2 = 2*(mag/2)*tav/MAX_TAV; //ezt kétszer kell venni a dupla magasság miatt
            RectF r = new RectF(param1+szel_margo/2, param2+mag_margo, szel-param1+szel_margo/2, 2*mag-param2+mag_margo); //2mag, hogy kitöltse a képet
            canvas.drawArc(r, kezdSzog, MAX_ELEM, false, mireFest);
            //méter szöveg
            param1 = (szel/2)*(tav+TAV_LEPTEK)/MAX_TAV; //azért kell +100 mert a következő léptékhez tartozik
            param2 = 2*(mag/2)*(tav+TAV_LEPTEK)/MAX_TAV;
            mireFest.setStrokeWidth(2f);
            mireFest.setColor(Color.GREEN);
            double parameter = kezdSzog * Math.PI / 180;
            float stopX = (float) (szel/2 + param1 * Math.cos(parameter));
            float stopY = (float) (mag + param2 * Math.sin(parameter));
            canvas.drawText(((tav*1f+TAV_LEPTEK)/100) + "m", stopX + szel_margo / 2- betu_szel_offset, stopY + mag_margo+betu_mag_offset/2, mireFest);
            parameter = vegSzog * Math.PI / 180;
            stopX = (float) (szel/2 + param1 * Math.cos(parameter));
            stopY = (float) (mag + param2 * Math.sin(parameter));
            canvas.drawText(((tav*1f+TAV_LEPTEK)/100)+"m",stopX+szel_margo/2+betu_szel_offset,stopY+mag_margo+betu_mag_offset/2,mireFest);
        }
        //körcikk vonalak
        for (int szog=kezdSzog;szog<=vegSzog;szog+=SZOG_LEPTEK){ //120° : 29..151°
            //vonal végpont az ellipszis egyenlete
            mireFest.setStrokeWidth(7f);
            mireFest.setColor(ContextCompat.getColor(getContext(),R.color.dark_green));
            double parameter = szog * Math.PI / 180;
            float stopX = (float) (szel/2 + szel/2 * Math.cos(parameter));
            float stopY = (float) (mag + mag * Math.sin(parameter)); //(mag/2)*2, azaz mag kell, mert fent az eltolás miatt 2mag van
            canvas.drawLine(szel/2  +szel_margo/2, mag+mag_margo, stopX +szel_margo/2, stopY+mag_margo, mireFest);
            //fok szöveg
            mireFest.setStrokeWidth(2f);
            mireFest.setColor(Color.GREEN);
            canvas.drawText(szog-180+"°",stopX+szel_margo/2,stopY+mag_margo-betu_mag_offset,mireFest);
        }

        //mert ertekek
        for(int i=0; i<MAX_ELEM; i++){
            mireFest2.setStrokeWidth(7f);
            mireFest2.setColor(Color.YELLOW);
            double parameter = (180+i+30) * Math.PI / 180;
            float stopX = (float) (szel/2 + (adatok.get(i)/MAX_TAV)*szel/2 * Math.cos(parameter));
            float stopY = (float) (mag + (adatok.get(i)/MAX_TAV)*mag * Math.sin(parameter)); //(mag/2)*2, azaz mag kell, mert fent az eltolás miatt 2mag van
            pontok.set(i, new Point((int) stopX, (int) stopY));
            canvas.drawLine(szel / 2 + szel_margo / 2, mag + mag_margo, stopX + szel_margo / 2, stopY+mag_margo, mireFest2);
        }

        //mert ertekek egybefüggő
        double parameter = kezdSzog * Math.PI / 180;
        float stopX2 = (float) (szel/2 + szel/2 * Math.cos(parameter));
        float stopY2 = (float) (mag + mag * Math.sin(parameter)); //(mag/2)*2, azaz mag kell, mert fent az eltolás miatt 2mag van
        mireFest2.setColor(Color.BLUE);
        mireFest2.setStyle(Paint.Style.STROKE);
        utvonal.reset();
        utvonal.moveTo(stopX2 + szel_margo / 2, stopY2 + mag_margo);
        //utvonal.lineTo(stopX2 + szel_margo / 2, stopY2 + mag_margo);
        for (int i=0; i<MAX_ELEM; i++){
            utvonal.lineTo((pontok.get(i)).x+szel_margo/2, (pontok.get(i)).y+mag_margo);
        }
        parameter = vegSzog * Math.PI / 180;
        stopX2 = (float) (szel / 2 + szel / 2 * Math.cos(parameter));
        stopY2 = (float) (mag + mag * Math.sin(parameter)); //(mag/2)*2, azaz mag kell, mert fent az eltolás miatt 2mag van
        utvonal.lineTo(stopX2 + szel_margo / 2, stopY2 + mag_margo);
        //utvonal.moveTo(stopX2 + szel_margo / 2, stopY2 + mag_margo);
        canvas.drawPath(utvonal, mireFest2);

        //hol tartunk átmenetes radar 3szög
        mireFest3.setStrokeWidth(7f);
        //mireFest3.setColor(0xff40f078);
        mireFest3.setARGB(125,80,240,120);
        mireFest3.setStyle(Paint.Style.FILL_AND_STROKE);
        //mireFest3.setShader(new LinearGradient(0,0,szel,mag,0x00000000,0xff40f078, Shader.TileMode.CLAMP));
        int startX = szel/2;
        int startY =mag;
        haromszogUtvonal.reset();
        haromszogUtvonal.moveTo(startX + szel_margo / 2, startY + mag_margo);
        parameter = (180+elsoSzog+30) * Math.PI / 180;
        stopX2 = (float) (szel/2 + szel/2 * Math.cos(parameter));
        stopY2 = (float) (mag + mag * Math.sin(parameter)); //(mag/2)*2, azaz mag kell, mert fent az eltolás miatt 2mag van
        haromszogUtvonal.lineTo(stopX2 + szel_margo / 2, stopY2 + mag_margo);
        parameter = (180+hatsoSzog+30) * Math.PI / 180;
        stopX2 = (float) (szel/2 + szel/2 * Math.cos(parameter));
        stopY2 = (float) (mag + mag * Math.sin(parameter)); //(mag/2)*2, azaz mag kell, mert fent az eltolás miatt 2mag van
        haromszogUtvonal.lineTo(stopX2 + szel_margo / 2, stopY2 + mag_margo);
        canvas.drawPath(haromszogUtvonal, mireFest3);

        //hol tartunk - radar vonal
        mireFest3.setStrokeWidth(7f);
        mireFest3.setColor(Color.RED);
        parameter = (180+aktSzog+30) * Math.PI / 180;
        stopX2 = (float) (szel/2 + szel/2 * Math.cos(parameter));
        stopY2 = (float) (mag + mag * Math.sin(parameter)); //(mag/2)*2, azaz mag kell, mert fent az eltolás miatt 2mag van
        canvas.drawLine(szel / 2 + szel_margo / 2, mag + mag_margo, stopX2 + szel_margo / 2, stopY2 + mag_margo, mireFest3);
    }
}
