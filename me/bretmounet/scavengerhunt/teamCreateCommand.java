package me.bretmounet.scavengerhunt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;


public class teamCreateCommand implements CommandExecutor {
        Connection connection = Scavengerhunt.connection;
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            String teamName = null;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length != 0) {
                    teamName = args[0];
                    if (!teamName.isEmpty()) {
                        if (teamName.length() >= 1 && teamName.length() <= 10) {
                            try {
                                createTable(teamName, player);
                                TeamManager.getTeamNames();
                                TeamManager.createTeams();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        } else if (teamName.length() > 10) {
                            player.sendMessage(ChatColor.RED + "Error: Team Name is Too Long, Max character size is 10");
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Error: Missing Argument /create <Team Name>");
                }

            }
            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        public void createTable(String teamName, Player player) throws SQLException {
            Statement statement = connection.createStatement();
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, teamName , null);
            if (tables.next()) {
                player.sendMessage(ChatColor.RED + "Sorry, team already exists. Please choose a new team name.");
            }
            else {
                String sql_table = "CREATE TABLE " + teamName + "" +
                        "(PlayerNames VARCHAR(45) NOT NULL, " +
                        " Points INTEGER, " +
                        " PRIMARY KEY ( PlayerNames ))";
                statement.executeUpdate(sql_table);

                player.sendMessage(ChatColor.GREEN + "Team Created");
            }

        }
}
