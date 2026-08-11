/*
 * Copyright (c) 2011, 2026, Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyright (c) 2011 IBM Corporation
 */

/*
 * @test
 * @bug 6312706
 * @summary Iterator.remove() from Map.entrySet().iterator() invalidates returned Entry.
 * @author Neil Richards <neil.richards@ngmr.net>, <neil_richards@uk.ibm.com>
 * @library /test/lib
 */

import jdk.test.lib.valueclass.VClass;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class EntrySetIteratorRemoveInvalidatesEntry {
    public static void main(String[] args) throws Exception {
        final IdentityHashMap<String, String> identityHashMap =
            new IdentityHashMap<>();

        identityHashMap.put("One", "Un");
        identityHashMap.put("Two", "Deux");
        identityHashMap.put("Three", "Trois");

        Iterator<Map.Entry<String, String>> entrySetIterator =
            identityHashMap.entrySet().iterator();
        Map.Entry<String, String> entry = entrySetIterator.next();

        entrySetIterator.remove();

        try {
            entry.getKey();
            throw new RuntimeException("Test FAILED: Entry not invalidated by removal.");
        } catch (Exception e) { }

        final IdentityHashMap<VClass, VClass> valueMap = new IdentityHashMap<>();
        valueMap.put(new VClass(1), new VClass(11));
        valueMap.put(new VClass(2), new VClass(22));
        valueMap.put(new VClass(3), new VClass(33));

        Iterator<Map.Entry<VClass, VClass>> valueIterator =
            valueMap.entrySet().iterator();
        Map.Entry<VClass, VClass> valueEntry = valueIterator.next();

        valueIterator.remove();

        try {
            valueEntry.getKey();
            throw new RuntimeException("Test FAILED: Value-class entry not invalidated by removal.");
        } catch (Exception e) { }
    }
}
