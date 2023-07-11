package juuxel.vineflowerforloom.impl;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import org.gradle.api.Project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scans decompiler preferences declared in project properties. Because this is run at plugin apply time,
 * it basically only looks at gradle.properties files. The keys are in the format
 * {@code [vineflower|loom-quiltflower].preference.<preference name>}.
 */
public final class PreferenceScanner {
    private static final Pattern PREFERENCE_PATTERN = Pattern.compile("^vineflower\\.preference\\.([a-z]{3})$");
    private static final Pattern OLD_PREFERENCE_PATTERN = Pattern.compile("^loom-quiltflower\\.preference\\.([a-z]{3})$");

    public static void scan(Project project, VineflowerExtension extension) {
        project.getProperties().forEach((key, value) -> {
            // Check deprecated properties first to let the new properties override them.
            Matcher matcher = OLD_PREFERENCE_PATTERN.matcher(key);
            if (matcher.matches()) {
                DeprecationReporter.get(project).reportRename(key, "vineflower.preference." + matcher.group(1));
                extension.getPreferences().set(matcher.group(1), value);
            }

            matcher = PREFERENCE_PATTERN.matcher(key);
            if (matcher.matches()) {
                extension.getPreferences().set(matcher.group(1), value);
            }
        });
    }
}
