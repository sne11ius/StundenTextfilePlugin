package nu.wasis.stunden.plugins.textfile.config;

public class StundenTextfilePluginConfig {

    private String readFrom;
    private boolean stripProjectNamesOnEqualitySign;

    public String getReadFrom() {
        return readFrom;
    }

	public boolean getStripProjectNamesOnEqualitySign() {
		return stripProjectNamesOnEqualitySign;
	}

}
