package com.hitao.codegen.util;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * <b>String Utility library</b><br>
 * Utility methods to perform various string operations. At this point all public methods are static
 * and final.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: StringUtils.java 44 2011-03-08 08:55:34Z guest $
 */
public final class StringUtils {
  /** The empty string, zero length. */
  public static final String EMPTY = "";
  /** The space string, zero length. */
  public static final String SPACE = " ";
  /** The space char, zero length. */
  public static final char SPACE_CHAR = ' ';
  /** The platform-appropriate line separator character. */
  public static final String NEW_LINE = System.getProperty("line.separator", "\n");
  /** The package separator */
  public static final char PACKAGE_SEPARATOR_CHAR = '.';
  /** The inner class separator */
  public static final char INNER_CLASS_SEPARATOR_CHAR = '$';


  private static ErrorHandler loggingErrorHandler_ = null;

  /**
   * Returns a string consisting of each supplied argument separated by a platform-appropriate
   * newline character.
   *
   * @param argLines the lines of text to be separated by newline characters
   * @return a string consisting of each element in <code>argLines</code> separated by newline
   * characters
   */
  public static final String toLines(String... argLines) {
    return toLines(false, argLines);
  }

  /**
   * Returns a string consisting of each supplied argument separated by a platform-appropriate
   * newline character.
   *
   * @param argNewLineAtEnd <code>true</code> if the return value should include a newline character
   * at the end; <code>false</code> otherwise
   * @param argLines the lines of text to be separated by newline characters
   * @return a string consisting of each element in <code>argLines</code> separated by newline
   * characters
   */
  public static final String toLines(boolean argNewLineAtEnd, String... argLines) {
    StringBuffer result = new StringBuffer();

    if (argLines != null) {
      for (int i = 0, n = argLines.length; i < n; i++) {
        result.append(argLines[i]);
        if (argNewLineAtEnd || (i < (n - 1))) {
          result.append(NEW_LINE);
        }
      }
    }
    
    return result.toString();
  }

  /**
   * Returns the specified string with a newline character appended to it.
   *
   * @param argText the text to which a newline character is to be appended
   * @return <code>argText</code> concatenated with a newline character
   */
  public static final String appendLine(String argText) {
    return appendLine(argText, "");
  }

  /**
   * Returns a string assembled by appending a newline character and, subsequently, a string to an
   * existing string.
   *
   * @param argText the text to which a newline character and <code>argNewLine</code> are to be
   * appended
   * @param argNewLine the text to be appended to <code>argText</code>
   * @return <code>argText</code> concatenated with a newline character followed by
   * <code>argNewLine</code>
   */
  public static final String appendLine(String argText, String argNewLine) {
    return argText + NEW_LINE + argNewLine;
  }

  /**
   * Returns the specified string with a newline character appended to it.
   *
   * @param argText the text to which a newline character is to be appended
   * @return <code>argText</code> concatenated with a newline character
   */
  public static final StringBuilder appendLine(StringBuilder argText) {
    return appendLine(argText, "");
  }

  /**
   * Returns a string assembled by appending a newline character and, subsequently, a string to an
   * existing string.
   *
   * @param argText the text to which a newline character and <code>argNewLine</code> are to be
   * appended
   * @param argNewLine the text to be appended to <code>argText</code>
   * @return <code>argText</code> concatenated with a newline character followed by
   * <code>argNewLine</code>
   */
  public static final StringBuilder appendLine(StringBuilder argText, String argNewLine) {
    return argText.append(NEW_LINE).append(argNewLine);
  }

  /**
   * Pads a string with spaces on the beginning and end to center it to the specified length.
   *
   * @param argTarget the string to center
   * @param argLength the minimum length of returned string
   * @return a padded string, or <tt>argTarget</tt> if is longer than <tt>argLength</tt>
   * @author Doug Berkland
   * @created February 21, 2003
   */
  public static final String center(String argTarget, int argLength) {
    return center(argTarget, argLength, " ");
  }

  /**
   * Pads a string on the beginning and end to center it to the specified length.
   *
   * @param argTarget the string to center
   * @param argLength the minimum length of returned string
   * @param argPadding the padding to place around the string
   * @return a padded string, <tt>argTarget</tt> if is longer than <tt>argLength</tt> , or null
   * if <tt>argTarget</tt> is <tt>null</tt>
   * @throws IllegalArgumentException if <tt>argPadding.length() == 0</tt>
   * @throws NullPointerException if <tt>argPadding</tt> is null
   * @author Doug Berkland
   * @created February 21, 2003
   */
  public static final String center(String argTarget, int argLength, String argPadding) {
    if (argTarget == null) {
      return argTarget;
    }
    if (argPadding.length() == 0) {
      throw new IllegalArgumentException();
    }
    StringBuilder out = new StringBuilder(argLength);
    out.append(argTarget);
    // loop adding alternating the contents of argPadding
    // to the beginning and end until we fill the line
    if (out.length() < argLength) {
      int padIndex = 0;
      for (;;) {
        // add to beginning
        out.insert(0, argPadding.charAt(padIndex));
        // check length
        if (out.length() >= argLength) {
          break;
        }
        // add to end
        out.append(argPadding.charAt(padIndex));
        //check length
        if (out.length() >= argLength) {
          break;
        }
        //next pad character, wrapping if we are on the last
        padIndex++ ;
        if (padIndex > argPadding.length() - 1) {
          padIndex = 0;
        }
      }
    }
    return out.toString();
  }

  /**
   * Returns the combination of the specified text elements. Specifically, the second string is
   * inserted into, or replaces characters in, the first string at the specified offset.
   *
   * @param primary String the text in which <code>secondary</code> is to be inserted or updated
   * @param secondary String the text to replace/insert into <code>primary</code>
   * @param offset int the index at which characters from <code>secondary</code> are to be
   * inserted/updated in <code>primary</code>
   * @param insert boolean <code>true</code> if <code>secondary</code> is to be inserted into
   * <code>primary</code>; <code>false</code> if <code>secondary</code> is to overwrite
   * <code>primary</code>
   * @return String <code>primary</code> modified to reflect the insertion of/replacement by
   * <code>secondary</code>
   */
  public static final String combine(String primary, String secondary, int offset, boolean insert) {
    return combine(new StringBuffer(primary), secondary, offset, insert);
  }

  /**
   * Returns the combination of the specified text elements. Specifically, the second string is
   * inserted into, or replaces characters in, the first string at the specified offset.
   *
   * @param primary StringBuffer the text in which <code>secondary</code> is to be inserted or
   * updated
   * @param secondary String the text to replace/insert into <code>primary</code>
   * @param offset int the index at which characters from <code>secondary</code> are to be
   * inserted/updated in <code>primary</code>
   * @param insert boolean <code>true</code> if <code>secondary</code> is to be inserted into
   * <code>primary</code>; <code>false</code> if <code>secondary</code> is to overwrite
   * <code>primary</code>
   * @return String <code>primary</code> modified to reflect the insertion of/replacement by
   * <code>secondary</code>
   */
  public static final String combine(StringBuffer primary, String secondary, int offset, boolean insert) {
    if (isEmpty(secondary)) {
      return (primary != null) ? primary.toString() : null;
    }
    if (primary == null) {
      primary = new StringBuffer();
    }

    if (offset >= primary.length()) {
      return primary.append(secondary).toString();
    }
    else if (offset < 0) {
      offset = 0;
    }
    return (insert) ? primary.insert(offset, secondary).toString() : primary.replace(offset,
        offset + secondary.length(), secondary).toString();
  }

  /**
   * Returns the combination of the specified text elements. Specifically, the second string is
   * inserted into, or replaces characters in, the first string at the specified offset.
   *
   * @param primary StringBuffer the text in which <code>secondary</code> is to be inserted or
   * updated
   * @param secondary StringBuffer the text to replace/insert into <code>primary</code>
   * @param offset int the index at which characters from <code>secondary</code> are to be
   * inserted/updated in <code>primary</code>
   * @param insert boolean <code>true</code> if <code>secondary</code> is to be inserted into
   * <code>primary</code>; <code>false</code> if <code>secondary</code> is to overwrite
   * <code>primary</code>
   * @return String <code>primary</code> modified to reflect the insertion of/replacement by
   * <code>secondary</code>
   */
  public static final String combine(StringBuffer primary, StringBuffer secondary, int offset, boolean insert) {
    return combine(primary, nonNull(secondary), offset, insert);
  }

  /**
   * Returns the combination of the specified text elements. Specifically, the second string is
   * inserted into, or replaces characters in, the first string at the specified offset.
   *
   * @param primary StringBuilder the text in which <code>secondary</code> is to be inserted or
   * updated
   * @param secondary String the text to replace/insert into <code>primary</code>
   * @param offset int the index at which characters from <code>secondary</code> are to be
   * inserted/updated in <code>primary</code>
   * @param insert boolean <code>true</code> if <code>secondary</code> is to be inserted into
   * <code>primary</code>; <code>false</code> if <code>secondary</code> is to overwrite
   * <code>primary</code>
   * @return String <code>primary</code> modified to reflect the insertion of/replacement by
   * <code>secondary</code>
   */
  public static final String combine(StringBuilder primary, String secondary, int offset, boolean insert) {
    if (isEmpty(secondary)) {
      return (primary != null) ? primary.toString() : null;
    }
    if (primary == null) {
      primary = new StringBuilder();
    }

    if (offset >= primary.length()) {
      return primary.append(secondary).toString();
    }
    else if (offset < 0) {
      offset = 0;
    }
    return (insert) ? primary.insert(offset, secondary).toString() : primary.replace(offset,
        offset + secondary.length(), secondary).toString();
  }

