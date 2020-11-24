package me.bretmounet.scavengerhunt;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class joinRandomTeamCommand  implements CommandExecutor {
    public static List<String> teamsNotFull = new ArrayList<>(); //Stores the total amount of players  on Red Team.
    public static List<String> teamsFulls = new ArrayList<>(); //Stores the total amount of players  on Red Team.


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Connection connection = Scavengerhunt.connection;
        if (sender instanceof Player) {
            try {
                Statement statement = connection.createStatement();
                List<String> playersTeam = new ArrayList<>();
                List<Boolean> onTeam = new ArrayList<>();
                onTeam.add(0, false);
                int maxTeamSize = Scavengerhunt.maxteamsize;
                Player player = (Player) sender;
                UUID id = player.getUniqueId();
                String playerName = id.toString();
                TeamManager.getFullTeams(teamsNotFull, maxTeamSize, teamsFulls);
                TeamManager.getPlayersTeam(playerName,playersTeam,onTeam);
                //Condition to check if all teams are full
                if (teamsNotFull.isEmpty()){
                    player.sendMessage(ChatColor.RED + "Their are no current teams to join. You have to create a team."); //Displays message to player that teams are full
                } // If 1 or more teams are available and player is not on a team already then assign to an available team.
                else if (!teamsNotFull.isEmpty()){
                    String randomTeam= TeamManager.getRandomElement(teamsNotFull); // Calls function that randomly chooses a number inside of array
                    if(onTeam.get(0).equals(true) && !teamsFulls.contains(randomTeam)){
                        PreparedStatement pstmt = null;
                        String SQL = "DELETE FROM " + playersTeam.get(0) + " WHERE PlayerNames ='" + playerName + "'";
                        pstmt = connection.prepareStatement(SQL);
                        TeamManager.removePlayerFromTeam(playersTeam.get(0),player);
                        pstmt.executeUpdate();
                    }
                    statement.executeUpdate("INSERT INTO " + randomTeam + " (PlayerNames, Points) VALUES ('" + playerName + "', 0);"); // If true then add them to Red Team
                    TeamManager.assignTeam(randomTeam,player);
                    player.setScoreboard(TeamManager.board);
                    player.sendMessage(ChatColor.GREEN + "You have Succesfully Joined a Team.");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return true;
    }
}
