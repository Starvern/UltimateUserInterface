package self.starvern.ultimateuserinterface.managers;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatManager
{
    /**
     * Formats color to each string in the list
     * @param list The list to colorize
     * @return The colorized list
     * @since 0.1.0
     */
    public static List<String> colorize(List<String> list)
    {
        List<String> newList = new ArrayList<>();

        for (String line : list)
        {
            newList.add(colorize(line));
        }

        return newList;
    }

    /**
     * Formats color to the string
     * @param string the string to colorize
     * @return the colorized string
     * @since 0.1.0
     */
    public static String colorize(String string)
    {
        StringBuffer hexString = parseDouble(parseTriple(parseSingle(new StringBuffer(string))));

        return ChatColor.translateAlternateColorCodes('&', hexString.toString());
    }

    /**
     * Checks the regex of standard hex colors {#fff}
     * @param string the string buffer to check
     * @return the string with single colors parsed
     * @since 0.1.0
     */
    private static StringBuffer parseSingle(StringBuffer string)
    {
        String singleRegex = "[{]#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})[}]";
        Pattern singlePattern = Pattern.compile(singleRegex);
        Matcher singleMatcher = singlePattern.matcher(string);

        StringBuffer newString = new StringBuffer();

        while (singleMatcher.find())
        {
            String preColor = (singleMatcher.group(1).length() == 3) ? String.valueOf(singleMatcher.group(1).charAt(0))
                    + singleMatcher.group(1).charAt(0) + singleMatcher.group(1).charAt(1)
                    + singleMatcher.group(1).charAt(1) + singleMatcher.group(1).charAt(2)
                    + singleMatcher.group(1).charAt(2) : singleMatcher.group(1);

            singleMatcher.appendReplacement(newString, ChatColor.of(Color.decode("#" + preColor)).toString());
        }

        singleMatcher.appendTail(newString);

        return newString;
    }

    /**
     * Checks the regex of triple hex colors {#fff>} {#fff<>} {#fff<}
     * @param string the string buffer to check
     * @return the string with triple colors parsed
     * @since 0.1.0
     */
    private static StringBuffer parseTriple(StringBuffer string)
    {
        String tripleRegex = "[{]#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})[>][}](.*)[{]#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})[<][>][}](.*)[{]#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})[<][}]";
        Pattern triplePattern = Pattern.compile(tripleRegex);
        Matcher tripleMatcher = triplePattern.matcher(string);

        StringBuffer newString = new StringBuffer();

        while (tripleMatcher.find())
        {
            String preColor1 = (tripleMatcher.group(1).length() == 3) ? String.valueOf(tripleMatcher.group(1).charAt(0))
                    + tripleMatcher.group(1).charAt(0) + tripleMatcher.group(1).charAt(1)
                    + tripleMatcher.group(1).charAt(1) + tripleMatcher.group(1).charAt(2)
                    + tripleMatcher.group(1).charAt(2) : tripleMatcher.group(1);
            Color color1 = Color.decode("#" + preColor1);

            String preColor2 = (tripleMatcher.group(3).length() == 3) ? String.valueOf(tripleMatcher.group(3).charAt(0))
                    + tripleMatcher.group(3).charAt(0) + tripleMatcher.group(3).charAt(1)
                    + tripleMatcher.group(3).charAt(1) + tripleMatcher.group(3).charAt(2)
                    + tripleMatcher.group(3).charAt(2) : tripleMatcher.group(3);
            Color color2 = Color.decode("#" + preColor2);

            String preColor3 = (tripleMatcher.group(5).length() == 3) ? String.valueOf(tripleMatcher.group(5).charAt(0))
                    + tripleMatcher.group(5).charAt(0) + tripleMatcher.group(5).charAt(1)
                    + tripleMatcher.group(5).charAt(1) + tripleMatcher.group(5).charAt(2)
                    + tripleMatcher.group(5).charAt(2) : tripleMatcher.group(5);
            Color color3 = Color.decode("#" + preColor3);

            String unColored1 = tripleMatcher.group(2);
            String unColored2 = tripleMatcher.group(4);

            StringBuilder finalString = new StringBuilder();

            int steps = unColored1.length() - 1;

            for (int i = 0; i <= steps; i++) {
                float ratio = (float) i / (float) steps;
                int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
                int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
                int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
                Color stepColor = new Color(red, green, blue);
                finalString.append(ChatColor.of(stepColor)).append(unColored1.charAt(i));
            }

            steps = unColored2.length() -1;

            for (int i = 0; i <= steps; i++) {
                float ratio = (float) i / (float) steps;
                int red = (int) (color3.getRed() * ratio + color2.getRed() * (1 - ratio));
                int green = (int) (color3.getGreen() * ratio + color2.getGreen() * (1 - ratio));
                int blue = (int) (color3.getBlue() * ratio + color2.getBlue() * (1 - ratio));
                Color stepColor = new Color(red, green, blue);
                finalString.append(ChatColor.of(stepColor)).append(unColored2.charAt(i));
            }

            tripleMatcher.appendReplacement(newString, finalString.toString());
        }
        tripleMatcher.appendTail(newString);

        return newString;
    }

    /**
     * Checks the regex of double hex colors {#fff>} {#fff<}
     * @param string the string buffer to check
     * @return the string with double colors parsed
     * @since 0.1.0
     */
    private static StringBuffer parseDouble(StringBuffer string)
    {
        String doubleRegex = "[{]#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})[>][}](.*)[{]#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})[<][}]";
        Pattern doublePattern = Pattern.compile(doubleRegex);
        Matcher doubleMatcher = doublePattern.matcher(string);

        StringBuffer newString = new StringBuffer();
        while (doubleMatcher.find())
        {
            String preColor1 = (doubleMatcher.group(1).length() == 3) ?
                    (doubleMatcher.group(1).charAt(0)
                            + String.valueOf(doubleMatcher.group(1).charAt(0)) + doubleMatcher.group(1).charAt(1)
                            + doubleMatcher.group(1).charAt(1) + doubleMatcher.group(1).charAt(2)
                            + doubleMatcher.group(1).charAt(2)) : doubleMatcher.group(1);

            Color color1 = Color.decode("#" + preColor1);

            String preColor2 = (doubleMatcher.group(3).length() == 3) ?
                    String.valueOf((doubleMatcher.group(3).charAt(0))
                            + doubleMatcher.group(3).charAt(0) + doubleMatcher.group(3).charAt(1))
                            + doubleMatcher.group(3).charAt(1) + doubleMatcher.group(3).charAt(2)
                            + doubleMatcher.group(3).charAt(2) : doubleMatcher.group(3);

            Color color2 = Color.decode("#" + preColor2);

            String unColored = doubleMatcher.group(2);
            StringBuilder finalString = new StringBuilder();

            int steps = unColored.length() - 1;

            for (int i = 0; i <= steps; i++) {
                float ratio = (float) i / (float) steps;
                int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
                int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
                int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
                Color stepColor = new Color(red, green, blue);
                finalString.append(ChatColor.of(stepColor)).append(unColored.charAt(i));
            }
            doubleMatcher.appendReplacement(newString, finalString.toString());
        }
        doubleMatcher.appendTail(newString);

        return newString;
    }
}