  /**
   * Returns the combination of the specified text elements. Specifically, the second string is
   * inserted into, or replaces characters in, the first string at the specified offset.
   *
   * @param primary StringBuilder the text in which <code>secondary</code> is to be inserted or
   * updated
   * @param secondary StringBuilder the text to replace/insert into <code>primary</code>
   * @param offset int the index at which characters from <code>secondary</code> are to be
   * inserted/updated in <code>primary</code>
   * @param insert boolean <code>true</code> if <code>secondary</code> is to be inserted into
   * <code>primary</code>; <code>false</code> if <code>secondary</code> is to overwrite
   * <code>primary</code>
   * @return String <code>primary</code> modified to reflect the insertion of/replacement by
   * <code>secondary</code>
   */
  public static final String combine(StringBuilder primary, StringBuilder secondary, int offset,
      boolean insert) {

    return combine(primary, nonNull(secondary), offset, insert);
  }

  /**
   * Efficiently compare two strings, ignoring case and any embedded whitespace.
   *
   * @param s1 the first string
   * @param s2 the second string
   * @return a negative integer, zero, or a positive integer as the first String is greater than,
   * equal to, or less than the second String, ignoring case considerations and whitespace.
   */
  @SuppressWarnings("unused")
public static int compareIgnoreCaseIgnoreWhiteSpace(String s1, String s2) {
    int len1 = s1.length();
    int len2 = s2.length();
    int nonWhiteCount1 = s1.length();
    int nonWhiteCount2 = s2.length();
    int idx1 = 0;
    int idx2 = 0;
    for (; (idx1 < len1) && (idx2 < len2); idx1++ , idx2++ ) {
      char c1 = s1.charAt(idx1);
      while (Character.isWhitespace(c1)) {
        nonWhiteCount1-- ;
        if (idx1 < len1 - 1) {
          c1 = s1.charAt( ++idx1);
        }
        else {
          // only match if all the rest of s2 is whitespace
          for (; idx2 < len2;) {
            if (!Character.isWhitespace(s2.charAt(idx2++ ))) {
              return -1;
            }
          }
          return 0;
        }
      }
      char c2 = s2.charAt(idx2);
      while (Character.isWhitespace(c2)) {
        nonWhiteCount2-- ;
        if (idx2 < len2 - 1) {
          c2 = s2.charAt( ++idx2);
        }
        else {
          // only match if all the rest of s1 is whitespace
          for (; idx1 < len1;) {
            if (!Character.isWhitespace(s1.charAt(idx1++ ))) {
              return 1;
            }
          }
          return 0;
        }
      }
      if (c1 != c2) {
        c1 = Character.toUpperCase(c1);
        c2 = Character.toUpperCase(c2);
        if (c1 != c2) {
          c1 = Character.toLowerCase(c1);
          c2 = Character.toLowerCase(c2);
          if (c1 != c2) {
            return c1 - c2;
          }
        }
      }
    }
    // we've reached the end of one or both strings; if either string has non-whitespace remaining, it's no match
    for (; idx1 < len1;) {
      if (!Character.isWhitespace(s1.charAt(idx1++ ))) {
        return 1;
      }
    }
    for (; idx2 < len2;) {
      if (!Character.isWhitespace(s2.charAt(idx2++ ))) {
        return -1;
      }
    }
    // both strings have only whitespace remaining, so we have a match
    return 0;
  }

  /**
   * Ensures that the first character is lower case
   *
   * @param argString string - if null or empty is given, null or empty will be returned,
   * respectively.
   *
   * @return given: Joe, return: joe
   */
  public static String ensureFirstLowerCase(String argString) {
    if (isEmpty(argString)) {
      return argString;
    }
    return Character.toLowerCase(argString.charAt(0)) + argString.substring(1);
  }

  /**
   * Ensures that the first character is upper case
   *
   * @param argString string - if null or empty is given, null or empty will be returned,
   * respectively.
   *
   * @return given: joe, return: Joe
   */
  public static String ensureFirstUpperCase(String argString) {
    if (isEmpty(argString)) {
      return argString;
    }
    return Character.toUpperCase(argString.charAt(0)) + argString.substring(1);
  }

  /**
   * Returns whether the specified strings are equivalent to each other, making accommodations for
   * <code>null</code> values and ignoring differences in case.
   *
   * @param argObj1 the string to compare with <code>argObj2</code>
   * @param argObj2 the string to compare with <code>argObj1</code>
   * @return <code>true</code> if both <code>argObj1</code> and <code>argObj2</code> are
   * <code>null</code> or differ from each other only by case; <code>false</code> otherwise
   */
  public static final boolean equivalentIgnoreCase(String argObj1, String argObj2) {
    boolean equivalent = false;

    if ((argObj1 != null) && (argObj2 != null) && (argObj1.equalsIgnoreCase(argObj2))) {
      equivalent = true;
    }
    else {
      equivalent = ((argObj1 == null) && (argObj2 == null));
    }
    return equivalent;
  }

  /**
   * Returns whether the specified strings are equivalent to each other, making accommodations for
   * <code>null</code> values.
   *
   * @param argObj1 the string to compare with <code>argObj2</code>
   * @param argObj2 the string to compare with <code>argObj1</code>
   * @return <code>true</code> if both <code>argObj1</code> and <code>argObj2</code> are
   * <code>null</code> or differ from each other only by case; <code>false</code> otherwise
   */
  public static boolean equals(String argObj1, String argObj2) {
      if (argObj1 == null) {
          return argObj2 == null;
      }

      return argObj1.equals(argObj2);
  }
  
  /**
   * Returns a new string consisting of the <tt>argFillChar</tt> character repeated
   * <tt>argFillCount</tt> times.
   *
   * @param argFillChar the character with which to repeatedly populate the new string
   * @param argFillCount the number of times to repeat <tt>argFillString</tt> in the newly-created
   * string
   * @return the new repeating string
   */
  public static final String fill(char argFillChar, int argFillCount) {
    return fill(Character.toString(argFillChar), argFillCount);
  }

  /**
   * Returns a new string consisting of the <tt>argFillString</tt> text repeated
   * <tt>argFillCount</tt> times. Note that <tt>argFillString</tt> can be one or more characters
   * long.
   *
   * @param argFillString the text with which to repeatedly populate the new string
   * @param argFillCount the number of times to repeat <tt>argFillString</tt> in the newly-created
   * string
   * @return the new repeating string
   */
  public static final String fill(String argFillString, int argFillCount) {
    return fill(new StringBuilder(argFillCount), argFillString, argFillCount).toString();
  }

  /**
   * Returns a new string consisting of the <code>argFillString</code> text repeated
   * <code>argFillCount</code> times. Note that <code>argFillString</code> can be one or more
   * characters long.
   *
   * @param argFillString the text with which to repeatedly populate the new string
   * @param argFillCount the number of times to repeat <tt>argFillString</tt> in the newly-created
   * string
   * @return the new repeating string
   */
  public static final StringBuffer fill(StringBuffer argFillString, int argFillCount) {
    return fill(new StringBuffer(argFillCount), argFillString.toString(), argFillCount);
  }

  /**
   * Returns a new string consisting of the <code>argFillString</code> text repeated
   * <code>argFillCount</code> times. Note that <code>argFillString</code> can be one or more
   * characters long.
   *
   * @param argFillBuffer the string buffer to append to
   * @param argFillString the text with which to repeatedly populate the new string
   * @param argFillCount the number of times to repeat <tt>argFillString</tt> in the newly-created
   * string
   * @return the new repeating string
   */
  public static final StringBuffer fill(StringBuffer argFillBuffer, String argFillString, int argFillCount) {
    for (int i = 0; i < argFillCount; i++ ) {
      argFillBuffer.append(argFillString);
    }
    return argFillBuffer;
  }

  /**
   * Returns a new string consisting of the <code>argFillString</code> text repeated
   * <code>argFillCount</code> times. Note that <code>argFillString</code> can be one or more
   * characters long.
   *
   * @param argFillString the text with which to repeatedly populate the new string
   * @param argFillCount the number of times to repeat <tt>argFillString</tt> in the newly-created
   * string
   * @return the new repeating string
   */
  public static final StringBuilder fill(StringBuilder argFillString, int argFillCount) {
    return fill(new StringBuilder(argFillCount), argFillString.toString(), argFillCount);
  }

