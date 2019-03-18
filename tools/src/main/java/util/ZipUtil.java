package util;

import android.util.Log;

import com.annimon.stream.Stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by chengkai on 19-1-17.
 */

public class ZipUtil {
    private static final String TAG = "ZipUtil";

    public static boolean isZip(File file) {
        try {
            Log.d(TAG, "isZip: " + file.getPath());
            return Stream.of(file)
                    .filter(FileUtil::isFile)
                    .filter(file1 -> file1.getName().endsWith(".zip"))
                    .count() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> listZip(File file) {
        List<String> list = new ArrayList();
        if (!isZip(file)) {
            return list;
        }
        try {
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                list.add(zipEntry.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean unZipFile(File file, String outPath, String fileName) {
        final String outFile = outPath + File.separator + fileName;
        boolean findFile = false;
        if (Stream.of("")
                .filter(v -> isZip(file))
                .filter(v -> FilePart.newPath(outFile).createFile())
                .count() == 0) {
            Log.e(TAG, "unZipFile: ");
            return false;
        }

        ZipInputStream inZip = null;
        FileOutputStream fileOutputStream = null;
        try {
            inZip = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry;

            while ((zipEntry = inZip.getNextEntry()) != null) {
                if (Stream.of(zipEntry.getName().split(File.separator))
                        .findLast()
                        .get().endsWith(fileName)) {
                    Log.d(TAG, "unZipFile: find " + zipEntry.getName());
                    findFile = true;
                    byte[] buf = new byte[1024];
                    fileOutputStream = new FileOutputStream(new File(outFile));
                    int ret = 0;
                    while ((ret = inZip.read(buf)) > 0) {
                        fileOutputStream.write(buf, 0, ret);
                    }
                    break;
                }
            }
            Log.d(TAG, "unZipFile: " + (findFile ? "" : "not") + " find " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inZip.closeEntry();
                inZip.close();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return findFile;
    }
}
