package nu.wasis.stunden.plugins.textfile.config;

public class StundenTextfilePluginConfig {

    private String readFrom;
    private int nightLimit;
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
