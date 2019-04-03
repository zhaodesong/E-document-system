package com.zhaodesong.Edocumentsystem.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilsTest {
    private static String UPLOADED_FOLDER = "D://temp//";

    @Test
    public void testCreateDir() {
        int projectId = 1;
        boolean res = FileUtils.createDir(UPLOADED_FOLDER + projectId);
        Assert.assertEquals(true,res);
    }
}