/* 
 * Copyright 2021 Shang Yehua
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

import java.util.HashMap;
import java.util.Map;

import win.shangyh.cmnpro.smp8583.BodyField;
import win.shangyh.cmnpro.smp8583.BodyFieldType;
import win.shangyh.cmnpro.smp8583.exception.UnsupportedFieldException;
import static win.shangyh.cmnpro.smp8583.BodyFieldType.*;

/**
 *
 * 域构造工厂
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  17:04
 *
 */
public class BodyFieldFactory {

    private BodyFieldFactory() {
    }

    //不知疲倦的劳工们
    private final static BodyFieldWorker[] INDEFATIGABLE_STAFF;

    static {
        INDEFATIGABLE_STAFF = new BodyFieldWorker[128];
        employ();
    }

    public static BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx) {
        return INDEFATIGABLE_STAFF[bodyFieldIdx - 1].parseField(source, bodyOffset, bodyFieldIdx);
    }

    private static void employ() {
        Map<String, BodyFieldWorker> classRoom = new HashMap<>();

        //域1 不存在
        INDEFATIGABLE_STAFF[0] = FakeWorker.INSTANCE;
        
        //域2
        INDEFATIGABLE_STAFF[1] = trainVariableWorker(2, NUMBER, classRoom);

        //域3
        INDEFATIGABLE_STAFF[2] = trainFixedWorker(6, NUMBER, classRoom);
        
        //域4
        INDEFATIGABLE_STAFF[3] = trainFixedWorker(12, NUMBER, classRoom);
        
        //域5
        
    }

    private static BodyFieldWorker trainFixedWorker(int length, BodyFieldType type,
            Map<String, BodyFieldWorker> classRoom) {
        String key = "f" + length + type.toString();
        BodyFieldWorker worker = classRoom.get(key);
        if (worker == null) {
            worker = new FixedLengthFieldWorker(length, type);
            classRoom.put(key, worker);
        }
        return worker;
    }

    private static BodyFieldWorker trainVariableWorker(int length, BodyFieldType type,
            Map<String, BodyFieldWorker> classRoom) {
        String key = "v" + length + type.toString();
        BodyFieldWorker worker = classRoom.get(key);
        if (worker == null) {
            worker = new VariableLengthFieldWorker(length, type);
            classRoom.put(key, worker);
        }
        return worker;
    }

}
