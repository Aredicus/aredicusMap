package ru.aredicus.map;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


public class MapTest {

    @Test
    public void mapPut_Test() {
        Map<String, Integer> map = generateMap(4);
        MiniHashMap<String, Integer> miniHashMap = generateMiniHashMap(4);
        Assertions.assertEquals(map.size(), miniHashMap.getSize());
    }

    @Test
    public void mapPutOneBucket_Test() {
        MiniHashMap<Integer, Integer> miniHashMap = new MiniHashMap<>();
        miniHashMap.put(1, 1);
        miniHashMap.put(17, 1);
        Assertions.assertEquals(2, miniHashMap.getSize());
    }

    @Test
    public void mapGet_Test() {
        Map<String, Integer> map = generateMap(4);
        MiniHashMap<String, Integer> miniHashMap = generateMiniHashMap(4);
        List<String> keys = IntStream.iterate(0, v -> v + 1).limit(4).mapToObj(String::valueOf).toList();

        for (String s : keys) {
            Assertions.assertEquals(map.get(s), miniHashMap.get(s));
        }
    }

    @Test
    public void mapResize_Test() {
        int start = 4;
        int end = 12;
        MiniHashMap<String, Integer> miniHashMap = generateMiniHashMap(start);
        Map<String, Integer> map = generateMap(start);

        List<String> keys = IntStream.iterate(0, v -> v + 1).limit(end).mapToObj(String::valueOf).toList();
        int capacity = miniHashMap.getCapacity();

        for (int i = start; i < end; i++) {
            miniHashMap.put(String.valueOf(i), i);
            map.put(String.valueOf(i), i);
        }

        Assertions.assertEquals(capacity << 1, miniHashMap.getCapacity());
        for (String s : keys) {
            Assertions.assertEquals(map.get(s), miniHashMap.get(s));
        }
    }


    @Test
    public void mapRemove_Test() {
        Map<String, Integer> map = generateMap(4);
        MiniHashMap<String, Integer> hashMap = generateMiniHashMap(4);
        map.remove("1");
        hashMap.remove("1");

        Assertions.assertNull(hashMap.get("1"));
        Assertions.assertEquals(map.get("1"), hashMap.get("1"));
        Assertions.assertEquals(map.size(), hashMap.getSize());
    }


    @Test
    public void mapComplex_Test() {
        MiniHashMap<Integer, String> map =
                new MiniHashMap<>();
        map.put(1, "A");
        map.put(17, "B");
        map.put(33, "C");

        map.remove(17);
        Assertions.assertEquals(2, map.getSize());
        Assertions.assertEquals("C", map.get(33));
    }

    @Test
    public void mapGetNull_Test() {
        MiniHashMap<Integer, String> map =
                new MiniHashMap<>();
        Assertions.assertNull(map.get(null));
    }

    @Test
    public void mapRemoveNull_Test() {
        MiniHashMap<Integer, String> map =
                new MiniHashMap<>();
        map.remove(null);
    }

    @Test
    public void mapRemoveTwice_Test() {
        MiniHashMap<Integer, String> map =
                new MiniHashMap<>();
        map.put(1, "1");
        map.remove(1);
        map.remove(1);
    }

    @Test
    public void mapPutNullTwice_Test() {
        MiniHashMap<Integer, String> map =
                new MiniHashMap<>();
        map.put(null, "1");
        map.put(null, "2");
        Assertions.assertEquals(1, map.getSize());
        Assertions.assertEquals("2", map.get(null));
    }

    @Test
    public void mapResizeWithNull_Test() {
        MiniHashMap<Integer, String> map =
                new MiniHashMap<>();
        map.put(null, "1");
        for (int i = 0; i < 12; i++) {
            map.put(i, String.valueOf(i));
        }
        Assertions.assertEquals(13, map.getSize());
        Assertions.assertEquals(32, map.getCapacity());
        Assertions.assertEquals("1", map.get(null));
    }

    private static Map<String, Integer> generateMap(int n) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(String.valueOf(i), i);
        }
        return map;
    }

    private static MiniHashMap<String, Integer> generateMiniHashMap(int n) {
        MiniHashMap<String, Integer> miniHashMap = new MiniHashMap<>();
        for (int i = 0; i < n; i++) {
            miniHashMap.put(String.valueOf(i), i);
        }
        return miniHashMap;
    }

}
