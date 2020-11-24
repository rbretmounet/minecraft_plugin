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
import java.util.UUID;

public class LeaveTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Connection connection = Scavengerhunt.connection;
        if (sender instanceof Player) {
            List<String> playersTeam = new ArrayList<>();
            List<Boolean> onTeam = new ArrayList<>();
            Player player = (Player) sender;
            UUID id = player.getUniqueId();
            String playerName = id.toString();
            try {
                TeamManager.getTeamNames();
                TeamManager.getPlayersTeam(playerName, playersTeam, onTeam);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                if(!playersTeam.isEmpty()) {
                    Statement statement = connection.createStatement();
                    PreparedStatement pstmt = null;
                    String SQL = "DELETE FROM " + playersTeam.get(0) + " WHERE PlayerNames ='" + playerName + "'";
                    pstmt = connection.prepareStatement(SQL);
                    if (!onTeam.isEmpty()) {
                        if (onTeam.get(0).equals(true)) {
                            pstmt.executeUpdate();
                            TeamManager.removePlayerFromTeam(playersTeam.get(0),player);
                            player.sendMessage(ChatColor.GREEN + "You have left your team.");
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Error: Not on a team.");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return true;
    }
}
