package com.zhaodesong.Edocumentsystem.util;

import com.zhaodesong.Edocumentsystem.exception.FileException;

import java.io.*;

/**
 * @author ZhaoDesong
 * @date 2019/4/2 13:52
 */
public class FileUtils {
    //文件重命名
    public static boolean renameFile(String path, String oldname, String newname) {
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

    //删除文件&文件夹
    public static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return false;
        }
        boolean success = true;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            success = dir.delete();
        } else {
            success = dir.delete();
        }
        return success;
    }

    //复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();
        //关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

}
