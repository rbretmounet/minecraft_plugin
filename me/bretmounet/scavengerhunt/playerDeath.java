package me.bretmounet.scavengerhunt;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class playerDeath {
    @EventHandler
    public  void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        // Code to set players status from Alive to Dead.
    }
}
