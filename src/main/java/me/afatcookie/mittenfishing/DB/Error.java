package me.afatcookie.mittenfishing.DB;

import me.afatcookie.mittenfishing.MittenFishing;

import java.util.logging.Level;

public class Error {
    public static void execute(MittenFishing plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(MittenFishing plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
