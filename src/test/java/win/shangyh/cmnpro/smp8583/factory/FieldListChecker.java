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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * TODO FieldListChecker说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-03-12  18:11
 *
 */
public class FieldListChecker {

    public static void main(String[] args) throws IOException {
        try (InputStream fieldList = FieldListChecker.class.getResourceAsStream("/field-list.properties")) {
            Properties fieldListProp = new Properties();
            fieldListProp.load(fieldList);
            List<Integer> fieldIdxList = new ArrayList<>();
            
            for (Object key : fieldListProp.keySet()) {
                String keystr = key.toString();
                int idx = Integer.parseInt(keystr.substring(1));
                fieldIdxList.add(idx);
                String value = fieldListProp.getProperty(keystr).trim();
                System.out.println(value+":"+value.length());
            }
        }

    }
}
