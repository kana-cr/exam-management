package us.sep.util.utils;



import org.apache.poi.hssf.usermodel.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * @author zjb
 * @version : ExcelUtil.java 2019/8/1 14:50 zjb
 */
public class ExcelUtil {


    /**
     * 临时文件路径
     */
    private static final String TEMP_FILE_PATH = "./file/temp/";



    public static <T> HSSFWorkbook createExcel(List<T> list) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultColumnWidth(20);
        for (int i =0;i<list.size();i++){
            //利用反射机制获取所有成员字段
            Class<?> itemClass = list.get(i).getClass();
            Field[] fields = itemClass.getDeclaredFields();
            HSSFCell cell = null;
            HSSFRow row;

            //表头
            if(i==0) {
                row = wb.getSheetAt(0).createRow((int) 0);
                for (int j = 0; j < fields.length; j++) {
                    boolean flag = fields[j].isAccessible();
                    fields[j].setAccessible(true);    //设置该成员字段可访问
                    cell = row.createCell((short) j);
                    cell.setCellValue(fields[j].getName());
                    fields[j].setAccessible(flag);
                }
            }

            //数据
            row = wb.getSheetAt(0).createRow((int) i+1);
            for (int j =0;j<fields.length;j++){
                try {
                    boolean flag = fields[j].isAccessible();
                    fields[j].setAccessible(true);
                    cell = row.createCell((short) j);
                    cell.setCellValue(fields[j].get(list.get(i))==null?"":fields[j].get(list.get(i)).toString());
                    fields[j].setAccessible(flag);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }



    /**
     * 将List对象转存为Excel临时文件
     * @param list
     * @param filePath
     * @param <T>
     * @return
     */
    public static <T> String list2ExcelFile(List<T> list,String filePath,String fileName){

        HSSFWorkbook wb =createExcel(list);
        String nowTime = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS");


        String path = "";
        if(!fileName.endsWith(".xls")) fileName+=".xls";
        if (filePath == null || filePath == ""){
            path = TEMP_FILE_PATH + nowTime + fileName;
        }else{
            if(!filePath.endsWith("/")) filePath+="/";
            path = filePath + nowTime + fileName;
        }


        try{
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(path);
            } catch (FileNotFoundException e) {

                File newFile = new File(path);
                if (!newFile.getParentFile().exists()) {
                    try {
                        newFile.getParentFile().mkdirs();
                        fout = new FileOutputStream(path);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
            wb.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;

    }

}
