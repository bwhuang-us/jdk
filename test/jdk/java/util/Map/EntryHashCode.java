/*
 * Copyright (c) 2012, 2026, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Portions Copyright (c) 2012 IBM Corporation
 */

/*
 * @test
 * @bug 8000955
 * @library /test/lib
 * @summary Map.Entry implementations need to comply with Map.Entry.hashCode() defined behaviour.
 * @run testng EntryHashCode
 * @author ngmr
 */
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import jdk.test.lib.valueclass.VClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

public class EntryHashCode {
    private static final int TEST_SIZE = 100;

    static final Object[][] entryData = {
        new Object[TEST_SIZE],
        new Object[TEST_SIZE]
    };

    static {
        for (int i = 0; i < entryData[0].length; i++) {
            // key objects need to be Comparable for use in TreeMap
            entryData[0][i] = new Comparable<Object>() {
                public int compareTo(Object o) {
                    return (hashCode() - o.hashCode());
                }
            };
            entryData[1][i] = new Object();
        }
    }

    @Test(dataProvider = "Map<Object,Object>")
    public void testEntryHashCode(String description, Map<Object,Object> map) {
        addTestData(map);
        checkEntryHashCodes(description, map);
    }

    @Test(dataProvider = "Map<VClass,VClass>")
    public void testVClassEntryHashCode(String description, Map<VClass,VClass> map) {
        addVClassTestData(map);
        checkEntryHashCodes(description, map);
    }

    @DataProvider(name = "Map<Object,Object>")
    private static Iterator<Object[]> makeMaps() {
        return Arrays.asList(
            new Object[]{"HashMap", new HashMap<>()},
            new Object[]{"Hashtable", new Hashtable<>()},
            new Object[]{"IdentityHashMap", new IdentityHashMap<>()},
            new Object[]{"LinkedHashMap", new LinkedHashMap<>()},
            new Object[]{"TreeMap", new TreeMap<>()},
            new Object[]{"WeakHashMap", new WeakHashMap<>()},
            new Object[]{"ConcurrentHashMap", new ConcurrentHashMap<>()},
            new Object[]{"ConcurrentSkipListMap", new ConcurrentSkipListMap<>()}
        ).iterator();
    }

    @DataProvider(name = "Map<VClass,VClass>")
    private static Iterator<Object[]> makeVClassMaps() {
        return Arrays.asList(
            new Object[]{"HashMap", new HashMap<>()},
            new Object[]{"Hashtable", new Hashtable<>()},
            new Object[]{"LinkedHashMap", new LinkedHashMap<>()},
            new Object[]{"TreeMap", new TreeMap<>()},
            new Object[]{"ConcurrentHashMap", new ConcurrentHashMap<>()},
            new Object[]{"ConcurrentSkipListMap", new ConcurrentSkipListMap<>()}
        ).iterator();
    }

    private static void addTestData(Map<Object,Object> map) {
        for (int i = 0; i < entryData[0].length; i++) {
            map.put(entryData[0][i], entryData[1][i]);
        }
    }

    private static void addVClassTestData(Map<VClass,VClass> map) {
        for (int i = 0; i < TEST_SIZE; i++) {
            map.put(new VClass(i), new VClass(i + TEST_SIZE));
        }
    }

    private static void checkEntryHashCodes(String description, Map<?,?> map) {
        for (Map.Entry<?,?> e: map.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            int expectedEntryHashCode =
                (Objects.hashCode(key) ^ Objects.hashCode(value));

            if (e.hashCode() != expectedEntryHashCode) {
                failEntryHashCode(description, e);
            }
        }
    }

    private static void failEntryHashCode(String description, Map.Entry<?,?> e) {
        fail("FAILURE: " + description + ": " +
                e.getClass().getName() +
                ".hashCode() does not conform to defined" +
                " behaviour of java.util.Map.Entry.hashCode()");
    }
}
