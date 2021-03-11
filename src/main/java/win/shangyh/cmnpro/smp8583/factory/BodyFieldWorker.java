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

    /**
     * 从报文中解析域
     * 
     * @param source 报文
     * @param bodyOffset 解析起始位置
     * @param bodyFieldIdx 域序号 (按照8583规范，从1开始计数)
     * @return 解析后的域对象
     */
    BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx);

    /**
     * 从ascii码表示的值创建一个域
     * 注意：值可能是原始值，需要进行补全
     * 按照8583规范，对于固定长度的报文域有以下填充规则:
     *     1. 如果是数字域，右靠齐，左边多余位填零
     *     2. 如果不是数字域，左靠齐，右边多余位填空格
     * 
     * @param ascii 原始域值
     * @param bodyFieldIdx 域序号 (按照8583规范，从1开始计数)
     * @return 生成的符合规范的域
     */
    BodyField createField(String ascii,int bodyFieldIdx);
}
