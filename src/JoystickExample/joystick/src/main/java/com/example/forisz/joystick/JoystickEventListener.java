package com.example.forisz.joystick;

/**
 * Created by Forisz on 30/04/16.
 */
public interface JoystickEventListener {
    /**
     * @param x Horizontal position of the joystick (a number between -1 and 1)
     * @param y Vertical position of the joystick (a number between -1 and 1)
     */
    void onPositionChange(float x, float y, float deg);

    /**
     * Fires when the user releases the joystick
     */
    void onJoystickReleased();

    /**
     * Fired when the user touches the joystick
     */
    void onJoystickTouched();
}