  /**
   * Returns a new string consisting of the <code>argFillString</code> text repeated
   * <code>argFillCount</code> times. Note that <code>argFillString</code> can be one or more
   * characters long.
   *
   * @param argFillBuffer the string buffer to append to
   * @param argFillString the text with which to repeatedly populate the new string
   * @param argFillCount the number of times to repeat <tt>argFillString</tt> in the newly-created
   * string
   * @return the new repeating string
   */
  public static final StringBuilder fill(StringBuilder argFillBuffer, String argFillString, int argFillCount) {
    for (int i = 0; i < argFillCount; i++ ) {
      argFillBuffer.append(argFillString);
    }
    return argFillBuffer;
  }


  /**
   * Safely gets the bytes from a string, giving preference to UTF-8. Instead of throwing
   * {@link java.io.UnsupportedEncodingException}, the platform's default charset will be used to
   * get the bytes.
   *
   * @param argString the string whose bytes will be retreived
   * @return the bytes for the string
   * @see java.lang.String#getBytes()
   */
  public static final byte[] getBytes(String argString) {
    return getBytes(argString, "UTF-8");
  }

  /**
   * Safely gets the bytes from a string, giving preference to the specified encoding. Instead of
   * throwing {@link java.io.UnsupportedEncodingException}, the platform's default charset will be
   * used to get the bytes.
   *
   * @param argString the string whose bytes will be retreived
   * @param argEncoding the encoding to use
   * @return the bytes for the string
   * @see java.lang.String#getBytes()
   */
  public static final byte[] getBytes(String argString, String argEncoding) {
    try {
      return argString.getBytes(argEncoding);
    }
    catch (Exception ex) {
      return argString.getBytes();
    }
  }


  /**
   * Intersperse a string within another string at a given interal. For example, calling this method
   * with args ("test", " ") yeilds: ("t e s t")
   *
   * @param argOriginalString string to begin with
   * @param argStringToIntersperse string to interperse within original string
   * @return original string with intersperser with new string.
   */
  public static String intersperse(String argOriginalString, String argStringToIntersperse) {
    return intersperse(argOriginalString, argStringToIntersperse, 1);
  }

  /**
   * Intersperse a string within another string at a given interal. For example, calling this method
   * with args ("test", " ", 1) yeilds: ("t e s t")
   *
   * @param argOriginalString string to begin with
   * @param argStringToIntersperse string to interperse within original string
   * @param interval the interval (in characters) to intersperse the new string.
   * @return original string with intersperser with new string.
   */
  public static String intersperse(String argOriginalString, String argStringToIntersperse, int interval) {
    if (interval < 1) {
      throw new IllegalArgumentException("interval must be positive but was: " + interval
          + ". argOriginalString: " + argOriginalString + " argStringToIntersperse: "
          + argStringToIntersperse);
    }

    if (isEmpty(argOriginalString)) {
      return argOriginalString;
    }

    StringBuilder buff = new StringBuilder(argStringToIntersperse.length() * 2);

    for (int ii = 0; ii < argOriginalString.length(); ii++ ) {
      if ((ii != 0) && ((ii % interval) == 0)) {
        buff.append(argStringToIntersperse);
      }
      buff.append(argOriginalString.charAt(ii));
    }
    return buff.toString();
  }

  /**
   * Returns whether or not the specified string represents a boolean value.
   *
   * @param argString String the string to test for boolean representation
   * @return boolean <code>true</code> if <code>argString</code> can be converted unambiguously
   * to a <code>boolean</code> or <code>Boolean</code> value; <code>false</code> otherwise
   */
  public static final boolean isBoolean(String argString) {
    return (Boolean.TRUE.toString().equalsIgnoreCase(argString) || Boolean.FALSE.toString().equalsIgnoreCase(
        argString));
  }

  /**
   * Returns whether or not the provided string identifies a numeric value. This method looks for
   * any non-numeric characters within the string. This method allows digits and an optional
   * negative sign.
   *
   * @param argString String the string to evaluate
   * @return boolean whether or not the provided string identifies a numeric value
   */
  public static final boolean isDigit(String argString) {
    return isDigit(argString, new DecimalFormatSymbols());
  }

  /**
   * Returns whether or not the provided string identifies a numeric value. This method looks for
   * any non-numeric characters within the string. This method allows digits and an optional
   * negative sign.
   *
   * @param argString String the string to evaluate
   * @param argSymbols DecimalFormatSymbols the decimal characters considered in parsing
   * <code>argString</code>
   * @return boolean whether or not the provided string identifies a numeric value
   */
  public static final boolean isDigit(String argString, DecimalFormatSymbols argSymbols) {
    if (isEmpty(argString)) {
      return false;
    }
    if (argSymbols == null) {
      argSymbols = new DecimalFormatSymbols();
    }
    char minusSign = argSymbols.getMinusSign();

    String pattern = minusSign + "?[\\d]+";
    return argString.matches(pattern);
  }

  /**
   * Returns whether or not the provided string identifies a numeric value. This method looks for
   * any non-numeric characters within the string. This method allows digits and an optional
   * negative sign.
   *
   * @param argString String the string to evaluate
   * @param argLocale Locale the locale from which to derive any decimal characters considered in
   * parsing <code>argString</code>
   * @return boolean whether or not the provided string identifies a numeric value
   */
  public static final boolean isDigit(String argString, Locale argLocale) {
    if (argLocale == null) {
      argLocale = Locale.getDefault();
    }
    return isDigit(argString, new DecimalFormatSymbols(argLocale));
  }

  /**
   * Determine if the passed CharSequence is empty or not. Empty is defined as being null or
   * consisting only of whitespace characters (space, tab, etc). This method is a convenience method
   * for working with <code>StringBuffer</code>s and <code>StringBuilder</code>s, implementors
   * of <code>CharSequence</code>, rather than having to call <code>toString()</code> and use
   * the overridden method.
   *
   * @param argSeq the string to check
   * @return a boolean indicating whether or not the string is empty.
   */
  public static final boolean isEmpty(CharSequence argSeq) {
    // Shortcut: check for zero length before performing conversion to string.
    return (argSeq == null) || (argSeq.length() == 0) || argSeq.toString().trim().equals(EMPTY);
  }

  /**
   * Determine if the passed string is empty or not. Empty is defined as being null or consisting
   * only of whitespace characters (space, tab, etc).
   *
   * @param argString the string to check
   * @return a boolean indicating whether or not the string is empty.
   * @created January 2, 2003
   */
  public static final boolean isEmpty(String argString) {
    return (argString == null) || argString.trim().equals(EMPTY);
  }

  /**
   * Returns whether or not the provided string identifies a numeric value. This method looks for
   * any non-numeric characters within the string. This method allows whole or fractional numbers
   * and an optional negative sign. (e.g. 1, 1.3, .3, -1, -1.3, or -.3)
   *
   * @param argString String the string to evaluate
   * @return boolean whether or not the provided string identifies a numeric value
   */
  public static final boolean isNumber(String argString) {
    return isNumber(argString, new DecimalFormatSymbols());
  }

  /**
   * Returns whether or not the provided string identifies a numeric value. This method looks for
   * any non-numeric characters within the string. This method allows whole or fractional numbers
   * and an optional negative sign. (e.g. 1, 1.3, .3, -1, -1.3, or -.3)
   *
   * @param argString String the string to evaluate
   * @param argSymbols DecimalFormatSymbols the decimal characters considered in parsing
   * <code>argString</code>
   * @return boolean whether or not the provided string identifies a numeric value
   */
  public static final boolean isNumber(String argString, DecimalFormatSymbols argSymbols) {
    if (isEmpty(argString)) {
      return false;
    }
    if (argSymbols == null) {
      argSymbols = new DecimalFormatSymbols();
    }
    char minusSign = argSymbols.getMinusSign();
    char decimalSeparator = argSymbols.getDecimalSeparator();

    String pattern = minusSign + "?[\\d]*[\\" + decimalSeparator + "]?[\\d]*";
    return argString.matches(pattern);
  }

  /**
   * Returns whether or not the provided string identifies a numeric value. This method looks for
   * any non-numeric characters within the string. This method allows whole or fractional numbers
   * and an optional negative sign. (e.g. 1, 1.3, .3, -1, -1.3, or -.3)
   *
   * @param argString String the string to evaluate
   * @param argLocale Locale the locale from which to derive any decimal characters considered in
   * parsing <code>argString</code>
   * @return boolean whether or not the provided string identifies a numeric value
   */
  public static final boolean isNumber(String argString, Locale argLocale) {
    if (argLocale == null) {
      argLocale = Locale.getDefault();
    }
    return isNumber(argString, new DecimalFormatSymbols(argLocale));
  }

  /**
   * Returns whether or not the provided string identifies a positive numeric value. This method
   * looks for any non-numeric characters within the string. This method allows digits only.
   *
   * @param argString String the string to evaluate
   * @return boolean whether or not the provided string identifies a numeric value
   */
  public static final boolean isPositiveDigit(String argString) {
    String pattern = "[\\d]+";
    return (argString != null) ? argString.matches(pattern) : false;
  }

