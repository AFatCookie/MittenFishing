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

    private final String LOAD_ACTIVE_QUEST_DATA = "SELECT * FROM {table_name};";

    private final String CREATE_DAILY_QUEST_TABLE = "CREATE TABLE IF NOT EXISTS {table_name}(" +
            "uuid VARCHAR(36) NOT NULL," +
            "progress BIGINT    NOT NULL," +
            "PRIMARY KEY (uuid)" +
            ");";

    private final String DELETE_PREVIOUS_QUESTS = "DELETE FROM currentdailies;";

    private final String CREATE_QUEST_NAME_TABLE = "CREATE TABLE IF NOT EXISTS currentdailies(" +
            "questname VARCHAR(36) NOT NULL" + ");";

    private final String DELETE_TABLE = "DROP TABLE {table_name};";

    private final String INSERT_INTO_TABLE = "INSERT OR REPLACE INTO {table_name}(uuid, progress) VALUES(?,?);";

    private final String INSERT_NAME_INTO_TABLE = "INSERT OR REPLACE INTO {table_name}(questname) VALUES(?);";

    private final String DELETE_PLAYER = "DELETE FROM {table_name} WHERE uuid=?;";

    public Database(MittenFishing instance) {
        plugin = instance;
    }


    public abstract Connection getSQLConnection();


    public void createQuestTable(Quest quest) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_DAILY_QUEST_TABLE.replace("{table_name}", quest.getName().toLowerCase().replace(" ", "_").replace(".", "_")));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);

        }
    }

    public void createQuestNamesTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_QUEST_NAME_TABLE);
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
            String renovatedName = playerQuest.getQuest().getName().toLowerCase().replace(" ", "_").replace(".", "_");
            ps = conn.prepareStatement(INSERT_INTO_TABLE.replace("{table_name}", renovatedName));
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

    public void saveToQuestNameTable(Quest quest) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(INSERT_NAME_INTO_TABLE.replace("{table_name}", "currentdailies"));
            ps.setString(1, quest.getName().toLowerCase().replace(" ", "_").replace(".", "_"));
            ps.addBatch();
            ps.executeBatch();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    public PlayerQuest loadDataAfterRestart(Quest quest) {
        Connection conn = null;
        PreparedStatement ps = null;
        PlayerQuest playerQuest = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_DATA.replace("{table_name}", quest.getName().toLowerCase().replace(" ", "_").replace(".", "_")));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                System.out.println(quest.getName());
                playerQuest = new PlayerQuest(UUID.fromString(resultSet.getString(1)), quest, resultSet.getInt(2), plugin);
            }

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
        return playerQuest;
    }

    public ArrayList<String> loadCurrentDailiesAfterRestart() {
        ArrayList<String> quests = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_ACTIVE_QUEST_DATA.replace("{table_name}", "currentdailies"));
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                quests.add(resultSet.getString(1));
            }

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
        return quests;
    }


    public void deleteTable(Quest quest) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE_TABLE.replace("{table_name}", quest.getName().toLowerCase().replace(" ", "_").replace(".", "_")));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    public void deleteQuestNameTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE_TABLE.replace("{table_name}", "currentdailies"));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    public void clearQuestNameTable() {
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

    public void removePlayer(Quest quest, UUID player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(DELETE_PLAYER.replace("{table_name}", quest.getName().toLowerCase().replace(" ", "_").replace(".", "_")));
            ps.setString(1, player.toString());
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
    }

    public boolean findPlayer(Quest quest, UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "SELECT * FROM " + quest.getName().toLowerCase().replace(" ", "_").replace(".", "_") + " WHERE uuid = ?";
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(sql);
            if (ps == null){
                return false;
            }
            ResultSet resultSet = ps.executeQuery();

            return resultSet.next();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, conn);
        }
        return false;
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