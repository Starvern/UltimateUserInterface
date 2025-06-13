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
     * @param list the list to colorize
     * @return the colorized list
     * @since 0.1.0
     */
    public static List<String> colorize(List<String> list)
    {
        if (list == null) return null;

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
        if (string == null) return null;
        StringBuffer hexString = parseSingle(parseDouble(parseTriple(new StringBuffer(string))));

        return ChatColor.translateAlternateColorCodes('&', hexString.toString());
    }

    /**
     * @param matcher The matcher to use.
     * @param redName The group name of the designated red color in the RegEx.
     * @param greenName The group name of the designated green color in the RegEx.
     * @param blueName The group name of the designated blue color in the RegEx.
     * @return A color based on the matcher constructed from a string + RegEx.
     * @since 0.2.5
     */
    private static Color extractColor(Matcher matcher, String redName, String greenName, String blueName)
    {
        String red = matcher.group(redName);
        String green = matcher.group(greenName);
        String blue = matcher.group(blueName);

        red += (red.length() == 1) ? red : "";
        green += (green.length() == 1) ? green : "";
        blue += (blue.length() == 1) ? blue : "";

        return new Color(
                Integer.parseInt(red, 16),
                Integer.parseInt(green, 16),
                Integer.parseInt(blue, 16));
    }

    /**
     * Builds a color gradient from 2 colors and applies it to the text.
     * @param color1 The first color (left).
     * @param color2 The second color (right).
     * @param text The text to colorize.
     * @return The colorized string.
     * @since 0.2.5
     */
    private static String constructGradient(Color color1, Color color2, String text)
    {
        int steps = text.length() - 1;

        StringBuilder newString = new StringBuilder();

        if (steps == 0)
        {
            return ChatColor.of(color1) + text;
        }

        for (int i = 0; i <= steps; i++)
        {
            float ratio = (float) i / (float) steps;
            int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
            int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
            int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
            Color stepColor = new Color(red, green, blue);
            newString.append(ChatColor.of(stepColor)).append(text.charAt(i));
        }

        return newString.toString();
    }

    /**
     * Checks the regex of standard hex colors {#fff}
     * @param string the string buffer to check
     * @return the string with single colors parsed
     * @since 0.1.0
     */
    private static StringBuffer parseSingle(StringBuffer string)
    {
        String regex = "[{][#](?<red>[A-Fa-f0-9]{1,2})(?<green>[A-Fa-f0-9]{1,2})(?<blue>[A-Fa-f0-9]|[A-Fa-f0-9]{1,2})(<|>){0,2}[}](?<text>.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        StringBuffer newString = new StringBuffer();

        while (matcher.find())
        {
            Color color = extractColor(matcher, "red", "green", "blue");

            matcher.appendReplacement(newString, ChatColor.of(color) + matcher.group("text"));
        }

        if (newString.isEmpty())
            return string;

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
        String regex = "[{]#(?<red1>[A-Fa-f0-9]{1,2})(?<green1>[A-Fa-f0-9]{1,2})(?<blue1>[A-Fa-f0-9]|[A-Fa-f0-9]{1,2})(<|>){1,2}[}](?<text1>.*)[{][#](?<red2>[A-Fa-f0-9]{1,2})(?<green2>[A-Fa-f0-9]{1,2})(?<blue2>[A-Fa-f0-9]|[A-Fa-f0-9]{1,2})(<|>){1,2}[}](?<text2>.*)[{][#](?<red3>[A-Fa-f0-9]{1,2})(?<green3>[A-Fa-f0-9]{1,2})(?<blue3>[A-Fa-f0-9]|[A-Fa-f0-9]{1,2})(>|<){1,2}[}]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        StringBuffer newString = new StringBuffer();

        while (matcher.find())
        {
            Color color1 = extractColor(matcher, "red1", "green1", "blue1");
            Color color2 = extractColor(matcher, "red2", "green2", "blue2");
            Color color3 = extractColor(matcher, "red3", "green3", "blue3");

            String text1 = matcher.group("text1");
            String text2 = matcher.group("text2");

            String finalString = constructGradient(color1, color2, text1) +
                    constructGradient(color2, color3, text2);

            matcher.appendReplacement(newString, finalString);
        }

        if (newString.isEmpty())
            return string;

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
        String regex = "[{][#](?<red1>[A-Fa-f0-9]{1,2})(?<green1>[A-Fa-f0-9]{1,2})(?<blue1>[A-Fa-f0-9]|[A-Fa-f0-9]{1,2})[>|<][}](?<text>.*)[{][#](?<red2>[A-Fa-f0-9]{1,2})(?<green2>[A-Fa-f0-9]{1,2})(?<blue2>[A-Fa-f0-9]|[A-Fa-f0-9]{1,2})[>|<][}]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        StringBuffer newString = new StringBuffer();

        while (matcher.find())
        {
            Color color1 = extractColor(matcher, "red1", "green1", "blue1");
            Color color2 = extractColor(matcher, "red2", "green2", "blue2");

            String text = matcher.group("text");

            matcher.appendReplacement(newString, constructGradient(color1, color2, text));
        }

        if (newString.isEmpty())
            return string;

        return newString;
    }
}