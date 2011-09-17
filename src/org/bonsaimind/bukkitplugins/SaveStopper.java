/*
 * This file is part of SaveStopper.
 *
 * SaveStopper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SaveStopper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SaveStopper.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Author: Robert 'Bobby' Zenz
 * Website: http://www.bonsaimind.org
 * GitHub: https://github.com/RobertZenz/org.bonsaimind.bukkitplugins/tree/master/SaveStopper
 * E-Mail: bobby@bonsaimind.org
 */
package org.bonsaimind.bukkitplugins;

import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
// for server loading wait
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.Server;

/**
 * 
 * @author Robert 'Bobby' Zenz
 */
public class SaveStopper extends JavaPlugin {

	private SaveStopperPlayerListener listener;
	private SaveStopperInterface saveStopper;

	public void onEnable() {
		saveStopper = new SaveStopperInterface(this);
		saveStopper.loadConfig();
		listener = new SaveStopperPlayerListener(saveStopper);

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_LOGIN, listener, Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_QUIT, listener, Priority.Monitor, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " " + pdfFile.getVersion() + " is enabled.");

		if (saveStopper.disableOnStart) {
			//saveStopper.disableNow();
			pm.registerEvent(Type.PLUGIN_ENABLE, new ServerLoadWait(this), Priority.Monitor, this);
		}
	}

	public void onDisable() {
		saveStopper.cancel();
		saveStopper = null;
		listener = null;
	}

	public SaveStopperInterface getSaveStopper() {
		return saveStopper;
	}

	private static class ServerLoadWait extends ServerListener {

		Server sv;
		SaveStopperInterface i;
		int wait = 15;
		Timer tm;
		boolean isRun = false;

		public ServerLoadWait(SaveStopper plugin) {
			sv = plugin.getServer();
			i = plugin.getSaveStopper();
			//wait = i.waitTime;
		}

		public void newWait() {
			if (tm != null) {
				tm.cancel();
			}
			tm = new Timer();
			tm.schedule(new TimerTask() {

				@Override
				public void run() {
					isRun = true;
					i.disableNow();
				}
			}, wait * 1000);
		}

		@Override
		public void onPluginEnable(PluginEnableEvent event) {
			if(!isRun)
			newWait();
		}
	}
}
