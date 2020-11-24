package me.bretmounet.scavengerhunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.graalvm.compiler.replacements.nodes.CStringConstant;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class TeamManager {
    public static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static Scoreboard board = manager.getNewScoreboard();
    public static Boolean playerOnTeam = false;

    public static List<Integer> teamsNotFull = new ArrayList<>(); //Stores the total amount of players  on Red Team.
    public static List<Integer> availableTeams = new ArrayList<>();
    public static String playersTeam = "";
    public static Connection connection = Scavengerhunt.connection; // Establishes a connection to mySQL database
    public static List<String> teamNames = new ArrayList<String>();


    public static void teamPermissions(Team team) {
        team.setAllowFriendlyFire(false); // disable friendly fire
        team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS); // hides players name tag from other teams
        team.setCanSeeFriendlyInvisibles(false); // sets it so you cant see invisible players on team
    }

    public static void assignTeam(String Team, Player player) {
        Team team = board.getTeam(Team);
        team.addPlayer(player);
    }
    public static void removePlayerFromTeam (String Team, Player player){
        Team team = board.getTeam(Team);
        team.removePlayer(player);
    }
    public static void deleteTeam(String Team){
        Team team = board.getTeam(Team);
        team.unregister();
    }

    public static void getScore(String teamName, List totalPoints) throws SQLException {
            Statement statement = connection.createStatement(); // creates a statement
            List<Integer> Points = new ArrayList<Integer>(); // used to store all the points from each player on team
            ResultSet results = statement.executeQuery("SELECT * FROM " + teamName + "  WHERE Points;"); // gets all points data from team's table
            while (results.next()) {
                Integer point = results.getInt("POINTS"); // stores point from that row into an interger
                Points.add(point); // adds it to list
            }

            results.close(); // close results
            statement.close(); // close statement
            totalPoints.clear();
            totalPoints.add(Points.stream().mapToInt(Integer::intValue).sum()); // sums all the points from each player
    }


    public static String getRandomElement(List<String> list) {
        Random rand = new Random(); // initializes rand
        return list.get(rand.nextInt(list.size())); // randomly returns a value that is stored in the list.
    }

    public static void getFullTeams(List teamsNotFull, Integer maxTeamSize, List teamsFull) throws SQLException {
        getTeamNames();
        teamsNotFull.clear();
        teamsFull.clear();
        for (int i = 0; i < teamNames.size(); i++) {
            List<String> playernames = new ArrayList<String>();
            String teamName = teamNames.get(i);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + teamName + ""); // Stores all data from Yellow Team.
            // Loops through each row of data:
            while (results.next()) {
                String name = results.getString("PlayerNames"); // Stores the player's name at that row.
                playernames.add(name);
            }

            int totalPlayers = playernames.size(); //Stores the total amount of players on Blue Team used to check if team is full.
            if (totalPlayers < maxTeamSize) {
                teamsNotFull.add(teamName);
            } else if (totalPlayers == maxTeamSize){
                teamsFull.add(teamName);
            }
            results.close(); // Closes results since no longer needed.
            statement.close();
        }

    }

    public static void getTeamNames() throws SQLException {
        teamNames.clear();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("Show tables");
        while (results.next()) {
            String teamName = results.getString(1);
            teamNames.add(teamName);
        }
        statement.close();
        results.close();
    }

    public static void createTeams() {
        String teamName = "";
        for (int i = 0; i < teamNames.size(); i++){
            teamName = teamNames.get(i);
            if (board.getTeam(teamName) == null){
                Team playerTeam = board.registerNewTeam(teamName);
                playerTeam.setPrefix(ChatColor.GRAY + "[" + teamName + "]" + ChatColor.WHITE); // Creates player tag for Team
                teamPermissions(playerTeam); // Sets up team permissions

            }
        }

    }

    public static void getTopScores(List<Integer> firstScore, List<Integer>  secondScore, List<Integer>  thirdScore) throws SQLException {
        getTeamNames();
        List<Integer> totalPoints = new ArrayList<>(); // initializes variable to store total points
        for (int i = 0; i < teamNames.size(); i++) {
            if (i == 0){
                totalPoints.clear();
            }
            String teamName = teamNames.get(i);
            Statement statement = connection.createStatement(); // creates a statement
            List<Integer> Points = new ArrayList<Integer>(); // used to store all the points from each player on team
            ResultSet results = statement.executeQuery("SELECT * FROM " + teamName + "  WHERE Points;"); // gets all points data from team's table
            while (results.next()) {
                Integer point = results.getInt("POINTS"); // stores point from that row into an interger
                Points.add(point); // adds it to list
            }

            results.close(); // close results
            statement.close(); // close statement
            totalPoints.add(Points.stream().mapToInt(Integer::intValue).sum()); // sums all the points from each player

        }
        if (totalPoints.size() >= 1){
            Collections.sort(totalPoints);
            Collections.reverse(totalPoints);
            firstScore.add(totalPoints.get(0));
            if(totalPoints.size() >= 2){
                secondScore.add(totalPoints.get(1));
            }
            if(totalPoints.size() >= 3){
                thirdScore.add(totalPoints.get(2));
            }
        }

    }
    public static void getTopTeams(List<String> one, List<String> two, List<String> three, List<Integer> firstScore, List<Integer>  secondScore, List<Integer>  thirdScore) throws SQLException {
        getTeamNames();
        Boolean oneSet = false;
        Boolean twoSet = false;
        Boolean threeSet = false;
        List<Integer> totalPoints = new ArrayList<>(); // initializes variable to store total points
        for (int i = 0; i < teamNames.size(); i++) {
            if (i == 0){
                totalPoints.clear();
            }
            String teamName = teamNames.get(i);
            Statement statement = connection.createStatement(); // creates a statement
            List<Integer> Points = new ArrayList<Integer>(); // used to store all the points from each player on team
            ResultSet results = statement.executeQuery("SELECT * FROM " + teamName + "  WHERE Points;"); // gets all points data from team's table
            while (results.next()) {
                Integer point = results.getInt("POINTS"); // stores point from that row into an interger
                Points.add(point); // adds it to list
            }

            results.close(); // close results
            statement.close(); // close statement
            totalPoints.add(Points.stream().mapToInt(Integer::intValue).sum()); // sums all the points from each player

            if(!firstScore.isEmpty()){
                if (totalPoints.get(i).equals(firstScore.get(0)) && oneSet.equals(false)){
                    one.clear();
                    one.add(teamName);
                    oneSet = true;
                    continue;
                }
            }
            if (!secondScore.isEmpty()){
                if(totalPoints.get(i).equals(secondScore.get(0)) && twoSet.equals(false)){
                    two.clear();
                    two.add(teamName);
                    twoSet = true;
                    continue;
                }
            }
            if (!thirdScore.isEmpty()){
                if (totalPoints.get(i).equals(thirdScore.get(0)) && threeSet.equals(false)){
                    three.clear();
                    three.add(teamName);
                    threeSet = true;
                    continue;
                }
            }

        }

    }
    public static void getPlayersTeam(String playerName, List<String> team, List<Boolean> onTeam) throws SQLException {
        getTeamNames();
        for (int i = 0; i < teamNames.size(); i++) {
            String teamName = teamNames.get(i);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + teamName + ""); // Stores all data from Yellow Team.
            // Loops through each row of data:
            while (results.next()) {
                String name = results.getString("PlayerNames"); // Stores the player's name at that row.
                if (name.equals(playerName)) {
                    team.clear();
                    onTeam.clear();
                    team.add(0, teamName);
                    onTeam.add(0, true);
                    playerOnTeam = true;
                    break;
                }
            }
            results.close(); // Closes results since no longer needed.
            statement.close();
        }
    }
    public static void addNewScore(Connection connection, String playerName, int newScore, Player player, String teamName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet teamResults = statement.executeQuery("SELECT * FROM " +teamName+ "");
        while (teamResults.next()) {
            Integer Points = teamResults.getInt("Points");
            newScore = Points + newScore;
            statement.executeUpdate("update " +teamName+ " set Points = " + newScore + " where PlayerNames = '" +playerName+ "'");
            player.sendMessage(ChatColor.GOLD + "You Have Earned Points For Your Team");
            break;
        }
        teamResults.close();
        statement.close();
    }

}
