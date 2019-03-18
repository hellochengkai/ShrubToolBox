package util;

import android.util.Log;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chengkai on 19-1-17.
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    private static boolean canRead(File file)
    {
        if(CheckNull.isNull(file)){
            return false;
        }
        boolean ret = file.canRead();
        if(!ret){
            Log.w(TAG, "canRead: " + file.getName() + " is false");
        }
        return ret;
    }

    /**
     * 通过 Consumer 遍历所有的文件，及文件夹下的文件
     * @param file
     * @param action
     */
    private static void forEachFile(File file,final Consumer<File> action) {
        try {
            if(isDir(file)){
                Stream.of(Arrays.asList(file.listFiles()))
                        .filter(FileUtil::canRead)
                        .forEach(file1 -> FileUtil.forEachFile(file1,action));
            } else if(isFile(file)){
                action.accept(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查file是不是存在且可读
     * @param file
     * @return
     */
    public static boolean exists(File file) {
        try {
            return Stream.of(file)
                    .filter(CheckNull::notNull)
                    .filter(File::exists)
                    .filter(File::canRead)
                    .count() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 检查file是不是不存在
     * @param file
     * @return
     */
    public static boolean notExists(File file)
    {
        return Stream.of(file)
                .filter(CheckNull::notNull)
                .filter(f -> !f.exists())
                .count() == 1;
    }


    /**
     * 检查file是不是可读的普通文件
     * @param file
     * @return
     */
    public static boolean isFile(File file) {
        try {
            return Stream.of(file)
                    .filter(FileUtil::exists)
                    .filter(File::isFile)
                    .count() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查file是不是可读的普通目录
     * @param dir
     * @return
     */
    public static boolean isDir(File dir) {
        try {
            return Stream.of(dir)
                    .filter(FileUtil::exists)
                    .filter(File::isDirectory)
                    .count() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean newDir(File dir) {
        try {
            return Stream.of(dir)
                    .filter(FileUtil::notExists)
                    .filter(File::mkdir)
                    .filter(FileUtil::isDir)
                    .count() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createNewFile(File f)
    {
        try {
            return f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建文件（已经存在的话返回false）
     * @param file
     * @return
     */
    public static boolean newFile(File file) {
        try {
            return Stream.of(file)
                    .filter(FileUtil::notExists)
                    .filter(FileUtil::createNewFile)
                    .filter(FileUtil::isFile)
                    .count() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除文件
     * 是文件的话就直接删除，是目录的话递归删除
     * @param file
     * @return
     */
    public static boolean del(File file)
    {
        forEachFile(file,file1 -> file1.delete());
        return file.exists();
    }

    /**
     * 从文本文件中一行一行的读取string
     * @param file
     * @return
     */
    public static List<String> readLines(File file)
    {
        List<String> list = new ArrayList<>();
        if(!isFile(file))
            return list;
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null){
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 直接从文本文件中读取string
     * @param file
     * @return
     */
    public static String readStr(File file)
    {
        StringBuilder builder = new StringBuilder();
        List<String> strings = readLines(file);
        Stream.of(strings).forEach(s -> builder.append(s));
        return builder.toString();
    }
}
