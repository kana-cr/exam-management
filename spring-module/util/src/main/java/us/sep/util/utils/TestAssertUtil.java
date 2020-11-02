/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import org.junit.Assert;

/**
 * 测试用断言方法
 *
 * @author dango.yxm
 * @version : TestAssertUtil.java 2018/10/21 下午10:55 dango.yxm
 */
public class TestAssertUtil {

    /**
     * 测试case
     */
    public interface TestCase {
        void exce();
    }

    /**
     * 断言测试case会抛出指定异常
     *
     * @param excClass
     * @param testCase
     */
    public static void assertThrow(Class<? extends Throwable> excClass, TestCase testCase) {
        Throwable throwable = assertThrow(testCase);
        Assert.assertEquals(excClass, throwable.getClass());
    }


    /**
     * 断言测试case必然抛出异常
     *
     * @param testCase
     * @return
     */
    public static Throwable assertThrow(TestCase testCase) {
        Throwable throwable = null;
        try {
            testCase.exce();
        } catch (Exception e) {
            throwable = e;
        }
        AssertUtil.assertNotNull(throwable);
        return throwable;
    }
}
