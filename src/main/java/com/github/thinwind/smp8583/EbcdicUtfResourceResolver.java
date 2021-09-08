package com.github.thinwind.smp8583;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EbcdicUtfResourceResolver {

    //多字节时界定符
    //shiftout是起始符，shiftin是终止符
    // private final static String SHIFT_OUT = "0E";

    // private final static String SHIFT_IN = "0F";

    private final static byte ESCAPE_BEGIN = 0x0E;

    private final static byte ESCAPE_END = 0x0F;

    //转码找不到映射时默认填充字符
    private final static String DEFAULT_FILLED_CHAR = "?";

    private final static String DEFAULT_FILLED_CHAR_EBC = "6F";

    /**
     * key: ebcdic
     * val: gbk
     */
    private final static Map<String, String> EBC2UTF = new HashMap<>();

    /**
     * key: gbk
     * val: ebcdic escaped
     */
    private final static Map<String, String> UTF2EBC = new HashMap<>();

    static {
        loadMappingResource();
    }

    private static void loadMappingResource() {
        try (InputStream inputStream = EbcdicUtfResourceResolver.class.getResourceAsStream("/ebcdid_gbk.txt")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("//") || line.isEmpty()) {
                        continue;
                    }

                    int gbkstart = line.indexOf("<");
                    int gbkend = line.lastIndexOf(">");
                    String ebc = line.substring(0, gbkstart).trim().toUpperCase();
                    String utf = line.substring(gbkstart + 1, gbkend);
                    EBC2UTF.put(ebc, utf);
                    UTF2EBC.put(utf, ebc);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Load ebcdid_gbk.txt failed", e);
        }
    }

    /**
     * 将一个字符串转成ebcdic字节数组
     * 
     * 如果遇到没有映射的字符串，会转成ascii的?，然后再做映射
     * 
     * @param utfStr 要编码的字符串
     * @return ebcdic字节数组
     */
    public static byte[] utfToebcBytes(String utfStr) {
        char[] chars = utfStr.toCharArray();
        //最多一个字符转成四个字节
        byte[] result = new byte[chars.length * 4];
        int cursor = 0;
        boolean isDbcs = false;
        for (char c : chars) {
            String ebc = UTF2EBC.get(String.valueOf(c));
            if (ebc == null) {
                //没有映射的字符串，使用“?”代替
                ebc = DEFAULT_FILLED_CHAR_EBC;
            }

            if (isDbcs) {
                //前一次处理是多字节编码
                if (ebc.length() == 2) {
                    //此次是单字节
                    //多字节需要结束
                    result[cursor++] = ESCAPE_END;
                    result[cursor++] = (byte) (Integer.parseInt(ebc, 16) & 0xff);
                    isDbcs = false;
                } else {
                    //前一次处理是多字节编码，此次依然是多字节
                    //继续处理
                    for (int i = 0; i < ebc.length() - 1; i += 2) {
                        String hex = ebc.substring(i, i + 2);
                        result[cursor++] = (byte) (Integer.parseInt(hex, 16) & 0xff);
                    }
                }
            } else {
                //前一次处理是单字节编码
                if (ebc.length() == 2) {
                    //此次是单字节
                    //继续添加即可
                    result[cursor++] = (byte) (Integer.parseInt(ebc, 16) & 0xff);
                } else {
                    //前一次处理是单字节编码，此次是多字节
                    //添加多字节开始标识
                    result[cursor++] = ESCAPE_BEGIN;
                    for (int i = 0; i < ebc.length() - 1; i += 2) {
                        String hex = ebc.substring(i, i + 2);
                        result[cursor++] = (byte) (Integer.parseInt(hex, 16) & 0xff);
                    }
                    isDbcs = true;
                }
            }
        }
        if (isDbcs) {
            //如果最后一次处理是多字节，那么添加结束符
            result[cursor++] = ESCAPE_END;
        }
        if (cursor == result.length) {
            return result;
        } else {
            byte[] trimedResult = new byte[cursor];
            System.arraycopy(result, 0, trimedResult, 0, cursor);
            return trimedResult;
        }
    }

    /**
     * 将ebcdid字节数组转成字符串
     * 
     * 映射表中不存在的字符，将会默认使用?填充
     * 
     * @param bytes ebcdid字节数组
     * @return 对应的字符串
     */
    public static String ebcBytesToUtf(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        StringBuilder cache = null;
        boolean isDbcs = false;
        for (byte b : bytes) {
            if (isDbcs) {
                //双字节编码处理中
                if (ESCAPE_END == b) {
                    //双字节编码结束
                    isDbcs = false;
                    for (int i = 0; i < cache.length() - 3; i += 4) {
                        String utf = EBC2UTF.get(cache.substring(i, i + 4));
                        if (utf == null) {
                            builder.append(DEFAULT_FILLED_CHAR);
                        } else {
                            builder.append(utf);
                        }
                    }
                } else {
                    cache.append(BitUtil.byte2hex(b));
                }
            } else {
                if (ESCAPE_BEGIN == b) {
                    //开始处理双字节编码
                    isDbcs = true;
                    cache = new StringBuilder();
                } else {
                    //既不是双字节开始，又没有处于双字节处理中，只有单字节编码了
                    String utf = EBC2UTF.get(BitUtil.byte2hex(b));
                    if (utf == null) {
                        builder.append(DEFAULT_FILLED_CHAR);
                    } else {
                        builder.append(utf);
                    }
                }
            }
        }

        return builder.toString();
    }

}