  /**
   * Returns whether or not the provided string identifies a positive numeric value. This method
   * looks for any non-numeric characters within the string. This method allows whole or fractional
   * numbers, but no sign. (e.g. 1, 1.3, or .3; but not -1, -1.3, or -.3)
   *
   * @param argString String the string to evaluate
   * @return whether or not the provided string identifies a numeric value
   */
  public static final boolean isPositiveNumber(String argString) {
    return isPositiveNumber(argString, new DecimalFormatSymbols());
  }

  /**
   * Returns whether or not the provided string identifies a positive numeric value. This method
   * looks for any non-numeric characters within the string. This method allows whole or fractional
   * numbers, but no sign. (e.g. 1, 1.3, or .3; but not -1, -1.3, or -.3)
   *
   * @param argString String the string to evaluate
   * @param argSymbols DecimalFormatSymbols the decimal characters considered in parsing
   * <code>argString</code>
   * @return whether or not the provided string identifies a numeric value
   */
  public static final boolean isPositiveNumber(String argString, DecimalFormatSymbols argSymbols) {
    if (isEmpty(argString)) {
      return false;
    }
    if (argSymbols == null) {
      argSymbols = new DecimalFormatSymbols();
    }
    char decimalSeparator = argSymbols.getDecimalSeparator();

    String pattern = "[\\d]*[\\" + decimalSeparator + "]?[\\d]*";
    return argString.matches(pattern);
  }

  /**
   * Returns whether or not the provided string identifies a positive numeric value. This method
   * looks for any non-numeric characters within the string. This method allows whole or fractional
   * numbers, but no sign. (e.g. 1, 1.3, or .3; but not -1, -1.3, or -.3)
   *
   * @param argString String the string to evaluate
   * @param argLocale Locale the locale from which to derive any decimal characters considered in
   * parsing <code>argString</code>
   * @return whether or not the provided string identifies a numeric value
   */
  public static final boolean isPositiveNumber(String argString, Locale argLocale) {
    if (argLocale == null) {
      argLocale = Locale.getDefault();
    }
    return isPositiveNumber(argString, new DecimalFormatSymbols(argLocale));
  }


  /**
   * This joins the toString of each object in the LIst into a single String with the specified
   * delimiter. So join( {"A","B",C"}, ";") results in "A;B;C".
   *
   * @param argSource List of objects to be joined
   * @param argDelimiter The delimeter to place between each element
   * @return the joined String
   * @throws NullPointerException if argSource is null
   * @created January 9, 2003
   */
  public static final String join(List<?> argSource, String argDelimiter) {
    StringBuilder buf = new StringBuilder(100);

    for (int i = 0; i < argSource.size(); i++ ) {
      buf.append(argSource.get(i).toString());

      if (i < (argSource.size() - 1)) {
        buf.append(argDelimiter);
      }
    }

    return buf.toString();
  }

  /**
   * This joins the toString of each object in the object array into a single String with the
   * specified delimiter. So join( {"A","B",C"}, ";") results in "A;B;C".
   *
   * @param argSource Array of objects to be joined
   * @param argDelimiter The delimeter to place between each element
   * @return the joined String
   * @throws NullPointerException if argSource is null
   * @created December 13, 2002
   */
  public static final String join(Object[] argSource, String argDelimiter) {
    return join(argSource, argDelimiter, 0, argSource.length - 1);
  }

  /**
   * This joins the toString of each object in the object array into a single String with the
   * specified delimiter. So join( {"A","B",C"}, ";", 1, 2) results in "B;C".
   *
   * @param argSource Array of objects to be joined
   * @param argDelimiter The delimeter to place between each element
   * @param argStart The index of the first array element to join
   * @param argEnd The index of the last array element to join
   * @return the joined String
   * @throws NullPointerException if argSource is null
   * @throws ArrayIndexOutOfBoundsException if argStart or argEnd are invalid
   * @created February 1, 2003
   */
  public static final String join(Object[] argSource, String argDelimiter, int argStart, int argEnd) {
    StringBuilder buf = new StringBuilder(100);

    for (int i = argStart; i <= argEnd; i++ ) {
      buf.append(argSource[i].toString());

      if (i < argEnd) {
        buf.append(argDelimiter);
      }
    }
    return buf.toString();
  }

  /**
   * Return a string containing the <tt>argLength</tt> left-most characters of the specified
   * string.
   *
   * @param argText the string to par
   * @param argLength the length of string to return
   * @return the new right-truncated string
   * @throws IllegalArgumentException if argLength is negative
   */
  public static final String left(String argText, int argLength) {
    if (argText == null) {
      return null;
    }
    if (argLength < 0) {
      throw new IllegalArgumentException();
    }
    argLength = Math.min(argLength, argText.length());

    return argText.substring(0, argLength);
  }

  /**
   * This adds <tt>argPadChar</tt> to the front of <tt>argTarget</tt> until it is at least the
   * requested length. If the passed <tt>argTarget</tt> is at least as long as <tt>argLength</tt> ,
   * the string is unmodified.
   *
   * @param argTarget the string to be added to
   * @param argLength the length to pad to
   * @param argPadChar the character to use in padding
   * @return the padded string
   * @created January 20, 2003
   */
  public static final String leftPad(String argTarget, int argLength, char argPadChar) {
    return leftPad(new StringBuilder(argTarget), argLength, argPadChar).toString();
  }

  /**
   * This adds <tt>argPadChar</tt> to the front of <tt>argTarget</tt> until it is at least the
   * requested length. If the passed <tt>argTarget</tt> is at least as long as <tt>argLength</tt> ,
   * the buffer is unmodified.
   *
   * @param argTarget the buffer to be modified
   * @param argLength the length to pad to
   * @param argPadChar the character to use in padding
   * @return the modified buffer
   * @created January 20, 2003
   */
  public static final StringBuffer leftPad(StringBuffer argTarget, int argLength, char argPadChar) {
    while (argTarget.length() < argLength) {
      argTarget.insert(0, argPadChar);
    }
    return argTarget;
  }

  /**
   * This adds <tt>argPadChar</tt> to the front of <tt>argTarget</tt> until it is at least the
   * requested length. If the passed <tt>argTarget</tt> is at least as long as <tt>argLength</tt> ,
   * the buffer is unmodified.
   *
   * @param argTarget the buffer to be modified
   * @param argLength the length to pad to
   * @param argPadChar the character to use in padding
   * @return the modified buffer
   * @created January 20, 2003
   */
  public static final StringBuilder leftPad(StringBuilder argTarget, int argLength, char argPadChar) {
    while (argTarget.length() < argLength) {
      argTarget.insert(0, argPadChar);
    }
    return argTarget;
  }

  /**
   * This adds spaces to the front of <tt>argTarget</tt> until it is at least the requested
   * length. If the passed <tt>argTarget</tt> is at least as long as <tt>argLength</tt> , the
   * string is unmodified.
   *
   * @param argTarget the string to be added to
   * @param argLength the length to pad to
   * @return the padded string
   * @created January 20, 2003
   */
  public static final String leftPadSpaces(String argTarget, int argLength) {
    return leftPad(argTarget, argLength, ' ');
  }

  /**
   * This adds zeros (0) to the front of <tt>argTarget</tt> until it is at least the requested
   * length. If the passed <tt>argTarget</tt> is at least as long as <tt>argLength</tt> , the
   * string is unmodified.
   *
   * @param argTarget the string to be added to
   * @param argLength the length to pad to
   * @return the padded string
   * @created January 20, 2003
   */
  public static final String leftPadZeros(long argTarget, int argLength) {
    return leftPad(Long.toString(argTarget), argLength, '0');
  }

  /**
   * This adds zeros (0) to the front of <tt>argTarget</tt> until it is at least the requested
   * length. If the passed <tt>argTarget</tt> is at least as long as <tt>argLength</tt> , the
   * string is unmodified.
   *
   * @param argTarget the string to be added to
   * @param argLength the length to pad to
   * @return the padded string
   * @created January 20, 2003
   */
  public static final String leftPadZeros(String argTarget, int argLength) {
    return leftPad(argTarget, argLength, '0');
  }

  /**
   * Removes all white space from the beginning of a string
   * @param argString the string to left trim
   * @return the trimmed string
   * @created July 11, 2003
   */
  public static final String ltrim(String argString) {
    if (argString == null) {
      return null;
    }
    final int stringLength = argString.length();
    int startOfKeep = 0;
    while ((startOfKeep < stringLength) && Character.isWhitespace(argString.charAt( ++startOfKeep))) {
      /*all work is done within the while clause*/
    }

    return argString.substring(startOfKeep);
  }

  /**
   * Returns the specified object's string representation if it is non-<code>null</code> and the
   * empty string otherwise.
   *
   * @param argObj the object to convert to a non-null string representation
   * @return <code>argObj.toString()</code> iff <code>argObj</code> is not <code>null</code> and
   * <code>argObj.toString()</code> is not <code>null</code>; the empty string otherwise
   */
  public static final String nonNull(Object argObj) {
    if (argObj == null) {
      return EMPTY;
    }
    String toString = argObj.toString();
    return (toString != null) ? toString : EMPTY;
  }

