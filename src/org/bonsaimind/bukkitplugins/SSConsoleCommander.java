/**
 * Programmer: Jacob Scott
 * Program Name: SSConsoleCommander
 * Description:
 * Date: Apr 22, 2011
 */
package org.bonsaimind.bukkitplugins;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.ServerCommand;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

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

    public void runCommand(String command) {
        if (cserv == null) {
            serv.dispatchCommand(new RunCommander(true), command);
        } else {
            //cserv.getServer().a(command);
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

		public void setOp(boolean bln) {
			op = bln;
		}
		
        public Server getServer() {
            return serv;
        }

        public void sendMessage(String string) {
            //serv.getLogger().log(Level.INFO, string);
        }

		public boolean isPermissionSet(String string) {
			return true;
		}

		public boolean isPermissionSet(Permission prmsn) {
			return true;
		}

		public boolean hasPermission(String string) {
			return true;
		}

		public boolean hasPermission(Permission prmsn) {
			return true;
		}

		public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln) {
		return null;
		}

		public PermissionAttachment addAttachment(Plugin plugin) {
		return null;
		}

		public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln, int i) {
		return null;
		}

		public PermissionAttachment addAttachment(Plugin plugin, int i) {
		return null;
		}

		public void removeAttachment(PermissionAttachment pa) {
		}

		public void recalculatePermissions() {
		}

		public Set<PermissionAttachmentInfo> getEffectivePermissions() {
			HashSet<PermissionAttachmentInfo> ret = new HashSet<PermissionAttachmentInfo>();
			ret.add(new PermissionAttachmentInfo(this, "*", null, true));
			return ret;
		}

    }
} // end class SSConsoleCommander

