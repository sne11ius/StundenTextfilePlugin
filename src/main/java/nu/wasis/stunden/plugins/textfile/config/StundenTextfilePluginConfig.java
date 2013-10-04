package nu.wasis.stunden.plugins.textfile.config;

import nu.wasis.stunden.plugins.textfile.StundenTextfilePlugin;

/**
 * Configuration class for {@link StundenTextfilePlugin}.
 */
public class StundenTextfilePluginConfig {

	/**
	 * File or directory to read from.
	 */
    private String readFrom;
    
    /**
     * Number of hours of a day that should belong to the preceding day. If e.g.
     * you happen to work from `23:00 - 02:00`, this plugin might kinda freak
     * out and compute negative durations. So if you have such cases, make sure
     * (nightLimit >= end work time).
     */
    private int nightLimit;
    
    /**
     * Whether or not `=' should be treated as a line comment indicator. If
     * <code>true</code>, a project name like `Project A = XYZ' will become
     * `Project A'.
     */
    private boolean stripProjectNamesOnEqualitySign;

    public String getReadFrom() {
        return readFrom;
    }
    
    public int getNightLimit() {
    	return nightLimit;
    }

	public boolean getStripProjectNamesOnEqualitySign() {
		return stripProjectNamesOnEqualitySign;
	}

}
