package me.bretmounet.scavengerhunt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class Scavengerhunt extends JavaPlugin {
    public String host, database, username, password;
    public int port;
    public static Connection connection;
    public static int maxteamsize;
    private File customConfigFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {
        createCustomConfig("mysql.yml");
        host = getCustomConfig().getString("host");
        port = getCustomConfig().getInt("port");
        database = getCustomConfig().getString("database");
        username = getCustomConfig().getString("username");
        password = getCustomConfig().getString("password");
        createCustomConfig("teamconfig.yml");
        maxteamsize = getCustomConfig().getInt("maxteamsize");

        try {
            openConnection();
            TeamManager.getTeamNames();
            TeamManager.createTeams();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(new playerJoin(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandExecuted(),this);
        this.getCommand("sc").setExecutor(new ScavengerHuntCommand());
        this.getCommand("join").setExecutor(new JoinTeamCommand());
        this.getCommand("create").setExecutor(new teamCreateCommand());
        this.getCommand("leave").setExecutor(new LeaveTeamCommand());
        this.getCommand("delete").setExecutor(new DeleteTeamCommand());
        this.getCommand("random").setExecutor(new joinRandomTeamCommand());

        new spigotExpansion().register();
    }


    @Override
    public void onDisable() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() throws SQLException,ClassNotFoundException{
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig(String fileName) {
        customConfigFile = new File(getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource(fileName, false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
