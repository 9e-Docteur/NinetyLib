package fr.ninedocteur.ninetylib.api;

import fr.ninedocteur.ninetylib.utils.MathUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

/**
 * Custom, beter cooldown system wich work with seconds for easy use.
 */
public class Cooldown {
    public static int cooldown;

    public static void addCooldown(Player player, Item item, int seconds){
        player.getCooldowns().addCooldown(item, MathUtils.convertSecondsToTick(seconds));
        Cooldown.setCooldown(seconds);
    }


    public static int getCooldown() {
        return cooldown;
    }

    public static void setCooldown(int cooldown) {
        Cooldown.cooldown = cooldown;
    }

    public static void substractCooldown(int toSubstract){
        int i = getCooldown() - toSubstract;
        Cooldown.setCooldown(i);
    }

    public static void addToCooldown(int toAdd){
        int i = getCooldown() + toAdd;
        Cooldown.setCooldown(i);
    }

    public static void removeCooldown(Player player, Item item){
        player.getCooldowns().removeCooldown(item);
        Cooldown.setCooldown(0);
    }
}
