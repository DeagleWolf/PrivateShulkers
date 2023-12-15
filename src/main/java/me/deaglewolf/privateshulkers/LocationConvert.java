package me.deaglewolf.privateshulkers;

import org.bukkit.Location;

public class LocationConvert {
    public static String convert(Location location) {
        return (int) location.getX() + ", " + (int) location.getY() + ", " + (int) location.getZ() + ", " + location.getWorld().getName();
    }
}
