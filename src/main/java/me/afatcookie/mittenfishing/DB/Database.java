package me.afatcookie.mittenfishing.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.Quest;


public abstract class Database {
    MittenFishing plugin;
    Connection connection;

    private final String LOAD_DATA = "SELECT * FROM {table_name} ORDER BY progress DESC;";

    private final String CREATE_DAILY_QUEST_TABLE = "CREATE TABLE IF NOT EXISTS {table_name}(" +
            "uuid VARCHAR(36) NOT NULL," +
            "progress BIGINT    NOT NULL," +
            "PRIMARY KEY (uuid)" +
            ");";


    private final String DELETE_TABLE = "DROP TABLE {table_name};";

    private final String INSERT_INTO_TABLE = "INSERT OR REPLACE INTO {table_name}(uuid, progress) VALUES(?,?);";
    public Database(MittenFishing instance) {
        plugin = instance;
    }


    public abstract Connection getSQLConnection();


    public void createQuestTable(Quest quest) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_DAILY_QUEST_TABLE.replace("{table_name}", quest.getName()));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);

        }
    }

    public void savePlayerDataToTable(PlayerQuest playerQuest) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(INSERT_INTO_TABLE.replace("{table_name}", playerQuest.getQuest().getName()));

                ps.setString(1, playerQuest.getPlayer().toString());
                ps.setLong(2, playerQuest.getProgress());
                ps.addBatch();
            ps.executeBatch();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
        close(ps, conn);
        }
    }

    public ArrayList<PlayerQuest> loadDataAfterRestart(Quest quest) {
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<PlayerQuest> savedDailies = new ArrayList<>();
        boolean tableExists = false;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_DATA.replace("{table_name}", quest.getName()));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                savedDailies.add(new PlayerQuest(UUID.fromString(resultSet.getString(1)),quest, resultSet.getInt(2)));
            }

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
        return savedDailies;
    }


    public void deleteTable(Quest quest){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE_TABLE.replace("{table_name}", quest.getName()));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }





    public void close(PreparedStatement ps, Connection cn) {
        try {
            if (ps != null)
                ps.close();
            if (cn != null)
                cn.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}