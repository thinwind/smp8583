/*
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package win.shangyh.cmnpro.smp8583;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import win.shangyh.cmnpro.smp8583.factory.BodyFieldFactory;

/**
 *
 * TODO BodyFieldTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-23  11:30
 *
 */
public class BodyFieldTest {

    @Test
    public void whenSortedThenInOrderByLocation() {
        List<BodyField> fields = new ArrayList<>();
        fields.add(BodyFieldFactory.createBodyField("123", 16));
        fields.add(BodyFieldFactory.createBodyField("1234578910", 2));
        fields.add(BodyFieldFactory.createBodyField("test", 102));
        Collections.sort(fields);
        assertEquals(2, fields.get(0).getLocation());
        assertEquals(16, fields.get(1).getLocation());
        assertEquals(102, fields.get(2).getLocation());
    }
}
