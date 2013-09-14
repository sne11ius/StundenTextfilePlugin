package nu.wasis.stunden.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import nu.wasis.stunden.exception.InvalidArgumentsException;
import nu.wasis.stunden.model.Day;
import nu.wasis.stunden.model.Entry;
import nu.wasis.stunden.model.Project;
import nu.wasis.stunden.model.WorkPeriod;
import nu.wasis.stunden.plugin.InputPlugin;
import nu.wasis.stunden.plugins.exception.EmptyFileException;
import nu.wasis.stunden.plugins.exception.InvalidEntryException;
import nu.wasis.stunden.plugins.exception.InvalidFilenameException;
import nu.wasis.stunden.plugins.textfile.config.StundenTextfilePluginConfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

@PluginImplementation
public class StundenTextfilePlugin implements InputPlugin {

    private static final Logger LOG = Logger.getLogger(StundenTextfilePlugin.class);

    @Override
    public WorkPeriod read(final Object config) {
        if (null == config) {
            throw new InvalidArgumentsException("Param `config' must not be null.");
        }
        final StundenTextfilePluginConfig myConfig = (StundenTextfilePluginConfig) config;
        final String readFrom = myConfig.getReadFrom();
        if (null == readFrom) {
            throw new InvalidArgumentsException("Config must contain `readFrom' param.");
        }
        final File inputFile = new File(readFrom);
        if (!inputFile.exists()) {
            throw new InvalidArgumentsException("Arg `readFrom' must be an existing file or directory.");
        }
        final boolean stripProjectNamesOnEqualitySign = myConfig.getStripProjectNamesOnEqualitySign();

        LOG.info("Parsing `" + readFrom + "' ...");

        WorkPeriod workPeriod = null;

        try {
            if (inputFile.isDirectory()) {
                workPeriod = readDirectory(inputFile, stripProjectNamesOnEqualitySign);
            } else {
                workPeriod = readSingleFile(inputFile, stripProjectNamesOnEqualitySign);
            }
        } catch (final IOException e) {
            throw new RuntimeException("Something went horribly wrong.", e);
        }
        LOG.info("... done.");
        return workPeriod;
    }

    private WorkPeriod readDirectory(final File directory, final boolean stripProjectNamesOnEqualitySign) throws FileNotFoundException, IOException {
        WorkPeriod workPeriod = null;
        for (final File file : FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)) {
            if (null == workPeriod) {
                workPeriod = readSingleFile(file, stripProjectNamesOnEqualitySign);
            } else {
                workPeriod.addAll(readSingleFile(file, stripProjectNamesOnEqualitySign));
            }
        }
        return workPeriod;
    }

    private WorkPeriod readSingleFile(final File file, final boolean stripProjectNamesOnEqualitySign) throws FileNotFoundException, IOException {
        // LOG.debug("Parsing file " + file.getName());
        final DateTime date = getDate(file);
        final WorkPeriod workPeriod = new WorkPeriod();
        final Day day = parseContent(date, file, stripProjectNamesOnEqualitySign);
        workPeriod.getDays().add(day);
        return workPeriod;
    }

    private DateTime getDate(final File file) {
        if (!file.isFile()) {
            throw new RuntimeException("Can only get dates from files :D - but I smile.");
        }
        final Parser dateParser = new Parser();
        final List<DateGroup> dateGroups = dateParser.parse(FilenameUtils.removeExtension(file.getName()));
        if (dateGroups.size() != 1) {
            throw new InvalidFilenameException("Filename " + file.getName() + " contains no or ambigous date(s).");
        }
        final DateGroup dateGroup = dateGroups.get(0);
        if (dateGroup.getDates().size() != 1) {
            throw new InvalidFilenameException("Filename " + file.getName() + " contains no or ambigous date(s).");
        }
        return new DateTime(dateGroup.getDates().get(0));
    }

    private Day parseContent(final DateTime date, final File file, final boolean stripProjectNamesOnEqualitySign) throws FileNotFoundException, IOException {
        @SuppressWarnings("resource")
		UnicodeBOMInputStream unicodeBOMInputStream = new UnicodeBOMInputStream(new FileInputStream(file));
		final List<String> lines = IOUtils.readLines(new InputStreamReader(unicodeBOMInputStream.skipBOM(), "UTF8"));
        if (lines.isEmpty()) {
            throw new EmptyFileException("File `" + file + "' is empty. Empty files are not cool.");
        }
        final List<Entry> entries = new LinkedList<>();

        for (final String line : lines) {
        	if (line.startsWith("#") || line.isEmpty()) {
        		continue;
        	}
            if (line.length() < 15) {
                // compare:
                // 0123456789012345
                // 10:00 - 10:45: Intern
                throw new InvalidEntryException("Magic tells me that the line `" + line + "' in file `" + file + "' is not cool.");
            }
            try {
                final String beginHourString = StringUtils.stripStart(line.substring(0, 2), "0");
				int beginHour = 0;
				try {
					beginHour = Integer.parseInt(beginHourString);
				} catch (final NumberFormatException e) {
	                throw new InvalidEntryException("Unable to parse line `" + line + "' of file `" + file + "'. => invalid starting hour of `" + beginHourString + "'.", e);
	            } 
                int beginMinutes = 0;
                final String beginMinutesString = line.substring(3, 5);
                if (!"00".equals(beginMinutesString)) {
                	beginMinutes = Integer.parseInt(beginMinutesString);
                }
                final int endHour = Integer.parseInt(StringUtils.stripStart(line.substring(8, 10), "0"));
                int endMinutes = 0;
                final String endMinutesString = line.substring(11, 13);
				if (!"00".equals(endMinutesString)) {
					endMinutes = Integer.parseInt(endMinutesString);
				}
				String projectName = null;
				int stopAt = -1;
				if (stripProjectNamesOnEqualitySign) {
					stopAt = line.indexOf("=");
				}
				if (-1 != stopAt) {
					projectName = line.substring(15, stopAt).trim();
				} else {
					projectName = line.substring(15).trim();
				}
				
                final MutableDateTime begin = new MutableDateTime(date);
                begin.setHourOfDay(beginHour);
                begin.setMinuteOfHour(beginMinutes);
                final MutableDateTime end = new MutableDateTime(date);
                end.setHourOfDay(endHour);
                end.setMinuteOfHour(endMinutes);
                entries.add(new Entry(begin.toDateTime(), end.toDateTime(), new Project(projectName)));
            } catch (final NumberFormatException e) {
                throw new InvalidEntryException("Unable to parse line `" + line + "' of file `" + file + "'.", e);
            }
        }

        return new Day(date, entries);
    }

    @Override
    public Class<StundenTextfilePluginConfig> getConfigurationClass() {
        return StundenTextfilePluginConfig.class;
    }
}
