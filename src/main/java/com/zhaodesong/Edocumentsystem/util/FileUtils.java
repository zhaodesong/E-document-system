package com.zhaodesong.Edocumentsystem.util;

import com.zhaodesong.Edocumentsystem.exception.FileException;

import java.io.File;

/**
 * @author ZhaoDesong
 * @date 2019/4/2 13:52
 */
public class FileUtils {
    //文件重命名
    public static boolean renameFile(String path, String oldname, String newname) throws FileException {
        //新的文件名和以前文件名不同时,才有必要进行重命名
        if (oldname.equals(newname)) {
            return true;
        }
        File oldfile = new File(path + "/" + oldname);
        File newfile = new File(path + "/" + newname);
        if (!oldfile.exists()) {
            throw new FileException("重命名文件不存在");
        }
        //若在该目录下已经有一个文件和新文件名相同，则不允许重命名
        if (newfile.exists()) {
            throw new FileException("新文件名和其它文件冲突");
        }
        oldfile.renameTo(newfile);
        return true;
    }

    //创建文件夹
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            throw new FileException("目标目录已存在");
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            throw new FileException("创建目录失败");
        }
    }
}
