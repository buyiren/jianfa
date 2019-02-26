/**
 * Copyright 1993-2006 Agree Tech. All rights reserved.
 */
package tc.View.Agent.utils;


import java.math.BigDecimal;

/**
 * 四则运算
 * 
 * @author PuYun &lt;pu.yun@agree.com.cn&gt;
 * @version $Revision: 1.6 $ $Date: 2007/06/06 11:35:00 $
 */
public class MathUtil
{

    /**
     * 加法
     * 
     * @param decimalLiteral1
     *        被加数
     * @param decimalLiteral2
     *        加数
     * @return decimalLiteral1＋decimalLiteral2
     */
    public static String addBigDecimal(String decimalLiteral1,
            String decimalLiteral2)
    {
        return getDecimal(decimalLiteral1).add(getDecimal(decimalLiteral2))
                .toString();
    }

    /**
     * 比较两个数字的大小，相等返回0，前者大于后者返回1，若小于返回-1
     * 
     * @param decimalLiteral1
     *        String
     * @param decimalLiteral2
     *        String
     * @return int
     */
    public static int compare(String decimalLiteral1, String decimalLiteral2)
    {
        return getDecimal(decimalLiteral1).compareTo(
                getDecimal(decimalLiteral2));
    }

    /**
     * 除法. 小数点位数缺省2位
     * 
     * @param decimalLiteral1
     *        被除数
     * @param decimalLiteral2
     *        除数
     * @return decimalLiteral1/decimalLiteral2
     */
    public static String divideBigDecimal(String decimalLiteral1,
            String decimalLiteral2)
    {
        return divideBigDecimal(decimalLiteral1, decimalLiteral2, 2);
    }

    /**
     * 除法
     * 
     * @param decimalLiteral1
     *        被除数
     * @param decimalLiteral2
     *        除数
     * @param 小数点位数
     * @return decimalLiteral1/decimalLiteral2
     */
    public static String divideBigDecimal(String decimalLiteral1,
            String decimalLiteral2, int 小数点位数)
    {
        return getDecimal(decimalLiteral1).divide(getDecimal(decimalLiteral2),
                小数点位数, BigDecimal.ROUND_HALF_UP).toString();
    }

    private static BigDecimal getDecimal(String literal)
    {
        if (literal == null || literal.length() == 0)
        {
            return new BigDecimal("0");//改成"0"，为了兼容Java 1.4
        }
        if (literal.charAt(0) == '.')
        {
            return new BigDecimal("0" + literal);
        }
        return new BigDecimal(literal);
    }

    /**
     * 乘法, 保留小数点为两个参数保留的位数相加
     * 
     * @param decimalLiteral1
     *        被乘数
     * @param decimalLiteral2
     *        乘数
     * @return decimalLiteral1×decimalLiteral2
     */
    public static String multiplyBigDecimal(String decimalLiteral1,
            String decimalLiteral2)
    {
        return getDecimal(decimalLiteral1)
                .multiply(getDecimal(decimalLiteral2)).toString();
    }

    /**
     * 乘法, 保留小数点为两个参数保留的位数相加
     * 
     * @param decimalLiteral1
     *        被乘数
     * @param decimalLiteral2
     *        乘数
     * @param 小数位数
     * @return decimalLiteral1×decimalLiteral2
     */
    public static String multiplyBigDecimal(String decimalLiteral1,
            String decimalLiteral2, int 小数位数)
    {
        BigDecimal bd = getDecimal(decimalLiteral1).multiply(
                getDecimal(decimalLiteral2));
        bd = bd.setScale(小数位数, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static String setScale(String decimalLiteral, int 小数位数)
    {
        BigDecimal d = getDecimal(decimalLiteral);
        d = d.setScale(小数位数, BigDecimal.ROUND_HALF_UP);
        return d.toString();
    }

    /**
     * 减法
     * 
     * @param decimalLiteral1
     *        被减数
     * @param decimalLiteral2
     *        减数
     * @return decimalLiteral1- decimalLiteral2
     */
    public static String subtractBigDecimal(String decimalLiteral1,
            String decimalLiteral2)
    {
        return getDecimal(decimalLiteral1)
                .subtract(getDecimal(decimalLiteral2)).toString();
    }

}
