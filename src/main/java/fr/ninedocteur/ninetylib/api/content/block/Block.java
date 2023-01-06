package fr.ninedocteur.ninetylib.api.content.block;

import fr.ninedocteur.ninetylib.api.Cooldown;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Block extends net.minecraft.world.level.block.Block {
    private static boolean addInfo;
    private Component text;

    public Block(Properties p_49795_) {
        super(p_49795_);
    }

    public void addShiftableInformation(Component text){
        this.text = text;
        addInfo = true;
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable BlockGetter p_49817_, List<Component> p_49818_, TooltipFlag p_49819_) {
        super.appendHoverText(p_49816_, p_49817_, p_49818_, p_49819_);
        if(addInfo) {
            if (Screen.hasShiftDown()) {
                p_49818_.add(this.text);
            } else {
                p_49818_.add(Component.literal("Press " + ChatFormatting.YELLOW + ChatFormatting.BOLD + "SHIFT " + ChatFormatting.RESET + "to get more informations"));
            }
        }
    }
}
