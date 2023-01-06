package fr.ninedocteur.ninetylib.api;

import fr.ninedocteur.ninetylib.utils.MathUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

/**
 * Custom, beter cooldown system wich work with seconds for easy use.
 */
public class Cooldown {
    public int cooldown;

    public void addCooldown(Player player, Item item, int seconds){
        player.getCooldowns().addCooldown(item, MathUtils.convertSecondsToTick(seconds));
        this.setCooldown(seconds);
    }


    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void substractCooldown(int toSubstract){
        int i = getCooldown() - toSubstract;
        this.setCooldown(i);
    }

    public void addToCooldown(int toAdd){
        int i = getCooldown() + toAdd;
        this.setCooldown(i);
    }

    public void removeCooldown(Player player, Item item){
        player.getCooldowns().removeCooldown(item);
        this.setCooldown(0);
    }
}
