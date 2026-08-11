/*
 * Copyright (c) 1999, 2026, Oracle and/or its affiliates. All rights reserved.
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
 * @test
 * @bug 4295163
 * @summary AddAll(int, Collection) intersperses the Collection with this List.
 * @library /test/lib
 */

import jdk.test.lib.valueclass.VClass;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class AddAll {
    public static void main(String[] args) throws Exception {
        List<String> t = new FooList<>();
        t.add("b"); t.add("a"); t.add("r");
        t.addAll(0, Arrays.asList("f", "o", "o"));
        if (!t.equals(Arrays.asList("f", "o", "o", "b", "a", "r")))
            throw new Exception("addAll is broken");

        List<VClass> vt = new FooList<>();
        vt.add(new VClass(2)); vt.add(new VClass(1)); vt.add(new VClass(18));
        vt.addAll(0, Arrays.asList(new VClass(6), new VClass(15), new VClass(15)));
        if (!vt.equals(Arrays.asList(new VClass(6), new VClass(15), new VClass(15),
                                    new VClass(2), new VClass(1), new VClass(18))))
            throw new Exception("addAll with VClass is broken");
    }
}

class FooList<E> extends AbstractSequentialList<E> {
    List<E> a = new ArrayList<>();

    public int size() {
        return a.size();
    }

    public ListIterator<E> listIterator(int index) {
        return a.listIterator(index);
    }
}
