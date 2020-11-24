package me.bretmounet.scavengerhunt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteTeamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Connection connection = Scavengerhunt.connection;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("delete")){
                if (args.length != 0) {
                    String teamName = args[0];
                    try {
                        Statement statement = connection.createStatement();
                        TeamManager.getTeamNames();
                        if (TeamManager.teamNames.contains(teamName)) {
                            String sql = "DROP TABLE " + teamName + "";
                            statement.executeUpdate(sql);
                            TeamManager.removePlayerFromTeam(teamName, player);
                            TeamManager.deleteTeam(teamName);
                            player.sendMessage(ChatColor.GREEN + "Team Deleted.");
                        } else if (!TeamManager.teamNames.contains(teamName)) {
                            player.sendMessage(ChatColor.RED + "Error: Team Not Found.");
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }else {
                    player.sendMessage(ChatColor.RED + "Error: Missing Argument /delete <Team Name>");
                }
            }else {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }

        }
        return true;
    }
}
