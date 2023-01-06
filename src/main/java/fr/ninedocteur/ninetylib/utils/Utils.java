package fr.ninedocteur.ninetylib.utils;

import com.mojang.blaze3d.platform.NativeImage;
import fr.ninedocteur.ninetylib.NinetyLib;
import fr.ninedocteur.ninetylib.integration.discord.rpc.DiscordEventHandlers;
import fr.ninedocteur.ninetylib.integration.discord.rpc.DiscordRPC;
import fr.ninedocteur.ninetylib.integration.discord.rpc.DiscordRichPresence;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.world.entity.player.Player;
import org.stringtemplate.v4.ST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utils {
    public static class Game {
        public static String getFPS(Minecraft mc) {

            return formatText(mc.fpsString.split("\\s+")[0]);

        }

        private static String formatText(String text) {

            Component fpsString = Component.literal(text);

            return fpsString.getString();

        }

        public static int getColoredFPS(Minecraft mc) {
            if(Integer.valueOf(getFPS(mc)) < 29) {
                return ColorUtils.getRed();
            } else if(Integer.valueOf(getFPS(mc)) < 50){
                return ColorUtils.getOrange();
            } else {
                return ColorUtils.getGreen();
            }
        }
    }

    public static class Discord{
        public static int APPID;
        public static String state;
        public static String detail;
        public static String largeImage;
        public static String largeImageText;

        public static void startRPC(String appId, String state, String detail, String largeImage, String largeImageText){
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = appId;
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            handlers.ready = (user) ->{
                NinetyLib.LOGGER.info("Ninety's Lib RPC Ready!");
                NinetyLib.LOGGER.warn("Ninety's Lib RPC Version 1.0.1");
            };
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.largeImageKey = largeImage;
            presence.largeImageText = largeImageText;
           // presence.smallImageKey = "zinc_icon";
            //presence.smallImageText = "Player: " + Minecraft.getInstance().getUser().getName();
            presence.details = detail;
            presence.state = state;
            lib.Discord_UpdatePresence(presence);
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        lib.Discord_UpdatePresence(presence);
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}
                }
            }, "Ninety's Lib-RPC").start();
        }

        public static void startRPC(String appId, String state, String detail, String largeImage, String largeImageText, String smallImage, String smallImageText){
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = appId;
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            handlers.ready = (user) ->{
                NinetyLib.LOGGER.info("Ninety's Lib RPC Ready!");
                NinetyLib.LOGGER.warn("Ninety's Lib RPC Version 1.0.1");
            };
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.largeImageKey = largeImage;
            presence.largeImageText = largeImageText;
            presence.smallImageKey = smallImage;
            presence.smallImageText = smallImageText;
            presence.details = detail;
            presence.state = state;
            lib.Discord_UpdatePresence(presence);
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        lib.Discord_UpdatePresence(presence);
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}
                }
            }, "Ninety's Lib-RPC").start();
        }
    }

    public static class Web {
        private static final Map<String, ResourceLocation> DOWNLOADED_TEXTURES = new HashMap<>();
        public static String readWebContent(String stringurl) {
            try {
                URL url = new URL(stringurl);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String string;
                while ((string = bufferedReader.readLine()) != null) stringBuffer.append(string);
                bufferedReader.close();
                return stringBuffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return ChatFormatting.RED + "Cannot get information";
            }
        }

        public static ResourceLocation readTexture(final String url, String name, String modId){
            if(DOWNLOADED_TEXTURES.containsKey(url)){
                return DOWNLOADED_TEXTURES.get(url);
            }
            ResourceLocation resourceLocation = new ResourceLocation(modId, "textures/" + name.toLowerCase());
            DOWNLOADED_TEXTURES.put(url, resourceLocation);
            try{
                InputStream inputStream = new URL(url).openStream();
                NativeImage image = NativeImage.read(inputStream);
                DynamicTexture texture = new DynamicTexture(image);
                inputStream.close();
                Minecraft.getInstance().getTextureManager().register(resourceLocation, texture);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resourceLocation;
        }

        public static void openLink(String url){
            Util.getPlatform().openUri(url);
        }
    }

    public class Screen {
        public static void openScreen(net.minecraft.client.gui.screens.Screen screen) {
            Minecraft.getInstance()
                    .tell(() -> {
                        Minecraft.getInstance()
                                .setScreen(screen);
                    });
        }
    }

    public static class Player {
        public static boolean isSneaking(net.minecraft.world.entity.player.Player player){
            return player.isCrouching();
        }

        public static boolean isInCreativeMode(net.minecraft.world.entity.player.Player player){
            return player.isCreative();
        }

        public static boolean isInSurvivalMode(net.minecraft.world.entity.player.Player player){
            return !player.isCreative();
        }

        public static ResourceLocation getPlayerHead(final String name){
            return Utils.Web.readTexture("https://minotar.net/helm/" + name + "/16.png", name + "_player_helm", NinetyLib.MODID);
        }

        public static ResourceLocation get3DPlayerHead(final String uuid){
            return Utils.Web.readTexture("https://crafatar.com/renders/head/" + uuid + "?scale=10&size=512&overlay", uuid + "_player_head", NinetyLib.MODID);
        }

        public static ResourceLocation get3DSkin(final String uuid){
            return Utils.Web.readTexture("https://crafatar.com/renders/body/" + uuid, uuid + "_player_body", NinetyLib.MODID);
        }

        public static ResourceLocation getSkin(final String UUID){
            return Utils.Web.readTexture("https://crafatar.com/skins/", UUID, NinetyLib.MODID);
        }

        public static String getUserUUID(String playerName) {
            return Utils.Web.readWebContent("https://minecraft-api.com/api/uuid/" + playerName);
        }

        public static String getUserNameByUUID(UUID playerUUID) {
            return Utils.Web.readWebContent("https://minecraft-api.com/api/pseudo/" + playerUUID);
        }
    }
}
