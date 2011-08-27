/**
 * Programmer: Jacob Scott
 * Program Name: SaveStopperInterface
 * Description:
 * Date: Aug 26, 2011
 */
package org.bonsaimind.bukkitplugins;

import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Server;
import org.bukkit.util.config.Configuration;

/**
 * @author jacob
 */
public class SaveStopperInterface {

	SaveStopper plugin;
	Server server;
	private SSConsoleCommander serverCommander;
	private Timer timer = new Timer(true);
	private boolean isSaving = true;

	// settings
	public boolean verbose = true,
			disableOnStart = true,
			saveAll = true;
	protected int waitTime = 300;

	public SaveStopperInterface(SaveStopper plugin) {
		this.plugin = plugin;
		this.server = plugin.getServer();
		this.serverCommander = new SSConsoleCommander(server);
	}

	public void loadConfig(){
		Configuration config = plugin.getConfiguration();
		boolean needsave = config.getProperty("disableOnStart") == null
				 || config.getProperty("saveAll") == null
				 || config.getProperty("wait") == null
				 || config.getProperty("verbose") == null;
		disableOnStart = config.getBoolean("disableOnStart", disableOnStart);
		saveAll = config.getBoolean("saveAll", saveAll);
		verbose = config.getBoolean("verbose", verbose);
		waitTime = config.getInt("wait", waitTime);
		if(needsave){
			config.save();
		}
	}

	
	public void cancel(){
		timer.purge();
	}

	/**
	 * Enable saving.
	 */
	public void enable() {
		if (server.getOnlinePlayers().length == 0 && isSaving && verbose) {
			System.out.println("SaveStopper: Canceling scheduled disabling...");
			timer.purge();
		}

		if (!isSaving) {
			if (verbose) {
				System.out.println("SaveStopper: Enabling saving...");
			}
			serverCommander.runCommand("save-on");
			isSaving = true;
		}
	}

	/**
	 * Disable saving, check if we should use the timer or not.
	 */
	public void disable() {
		if (isSaving && server.getOnlinePlayers().length <= 1) {
			if (waitTime > 0) {
				if (verbose) {
					System.out.println("SaveStopper: Scheduling disabling in " + waitTime + " seconds...");
				}

				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						disableNow();
					}
				}, waitTime * 1000);
			} else {
				disableNow();
			}
		}
	}

	/**
	 * Disable saving.
	 */
	public void disableNow() {
		if (isSaving && server.getOnlinePlayers().length == 0) {
			if (verbose) {
				System.out.println("SaveStopper: Disabling saving...");
			}

			if (saveAll) {
				serverCommander.runCommand("save-all");
			}

			serverCommander.runCommand("save-off");

			isSaving = false;
		}
	}
} // end class SaveStopperInterface

