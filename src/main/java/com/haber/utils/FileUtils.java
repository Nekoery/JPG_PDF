package com.haber.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static String getPath(String path,String format){
        String temp = new String();
        if(path == null)
            return "";
        File file = new File(path);
        if(file.isDirectory()){
            File [] files = file.listFiles();
            for (File eachFile:
                    files) {
                temp += getPath(eachFile.getPath(),format);
            }
        }else if(file.isFile()&&file.getName().endsWith(format)){
            temp = file.getPath()+";";
        }
        return temp;
    }
    public static List<String> getPathList(String path,List<String> formatList){
        List<String> allList = new ArrayList<String>();
        File file = new File(path);
        if(file.isDirectory()){
            for(String underPath
            :file.list()){
                allList.addAll(getPathList(path + "\\" + underPath,formatList));
            }
        }else {
            for(String format:
            formatList){
                if(file.getName().endsWith(format) && file.length() != 0){
                    allList.add(file.getAbsolutePath());
                }
            }

        }
        return allList;
    }
    public static List<String> getFormatList(){
        List<String> formatList = new ArrayList<String>();
        formatList.add(".png");
        formatList.add(".jpg");
        formatList.add(".jpeg");
        return formatList;
    }
}
