package util;

import com.annimon.stream.Stream;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by chengkai on 19-1-17.
 */

public class FilePart {
    private static final String TAG = "FilePart";

    private String name;
    private FilePart upPath;

    private FilePart(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if(upPath == null)
            return File.separator + name;
        return upPath.getFile() + File.separator + name;
    }

    private void setUpPath(FilePart upPath) {
        this.upPath = upPath;
    }

    public boolean createFile()
    {
        if(!FileUtil.isDir(upPath.getFile())){
            if(!upPath.createDir()){
                return false;
            }
        }
        return FileUtil.newFile(getFile());
    }

    public boolean createDir()
    {
        if(upPath == null){
            return FileUtil.newDir(getFile());
        }
        if(!FileUtil.isDir(upPath.getFile())){
            if(!upPath.createDir()){
                return false;
            }
        }
        return FileUtil.newDir(getFile());
    }
    private File getFile()
    {
        return new File(this.toString());
//        if(upPath == null)
//            return new File(File.separator + name);
//        return new File(upPath.getFile() + File.separator + name);
    }


    public static FilePart newPath(String path)
    {
        List<FilePart> list = Stream.of(path.split(File.separator))
                .filter(s->!s.isEmpty())
                .map(FilePart::new)
                .toList();
        Collections.reverse(list);
        for (int i = 0;i < list.size() - 1;i++){
            list.get(i).setUpPath(list.get(i + 1));
        }
        return list.get(0);
    }
}
