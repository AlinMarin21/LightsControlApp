package com.example.smartliving;

public class GlobalBuffer {
    public static final int RX_ELEMENTS = 16;
    public static final int TX_ELEMENTS = 16;

    public static final int SOB = 255;
    public static final int EOB = 255;

    public static byte[] RxBuffer = new byte[RX_ELEMENTS];
    /* Rx Buffer
    * 0: SOB (Start Of Buffer)
    * 1: Motion Sensor Value
    * 2: Photoresistor Value
    * 15: EOB (End Of Buffer) */
    public static byte[] TxBuffer = new byte[TX_ELEMENTS];
    /* Tx Buffer
    * 0: SOB (Start Of Buffer)
    * 1: Living Room LED State
    * 2: Living Room LED Brightness
    * 3: Kitchen LED State
    * 4: Room 1 LED State
    * 5: Room 1 LED Red (RGB) Value
    * 6: Room 1 LED Green (RGB) Value
    * 7: Room 1 LED Blue (RGB) Value
    * 8: Room 2 LED State
    * 9: Room 2 LED Red (RGB) Value
    * 10: Room 2 LED Green (RGB) Value
    * 11: Room 2 LED Blue (RGB) Value
    * 12: Terrace LED State For Manual Control
    * 13: Terrace LED Mode Control
    * 14: Bathroom LED State
    * 15: EOB (End Of Buffer) */

    public static int Room1LedColor = -1;
    public static int Room2LedColor = -1;
}
