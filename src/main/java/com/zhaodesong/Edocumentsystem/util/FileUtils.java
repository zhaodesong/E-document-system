package com.zhaodesong.Edocumentsystem.util;

import java.io.*;

/**
 * @author ZhaoDesong
 * @date 2019/4/2 13:52
 */
public class FileUtils {

    /**
     * 创建文件夹
     *
     * @param dirName 文件夹路径
     * @return
     */
    public static boolean createDir(String dirName) {
        File dir = new File(dirName);
        if (dir.exists()) {
            return false;
        }
        //创建目录
        return dir.mkdirs();
    }

    /**
     * 删除文件或文件夹
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return false;
        }
        boolean success;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            success = dir.delete();
        } else {
            success = dir.delete();
        }
        return success;
    }

    /**
     * 复制文件
     *
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
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
