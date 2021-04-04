/* 
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package win.shangyh.cmnpro.smp8583.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * TODO FieldDefineTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-03-14  23:44
 *
 */
public class FieldDefineTest {

    @Test(expected = RuntimeException.class)
    public void testDefine() {
        String define = "ansb...512(LLVAR)";
        new FieldDefine(define);
    }

    @Test(expected = RuntimeException.class)
    public void testDefineExp() {
        String define = "ansb...99(LLVAR)";
        new FieldDefine(define);
    }

    @Test
    public void testDefine2() {
        String define = "ansb...512(LLLVAR)";
        FieldDefine fd = new FieldDefine(define);
        assertTrue(fd.isHasA());
        assertTrue(fd.isHasN());
        assertFalse(fd.isCn());
        assertTrue(fd.isHasLowerB());
        assertTrue(fd.isHasS());
        assertFalse(fd.isHasUpperB());
        assertFalse(fd.isHasX());
        assertFalse(fd.isHasZ());
        assertEquals(3, fd.getFieldLengthSize());
        assertEquals(512, fd.getLength());
        assertFalse(fd.isFixed());
    }

    @Test
    public void testDefine3() {
        String define = "ans...512(LLLVAR)";
        FieldDefine fd = new FieldDefine(define);
        assertTrue(fd.isHasA());
        assertTrue(fd.isHasN());
        assertFalse(fd.isCn());
        assertFalse(fd.isHasLowerB());
        assertTrue(fd.isHasS());
        assertFalse(fd.isHasUpperB());
        assertFalse(fd.isHasX());
        assertFalse(fd.isHasZ());
        assertEquals(3, fd.getFieldLengthSize());
        assertEquals(512, fd.getLength());
        assertFalse(fd.isFixed());
    }

    @Test
    public void testDefine4() {
        String define = "an12";
        FieldDefine fd = new FieldDefine(define);
        assertTrue(fd.isHasA());
        assertTrue(fd.isHasN());
        assertFalse(fd.isCn());
        assertFalse(fd.isHasLowerB());
        assertFalse(fd.isHasS());
        assertFalse(fd.isHasUpperB());
        assertFalse(fd.isHasX());
        assertFalse(fd.isHasZ());
        assertEquals(0, fd.getFieldLengthSize());
        assertEquals(12, fd.getLength());
        assertTrue(fd.isFixed());
    }

    @Test
    public void testDefine5() {
        String define = "ans48";
        FieldDefine fd = new FieldDefine(define);
        assertTrue(fd.isHasA());
        assertTrue(fd.isHasN());
        assertFalse(fd.isCn());
        assertFalse(fd.isHasLowerB());
        assertTrue(fd.isHasS());
        assertFalse(fd.isHasUpperB());
        assertFalse(fd.isHasX());
        assertFalse(fd.isHasZ());
        assertEquals(0, fd.getFieldLengthSize());
        assertEquals(48, fd.getLength());
        assertTrue(fd.isFixed());
    }

    @Test
    public void testDefine6() {
        String define = "b64";
        FieldDefine fd = new FieldDefine(define);
        assertFalse(fd.isHasA());
        assertFalse(fd.isHasN());
        assertFalse(fd.isCn());
        assertTrue(fd.isHasLowerB());
        assertFalse(fd.isHasS());
        assertFalse(fd.isHasUpperB());
        assertFalse(fd.isHasX());
        assertFalse(fd.isHasZ());
        assertEquals(0, fd.getFieldLengthSize());
        assertEquals(8, fd.getLength());
        assertTrue(fd.isFixed());
    }

}
