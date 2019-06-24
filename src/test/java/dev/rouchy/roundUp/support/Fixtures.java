package dev.rouchy.roundUp.support;

import java.util.Scanner;

public class Fixtures {
    public static String load(String resource, String method) {
        var stream = Fixtures.class.getClassLoader().getResourceAsStream(String.format("fixtures/%s/%s.json", resource, method));
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
