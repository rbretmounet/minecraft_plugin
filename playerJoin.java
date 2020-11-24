package me.bretmounet.scavengerhunt;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;


import javax.swing.*;
import java.sql.*;
import java.util.*;


public class playerJoin implements Listener {
    public static Connection connection = Scavengerhunt.connection; // Establishes a connection to mySQL database
    @EventHandler
    public void event(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        String playerName = id.toString(); //stores player's uuid instead of username since it can change
        List<String> playersTeam = new ArrayList<>();
        List<Boolean> onTeam = new ArrayList<>();

        TeamManager.getTeamNames();
        TeamManager.getPlayersTeam(playerName,playersTeam,onTeam);
        TeamManager.createTeams();
        if(!onTeam.isEmpty()) {
            if (onTeam.get(0) == true){
                TeamManager.assignTeam(playersTeam.get(0),player);
                player.sendMessage("Welcome Back!");
            }

        }else{
            player.sendMessage(ChatColor.GOLD + "You are not on a team. Use /create to create a team or /join to join a team");
        }


        Statement statement = connection.createStatement(); // creates a statement used to access data from database.



        statement.close(); // closes statement
        player.setScoreboard(TeamManager.board);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

    }


    }
