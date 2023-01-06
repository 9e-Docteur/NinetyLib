package fr.ninedocteur.ninetylib.api.content.item;

import fr.ninedocteur.ninetylib.api.Cooldown;
import fr.ninedocteur.ninetylib.api.ShiftableTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Item extends net.minecraft.world.item.Item {
    private static boolean addInfo;
    private Component text;
    private Cooldown cooldown = new Cooldown();
    public Item(Properties p_41383_) {
        super(p_41383_);
    }

    public void addShiftableInformation(Component text){
        this.text = text;
        addInfo = true;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        if(addInfo) {
            if (Screen.hasShiftDown()) {
                p_41423_.add(this.text);
            } else {
                p_41423_.add(Component.literal("Press " + ChatFormatting.YELLOW + ChatFormatting.BOLD + "SHIFT " + ChatFormatting.RESET + "to get more informations"));
            }
        }
    }

    public Cooldown getCooldown(){
        return this.cooldown;
    }
}
