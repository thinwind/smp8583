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
import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;

import win.shangyh.cmnpro.smp8583.BodyField;

/**
 *
 * 域构造工厂
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  17:04
 *
 */
public class BodyFieldFactory {

    /**
     * field-list.properties中域标识的字符的长度
     * 默认为f，长度为1
     */
    public static final int FIELD_PREFIX_LENGTH = 1;

    private BodyFieldFactory() {
    }

    //不知疲倦的劳工们
    private final static BodyFieldWorker[] INDEFATIGABLE_STAFF;

    static {
        INDEFATIGABLE_STAFF = new BodyFieldWorker[128];
        try {
            //招募打工人
            employ();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx) {
        return INDEFATIGABLE_STAFF[bodyFieldIdx - 1].parseField(source, bodyOffset, bodyFieldIdx);
    }

    //只在工厂建厂时招募一次
    //不用过于考虑效率和内存占用
    //初始化完成后只留有INDEFATIGABLE_STAFF
    //其它对象都被回收了
    private static void employ() throws IOException {
        Map<String, BodyFieldWorker> classRoom = new HashMap<>();
        try (InputStream fieldList = BodyFieldFactory.class.getResourceAsStream("/field-list.properties")) {
            Properties fieldListProp = new Properties();
            fieldListProp.load(fieldList);
            for (Object key : fieldListProp.keySet()) {
                String keystr = key.toString();
                int idx = Integer.parseInt(keystr.substring(FIELD_PREFIX_LENGTH));
                String value = fieldListProp.getProperty(keystr).trim();
                if (value.isEmpty()) {
                    //空岗位，加个冒名领工资的
                    INDEFATIGABLE_STAFF[idx-1] = FakeWorker.INSTANCE;
                } else {
                    FieldDefine define = new FieldDefine(value);
                    INDEFATIGABLE_STAFF[idx-1] = trainWorker(define, classRoom);
                }
            }
        }

    }

    //入职培训
    private static BodyFieldWorker trainWorker(FieldDefine define, Map<String, BodyFieldWorker> classRoom) {
        String key = define.cacheKey();
        BodyFieldWorker worker = classRoom.get(key);
        if (worker == null) {
            worker = define.newBodyFieldWorker();
            classRoom.put(key, worker);
        }
        return worker;
    }
    
    
}
