/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 集合工具
 *
 * @author dango.yxm
 * @version : CollectionUtils.java 2018/11/16 下午10:08 dango.yxm
 */
public class CollectionUtils {

    /**
     * 转换stream 如果为空 返回空流 避免npe
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> Stream<T> toStream(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return Stream.empty();
        }
        return collection.stream();
    }

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 不同的两个集合 按一定方法去重
     * <br/> collection 通过getR 去除 remove
     *
     * @param collection
     * @param remove
     * @param getR
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> void removeDuplicate(Collection<T> collection, Collection<R> remove, Function<T, R> getR) {
        if (!isEmpty(remove)) {
            Iterator<T> iterator = collection.iterator();
            while (iterator.hasNext()) {
                T t = iterator.next();
                for (R r : remove) {
                    if (r.equals(getR.apply(t))) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    /**
     * 列表保留长度 还是原数组
     *
     * @param list   result
     * @param length
     * @param <T>
     * @return
     */
    public static <T> List<T> subList(List<T> list, int length) {
        List<T> result = (List<T>) copy(list);
        if (result.size() <= length) {
            return result;
        }
        return result.subList(0, length);
    }


    /**
     * 列表保留长度 删去尾部
     *
     * @param list   result
     * @param length
     * @param <T>
     * @return
     */
    public static <T> List<T> subSuffixList(List<T> list, int length) {
        List<T> result = (List<T>) copy(list);
        if (result.size() <= length) {
            return Collections.emptyList();
        }
        return result.subList(length, result.size());
    }

    /**
     * 集合深度克隆, 不建议直接使用
     *
     * @param collection
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> copy(Collection<T> collection) {
        Collection<T> result;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(collection);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            result = (Collection<T>) in.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("集合深度克隆失败");
        }
        return result;
    }

    /**
     * List去重
     *
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
