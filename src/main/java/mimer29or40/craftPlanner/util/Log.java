package mimer29or40.craftPlanner.util;

public class Log
{
    private static void log(String level, String format, Object... object)
    {
        System.out.println(String.format("[%s]:", level.toUpperCase()) + String.format(format, object));
    }

    public static void off(String format, Object... object)
    {
        log("off", format, object);
    }

    public static void off(Object object)
    {
        off("%s", object);
    }

    public static void fatal(String format, Object... object)
    {
        log("fatal", format, object);
    }

    public static void fatal(Object object)
    {
        fatal("%s", object);
    }

    public static void error(String format, Object... object)
    {
        log("error", format, object);
    }

    public static void error(Object object)
    {
        error("%s", object);
    }

    public static void warn(String format, Object... object)
    {
        log("warn", format, object);
    }

    public static void warn(Object object)
    {
        warn("%s", object);
    }

    public static void info(String format, Object... object)
    {
        log("info", format, object);
    }

    public static void info(Object object)
    {
        info("%s", object);
    }

    public static void debug(String format, Object... object)
    {
        log("debug", format, object);
    }

    public static void debug(Object object)
    {
        debug("%s", object);
    }

    public static void trace(String format, Object... object)
    {
        log("trace", format, object);
    }

    public static void trace(Object object)
    {
        trace("%s", object);
    }

    public static void all(String format, Object... object)
    {
        log("all", format, object);
    }

    public static void all(Object object)
    {
        all("%s", object);
    }
}
