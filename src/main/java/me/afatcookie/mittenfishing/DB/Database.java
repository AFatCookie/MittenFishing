package me.afatcookie.mittenfishing.DB;

import com.github.mittenmc.serverutils.UUIDConverter;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.fishinglevels.PlayerLevel;
import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

/*
Database class that allows access to quest, levels, and discoverable fish!
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class Database {
    MittenFishing plugin;
    Connection connection;

    //THIS SHOULD BE USED FOR ACTIVE QUESTS
    private final String LOAD_DATA = "SELECT * FROM {table_name} WHERE questID = ?;";

    //THIS IS TO SELECT ALL THINGS FROM SPECIFIED TABLE
    private final String LOAD_LEVEL_DATA = "SELECT * FROM {table_name};";


    //CREATES ACTIVE QUESTS TABLE
    private final String CREATE_DAILY_QUEST_TABLE = "CREATE TABLE IF NOT EXISTS {table_name}(" +
            "questID SMALLINT DEFAULT -2  NOT NULL," +
            "uuid BINARY(16) NOT NULL," +
            "progress BIGINT DEFAULT -1 NOT NULL," +
            "PRIMARY KEY (questID, uuid)" +
            ");";

    //CREATES PLAYER LEVEL TABLE
    private final String CREATE_PLAYER_LEVEL_TABLE = "CREATE TABLE IF NOT EXISTS {table_name}(" +
            "uuid BINARY(16) NOT NULL," +
            "currentlevel BIGINT DEFAULT 0 NOT NULL," +
            "tonextlevel DOUBLE DEFAULT -1.0 NOT NULL," +
            "PRIMARY KEY (uuid)" +
            ");";

    //DELETES EVERYTHING FROM ACTIVEQUESTS
    private final String DELETE_PREVIOUS_QUESTS = "DELETE FROM activequests;";


    //DELETES SPECIFIED TABLE
    private final String DELETE_TABLE = "DROP TABLE {table_name};";

    //MEANT TO INSERT INTO ACTIVEQUEST TABLE
    private final String INSERT_INTO_TABLE = "INSERT OR REPLACE INTO {table_name}(questID, uuid, progress) VALUES(?,?,?);";

    //INSERT INTO LEVEL TABLE
    private final String INSERT_LEVEL_TABLE = "INSERT OR REPLACE INTO {table_name}(uuid, currentlevel, tonextlevel) VALUES(?,?,?);";

//DELETES PLAYER FROM DAILY QUESTS AT QUESTID
    private final String DELETE_PLAYER = "DELETE FROM activequests WHERE questID = ?;";

    public Database(MittenFishing instance) {
        plugin = instance;
    }


    public abstract Connection getSQLConnection();


    /**
     * Creates Daily Quest Table
     */
    public void createQuestTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_DAILY_QUEST_TABLE.replace("{table_name}", "activequests"));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);

        }
    }

    /**
     * creates level table
     */
    public void createLevelTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_PLAYER_LEVEL_TABLE.replace("{table_name}", "playerlevels"));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);

        }
    }

    /**
     * Saves playerQuest to daily quests table. This method is used when a player completes an event.
     * @param playerQuest playerQuest to save
     */
    public void savePlayerToQuestTable(PlayerQuest playerQuest){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(INSERT_INTO_TABLE.replace("{table_name}", "activequests"));
            ps.setInt(1, playerQuest.getQuest().getQuestID());
            ps.setBytes(2, UUIDConverter.convert(playerQuest.getPlayer()));
            ps.setInt(3, playerQuest.getProgress());
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    /**
     * Saves playerQuest to daily quests table. This method is used on server reload.
     * @param playerQuest playerQuest to save
     */
    public void saveToQuestTable(PlayerQuest playerQuest){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(INSERT_INTO_TABLE.replace("{table_name}", "activequests"));
            ps.setInt(1, playerQuest.getQuest().getQuestID());
            ps.setBytes(2, UUIDConverter.convert(playerQuest.getPlayer()));
            ps.setInt(3, playerQuest.getProgress());
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    /**
     * Saves PlayerLevel to the level table
     * @param playerLevel PlayerLevel to save
     */
    public void saveToLevelTable(PlayerLevel playerLevel){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(INSERT_LEVEL_TABLE.replace("{table_name}", "playerlevels"));
            ps.setBytes(1, UUIDConverter.convert(playerLevel.getPlayerUUID()));
            ps.setInt(2, playerLevel.getLevel());
            ps.setDouble(3, playerLevel.getToNextLevel());
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    /**
     * Gets player data from dail quest table and returns the object created from the data.
     * @param quest The quest to check the id of and see if the table contains the player with the quest id
     * @return the constructed PlayerQuest from the data.
     */
    public PlayerQuest getPlayerDataFromQuestTable(Quest quest){
        Connection conn = null;
        PreparedStatement ps = null;
        PlayerQuest playerQuest = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_DATA.replace("{table_name}", "activequests"));
            ps.setInt(1, quest.getQuestID());
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                playerQuest = new PlayerQuest(UUIDConverter.convert(resultSet.getBytes(2)),quest, resultSet.getInt(3), plugin);

            }

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
        return playerQuest;
    }

    /**
     * gets the player levels from the playerlevel table.
     * @return the ArrayList of all the player levels constructed from the database.
     */
    public ArrayList<PlayerLevel> getPlayerDataFromLevelTable(){
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<PlayerLevel> playerLevels = new ArrayList<>();
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_LEVEL_DATA.replace("{table_name}", "playerlevels"));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                playerLevels.add(new PlayerLevel(UUIDConverter.convert(resultSet.getBytes(1)), resultSet.getInt(2), resultSet.getInt(3)));
            }

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
        return playerLevels;
    }


    /**
     * Deletes the active Quest tables
     */
    public void deleteTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE_TABLE.replace("{table_name}", "activequests"));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }


    public void clearQuestTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE_PREVIOUS_QUESTS);
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    public void removePlayer(PlayerQuest player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE_PLAYER);
            ps.setInt(1, player.getQuest().getQuestID());
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    public Player findPlayer(UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "SELECT * FROM " + "activequests" + " WHERE uuid = ?;";
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(sql);
            ps.setBytes(1, UUIDConverter.convert(uuid));
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()){
                return Bukkit.getPlayer(UUIDConverter.convert(resultSet.getBytes(1)));
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
        return null;
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