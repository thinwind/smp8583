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
package win.shangyh.cmnpro.smp8583;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import win.shangyh.cmnpro.smp8583.exception.IllegalLengthException;
import win.shangyh.cmnpro.smp8583.exception.UnsupportedFieldOprationException;

/**
 *
 * TODO BodyFieldTypeTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-02  14:49
 *
 */
public class BodyFieldTypeTest {

    @Test
    public void whenNumberThenPaddingLeftWithZeros() {
        String target = "001234";
        byte[] nums=new byte[4];
        nums[0] = (byte)'1';
        nums[1] = (byte)'2';
        nums[2] = (byte)'3';
        nums[3] = (byte)'4';
        
        assertArrayEquals(BitUtil.toByteArray(target), BodyFieldType.NUMBER.normalize(nums,6));
    }
    
    @Test
    public void whenNumberEqualsThenReturn(){
        byte[] nums=new byte[4];
        nums[0] = (byte)'1';
        nums[1] = (byte)'2';
        nums[2] = (byte)'3';
        nums[3] = (byte)'4';
        
        assertTrue(nums == BodyFieldType.NUMBER.normalize(nums,4));
    }
    
    @Test(expected = IllegalLengthException.class)
    public void whenNumberLargerThanExpectedThenExcept(){
        byte[] nums=new byte[4];
        nums[0] = (byte)'1';
        nums[1] = (byte)'2';
        nums[2] = (byte)'3';
        nums[3] = (byte)'4';
        
        assertTrue(nums == BodyFieldType.NUMBER.normalize(nums,3));
    }
    
    @Test(expected = UnsupportedFieldOprationException.class)
    public void whenBinaryThenCannotNormalize(){
        byte[] nums=new byte[4];
        nums[0] = (byte)'1';
        nums[1] = (byte)'2';
        nums[2] = (byte)'3';
        nums[3] = (byte)'4';
        
        BodyFieldType.BINARY.normalize(nums,4);
    }
    
    @Test
    public void whenCharAndLessCharThenPaddingRightWithBlank(){
        String target = "abcd    ";
        byte[] chars=new byte[4];
        chars[0] = 'a';
        chars[1] = 'b';
        chars[2] = 'c';
        chars[3] = 'd';
        
        assertArrayEquals(BitUtil.toByteArray(target), BodyFieldType.CHARACTOR.normalize(chars,8));
    }
    
    @Test(expected = IllegalLengthException.class)
    public void whenCharAndLargerLengthThenExcept(){
        String target = "abc";
        byte[] chars=new byte[4];
        chars[0] = 'a';
        chars[1] = 'b';
        chars[2] = 'c';
        chars[3] = 'd';
        
        assertArrayEquals(BitUtil.toByteArray(target), BodyFieldType.CHARACTOR.normalize(chars,3));
    }
    
    @Test
    public void whenCharAndEqualsLengthThenReturn(){
        byte[] chars=new byte[4];
        chars[0] = 'a';
        chars[1] = 'b';
        chars[2] = 'c';
        chars[3] = 'd';
        
        assertTrue(chars==BodyFieldType.CHARACTOR.normalize(chars,4));
    }
}
