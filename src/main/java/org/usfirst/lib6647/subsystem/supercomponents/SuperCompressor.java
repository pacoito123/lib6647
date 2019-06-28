package org.usfirst.lib6647.subsystem.supercomponents;

import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Interface to allow Compressor initialization via JSON file. Subsystem needs
 * to extend SuperSubsystem.
 */
public interface SuperCompressor {
	/**
	 * HashMap storing the SuperSubsystem's Compressors.
	 */
	public HashMap<String, Compressor> compressors = new HashMap<String, Compressor>();

	/**
	 * Method to initialize Compressors declared in JSON file, and add them to the
	 * HashMap using its name as its key.
	 * 
	 * @param robotMap
	 * @param subsystemName
	 */
	default void initCompressors(JSONObject robotMap, String subsystemName) {
		try {
			JSONArray compressorArray = (JSONArray) ((JSONObject) ((JSONObject) robotMap.get("subsystems"))
					.get(subsystemName)).get("compressors");
			Arrays.stream(compressorArray.toArray()).map(json -> (JSONObject) json).forEach(json -> {
				try {
					Compressor compressor = new Compressor(Integer.parseInt(json.get("module").toString()));
					compressors.put(json.get("name").toString(), compressor);
				} catch (Exception e) {
					DriverStation.reportError("[!] SUBSYSTEM '" + subsystemName.toUpperCase()
							+ "' COMPRESSOR INIT ERROR: " + e.getMessage(), false);
					System.out.println("[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' COMPRESSOR INIT ERROR: "
							+ e.getMessage());
					System.exit(1);
				} finally {
					json.clear();
				}
			});
			compressorArray.clear();
		} catch (Exception e) {
			DriverStation.reportError(
					"[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' COMPRESSOR INIT ERROR: " + e.getMessage(),
					false);
			System.out.println(
					"[!] SUBSYSTEM '" + subsystemName.toUpperCase() + "' COMPRESSOR INIT ERROR: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Gets specified {@link Compressor}.
	 * 
	 * @return {@link Compressor}
	 * @param compressorName
	 */
	default Compressor getCompressor(String compressorName) {
		return compressors.get(compressorName);
	}
}