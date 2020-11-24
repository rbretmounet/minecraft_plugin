package me.bretmounet.scavengerhunt;


import org.bukkit.ChatColor;
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
import java.util.Random;
import java.util.UUID;

public class JoinTeamCommand implements CommandExecutor {
    public static String teamName = null;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Connection connection = Scavengerhunt.connection;
        if (sender instanceof Player) {
            List<String> playersTeam = new ArrayList<>();
            List<Boolean> onTeam = new ArrayList<>();
            List<String> fullTeams = new ArrayList<>();
            List<String> teamsNotFull = new ArrayList<>();
            Player player = (Player) sender;
            UUID id = player.getUniqueId();
            String playerName = id.toString();
            teamName = args[0];
            int maxTeamSize = Scavengerhunt.maxteamsize; // Variable used to set max team size
            try {
                TeamManager.getTeamNames();
                TeamManager.getPlayersTeam(playerName, playersTeam, onTeam);
                TeamManager.getFullTeams(teamsNotFull,maxTeamSize,fullTeams);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (!playersTeam.isEmpty()){
                if (TeamManager.teamNames.contains(teamName) && !playersTeam.get(0).equals(teamName)) {
                    try {
                        Statement statement = connection.createStatement();
                        PreparedStatement pstmt = null;
                        String SQL = "DELETE FROM " + playersTeam.get(0) + " WHERE PlayerNames ='" + playerName + "'";
                        pstmt = connection.prepareStatement(SQL);
                        if(onTeam.get(0).equals(true) && !fullTeams.contains(teamName)){
                            TeamManager.removePlayerFromTeam(teamName,player);
                            pstmt.executeUpdate();
                        }

                        if (!fullTeams.contains(teamName)){
                            statement.executeUpdate("INSERT INTO " + teamName + " (PlayerNames, Points) VALUES ('" + playerName + "', 0);");
                            TeamManager.assignTeam(teamName,player);
                            player.setScoreboard(TeamManager.board);
                            player.sendMessage(ChatColor.GREEN + "You have Succesfully Joined a Team.");
                        }else{
                            player.sendMessage(ChatColor.RED + "Error: Team is full.");
                        }


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }else if (playersTeam.get(0).equals(teamName)){
                    player.sendMessage(ChatColor.RED + "You are already on that team.");
                }else {
                    player.sendMessage(ChatColor.RED + "Error: Unknown Team Name");
                }
            }else if (playersTeam.isEmpty() && TeamManager.teamNames.contains(teamName)){

                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("INSERT INTO " + teamName + " (PlayerNames, Points) VALUES ('" + playerName + "', 0);");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                TeamManager.assignTeam(teamName,player);
                player.setScoreboard(TeamManager.board);
                player.sendMessage(ChatColor.GREEN + "You have Succesfully Joined a Team.");
            }else if (!TeamManager.teamNames.contains(teamName)){
                player.sendMessage(ChatColor.RED + "Error: Unknown Team Name");
            }
        }


        return true;
    }
    public int getRandomElement(List<Integer> list)
    {
        Random rand = new Random(); // initializes rand
        return list.get(rand.nextInt(list.size())); // randomly returns a value that is stored in the list.
    }

}
