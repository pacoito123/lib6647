package org.usfirst.lib6647.subsystem.supercomponents;

import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Interface to allow PowerDistributionPanel initialization via JSON file.
 * Subsystem needs to extend SuperSubsystem.
 */
public interface SuperPDP {
	/**
	 * HashMap storing the SuperSubsystem's PowerDistributionPanel objects.
	 */
	public HashMap<String, PowerDistributionPanel> PDPs = new HashMap<String, PowerDistributionPanel>();

	/**
	 * Method to initialize PDPs declared in JSON file, and add them to the HashMap
	 * using its name as its key.
	 * 
	 * @param robotMap
	 * @param subsystemName
	 */
	default void initPDPs(JSONObject robotMap, String subsystemName) {
		try {
			JSONArray PDPArray = (JSONArray) ((JSONObject) ((JSONObject) robotMap.get("subsystems")).get(subsystemName))
					.get("PDPs");
			Arrays.stream(PDPArray.toArray()).map(json -> (JSONObject) json).forEach(json -> {
				try {
					PowerDistributionPanel pdp = new PowerDistributionPanel(
							Integer.parseInt(json.get("module").toString()));
					PDPs.put(json.get("name").toString(), pdp);
				} catch (Exception e) {
					DriverStation.reportError(
							"[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' PDP INIT ERROR: " + e.getMessage(),
							false);
					System.out.println(
							"[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' PDP INIT ERROR: " + e.getMessage());
					System.exit(1);
				} finally {
					json.clear();
				}
			});
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