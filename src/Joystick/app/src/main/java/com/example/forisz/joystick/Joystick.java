package com.example.forisz.joystick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Forisz on 25/04/16.
 * Joystick View implementation
 */
class Joystick extends View {
    private static final int DEFAULT_OUTER_CIRCLE_WIDTH = 5;
    private static final int DEFAULT_INNER_CIRCLE_RADIOUS = 50;
    //private static final String TAG = "JoystickView";
    private static final int DEFAULT_INNER_CIRCLE_COLOR = Color.RED;
    private static final int DEFAULT_OUTER_CIRCLE_COLOR = Color.BLACK;

    private Paint outerCirclePaint, innerCirclePaint;
    private JoystickEventListener listener;
    private float outerCircleRadius, centerX, centerY;
    private int innerCircleRadius;
    private Point joy, touchStart;
    private int outerCircleWidth;



    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, outerCircleRadius - outerCircleWidth / 2, outerCirclePaint);
        canvas.drawCircle(joy.x, joy.y, innerCircleRadius, innerCirclePaint);
    }

    /**
     * Calculates the distance between two points in the coordinate system.
     * @param a {Point}
     * @param b {Point}
     * @return distance between {Point} a and {Point} b
     */
    private float getDistance(Point a, Point b) {
        return (float)Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y , 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchStart.x = (int) event.getX(0);
                touchStart.y = (int) event.getY(0);

                listener.onJoystickTouched();
                break;
            case MotionEvent.ACTION_MOVE:
                final Point pointer = new Point(event.getX(0), event.getY(0)),
                            center = new Point(this.getWidth() / 2, getHeight() / 2);

                // Prevent the inner circle from leaving the outer circle's boundaries.
                if (getDistance(pointer, center) > this.getWidth() / 2) {
                    joy = getCircleLineIntersectionPoint(pointer, center, center, this.getWidth() / 2).get(0);
                } else {
                    joy = getCircleLineIntersectionPoint(pointer, center, center, getDistance(pointer, center)).get(0);
                }

                // Let's translate the x and y position into the [-1, 1] range and call the callback with it;
                listener.onPositionChange(
                        joy.x / outerCircleRadius - 1,
                        -(joy.y / outerCircleRadius - 1));
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                joy.x = this.getWidth() / 2;
                joy.y = this.getHeight() / 2;
                this.invalidate();
                // Fire the onJoystickReleasedEvent
                listener.onJoystickReleased();
                break;
        }
        return true;
    }

    public void setJoystickEventListener(JoystickEventListener listener) {
        this.listener = listener;
        this.getDistance(null, null);
    }

    /**
     * Calculates the intersecting points of a line and a circle. Useful for getting the location of the
     * @param pointA {Point} First point of the line
     * @param pointB {Point} Second point of the line (2 points are defining a line ;) )
     * @param center {Point} The center of the circle
     * @param radius {float} The radius of the circle
     * @return {Point} The intersection of the circle and the line
     */
    private static List<Point> getCircleLineIntersectionPoint(Point pointA, Point pointB, Point center, float radius) {
        float baX = pointB.x - pointA.x;
        float baY = pointB.y - pointA.y;
        float caX = center.x - pointA.x;
        float caY = center.y - pointA.y;

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = caX * caX + caY * caY - radius * radius;

        float pBy2 = bBy2 / a;
        float q = c / a;

        float disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        float tmpSqrt = (float)Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Point p1 = new Point(pointA.x - baX * abScalingFactor1, pointA.y
                - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Point p2 = new Point(pointA.x - baX * abScalingFactor2, pointA.y
                - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        outerCircleRadius = w / 2;
        joy.x = w / 2;
        joy.y = h / 2;
        this.invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Joystick,
                0, 0);

        int innerCircleColor;
        int outerCircleColor;
        try {
            innerCircleRadius = a.getDimensionPixelSize(R.styleable.Joystick_innerCircleRadious, DEFAULT_INNER_CIRCLE_RADIOUS);
            innerCircleColor = a.getColor(R.styleable.Joystick_innerCircleColor, DEFAULT_INNER_CIRCLE_COLOR);
            outerCircleColor = a.getColor(R.styleable.Joystick_outerCircleColor, DEFAULT_OUTER_CIRCLE_COLOR);
            outerCircleWidth = a.getDimensionPixelSize(R.styleable.Joystick_outerCircleWidth, DEFAULT_OUTER_CIRCLE_WIDTH);
        } finally {
            a.recycle();
        }
        // Set the listener to be null.
        listener = null;

        joy = new Point(0f, 0f);
        touchStart = new Point(0f, 0f);

        outerCirclePaint = new Paint();
        outerCirclePaint.setStyle(Paint.Style.STROKE);
        outerCirclePaint.setColor(outerCircleColor);
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setStrokeWidth(outerCircleWidth);

        innerCirclePaint = new Paint();
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setColor(innerCircleColor);
        innerCirclePaint.setAntiAlias(true);
    }


    public Joystick(Context context) {
        super(context);
        init(context, null);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    interface JoystickEventListener {
        /**
         * @param x Horizontal position of the joystick (a number between -1 and 1)
         * @param y Vertical position of the joystick (a number between -1 and 1)
         */
        void onPositionChange(float x, float y);

        /**
         * Fires when the user releases the joystick
         */
        void onJoystickReleased();

        /**
         * Fired when the user touches the joystick
         */
        void onJoystickTouched();
    }
}

class Point {
    float x, y;

    public Point(float x, float y) { this.x = x; this.y = y; }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }
}

