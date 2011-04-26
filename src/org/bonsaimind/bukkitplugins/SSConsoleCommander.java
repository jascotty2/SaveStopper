/**
 * Programmer: Jacob Scott
 * Program Name: SSConsoleCommander
 * Description:
 * Date: Apr 22, 2011
 */
package org.bonsaimind.bukkitplugins;

import net.minecraft.server.ServerCommand;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;

/**
 * @author jacob
 */
public class SSConsoleCommander {

    static Server serv = null;
    static CraftServer cserv = null;

    public SSConsoleCommander(Server sv) {
        serv = sv;

        if (!(sv instanceof CraftServer)) {
            System.err.println("CommandHelper: server is not a CraftServer...is the plugin broken?");
            cserv = null;
        } else {
            cserv = (CraftServer) sv;
        }

    } // end default constructor

    public static void queueConsoleCommand(Server sv, String command) {
        serv = sv;
        if (!(sv instanceof CraftServer)) {
            System.err.println("CommandHelper: server is not a CraftServer...is the plugin broken?");
            cserv = null;
        } else {
            cserv = (CraftServer) sv;
        }
        if (cserv == null) {
            sv.dispatchCommand(new RunCommander(true), command);
        } else {
            cserv.getServer().a(command);
        }
    }

    public void runCommand(String command) {
        if (cserv == null) {
            serv.dispatchCommand(new RunCommander(true), command);
        } else {
            //cserv.getServer().a(command);
            cserv.getServer().consoleCommandHandler.handle(new ServerCommand(command, cserv.getServer()));//a(command);
        }
    }

    public static void queueConsoleCommand(String command) {
        if (cserv == null) {
            serv.dispatchCommand(new RunCommander(true), command);
        } else {
            cserv.getServer().consoleCommandHandler.handle(new ServerCommand(command, cserv.getServer()));//a(command);
        }
    }

    public static class RunCommander implements CommandSender {

        boolean op = false;

        public RunCommander(boolean asOp) {
            op = asOp;
        }

        public boolean isOp() {
            return op;
        }

        public Server getServer() {
            return serv;
        }

        public void sendMessage(String string) {
            //serv.getLogger().log(Level.INFO, string);
        }
    }
} // end class SSConsoleCommander

