package me.bretmounet.scavengerhunt;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class spigotExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "scavengerhunt";
    }

    @Override
    public String getAuthor() {
        return "Bret-Mounet";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return false;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        UUID id = p.getUniqueId();
        String playerUUID = id.toString();
        List<String> playersTeam = new ArrayList<>();
        List<Boolean> onTeam = new ArrayList<>();
        List<Integer> score = new ArrayList<>();
        List<String> one = new ArrayList<>();
        List<String> two = new ArrayList<>();
        List<String> three = new ArrayList<>();
        List<Integer> firstScore = new ArrayList<>();
        List<Integer>  secondScore = new ArrayList<>();
        List<Integer>  thirdScore = new ArrayList<>();

        if(p == null){
            return  "";
        }
        if(params.equals("teamname")){
            try {
                TeamManager.getPlayersTeam(playerUUID,playersTeam,onTeam);
                if(!playersTeam.isEmpty())
                {
                    return playersTeam.get(0);
                }else{
                    return "Not on a Team.";
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }else if (params.equals("score")) {
                try {
                    TeamManager.getPlayersTeam(playerUUID, playersTeam, onTeam);
                    if (!playersTeam.isEmpty()){
                        TeamManager.getScore(playersTeam.get(0), score);
                        return (Integer.toString(score.get(0)) + "pts");

                    }else{
                        return "Not on a Team.";
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        }else if (params.equals("firstScore")){
            try {
                TeamManager.getTopScores(firstScore,secondScore,thirdScore);
                if(!firstScore.isEmpty()){
                    return (Integer.toString(firstScore.get(0)) + "pts");
                }else{
                    return "None";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(params.equals("secondScore")){
            try {
                TeamManager.getTopScores(firstScore,secondScore,thirdScore);
                if(!secondScore.isEmpty()){
                    return (Integer.toString(secondScore.get(0)) + "pts");
                }else{
                    return "None";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(params.equals("thirdScore")){
            try {
                TeamManager.getTopScores(firstScore,secondScore,thirdScore);
                if(!thirdScore.isEmpty()){
                    return (Integer.toString(thirdScore.get(0)) + "pts");
                }else{
                    return "None";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if (params.equals("firstTeam")){
            try {
                TeamManager.getTopScores(firstScore,secondScore,thirdScore);
                TeamManager.getTopTeams(one,two,three,firstScore,secondScore,thirdScore);
                if(!one.isEmpty()){
                    return one.get(0);
                }else{
                    return "None";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(params.equals("secondTeam")){
            try {
                TeamManager.getTopScores(firstScore,secondScore,thirdScore);
                TeamManager.getTopTeams(one,two,three,firstScore,secondScore,thirdScore);
                if(!two.isEmpty()){
                    return two.get(0);
                }else{
                    return "None";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(params.equals("thirdTeam")){
            try {
                TeamManager.getTopScores(firstScore,secondScore,thirdScore);
                TeamManager.getTopTeams(one,two,three,firstScore,secondScore,thirdScore);
                if(!three.isEmpty()){
                    return three.get(0);
                }else{
                    return "None";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }
}
