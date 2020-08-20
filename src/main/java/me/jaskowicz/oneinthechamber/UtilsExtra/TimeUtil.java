package me.jaskowicz.oneinthechamber.UtilsExtra;

public class TimeUtil {

    public static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return (hours > 0 ? twoDigitString(hours) + "hr " : "") + (minutes > 0 ? twoDigitString(minutes) + "m " : "") + twoDigitString(seconds) + "s";
    }

    public static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

}
