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
package com.github.thinwind.smp8583.factory;

import com.github.thinwind.smp8583.BodyField;
import com.github.thinwind.smp8583.exception.UnsupportedFieldException;

/**
 *
 * 不做任何实现的worker
 * 
 * 此类用于位置填充，避免在选择worker的时候进行null判断
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-03-12  10:32
 *
 */
public class FakeWorker implements BodyFieldWorker{
    
    public final static FakeWorker INSTANCE = new FakeWorker();

    @Override
    public BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx) {
        //Oops! You get the wrong guy, I just do nothing but complains.
        throw new UnsupportedFieldException(bodyFieldIdx);
    }

    @Override
    public BodyField createField(String ascii, int bodyFieldIdx) {
        //Oops! You get the wrong guy, I just do nothing but complains.
        throw new UnsupportedFieldException(bodyFieldIdx);
    }

    @Override
    public BodyField createField(byte[] data, int bodyFieldIdx) {
        //Oops! You get the wrong guy, I just do nothing but complains.
        throw new UnsupportedFieldException(bodyFieldIdx);
    }

    @Override
    public BodyField createField(String text, int bodyFieldIdx, boolean valueToEbcdic, BytesEncoder encoder) {
        //Oops! You get the wrong guy, I just do nothing but complains.
        throw new UnsupportedFieldException(bodyFieldIdx);
    }

    @Override
    public BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx, BytesDecoder decoder) {
        //Oops! You get the wrong guy, I just do nothing but complains.
        throw new UnsupportedFieldException(bodyFieldIdx);
    }
    
}
