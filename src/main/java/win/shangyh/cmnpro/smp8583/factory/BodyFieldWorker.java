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

import win.shangyh.cmnpro.smp8583.BodyField;

/**
 *
 * TODO BodyFieldTemplate说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  22:19
 *
 */
public interface BodyFieldWorker {

    public BodyField createField(byte[] source, int bodyOffset, int bodyFieldIdx);

}
