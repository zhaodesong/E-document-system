package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@Slf4j
public class DocumentController {
    private static String FOLDER = "D://temp//";

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DocumentService documentService;

    @RequestMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpSession session) {
        Integer projectId = (Integer) session.getAttribute("projectId");
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "请选择要上传的文件");
            return "redirect:/toProject?pid=" + projectId;
        }
        try {
            // 向数据库中写入该文件的记录
            Document document = new Document();
            document.setDocId(documentService.getMaxDocId()+1);
            document.setProjectId(projectId);
            document.setName(file.getOriginalFilename());
            document.setVersion(0);
            document.setAccountIdCreate((Integer) session.getAttribute("accountId"));
            document.setAccountIdModify((Integer) session.getAttribute("accountId"));
            document.setType(false);
            document.setParentId(0L);
            document.setLevel((byte) 0);
            documentService.insert(document);

            // 向磁盘写入该文件
            String fileName = document.getDocId().toString() + "_" + document.getVersion().toString();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FOLDER + projectId + "//" + fileName);
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("msg",
                    " 文件 " + file.getOriginalFilename() + " 上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/toProject?pid=" + projectId;
    }

    @RequestMapping("/download")
    public void downloadFile(HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        Integer projectId = (Integer) session.getAttribute("projectId");
        String documentId = request.getParameter("did");
        String documentVersion = request.getParameter("dver");
        //设置文件路径
        File file = new File(FOLDER + projectId + "//" + documentId + "_" + documentVersion);
        System.out.println(FOLDER + projectId + "//" + documentId + documentVersion);
        System.out.println("文件是否存在" + file.exists());
        if (file.exists()) {
            // 在数据库中查询该文件的原文件名
            String fileName = documentService.getNameByDocId(Long.parseLong(documentId));
            System.out.println("原文件名" + fileName);
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];

            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                //request.setAttribute("msg", "下载成功");
                //return "redirect:/toProject?pid=" + projectId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        request.setAttribute("msg", "系统错误，下载失败，请稍后重试");
//        return "redirect:/toProject?pid=" + projectId;
    }
}
