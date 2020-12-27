package com.xli.study.olexec.execute;

import java.util.HashMap;
import java.util.Map;

/**
 * 介绍完会用到的基础知识，接下来就是本篇的重头戏：实现字节码修改器。通过之前的说明，我们可以通过以下流程完成我们的字节码修改器：
 * <p>
 * 取出常量池中的常量的个数 cpc；
 * 遍历常量池中 cpc 个常量，检查 tag = 1 的 CONSTANT_Utf8_info 常量；
 * 找到存储的常量值为 java/lang/System 的常量，把它替换为 org/olexec/execute/HackSystem；
 * 因为只可能有一个值为 java/lang/System 的 CONSTANT_Utf8_info 常量，所以找到之后可以立即返回修改后的字节码。
 */
public class ClassModifier {


    /**
     * 待修改的字节码
     */
    private byte[] bytes;

    /**
     * 1个和2个字节的符号数，用来在classByte数组中取tag和len
     * tag用u1个字节表示
     * len用u2个字节表示
     */
    private static final int u1 = 1;
    private static final int u2 = 2;
    private static final int u4 = 4;

    /**
     * Class文件中常量池的起始偏移
     */
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;


    /**
     * 11种常量池常量
     */
    /**
     * CONSTANT_Utf8_info {
     * u1 tag;
     * u2 length;
     * u1 bytes[length];
     * }
     */
    private static final int CONSTANT_Utf8_info = 1;
    /**
     * CONSTANT_Integer_info {
     * u1 tag;
     * u4 bytes;
     * }
     */
    private static final int CONSTANT_Integer_info = 3;
    /**
     * CONSTANT_Float_info {
     * u1 tag;
     * u4 bytes;
     * }
     */
    private static final int CONSTANT_Float_info = 4;


    /**
     * CONSTANT_Long_info {
     * u1 tag;
     * u4 high_bytes;
     * u4 low_bytes;
     * }
     */
    private static final int CONSTANT_Long_info = 5;
    /**
     * CONSTANT_Double_info {
     * u1 tag;
     * u4 high_bytes;
     * u4 low_bytes;
     * }
     */
    private static final int CONSTANT_Double_info = 6;
    /**
     * 长度 u1+u2
     * CONSTANT_Class_info {
     * u1 tag;
     * u2 name_index;
     * }
     */
    private static final int CONSTANT_Class_info = 7;
    /**
     * CONSTANT_String_info {
     * u1 tag;
     * u2 string_index;
     * }
     */
    private static final int CONSTANT_String_info = 8;
    /**
     * 长度 u1+u2+u2
     * CONSTANT_Fieldref_info {
     * u1 tag;
     * u2 class_index;
     * u2 name_and_type_index;
     * }
     */
    private static final int CONSTANT_Fieldref_info = 9;
    /**
     * 长度 u1+u2+u2
     * CONSTANT_Methodref_info {
     * u1 tag;
     * u2 class_index;
     * u2 name_and_type_index;
     * }
     */
    private static final int CONSTANT_Methodref_info = 10;
    /**
     * 长度 u1+ u2 + u2
     * CONSTANT_InterfaceMethodref_info {
     * u1 tag;
     * u2 class_index;
     * u2 name_and_type_index;
     * }
     */
    private static final int CONSTANT_InterfaceMethodref_info = 11;
    /**
     * CONSTANT_NameAndType_info {
     * u1 tag;
     * u2 name_index;
     * u2 descriptor_index;
     * }
     */
    private static final int CONSTANT_NameAndType_info = 12;
    /**
     * CONSTANT_MethodHandle_info {
     * u1 tag;
     * u1 reference_kind;
     * u2 reference_index;
     * }
     */
    private static final int CONSTANT_MethodHandle_info = 15;
    /**
     * CONSTANT_MethodType_info {
     * u1 tag;
     * u2 descriptor_index;
     * }
     */
    private static final int CONSTANT_MethodType_info = 16;
    /**
     * CONSTANT_InvokeDynamic_info {
     * u1 tag;
     * u2 bootstrap_method_attr_index;
     * u2 name_and_type_index;
     * }
     */
    private static final int CONSTANT_InvokeDynamic_info = 18;


    private static final Map<Integer, Integer> CONSTANT_ITEM_LENGTH_MAP = new HashMap<>();

    static {
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Utf8_info, u1 + u2 + u1);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Integer_info, u1 + u4);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Float_info, u1 + u4);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Long_info, u1 + u4 + u4);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Double_info, u1 + u4 + u4);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Class_info, u1 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_String_info, u1 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Fieldref_info, u1 + u2 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_Methodref_info, u1 + u2 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_InterfaceMethodref_info, u1 + u2 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_NameAndType_info, u1 + u2 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_MethodHandle_info, u1 + u2 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_MethodType_info, u1 + u2);
        CONSTANT_ITEM_LENGTH_MAP.put(CONSTANT_InvokeDynamic_info, u1 + u2 + u2);
    }

    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};


    public ClassModifier(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 从0x00000008开始向后取2个字节，表示的是常量池中常量的个数
     *
     * @return 常量池中常量的个数
     */
    public int getConstantPoolCount() {
        return ByteUtils.byte2Int(bytes, CONSTANT_POOL_COUNT_INDEX, u2);
    }


    public byte[] modifyConstantUtf8Info(String src, String tar) {
        int constantPoolCount = getConstantPoolCount();
        // constant_pool_count 索引从1开始 容量为 [constant_pool_count -1]
        // https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1
        // 常量池内容 在第10个字节开始
        int offset = CONSTANT_POOL_COUNT_INDEX + u2;
        for (int i = 1; i < constantPoolCount; i++) {
            int tag = ByteUtils.byte2Int(bytes, offset, u1);

            if (tag == CONSTANT_Utf8_info) {
                //CONSTANT_Utf8_info {
                //    u1 tag;
                //    u2 length;
                //    u1 bytes[length];
                //}
                int length = ByteUtils.byte2Int(bytes, offset + u1, u2);
                offset += u1 + u2;
                String constantUtf8InfoStr = ByteUtils.byte2String(bytes, offset, length);
                if (constantUtf8InfoStr.equals(src)) {
                    // 替換 CONSTANT_Utf8_info bytes[length],CONSTANT_Utf8_info length
                    byte[] tarStringByte = ByteUtils.string2Byte(tar);
                    byte[] tarLength = ByteUtils.int2Byte(tarStringByte.length, u2);
                    bytes = ByteUtils.replace(bytes, offset - u2, u2, tarLength);
                    bytes = ByteUtils.replace(bytes, offset, length, tarStringByte);
                    return bytes;
                }else {
                    offset +=length;
                }
            } else {
//                offset += CONSTANT_ITEM_LENGTH_MAP.get(tag);
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }
        return bytes;
    }
}
