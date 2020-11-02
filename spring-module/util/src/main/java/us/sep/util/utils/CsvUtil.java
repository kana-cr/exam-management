/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV文件读取
 *
 * @author MessiahJK
 * @version : CsvUtil.java 2018/11/28 20:24 MessiahJK
 */
public class CsvUtil {
    /**
     * 分隔符
     */
    private final static char DELIMITER = ',';

    /**
     * 字符集
     */
    private final static String CHARSET = "UTF-8";

    /**
     * 源文件是否包含header
     */
    private final static boolean HAVE_HEADER = false;


    /**
     * 将csv文件转为二维数组（含Header）
     *
     * @param filePath  路径
     * @param delimiter 分隔符
     * @param charset   字符集
     * @return
     */
    public static String[][] getWithHeader(String filePath, char delimiter, String charset) {
        try {
            CsvReader csvReader = new CsvReader(filePath, delimiter, Charset.forName(charset));
            csvReader.readHeaders();
            List<String[]> resultList = new ArrayList<>();
            resultList.add(csvReader.getHeaders());
            while (csvReader.readRecord()) {
                String[] row = csvReader.getValues();
                resultList.add(row);
            }
            int rowCount = resultList.size();
            String[][] result = new String[rowCount][csvReader.getValues().length];
            int i = 0;
            for (String[] row : resultList) {
                result[i] = row;
                i++;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将csv文件转为二维数组（含Header）
     *
     * @param filePath  路径
     * @param delimiter 分隔符
     * @return
     */
    public static String[][] getWithHeader(String filePath, char delimiter) {
        return getWithHeader(filePath, delimiter, CHARSET);
    }

    /**
     * 将csv文件转为二维数组（含Header）
     *
     * @param filePath 路径
     * @param charset  字符集
     * @return
     */
    public static String[][] getWithHeader(String filePath, String charset) {
        return getWithHeader(filePath, DELIMITER, charset);
    }

    /**
     * 将csv文件转为二维数组（含Header）
     *
     * @param filePath 路径
     * @return
     */
    public static String[][] getWithHeader(String filePath) {
        return getWithHeader(filePath, DELIMITER, CHARSET);
    }

    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath   路径
     * @param delimiter  分隔符
     * @param charset    字符集
     * @param haveHeader 源文件是否含Header
     * @return
     */
    public static String[][] getWithoutHeader(String filePath, char delimiter, String charset, boolean haveHeader) {
        try {
            CsvReader csvReader = new CsvReader(filePath, delimiter, Charset.forName(charset));
            List<String[]> resultList = new ArrayList<>();
            if (haveHeader) {
                csvReader.readHeaders();
            }
            while (csvReader.readRecord()) {
                String[] row = csvReader.getValues();
                resultList.add(row);
            }
            int rowCount = resultList.size();
            String[][] result = new String[rowCount][csvReader.getValues().length];
            int i = 0;
            for (String[] row : resultList) {
                result[i] = row;
                i++;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath  路径
     * @param delimiter 分隔符
     * @param charset   字符集
     * @return
     */
    public static String[][] getWithoutHeader(String filePath, char delimiter, String charset) {
        return getWithoutHeader(filePath, delimiter, charset, HAVE_HEADER);
    }


    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath   路径
     * @param delimiter  分隔符
     * @param haveHeader 源文件是否含Header
     * @return
     */
    public static String[][] getWithoutHeader(String filePath, char delimiter, boolean haveHeader) {
        return getWithoutHeader(filePath, delimiter, CHARSET, haveHeader);
    }

    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath   路径
     * @param charset    字符集
     * @param haveHeader 源文件是否含Header
     * @return
     */
    public static String[][] getWithoutHeader(String filePath, String charset, boolean haveHeader) {
        return getWithoutHeader(filePath, DELIMITER, charset, haveHeader);
    }

    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath  路径
     * @param delimiter 分隔符
     * @return
     */
    public static String[][] getWithoutHeader(String filePath, char delimiter) {
        return getWithoutHeader(filePath, delimiter, CHARSET, HAVE_HEADER);
    }

    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath   路径
     * @param haveHeader 源文件是否含Header
     * @return
     */
    public static String[][] getWithoutHeader(String filePath, boolean haveHeader) {
        return getWithoutHeader(filePath, DELIMITER, CHARSET, haveHeader);
    }

    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath 路径
     * @param charset  字符集
     * @return
     */
    public static String[][] getWithoutHeader(String filePath, String charset) {
        return getWithoutHeader(filePath, DELIMITER, charset, HAVE_HEADER);
    }

    /**
     * 将csv文件转为二维数组（不含Header）
     *
     * @param filePath 路径
     * @return
     */
    public static String[][] getWithoutHeader(String filePath) {
        return getWithoutHeader(filePath, DELIMITER, CHARSET, HAVE_HEADER);
    }

}
