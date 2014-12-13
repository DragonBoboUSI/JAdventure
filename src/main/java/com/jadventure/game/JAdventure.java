package com.jadventure.game;

import com.jadventure.game.menus.MainMenu;
import com.jadventure.game.QueueProvider;
import com.jadventure.game.Client;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the starting point of the game.
 * This class doesn't do much more than create
 * a new MainMenu that will handle the rest of
 * the game.
 */
public class JAdventure {
    private static Logger logger = LoggerFactory.getLogger(JAdventure.class);

    public static void main(String[] args) {
        logger.info("Starting JAdventure " + toString(args));
        GameModeType mode = GameModeType.STAND_ALONE;
        if (args.length == 1) {
            mode = GameModeType.valueOf(args[0].toUpperCase());
        }
        if (GameModeType.CLIENT == mode) {
            new Client();
        } else {
            if (GameModeType.SERVER == mode) {
                while (true) {
                	ServerSocket listener = null;
                    try {
                        listener = new ServerSocket(4044);
                        while (true) {
                            Socket server = listener.accept();
                            Runnable r = new MainMenu(server,mode);
                            new Thread(r).start();
                        }
                    } catch (SocketException e) { 
                       e.printStackTrace();
                    } catch (IOException c) {
                        c.printStackTrace();
                    } finally {
                    	try {
							listener.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
                    }
                }
            } else {
                QueueProvider.startMessenger(GameModeType.STAND_ALONE);
                new MainMenu();
            }
        }
    }

    private static String toString(String[] args) {
        if (args.length == 0) {
            return "";
        }

        final StringBuilder bldr = new StringBuilder();
        bldr.append("[ ");
        for (int index = 0; index < args.length; index++) {
            if (index > 0) {
                bldr.append(", ");
            }
            bldr.append(args[index]);
        }
        bldr.append(" ]");
        return bldr.toString();
    }
}
