package com.t1m0;

public class Main {
     public static void main(String args[]) {
         Util u = new Util();
         String value = u.getSample(Integer.parseInt(args[0]));
         System.out.println(value);
         u.getPercentageOfBase(value).forEach((c,p) -> System.out.println(c+":"+p+"%"));
     }
}
