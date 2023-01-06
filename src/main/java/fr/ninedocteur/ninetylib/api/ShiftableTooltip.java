package fr.ninedocteur.ninetylib.api;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ShiftableTooltip {
    private static List<Component> components = new ArrayList();
    private static Level level = Minecraft.getInstance().level;
    private static boolean isComponentCleared;

//    public static void addToItem(Item item, Component text){
//        if(Screen.hasShiftDown()){
//            setComponents(text);
//            item.appendHoverText(item.getDefaultInstance(), level, getComponents(), TooltipFlag.Default.NORMAL);
//        } else {
//            item.appendHoverText(item.getDefaultInstance(), level, getShiftComponent(), TooltipFlag.Default.NORMAL);
//        }
//    }

    public static void addToBlock(Block block, Component text){
        if(Screen.hasShiftDown()){
            setComponents(text);
            block.appendHoverText(block.asItem().getDefaultInstance(), null, getComponents(), TooltipFlag.Default.NORMAL);
        } else {
            block.appendHoverText(block.asItem().getDefaultInstance(), null, getShiftComponent(), TooltipFlag.Default.NORMAL);
        }
    }

    public static void setComponents(Component component) {
        ShiftableTooltip.components.add(component);
    }

    private static List<Component> getComponents() {
        if(isIsComponentCleared()){
            setIsComponentCleared(false);
        }
        return components;
    }

    private static boolean isIsComponentCleared() {
        return isComponentCleared;
    }

    private static void setIsComponentCleared(boolean isComponentCleared) {
        ShiftableTooltip.isComponentCleared = isComponentCleared;
    }

    public static List<Component> getShiftComponent(){
        //if(!isIsComponentCleared()){
          //  ShiftableTooltip.components.clear();
        //    setIsComponentCleared(true);
        //}
        setComponents(Component.literal("Press " + ChatFormatting.YELLOW + ChatFormatting.BOLD + "SHIFT " + ChatFormatting.RESET + "to get more informations"));
        return components;
    }

    public static boolean isComponentEmpty(){
        if(components.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
