package com.bhsc.test;

import junit.framework.Assert;

import com.bhsc.alg.Short62Util;

/**
 * 
 * @author bhsc881114
 *
 */
public class Short62Test {

  private static void output(long num) {
    String str = Short62Util.base62(num);
    System.out.println(num + ":" + str);
    long num2 = Short62Util.base10(str);
    System.out.println(num2 + ":" + str);

    Assert.assertEquals(num, num2);
  }

  public static void main(String[] args) {
    for (int i = 1; i < 10000; i++) {
      output(i);
    }
  }

}
