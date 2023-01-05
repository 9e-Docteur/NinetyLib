package fr.ninedocteur.ninetylib.utils;

import com.mojang.blaze3d.platform.NativeImage;
import fr.ninedocteur.ninetylib.NinetyLib;
import fr.ninedocteur.ninetylib.integrations.discord.IPCClient;
import fr.ninedocteur.ninetylib.integrations.discord.IPCListener;
import fr.ninedocteur.ninetylib.integrations.discord.entities.DiscordBuild;
import fr.ninedocteur.ninetylib.integrations.discord.entities.RichPresence;
import fr.ninedocteur.ninetylib.integrations.discord.exceptions.NoDiscordClientException;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.platform.NativeImage;
import org.stringtemplate.v4.ST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public class Game {
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

    public class Discord{
        public static long APPID;
        public static String state;
        public static String detail;
        public static String largeImage;
        public static String largeImageText;
        public static boolean button1enable;
        public static boolean button2enable;
        public static String button1Text;
        public static String button2Text;
        public static String button1url;
        public static String button2url;
        public Discord(long appId, String state, String detail, String largeImage, String largeImageText){
            this.APPID = appId;
            this.state = state;
            this.detail = detail;
            this.largeImage = largeImage;
            this.largeImageText = largeImageText;
            button1enable = false;
            button2enable = false;
        }

        /**
         *
         * @param appId
         * @param state
         * @param detail
         * @param largeImage
         * @param largeImageText
         * @param button1Text
         * @param button1url
         * With one button
         */
        public Discord(long appId, String state, String detail, String largeImage, String largeImageText, String button1Text, String button1url){
            this.APPID = appId;
            this.state = state;
            this.detail = detail;
            this.largeImage = largeImage;
            this.largeImageText = largeImageText;
            this.button1Text = button1Text;
            this.button1url = button1url;
            button1enable = true;
            button2enable = false;
        }

        /**
         *
         * @param appId
         * @param state
         * @param detail
         * @param largeImage
         * @param largeImageText
         * @param button1Text
         * @param button1url
         * @param button2Text
         * @param button2url
         * With two button
         */

        public Discord(long appId, String state, String detail, String largeImage, String largeImageText, String button1Text, String button1url, String button2Text, String button2url){
            this.APPID = appId;
            this.state = state;
            this.detail = detail;
            this.largeImage = largeImage;
            this.largeImageText = largeImageText;
            this.button1Text = button1Text;
            this.button1url = button1url;
            this.button2Text = button2Text;
            this.button2url = button2url;
            button1enable = true;
            button2enable = true;
        }

        public static final RichPresence.Builder DEFAULT_BUILDER =  new RichPresence.Builder().setState(getState()).setDetails(getDetail()).setLargeImage(getLargeImage(), getLargeImageText());
        public static final RichPresence.Builder ONEBTN_BUILDER =  new RichPresence.Builder().setState(getState()).setDetails(getDetail()).setLargeImage(getLargeImage(), getLargeImageText()).setButton1(getButton1Text(), getButton1url());
        public static final RichPresence.Builder TWOBTN_BUILDER =  new RichPresence.Builder().setState(getState()).setDetails(getDetail()).setLargeImage(getLargeImage(), getLargeImageText()).setButton1(getButton1Text(), getButton1url()).setButton2(getButton2Text(), getButton2url());

        public static IPCClient RPC;

        public static void startRPC(long appID) {
            RPC = new IPCClient(appID);
            RPC.setListener(new IPCListener() {
                @Override
                public void onReady(IPCClient client) {
                    if(!isButton1enable() && !isButton2enable()){
                        client.sendRichPresence(DEFAULT_BUILDER.build());
                    } else if(isButton1enable()){
                        client.sendRichPresence(ONEBTN_BUILDER.build());
                    } else if(isButton2enable()){
                        client.sendRichPresence(TWOBTN_BUILDER.build());
                    }
                    NinetyLib.LOGGER.info("Ninety's Lib RPC Ready!");
                    NinetyLib.LOGGER.warn("Ninety's Lib RPC Version 2.0");
                }
            });
            try {
                NinetyLib.LOGGER.info("Starting Ninety's Lib RPC...");
                RPC.connect(DiscordBuild.ANY);
            } catch (NoDiscordClientException e) {
                NinetyLib.LOGGER.error("Ninety's Lib RPC Failed:\n" + e.getMessage());
            }
        }

        public long getAppID() {
            return APPID;
        }

        public static String getState() {
            return state;
        }

        public static String getDetail() {
            return detail;
        }

        public static String getLargeImage() {
            return largeImage;
        }

        public static String getLargeImageText() {
            return largeImageText;
        }

        public static String getButton1Text() {
            return button1Text;
        }

        public static String getButton1url() {
            return button1url;
        }

        public static String getButton2Text() {
            return button2Text;
        }

        public static String getButton2url() {
            return button2url;
        }

        public static boolean isButton1enable() {
            return button1enable;
        }

        public static boolean isButton2enable() {
            return button2enable;
        }
    }

    public class Web {
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
}
