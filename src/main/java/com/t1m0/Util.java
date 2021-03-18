package com.t1m0;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public String getSample(int count){
        String[] bases = {"A","G","T","C"};
        String sequenc = "";
        for (int i = 0; i < count ; i++) {
            sequenc += bases[getRandomIntegerBetweenRange(0,3)];
        }
        return sequenc;
    }

    public double getPercentageOfBase(char base, String sequence) {
        int count = 0;
        for (int i = 0; i < sequence.length(); i++) {
            if(sequence.charAt(i) == base){
                count++;
            }
        }
        return (double) count/(double) sequence.length();
    }

    public Map<Character,Double> getPercentageOfBase(final String sequence) {
        final Map<Character,Double> returnMap = new HashMap<>();
        countOccurences(sequence).forEach((base,count) -> returnMap.put(base,calcPrecentage(count,sequence)));
        return returnMap;
    }

    private Map<Character,Integer> countOccurences(String sequence) {
        Map<Character,Integer> map = new HashMap<>();
        for (int i = 0; i < sequence.length(); i++) {
            char current = sequence.charAt(i);
            if(!map.containsKey(current)) {
                map.put(current,0);
            }
            int currentCount = map.get(current);
            map.put(current,currentCount+1);
        }
        return map;
    }

    private double calcPrecentage(int count, String sequence) {
        return ((double)count/(double) sequence.length())*100;
    }

    public int getRandomIntegerBetweenRange(int min, int max){
        return (int)(Math.random()*((max-min)+1))+min;
    }

}
