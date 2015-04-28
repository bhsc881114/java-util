package com.bhsc.alg;

/**
 * number to short id,high performance and thread safe
 * 
 * @author bhsc881114
 *
 */
public class Short62Util {

  private static int CHAR_NUM = 62;
  private static int numStart = 48;// 0(48)-9(57)
  private static int lowerStart = 97;// a(97)-z(122)
  private static int upperStart = 65;// A(65)-Z(90)

  private static final String[] alpahTable = new String[] {"a", "b", "c", "d", "e", "f", "g", "h",
      "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
      "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
      "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

  private static int getCharIndex(char c) {
    int v = (int) c;
    if (v >= 'a' && v <= 'z') {
      return v - lowerStart;
    } else if (v >= 'A' && v <= 'Z') {
      return v - upperStart + 26;
    } else if (v >= '0' && v <= '9') {
      return v - numStart + 52;
    } else {
      return -1;
    }
  }

  public static String base62(long id) {
    if (id < 0) {
      throw new RuntimeException("base62 not support negative");
    }
    if (id > Long.MAX_VALUE) {
      throw new RuntimeException("value bigger than " + Long.MAX_VALUE);
    }
    StringBuilder str = new StringBuilder();
    while (id > 0) {
      long x = id % CHAR_NUM;
      id = (id - x) / CHAR_NUM;
      str.append(alpahTable[(int) x]);
    }
    return str.toString();
  }

  public static long base10(String str) {
    long num = 0;
    for (int i = 0; i < str.length(); i++) {
      num += Math.pow(CHAR_NUM, i) * getCharIndex(str.charAt(i));
    }
    return num;
  }
}
