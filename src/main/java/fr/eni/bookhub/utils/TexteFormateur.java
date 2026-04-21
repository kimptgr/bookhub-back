package fr.eni.bookhub.utils;

import org.springframework.stereotype.Component;

@Component
public class TexteFormateur {
    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.trim().toCharArray()) {
            if (Character.isSpaceChar(ch) || ch == '-') {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}
