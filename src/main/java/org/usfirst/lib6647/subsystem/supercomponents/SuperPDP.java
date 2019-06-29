package org.usfirst.lib6647.subsystem.supercomponents;

import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.usfirst.lib6647.subsystem.PIDSuperSubsystem;
import org.usfirst.lib6647.subsystem.SuperSubsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Interface to allow {@link PowerDistributionPanel} initialization via JSON.
 * Subsystems declared need to extend {@link SuperSubsystem} or
 * {@link PIDSuperSubsystem} and implement this interface in order to initialize
 * {@link PowerDistributionPanel PDPs} declared in
 * {@link SuperSubsystem#robotMap robotMap}.
 */
public interface SuperPDP {
	/**
	 * HashMap storing the {@link SuperSubsystem}'s {@link PowerDistributionPanel
	 * PDPs}.
	 */
	public HashMap<String, PowerDistributionPanel> PDPs = new HashMap<String, PowerDistributionPanel>();

	/**
	 * Method to initialize {@link PowerDistributionPanel PDPs} declared in the
	 * {@link SuperSubsystem#robotMap robotMap} JSON file, and add them to the
	 * {@link #PDPs} HashMap using its declared name as its key.
	 * 
	 * @param {@link SuperSubsystem#robotMap}
	 * @param {@link SuperSubsystem#getName}
	 */
	default void initPDPs(JSONObject robotMap, String subsystemName) {
		try {
			// Create a JSONArray out of the declared objects.
			JSONArray PDPArray = (JSONArray) ((JSONObject) ((JSONObject) robotMap.get("subsystems")).get(subsystemName))
					.get("PDPs");
			// Create a stream to cast each entry in the JSONArray into a JSONObject, in
			// order to configure it using the values declared in the robotMap file.
			Arrays.stream(PDPArray.toArray()).map(json -> (JSONObject) json).forEach(json -> {
				try {
					// Create an object out of one index in the JSONArray.
					PowerDistributionPanel pdp = new PowerDistributionPanel(
							Integer.parseInt(json.get("module").toString()));
					// Put object in HashMap with its declared name as key after initialization and
					// configuration.
					PDPs.put(json.get("name").toString(), pdp);
				} catch (Exception e) {
					DriverStation.reportError(
							"[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' PDP INIT ERROR: " + e.getMessage(),
							false);
					System.out.println(
							"[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' PDP INIT ERROR: " + e.getMessage());
					System.exit(1);
				} finally {
					// Clear JSONObject after use, not sure if it does anything, but it might free
					// some unused memory.
					json.clear();
				}
			});
			// Clear JSONArray after use, not sure if it does anything, but it might free
			// some unused memory.
			PDPArray.clear();
		} catch (Exception e) {
			DriverStation.reportError(
					"[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' PDP INIT ERROR: " + e.getMessage(), false);
			System.out.println("[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' PDP INIT ERROR: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Gets specified {@link PowerDistributionPanel}.
	 * 
	 * @return {@link PowerDistributionPanel}
	 * @param pdpName
	 */
	default PowerDistributionPanel getPDP(String pdpName) {
		return PDPs.get(pdpName);
	}
}