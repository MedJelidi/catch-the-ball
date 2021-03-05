package com.example.catchtheball;

public class Check {
    public static boolean soundtrackPlaying = false;

    public static boolean isSoundtrackPlaying() {
        return soundtrackPlaying;
    }

    public static void setSoundtrackPlaying(boolean soundtrackPlaying) {
        Check.soundtrackPlaying = soundtrackPlaying;
    }
}
