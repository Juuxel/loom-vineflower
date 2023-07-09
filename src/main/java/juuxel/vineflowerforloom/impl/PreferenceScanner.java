package juuxel.vineflowerforloom.impl;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import org.gradle.api.Project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scans QF preferences declared in project properties. Because this is run at plugin apply time,
 * it basically only looks at gradle.properties files. The keys are in the format
 * {@code [vineflower|loom-quiltflower].preference.<preference name>}.
 */
public final class PreferenceScanner {
    // TODO: Report deprecations
    private static final Pattern PREFERENCE_PATTERN = Pattern.compile("^(?:loom-quiltflower|vineflower)\\.preference\\.([a-z]{3})$");

    public static void scan(Project project, VineflowerExtension extension) {
        project.getProperties().forEach((key, value) -> {
            Matcher matcher = PREFERENCE_PATTERN.matcher(key);
            if (matcher.matches()) {
                extension.getPreferences().set(matcher.group(1), value);
            }
        });
    }
}