  /**
   * Returns the specified object's string representation if it is non-<code>null</code> and non-
   * trivial (i.e. it consists of characters other than whitespace) and <code>null</code> otherwise.
   *
   * @param argObj the object whose string form to return
   * @return <code>argObj.toString()</code> iff <code>argObj</code> is not <code>null</code> and
   * <code>argObj.toString()</code> is not <code>null</code> and non-trivial; <code>null</code>
   * otherwise
   */
  public static final String nonEmpty(Object argObj) {
    String toString = nonNull(argObj);
    return (isEmpty(toString)) ?  null : toString;
  }

  /**
   * Removes white space from within a string. e.g. "023 234 4232" becomes "0232344232"
   *
   * @param argString the string to be packed
   * @return the string with white space removed.
   */
  public static final String pack(String argString) {
    if (argString == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder(argString);
    int index = 0;
    while (index < sb.length()) {
      if (Character.isWhitespace(sb.charAt(index))) {
        sb.replace(index, index + 1, EMPTY);
      }
      else {
        index++ ;
      }

    }
    return sb.toString();
  }

  /**
   * Removes all instances of the specified character from the beginning of a string.
   *
   * e.g. "00343" becomes "343"
   *
   * @param argString the string to modify
   * @param argLeadingChar this method will scan for an remove an uninterupted sequence of this
   * chacter starting from index 0
   * @return the modified string
   * @created August 16, 2007
   */
  public static final String removeLeadingChar(String argString, char argLeadingChar) {
    if (isEmpty(argString)) {
      return argString;
    }

    while (argString.length() > 0) {
      if (argString.charAt(0) == argLeadingChar) {
        argString = argString.substring(1);
      }
      else {
        break;
      }
    }
    return argString;
  }

  /**
   * Remove line feeds from the given string, trims each line, and puts the specified string
   * in between what used to be seperate lines.
   *
   * This is useful for compacting a beautifed SQL or XMl string for example.
   *
   * For Example, given: <m:Message xmlns:m="http://xmlns.vodafone.nl/GetCustomerRequest">
   * <m0:RequestHeader> <m0:TrackingId>{?}</m0:TrackingId>
   *
   * Would result in: <m:Message xmlns:m="http://xmlns.vodafone.nl/GetCustomerRequest"><m0:RequestHeader><m0:TrackingId>{?}</m0:TrackingId>
   *
   * @param argString to remove line feeds & compact.
   * @param argLineFeedReplacement the string to replace the line feeds with.
   * @return tighter, cleaned up string.
   */
  public static final String removeLineFeeds(String argString, String argLineFeedReplacement) {
    String[] lines = argString.split("\r\n|[\r\n]");
    StringBuilder cleansed = new StringBuilder(argString.length());

    for (int ii = 0; ii < lines.length; ++ii) {
      cleansed.append(lines[ii].trim());

      if (ii < (lines.length - 1)) {
        cleansed.append(argLineFeedReplacement);
      }
    }
    return cleansed.toString().trim();
  }

  /**
   * removes all characters from a string that are not numbers or letters
   *
   * @param argSource the string to remove characters from
   * @return a string with only letters and digits
   */
  public static final String removeNonAlphaNumeric(String argSource) {
    if (argSource == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder(argSource.length());
    for (int i = 0; i < argSource.length(); i++ ) {
      char c = argSource.charAt(i);
      if (Character.isLetterOrDigit(c)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }


  /**
   * This replaces all occurances of <tt>argReplace</tt> in <tt>argTarget</tt> with
   * <tt>argWith</tt> .
   *
   * @param argTarget The buffer to search and replace within.
   * @param argReplace The string to search for.
   * @param argWith The string to replace with.
   * @return The modified buffer
   * @created January 20, 2003
   */
  public static final StringBuffer replaceAll(StringBuffer argTarget, String argReplace, String argWith) {
    return replaceAll(argTarget, argReplace, argWith, 0);
  }

  /**
   * This replaces all occurances of <tt>argReplace</tt> in <tt>argTarget</tt> with
   * <tt>argWith</tt> .
   *
   * @param argTarget The buffer to search and replace within.
   * @param argReplace The string to search for.
   * @param argWith The string to replace with.
   * @param argStart The location to start replacing from
   * @return The modified buffer
   */
  public static final StringBuffer replaceAll(StringBuffer argTarget, String argReplace, String argWith,
      int argStart) {

    if ((argReplace == null) || EMPTY.equals(argReplace)) {
      return argTarget;
    }

    StringBuilder builder = new StringBuilder(argTarget.toString());
    replaceAll(builder, argReplace, argWith, argStart);

    argTarget.setLength(0);
    argTarget.append(builder.toString());

    return argTarget;
  }

  /**
   * This replaces all occurances of <tt>argReplace</tt> in <tt>argTarget</tt> with
   * <tt>argWith</tt> .
   *
   * @param argTarget The buffer to search and replace within.
   * @param argReplace The string to search for.
   * @param argWith The string to replace with.
   * @return The modified builder
   */
  public static final StringBuilder replaceAll(StringBuilder argTarget, String argReplace, String argWith) {
    return replaceAll(argTarget, argReplace, argWith, 0);
  }

  /**
   * This replaces all occurances of <tt>argReplace</tt> in <tt>argTarget</tt> with
   * <tt>argWith</tt> .
   *
   * @param argTarget The buffer to search and replace within.
   * @param argReplace The string to search for.
   * @param argWith The string to replace with.
   * @param argStart The location to start replacing from
   * @return The modified builder
   */
  public static final StringBuilder replaceAll(StringBuilder argTarget, String argReplace, String argWith,
      int argStart) {

    if ((argReplace == null) || EMPTY.equals(argReplace)) {
      return argTarget;
    }

    int found = argTarget.indexOf(argReplace, argStart);
    int replaceLength = argReplace.length();
    int replaceWithLength = argWith.length();

    while (found != -1) {
      argTarget.replace(found, found + replaceLength, argWith);

      // look for another instance
      if ((found + replaceWithLength) < argTarget.length()) {
        found = argTarget.indexOf(argReplace, (found + replaceWithLength));
      }
      else {
        break;
      }
    }

    return argTarget;
  }

  /**
   * Replace the variables in <code>argTarget</code>, looking up the values in
   * <code>argVariableLookupMap</code>. Variables are in the format ${VARIABLE_NAME}.
   *
   * @param argTarget the string containing variables to replace
   * @param argVariableLookupMap the map to use for the variable lookups
   * @return modified string
   */
  public static final String replaceVariables(String argTarget,
      Map<? extends Object, ? extends Object> argVariableLookupMap) {

    return replaceVariables(new StringBuilder(argTarget), argVariableLookupMap).toString();
  }

  /**
   * Replace the variables in <code>argTarget</code>, looking up the values in
   * <code>argVariableLookupMap</code>. Variables are in the format ${VARIABLE_NAME}.
   *
   * @param argTarget the string buffer containing variables to replace
   * @param argVariableLookupMap the map to use for the variable lookups
   * @return modified string buffer
   */
  public static final StringBuffer replaceVariables(StringBuffer argTarget,
      Map<? extends Object, ? extends Object> argVariableLookupMap) {

    StringBuilder builder = new StringBuilder(argTarget.toString());
    replaceVariables(builder, argVariableLookupMap);

    argTarget.setLength(0);
    argTarget.append(builder.toString());
    return argTarget;
  }

  /**
   * Replace the variables in <code>argTarget</code>, looking up the values in
   * <code>argVariableLookupMap</code>. Variables are in the format ${VARIABLE_NAME}.
   *
   * @param argTarget the string buffer containing variables to replace
   * @param argVariableLookupMap the map to use for the variable lookups
   * @return modified string buffer
   */
  public static final StringBuilder replaceVariables(StringBuilder argTarget,
      Map<? extends Object, ? extends Object> argVariableLookupMap) {

    int foundStart = argTarget.indexOf("${");
    int foundEnd;
    String variableName;
    String variableValue;
    while (foundStart != -1) {
      foundEnd = argTarget.indexOf("}", foundStart + 1);
      variableName = argTarget.substring(foundStart + 2, foundEnd);
      variableValue = nonNull(argVariableLookupMap.get(variableName));
      argTarget.replace(foundStart, foundEnd + 1, variableValue);

      // look for another variable
      foundStart = argTarget.indexOf("${", foundStart + 1);
    }
    return argTarget;
  }

  /**
   * Return a string containing the <code>argLength</code> right-most characters of the specified
   * string.
   *
   * @param argText the string to par
   * @param argLength the length of string to return
   * @return the new left-truncated string
   * @throws IllegalArgumentException if argLength is negative
   */
  public static final String right(String argText, int argLength) {
    if (argText == null) {
      return null;
    }
    if (argLength < 0) {
      throw new IllegalArgumentException();
    }
    argLength = Math.min(argLength, argText.length());

    return argText.substring(argText.length() - argLength, argText.length());
  }

  /**
   * Append the character <code>argPadChar</code> to <code>argTarget</code> until the length is
   * at least <code>argLength</code> characters in length.
   *
   * @param argTarget string to append to
   * @param argLength desired minimum length
   * @param argPadChar character to append
   * @return the modified buffer
   */
  public static final String rightPad(String argTarget, int argLength, char argPadChar) {
    return rightPad(new StringBuffer(argTarget), argLength, argPadChar).toString();
  }

  /**
   * Append the character <code>argPadChar</code> to <code>argTarget</code> until the length is
   * at least <code>argLength</code> characters in length.
   *
   * @param argTarget string buffer to append to
   * @param argLength desired minimum length
   * @param argPadChar character to append
   * @return the modified string builder
   */
  public static final StringBuffer rightPad(StringBuffer argTarget, int argLength, char argPadChar) {
    while (argTarget.length() < argLength) {
      argTarget.append(argPadChar);
    }
    return argTarget;
  }

  /**
   * Append the character <code>argPadChar</code> to <code>argTarget</code> until the length is
   * at least <code>argLength</code> characters in length.
   *
   * @param argTarget string buffer to append to
   * @param argLength desired minimum length
   * @param argPadChar character to append
   * @return the modified string builder
   */
  public static final StringBuilder rightPad(StringBuilder argTarget, int argLength, char argPadChar) {
    while (argTarget.length() < argLength) {
      argTarget.append(argPadChar);
    }
    return argTarget;
  }

  /**
   * Append spaces to <code>argTarget</code> until the length is at least <code>argLength</code>
   * characters in length.
   *
   * @param argTarget string buffer to append to
   * @param argLength desired minimum length
   * @return the modified string buffer
   */
  public static final String rightPadSpaces(String argTarget, int argLength) {
    return rightPad(argTarget, argLength, ' ');
  }

  /**
   * Removes all white space from the end of a string
   * @param argString the string to right trim
   * @return the trimmed string
   * @created July 11, 2003
   */
  public static final String rtrim(String argString) {
    if (argString == null) {
      return null;
    }
    int keepLength = argString.length() - 1;
    while ((keepLength > 0) && Character.isWhitespace(argString.charAt( --keepLength))) {
      /*all work is done within the while clause*/
    }

    return argString.substring(0, keepLength + 1);
  }

  /**
   * Extracts a section of a string and returns a new string.
   *
   * @param argString The string to slice.
   * @param argStart The zero-based index at which to begin extraction.
   * @param argEnd The zero-based index at which to end extraction. As a negative index, endSlice
   * indicates an offset from the end of the string.<br>
   * Example: <code>StringUtils.slice(s, 2, -1)</code> extracts the third character through the
   * second to last character in the string.
   * @return <code>null</code> if <code>argString</code> is <code>null</code>. or the new
   * string otherwise
   */
  public static String slice(String argString, int argStart, int argEnd) {
    if (argString == null) {
      return null;
    }
    if (argString.length() == 0) {
      return EMPTY;
    }
    if (argString.length() < argStart) {
      return EMPTY;
    }
    if (argStart < 0) {
      argStart = argString.length() + argStart;
    }
    if (argEnd < 0) {
      argEnd = argString.length() + argEnd;
    }
    if (argEnd > argString.length()) {
      argEnd = argString.length();
    }
    return argString.substring(argStart, argEnd);
  }

  /**
   * Splits the string based on locations of a specified character.
   *
   * The same affect could be had be using argSource.split("" + argSplitOn) but that was found to
   * have a bit of a performance hit, possibly due to the fact that it uses regEx. For splitting on
   * a the '.' character, this version appears to run at about 4x the speed of the
   * String.split("\\.")
   *
   * @param argSource the sequence to split
   * @param argSplitOn the character to use when splitting the sequence
   * @return the components
   */
  public static final String[] split(String argSource, char argSplitOn) {
    int found = argSource.indexOf(argSplitOn);

    if (found == -1) {
      return new String[] {argSource};
    }

    List<String> parts = new ArrayList<String>(4);

    int lastFound = 0;
    for (; found != -1; found = argSource.indexOf(argSplitOn, lastFound)) {
      parts.add(argSource.substring(lastFound, found));
      lastFound = found + 1;
    }

    // add the last method call
    /*
     * Do not return an extra blank element if the source string ends with the split
     * string.  - C Dusseau 3/28/06
     */
    if ((lastFound != argSource.length()) || (parts.size() == 0)) {
      parts.add(argSource.substring(lastFound, argSource.length()));
    }
    return parts.toArray(new String[parts.size()]);
  }

  /**
   * Converts the specified string into a two-element array, with the first element containing the
   * first half of the text and the second element containing the second half.
   *
   * @param argText the text to be split into an array
   * @return a two-element array representing the division of <code>argText</code> into its first
   * and second halves
   */
  public static CharSequence[] splitTo2Lines(CharSequence argText) {
    int leftCursor, rightCursor;
    // init all to center of text
    leftCursor = rightCursor = argText.length() / 2;
    // loop until the right cursor is at the end
    for (;;) {

      //check the character at the right cursor to see if it is a valid break
      if ((rightCursor < argText.length()) && Character.isWhitespace(argText.charAt(rightCursor))) {
        // there is a space at rightCursor...
        leftCursor = rightCursor;
        // ...and skip the space
        rightCursor++ ;
        break;
      }
      rightCursor++ ;
      if (rightCursor >= argText.length()) {
        // no breaks found...
        rightCursor = leftCursor = argText.length();
        break;
      }

      // check the left cursor for a valid break
      leftCursor-- ;
      if ((leftCursor > 0) && Character.isWhitespace(argText.charAt(leftCursor))) {
        // there is a space at leftCursor, so skip the space
        rightCursor = leftCursor + 1;
        break;
      }
    }
    CharSequence[] results = new CharSequence[2];
    results[0] = argText.subSequence(0, leftCursor);
    results[1] = argText.subSequence(rightCursor, argText.length());
    return results;
  }


  /**
   * Adds Java string escapes to bring within the range of printable ASCII characters. (e.g.
   * 32-127).
   *
   * @param value the value to escape
   * @return the escape ASCII text
   */
  public static final String toEscapedAscii(String value) {
    if (value == null) {
      return value;
    }
    StringBuilder sb = new StringBuilder(value);
    for (int i = 0; i < sb.length(); i++ ) {
      char c = sb.charAt(i);
      if ((c < 32) || (c > 127)) {
        sb.replace(i, i + 1, "\\u" + toFourDigitHex(c));
        i += 5;
      }
      else if (c == '\\') {
        sb.insert(i + 1, '\\');
        i++ ;
      }
    }
    return sb.toString();
  }

  /**
   * Returns a four digit hexadecimal number left-padded with zeros.
   *
   * @param c the value to return in hexadecimal
   * @return the four-digit hexadecimal number for the character
   */
  public static final String toFourDigitHex(char c) {
    if (c <= 0xf) {
      return "000" + Integer.toHexString(c);
    }
    if (c <= 0xff) {
      return "00" + Integer.toHexString(c);
    }
    if (c <= 0xfff) {
      return "0" + Integer.toHexString(c);
    }
    if (c <= 0xffff) {
      return "0" + Integer.toHexString(c);
    }
    return Integer.toHexString(c);
  }

  /**
   * Prepare the string to be displayed as HTML.
   *
   * @param sb the text to prepare
   */
  public static void toHtml(StringBuffer sb) {
    StringUtils.replaceAll(sb, "<", "&lt;");
    StringUtils.replaceAll(sb, ">", "&gt;");
    StringUtils.replaceAll(sb, "\r\n", "<br>");
    StringUtils.replaceAll(sb, "\r", "<br>");
    StringUtils.replaceAll(sb, "\n", "<br>");
    StringUtils.replaceAll(sb, " ", "&nbsp;");
    StringUtils.replaceAll(sb, "\t", "&nbsp;&nbsp;");
    sb.insert(0, "<font face=\"monospace\">");
    sb.append("</font>");
  }

  /**
   * Prepare the string to be displayed as HTML.
   *
   * @param sb the text to prepare
   */
  public static void toHtml(StringBuilder sb) {
    StringUtils.replaceAll(sb, "<", "&lt;");
    StringUtils.replaceAll(sb, ">", "&gt;");
    StringUtils.replaceAll(sb, "\r\n", "<br>");
    StringUtils.replaceAll(sb, "\r", "<br>");
    StringUtils.replaceAll(sb, "\n", "<br>");
    StringUtils.replaceAll(sb, " ", "&nbsp;");
    StringUtils.replaceAll(sb, "\t", "&nbsp;&nbsp;");
    sb.insert(0, "<font face=\"monospace\">");
    sb.append("</font>");
  }

  /**
   * Removes whitespace from a string based on the trim type requested.
   * @param argString the string to trim
   * @param argTrimType how to trim the string
   * @return the trimmed string
   * @created July 11, 2003
   */
  public static final String trim(String argString, StringTrimType argTrimType) {
    if (argString == null) {
      return null;
    }
    if (argTrimType == null) {
      argTrimType = StringTrimType.NONE;
    }
    switch (argTrimType) {
      case NONE:
        return argString;
      case FULL:
        return argString.trim();
      case RIGHT:
        return rtrim(argString);
      case LEFT:
        return ltrim(argString);
      default:
        throw new IllegalArgumentException("invalid trim type [" + argTrimType + "]");
    }
  }

  /**
   * Truncates a string to the given length.
   *
   * @param argString the string to truncate
   * @param argLen the length to truncate to
   * @return the truncated string
   */
  public static final String truncate(String argString, int argLen) {
    if ((argString == null) || (argString.length() <= argLen)) {
      return argString;
    }
    else if (argLen < 4) {
      return argString.substring(0, argLen);
    }
    else {
      return argString.substring(0, argLen - 3) + "...";
    }
  }

  /**
   * Translate java escape characters into the characters they represent. The escape sequences
   * supported are as follow:
   * <ul>
   * <li> '\\' -> backslash (\)</li>
   * <li> '\'' -> single-quote (')</li>
   * <li> '\"' -> double-quote (")</li>
   * <li> '\b' -> backspace, ctrl-H (8, 0x08)</li>
   * <li> '\n' -> newline, ctrl-J (10, 0x0A)</li>
   * <li> '\f' -> formfeed, ctrl-L (12, 0x0C)</li>
   * <li> '\r' -> carriage return, ctrl-M (13, 0x0D)</li>
   * <li> '\t' -> tab, ctrl-I (9, 0x09)</li>
   * <li> '\u003f' -> the unicode hex char code (must be exactly 4 digits) </li>
   * <li> '\377' -> octal char code (must be exactly 3 digits)</li>
   * </ul>
   *
   * @param value the string to parse and unescape
   * @return an unescaped version of the string
   * @author Doug Berkland
   * @created February 12, 2003
   */
  public static final String unescape(String value) {
    return unescape(value, getLoggingErrorHandler());
  }

  /**
   * Translate java escape characters into the characters they represent. The escape sequences
   * supported are as follow:
   * <ul>
   * <li> '\\' -> backslash (\)</li>
   * <li> '\'' -> single-quote (')</li>
   * <li> '\"' -> double-quote (")</li>
   * <li> '\b' -> backspace, ctrl-H (8, 0x08)</li>
   * <li> '\n' -> newline, ctrl-J (10, 0x0A)</li>
   * <li> '\f' -> formfeed, ctrl-L (12, 0x0C)</li>
   * <li> '\r' -> carriage return, ctrl-M (13, 0x0D)</li>
   * <li> '\t' -> tab, ctrl-I (9, 0x09)</li>
   * <li> '\u003f' -> the unicode hex char code (must be exactly 4 digits) </li>
   * <li> '\377' -> octal char code (must be exactly 3 digits)</li>
   * </ul>
   *
   * @param value the string to parse and unescape
   * @param logger the error handler to use
   * @return an unescaped version of the string
   */
  public static final String unescape(String value, ErrorHandler logger) {
    if (value == null) {
      return value;
    }
    StringBuilder sb = new StringBuilder(value);
    for (int i = 0; i < sb.length(); i++ ) {
      if ((sb.charAt(i) == '\\') && (i + 1 < sb.length())) {
        switch (sb.charAt(i + 1)) {
          case '\\': // backslash
            sb.replace(i, i + 2, "\\");
            break;
          case 'b': // backspace, ctrl-H (8, 0x08)
            sb.replace(i, i + 2, "\b");
            break;
          case 'n': // newline, ctrl-J (10, 0x0A)
            sb.replace(i, i + 2, "\n");
            break;
          case 'f': // formfeed, ctrl-L (12, 0x0C)
            sb.replace(i, i + 2, "\f");
            break;
          case 'r': // carriage return, ctrl-M (13, 0x0D)
            sb.replace(i, i + 2, "\r");
            break;
          case 't': // tab, ctrl-I (9, 0x09)
            sb.replace(i, i + 2, "\t");
            break;
          case '\'': // single-quote
            sb.replace(i, i + 2, "\'");
            break;
          case '\"': // double-quote
            sb.replace(i, i + 2, "\"");
            break;
          case 'u': {
            // unicode hex, (must be exactly 4 digits, e.g. '\u00ff')
            try {
              String hexcode = sb.substring(i + 2, i + 6);
              char charValue = (char) Integer.parseInt(hexcode, 16);
              sb.replace(i, i + 6, Character.toString(charValue));
            }
            catch (Exception ex) {
              logger.debug("CAUGHT EXCEPTION", ex);
            }
            break;
          }

          case '0':
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7': {
            // octal (must be exactly 3 digits e.g. '\000')
            try {
              String octalcode = sb.substring(i + 1, i + 4);
              char charValue = (char) Integer.parseInt(octalcode, 8);
              sb.replace(i, i + 4, Character.toString(charValue));
            }
            catch (Exception ex) {
              logger.debug("CAUGHT EXCEPTION", ex);
            }
            break;
          }
          default:
            logger.warn("problem unescaping " + value);

        } //switch
      } //if
    } //for

    return sb.toString();
  }

  /***
   * Converts the the first character of the argument to lower case.
   * @param str 
   *  			value the string to converts.
   * @return  
   * 			the converted string.
   */
  public static String uncapitalize(String str) {
      int strLen;

      if ((str == null) || ((strLen = str.length()) == 0)) {
          return str;
      }

      return new StringBuffer(strLen).append(Character.toLowerCase(str.charAt(0)))
                                     .append(str.substring(1)).toString();
  }
  
	/****
	 * Converts the the first character of the argument to capital.
	 * <ul>
	 * <li>'null' -> Null</li>
	 * <li>null -> null</li>
	 * <li>'abc' -> Abc</li>
	 * </ul>
	 *
	 * @param str
	 *            value the string to converts.
	 * @return
	 * 			  the converted string.
	 */
	public static String capitalize(String str) {
		int strLen;

		if ((str == null) || ((strLen = str.length()) == 0)) {
			return str;
		}

		return new StringBuffer(strLen)
				.append(Character.toTitleCase(str.charAt(0)))
				.append(str.substring(1)).toString();
	}

  private static ErrorHandler getLoggingErrorHandler() {
    if (loggingErrorHandler_ == null) {
      loggingErrorHandler_ = new LoggingErrorHandler();
    }
    return loggingErrorHandler_;
  }

  private StringUtils() {
    // no construction
  }

  /***
   * Get short class name.
   * @param clazz
   * @return Get short class name.
   */
  public static String getShortClassName(Class<? extends Object> clazz) {
      if (clazz == null) {
          return null;
      }

      return getShortClassName(clazz.getName());
  }

  /***
   * Get short class name.
   * @param clazz class name
   * @return Get short class name.
   */
  public static String getShortClassName(String className) {
      if (isEmpty(className)) {
          return className;
      }

      className = getClassName(className, false);

      char[] chars   = className.toCharArray();
      int    lastDot = 0;

      for (int i = 0; i < chars.length; i++) {
          if (chars[i] == PACKAGE_SEPARATOR_CHAR) {
              lastDot = i + 1;
          } else if (chars[i] == INNER_CLASS_SEPARATOR_CHAR) {
              chars[i] = PACKAGE_SEPARATOR_CHAR;
          }
      }

      return new String(chars, lastDot, chars.length - lastDot);
  }

  /**
   * Get simple class name.
   * @param className class name
   * @param processInnerClass
   *
   * @return simple name��or<code>null</code>
   */
  private static String getClassName(String className, boolean processInnerClass) {
      if (isEmpty(className)) {
          return className;
      }

      if (processInnerClass) {
          className = className.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
      }

      int length    = className.length();
      int dimension = 0;

      for (int i = 0; i < length; i++, dimension++) {
          if (className.charAt(i) != '[') {
              break;
          }
      }

      if (dimension == 0) {
          return className;
      }

      if (length <= dimension) {
          return className;
      }

      StringBuffer componentTypeName = new StringBuffer();

      switch (className.charAt(dimension)) {
          case 'Z':
              componentTypeName.append("boolean");
              break;

          case 'B':
              componentTypeName.append("byte");
              break;

          case 'C':
              componentTypeName.append("char");
              break;

          case 'D':
              componentTypeName.append("double");
              break;

          case 'F':
              componentTypeName.append("float");
              break;

          case 'I':
              componentTypeName.append("int");
              break;

          case 'J':
              componentTypeName.append("long");
              break;

          case 'S':
              componentTypeName.append("short");
              break;

          case 'L':

              if ((className.charAt(length - 1) != ';') || (length <= (dimension + 2))) {
                  return className;
              }

              componentTypeName.append(className.substring(dimension + 1, length - 1));
              break;

          default:
              return className;
      }

      for (int i = 0; i < dimension; i++) {
          componentTypeName.append("[]");
      }

      return componentTypeName.toString();
  }

  /**
   * To lower string separated by under scores。
   * 
   * <p>
   * if argSring is <code>null</code>, it will return <code>null</code>。
   * <pre>
   * StringUtil.toLowerCaseWithUnderscores(null)  = null
   * StringUtil.toLowerCaseWithUnderscores("")    = ""
   * StringUtil.toLowerCaseWithUnderscores("aBc") = "a_bc"
   * StringUtil.toLowerCaseWithUnderscores("aBc def") = "a_bc_def"
   * StringUtil.toLowerCaseWithUnderscores("aBc def_ghi") = "a_bc_def_ghi"
   * StringUtil.toLowerCaseWithUnderscores("aBc def_ghi 123") = "a_bc_def_ghi_123"
   * StringUtil.toLowerCaseWithUnderscores("__a__Bc__") = "__a__bc__"
   * </pre>
   * </p>
   * 
   * <p>
   * this method will remain all the character without blank.
   * </p>
   *
   * @param str the String to be converted.
   *
   * @return the lower string separated by under scores.
   */
  public static String toLowerCaseWithUnderscores(String argSring) {
      return LOWER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(argSring);
  }
  
  /**
   * convert the string into camel case。
   *
   * <p>
   * if string is<code>null</code>return<code>null</code>。
   * <pre>
   * StringUtil.toCamelCase(null)  = null
   * StringUtil.toCamelCase("")    = ""
   * StringUtil.toCamelCase("aBc") = "aBc"
   * StringUtil.toCamelCase("aBc def") = "aBcDef"
   * StringUtil.toCamelCase("aBc def_ghi") = "aBcDefGhi"
   * StringUtil.toCamelCase("aBc def_ghi 123") = "aBcDefGhi123"
   * </pre>
   * </p>
   *
   *
   * @param str need converted string
   *
   * @return camel case string.
   */
  public static String toCamelCase(String str) {
      return CAMEL_CASE_TOKENIZER.parse(str);
  }

  /**
   * <pre>
   *  SENTENCE = WORD (DELIMITER* WORD)*
   *
   *  WORD = UPPER_CASE_WORD | LOWER_CASE_WORD | TITLE_CASE_WORD | DIGIT_WORD
   *
   *  UPPER_CASE_WORD = UPPER_CASE_LETTER+
   *  LOWER_CASE_WORD = LOWER_CASE_LETTER+
   *  TITLE_CASE_WORD = UPPER_CASE_LETTER LOWER_CASE_LETTER+
   *  DIGIT_WORD      = DIGIT+
   *
   *  UPPER_CASE_LETTER = Character.isUpperCase()
   *  LOWER_CASE_LETTER = Character.isLowerCase()
   *  DIGIT             = Character.isDigit()
   *  NON_LETTER_DIGIT  = !Character.isUpperCase() && !Character.isLowerCase() && !Character.isDigit()
   *
   *  DELIMITER = WHITESPACE | NON_LETTER_DIGIT
   * </pre>
   */
  private abstract static class WordTokenizer {
      protected static final char UNDERSCORE = '_';

      /**
       * Parse sentence。
       */
      public String parse(String str) {
          if (isEmpty(str)) {
              return str;
          }

          int          length = str.length();
          StringBuffer buffer = new StringBuffer(length);

          for (int index = 0; index < length; index++) {
              char ch = str.charAt(index);

              if (Character.isWhitespace(ch)) {
                  continue;
              }

              if (Character.isUpperCase(ch)) {
                  int wordIndex = index + 1;

                  while (wordIndex < length) {
                      char wordChar = str.charAt(wordIndex);

                      if (Character.isUpperCase(wordChar)) {
                          wordIndex++;
                      } else if (Character.isLowerCase(wordChar)) {
                          wordIndex--;
                          break;
                      } else {
                          break;
                      }
                  }

                  if ((wordIndex == length) || (wordIndex > index)) {
                      index = parseUpperCaseWord(buffer, str, index, wordIndex);
                  } else {
                      index = parseTitleCaseWord(buffer, str, index);
                  }

                  continue;
              }

              if (Character.isLowerCase(ch)) {
                  index = parseLowerCaseWord(buffer, str, index);
                  continue;
              }

              if (Character.isDigit(ch)) {
                  index = parseDigitWord(buffer, str, index);
                  continue;
              }

              inDelimiter(buffer, ch);
          }

          return buffer.toString();
      }

      private int parseUpperCaseWord(StringBuffer buffer, String str, int index, int length) {
          char ch = str.charAt(index++);

          if (buffer.length() == 0) {
              startSentence(buffer, ch);
          } else {
              startWord(buffer, ch);
          }

          for (; index < length; index++) {
              ch = str.charAt(index);
              inWord(buffer, ch);
          }

          return index - 1;
      }

      private int parseLowerCaseWord(StringBuffer buffer, String str, int index) {
          char ch = str.charAt(index++);

          if (buffer.length() == 0) {
              startSentence(buffer, ch);
          } else {
              startWord(buffer, ch);
          }

          int length = str.length();

          for (; index < length; index++) {
              ch = str.charAt(index);

              if (Character.isLowerCase(ch)) {
                  inWord(buffer, ch);
              } else {
                  break;
              }
          }

          return index - 1;
      }

      private int parseTitleCaseWord(StringBuffer buffer, String str, int index) {
          char ch = str.charAt(index++);

          if (buffer.length() == 0) {
              startSentence(buffer, ch);
          } else {
              startWord(buffer, ch);
          }

          int length = str.length();

          for (; index < length; index++) {
              ch = str.charAt(index);

              if (Character.isLowerCase(ch)) {
                  inWord(buffer, ch);
              } else {
                  break;
              }
          }

          return index - 1;
      }

      private int parseDigitWord(StringBuffer buffer, String str, int index) {
          char ch = str.charAt(index++);

          if (buffer.length() == 0) {
              startDigitSentence(buffer, ch);
          } else {
              startDigitWord(buffer, ch);
          }

          int length = str.length();

          for (; index < length; index++) {
              ch = str.charAt(index);

              if (Character.isDigit(ch)) {
                  inDigitWord(buffer, ch);
              } else {
                  break;
              }
          }

          return index - 1;
      }

      protected boolean isDelimiter(char ch) {
          return !Character.isUpperCase(ch) && !Character.isLowerCase(ch)
          && !Character.isDigit(ch);
      }

      protected abstract void startSentence(StringBuffer buffer, char ch);

      protected abstract void startWord(StringBuffer buffer, char ch);

      protected abstract void inWord(StringBuffer buffer, char ch);

      protected abstract void startDigitSentence(StringBuffer buffer, char ch);

      protected abstract void startDigitWord(StringBuffer buffer, char ch);

      protected abstract void inDigitWord(StringBuffer buffer, char ch);

      protected abstract void inDelimiter(StringBuffer buffer, char ch);
  }


  /** word's parser. */
  private static final WordTokenizer CAMEL_CASE_TOKENIZER = new WordTokenizer() {
          protected void startSentence(StringBuffer buffer, char ch) {
              buffer.append(Character.toLowerCase(ch));
          }

          protected void startWord(StringBuffer buffer, char ch) {
              if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                  buffer.append(Character.toUpperCase(ch));
              } else {
                  buffer.append(Character.toLowerCase(ch));
              }
          }

          protected void inWord(StringBuffer buffer, char ch) {
              buffer.append(Character.toLowerCase(ch));
          }

          protected void startDigitSentence(StringBuffer buffer, char ch) {
              buffer.append(ch);
          }

          protected void startDigitWord(StringBuffer buffer, char ch) {
              buffer.append(ch);
          }

          protected void inDigitWord(StringBuffer buffer, char ch) {
              buffer.append(ch);
          }

          protected void inDelimiter(StringBuffer buffer, char ch) {
              if (ch != UNDERSCORE) {
                  buffer.append(ch);
              }
          }
      };
      
      private static final WordTokenizer LOWER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {
          protected void startSentence(StringBuffer buffer, char ch) {
              buffer.append(Character.toLowerCase(ch));
          }

          protected void startWord(StringBuffer buffer, char ch) {
              if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                  buffer.append(UNDERSCORE);
              }

              buffer.append(Character.toLowerCase(ch));
          }

          protected void inWord(StringBuffer buffer, char ch) {
              buffer.append(Character.toLowerCase(ch));
          }

          protected void startDigitSentence(StringBuffer buffer, char ch) {
              buffer.append(ch);
          }

          protected void startDigitWord(StringBuffer buffer, char ch) {
              if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                  buffer.append(UNDERSCORE);
              }

              buffer.append(ch);
          }

          protected void inDigitWord(StringBuffer buffer, char ch) {
              buffer.append(ch);
          }

          protected void inDelimiter(StringBuffer buffer, char ch) {
              buffer.append(ch);
          }
      };

  /** do-nothing class to handle errors in {@link StringUtils#unescape(String, ErrorHandler)} */
  public static class ErrorHandler {
    /**
     * handle a debug message
     * @param msg warning message
     * @param t throwable
     */
    public void debug(String msg, Throwable t) { /*do nothing by default*/}

    /**
     * handle a warning
     * @param msg warning message
     */
    public void warn(String msg) { /*do nothing by default*/}
  }

  /**
   *
   */
  public static class LoggingErrorHandler
      extends ErrorHandler {

    private final Logger log_ = Logger.getLogger(StringUtils.class);

    /** {@inheritDoc} */
    @Override
    public void debug(String msg, Throwable t) {
      log_.debug(msg, t);
    }

    /** {@inheritDoc} */
    @Override
    public void warn(String msg) {
      log_.warn(msg);
    }
  }

}
