package me.bretmounet.scavengerhunt;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ScavengerHuntCommand implements CommandExecutor {
    private List<String> playersTeam = new ArrayList<>();
    private List<Boolean> onTeam = new ArrayList<>();
    public static Integer newScore = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Connection connection = Scavengerhunt.connection;

        if (sender instanceof Player){
            Player Player = (Player) sender;
            if(Player.hasPermission("sc")) {
                if (args.length > 0){
                    try {
                        String p = args[0];
                        Player player = Bukkit.getServer().getPlayer(p);
                        UUID id = player.getUniqueId();
                        newScore = Integer.parseInt(args[1]);
                        String playerName = id.toString();
                        onTeam.add(0,false);
                        TeamManager.getPlayersTeam(playerName,playersTeam,onTeam);
                        if (onTeam.get(0).equals(true)) {
                            TeamManager.addNewScore(connection, playerName, newScore, player, playersTeam.get(0));
                            player.spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(),30);
                            player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,10,15);
                        }else {
                            player.sendMessage(ChatColor.RED + "Error: Not on a team.");
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException error) { // This will catch the error if someone put not an Integer at args[1]
                        Player.sendMessage(ChatColor.RED + "Error:Not an integer");
                    }
                }else{
                    Player.sendMessage(ChatColor.RED + "Error: You need to input an amount");
                    Player.sendMessage(ChatColor.RED + "/sc <amount>");
                }
            }else
                Player.sendMessage(ChatColor.RED + "You do not have permission to use this command");

        }else if (sender instanceof ConsoleCommandSender){
            if (args.length > 0){
                playerJoin playerJoin = new playerJoin();
                String p = args[0];
                Player player = Bukkit.getServer().getPlayer(p);
                UUID id = player.getUniqueId();
                try {
                    newScore = Integer.parseInt(args[1]);
                    String playerName = id.toString();
                    onTeam.add(0,false);
                    TeamManager.getPlayersTeam(playerName,playersTeam,onTeam);
                    if (onTeam.get(0).equals(true)) {
                        TeamManager.addNewScore(connection, playerName, newScore, player, playersTeam.get(0));
                        player.spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(),30);
                        player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,10,15);
                    }else {
                        player.sendMessage(ChatColor.RED + "Error: Not on a team.");
                    }

                } catch (NumberFormatException | SQLException error) { // This will catch the error if someone put not an Integer at args[1]
                    player.sendMessage(ChatColor.RED + "Error:Not an integer");
                }
            }

        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }

}
