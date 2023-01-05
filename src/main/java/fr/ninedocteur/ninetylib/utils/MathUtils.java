package fr.ninedocteur.ninetylib.utils;

public class MathUtils {
    public static int convertSecondsToTick(int seconds){
        return seconds * 20;
    }

    public static int convertTickToSeconds(int ticks){
        return ticks / 20;
    }
}
