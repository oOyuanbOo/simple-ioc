package javautils;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Created by majinjin on 2019/3/22.
 */
public class HashMapTest {

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= 100) ? 100 : n + 1;
    }

    public static void main(String[] args) {
        // 10001 右移一位是 01000  位或 11001
        // 11001 右移两位是 00110  位或 11111
        // 11111 右移四位是 00001  位或还是 11111 即十进制的31
        // n + 1就是32
//        System.out.println(tableSizeFor(17));
        HashMap map = new HashMap();
        map.put(null, null);
        WeakHashMap wmap = new WeakHashMap();
        wmap.put(null, 1);
        wmap.put(1, null);
        System.out.println(wmap);
        System.out.println(map);
        System.out.println(9%8);
        System.out.println(9&7);
    }

}
