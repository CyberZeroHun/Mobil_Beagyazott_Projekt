package com.example.forisz.joystick;

/**
 * Created by Forisz on 30/04/16.
 */
class Point {
    float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
        this.x = 0f;
        this.y = 0f;
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }
}