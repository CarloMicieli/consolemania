package com.example.consolemania.games.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * It converts a string to a "slug".
 *
 * @see <a href="http://www.codecodex.com/wiki/Generate_a_url_slug#Java">Original implementation</a>
 */
public record Slug(String value) {

    public Slug(String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException();
        }

        this.value = toSeoFriendlyString(value);
    }

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final String SEP = "-";

    private static String toSeoFriendlyString(String str) {
        String noWhitespace = WHITESPACE.matcher(str).replaceAll(SEP);
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        return NON_LATIN.matcher(normalized).replaceAll("").toLowerCase(Locale.ENGLISH);
    }
}
