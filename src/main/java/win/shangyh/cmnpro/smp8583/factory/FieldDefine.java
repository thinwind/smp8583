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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import win.shangyh.cmnpro.smp8583.BodyFieldType;
import win.shangyh.cmnpro.smp8583.exception.UnsupportedFieldException;

/**
 *
 * 域定义 
 * 仅用于BodyFieldFactory构建
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-03-14  13:58
 *
 */
@Getter
public class FieldDefine {

    private final String defineStr;

    //a 字母字符，A至Z，a至z
    private final boolean hasA;

    //s/S 特殊符号
    private final boolean hasS;

    //b 数据的二进制表示，后跟数字表示位(bit)的个数
    private final boolean hasLowerB;

    //B 用于表示变长的二进制数，后跟数字表示二进制数据所占字节(Byte)的个数
    private final boolean hasUpperB;

    //n 数值，0至9，右靠，首位有效数字前填零
    private final boolean hasN;

    //cn 压缩数字码, 即BCD码
    private final boolean isCn;

    //X 借贷符号，贷记为“C”，借记为“D”，并且总是与一个数字型金额数据元相连
    private final boolean hasX;

    //Z/z ISO 4909和ISO 7813中定义的磁卡第二、三磁道的代码集，但不包含起始符、结束符和LRC符
    private final boolean hasZ;

    //是否定长的域
    private final boolean isFixed;

    //定长域的长度，或者变成域的最大长度
    private final int length;

    //变长域的域长度的字节数
    private final int fieldLengthSize;

    //变长域的模式，比如 ansb...512(LLLVAR)
    //匹配模式为:
    //---------------------------------------------------------------
    //|   ansb   |    ...    |   512    |  (  |    LLL    | VAR | ) |
    //---------------------------------------------------------------
    //|  [^\\.]+ |    \\.+   |   \\d+   | \\( |     L+    | VAR | ) |
    //---------------------------------------------------------------
    //| group(1) |  group(2) | group(3) |     |  group(4) |     |   |
    //---------------------------------------------------------------
    private final Pattern variableFieldPattern = Pattern.compile("([^\\.]+)(\\.+)(\\d+)\\((L+)VAR\\)");

    //定长的域模式，比如 an12
    //匹配模式为:
    //-------------------------
    //|     ans   |     12    |
    //-------------------------
    //|  [^\\d]+  |    \\d+   |
    //-------------------------
    //|  group(1) |  group(2) |
    //-------------------------
    private final Pattern fixedFieldPattern = Pattern.compile("([^\\d]+)(\\d+)");

    FieldDefine(String defineStr) {
        this.defineStr = defineStr;
        Matcher matcher = variableFieldPattern.matcher(defineStr);
        if (matcher.matches()) {
            isFixed = false;
            int dotCnt = matcher.group(2).length();
            int dotCnt2 = matcher.group(4).length();
            int dotCnt3 = matcher.group(3).length();
            if (!((dotCnt == dotCnt2) && (dotCnt == dotCnt3))) {
                throw new RuntimeException("The define(" + defineStr + ") length not matched!");
            }
            fieldLengthSize = dotCnt;
            length = Integer.parseInt(matcher.group(3));
        } else {
            matcher = fixedFieldPattern.matcher(defineStr);
            if (!matcher.matches()) {
                throw new UnsupportedFieldException(defineStr);
            }
            isFixed = true;
            fieldLengthSize = 0;
            length = Integer.parseInt(matcher.group(2));
        }

        String type = matcher.group(1);
        isCn = type.contains("cn");
        hasA = type.contains("a");
        hasS = type.contains("s") || type.contains("S");
        hasLowerB = type.contains("b");
        hasUpperB = type.contains("B");
        hasN = !isCn && type.contains("n");
        hasX = type.contains("X");
        hasZ = type.contains("Z") || type.contains("z");
    }

    BodyFieldType getFieldType() {
        if (hasA || hasS || hasX) {
            if (hasLowerB || hasZ) {
                return BodyFieldType.BYTES;
            } else {
                return BodyFieldType.CHARACTOR;
            }
        }
        if (hasLowerB) {
            return BodyFieldType.BINARY;
        }
        if (hasUpperB) {
            return BodyFieldType.BYTES;
        }
        if (hasN) {
            return BodyFieldType.NUMBER;
        }
        throw new UnsupportedFieldException(defineStr);
    }

    BodyFieldWorker newBodyFieldWorker() {
        if (isFixed) {
            return new FixedLengthFieldWorker(length, getFieldType());
        } else {
            return new VariableLengthFieldWorker(length, getFieldType());
        }
    }

    //just for init
    //do not use for other purposes
    String cacheKey() {
        if (isFixed) {
            return "f" + length + getFieldType().toString();
        } else {
            return "v" + length + getFieldType().toString();
        }
    }

}
