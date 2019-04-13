package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import com.zhaodesong.Edocumentsystem.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class DocumentController extends BaseController {



    @Autowired
    private DocumentService documentService;
    @Autowired
    private ProjectAccountService projectAccountService;

    @RequestMapping("/toSingleFolder")
    public String toSingleFolder() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Long docId = Long.parseLong(request.getParameter("docId"));
        Byte level = Byte.valueOf(request.getParameter("level"));
        // 查询该项目下的所有文件
        List<Document> documentList = documentService.getAllDocInfoByParentId(docId, level);

        request.setAttribute("documents", documentList);
        request.setAttribute("parentId", docId);
        request.setAttribute("level", level + 1);
        request.setAttribute("title", "流云文档");
        return "single_folder";
    }

    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long parentId = Long.parseLong(request.getParameter("parentId"));
        Integer level = Integer.parseInt(request.getParameter("level"));
        int flag = Byte.valueOf(request.getParameter("flag"));
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "请选择要上传的文件");
            if (flag == 1) {
                return "redirect:/toProject?pid=" + projectId;
            } else {
                return "redirect:/toSingleFolder?docId=" + parentId + "&level=" + level;
            }
        }
        try {
            // 向数据库中写入该文件的记录
            Document document = new Document();
            document.setDocId(documentService.getMaxDocId() + 1);
            document.setProjectId(projectId);
            document.setName(file.getOriginalFilename());
            document.setVersion(0);
            document.setAccountIdCreate((Integer) session.getAttribute("accountId"));
            document.setAccountIdModify((Integer) session.getAttribute("accountId"));
            document.setType(false);
            document.setParentId(parentId);
            document.setLevel(level);
            document.setPower(1);
            document.setDelFlag(0);
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
        if (flag == 1) {
            return "redirect:/toProject?pid=" + projectId;
        } else {
            return "redirect:/toSingleFolder?docId=" + parentId + "&level=" + level;
        }
    }

    @RequestMapping("/download")
    public void fileDownload(HttpServletResponse response) throws UnsupportedEncodingException {
        Integer projectId = (Integer) session.getAttribute("projectId");
        String documentId = request.getParameter("did");
        String documentVersion = request.getParameter("dver");
        //设置文件路径
        File file = new File(FOLDER + projectId + "//" + documentId + "_" + documentVersion);
        if (file.exists()) {
            // 在数据库中查询该文件的原文件名
            String fileName = documentService.getNameByDocId(Long.parseLong(documentId));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/update")
    public String fileUpdate(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "请选择要上传的文件");
            return "redirect:/toProject?pid=" + projectId;
        }
        try {
            Document latestDoc = documentService.getLatestVersionInfo(docId);

            // 向数据库中写入该文件的记录
            Document document = new Document();
            document.setDocId(docId);
            document.setProjectId(projectId);
            document.setName(file.getOriginalFilename());
            document.setVersion(latestDoc.getVersion() + 1);
            document.setAccountIdCreate(latestDoc.getAccountIdCreate());
            document.setAccountIdModify((Integer) session.getAttribute("accountId"));
            document.setType(false);
            document.setParentId(latestDoc.getParentId());
            document.setLevel(latestDoc.getLevel());
            document.setPower(latestDoc.getPower());
            document.setDelFlag(latestDoc.getDelFlag());
            documentService.insert(document);

            // 向磁盘写入该文件
            String fileName = document.getDocId().toString() + "_" + document.getVersion().toString();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FOLDER + projectId + "//" + fileName);
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("msg",
                    " 文件 " + file.getOriginalFilename() + " 更新成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/toProject?pid=" + projectId;
    }

    // TODO 文件删除回收站功能
    @RequestMapping("/fileDelete")
    @ResponseBody
    public Object fileDelete() {
        Map<String, Object> res = new HashMap<>();
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        boolean isFolder = documentService.getAllDocInfoByDocId(docId).get(0).getType();
        List<Document> documentList = new ArrayList<>();
        if (isFolder) {
            documentList = documentService.getDeleteInfoByParentId(docId);
        } else {
            documentList = documentService.getDeleteInfoByDocId(docId);
        }

        for (int i = 0; i < documentList.size(); i++) {
            documentService.deleteByDocId(documentList.get(i).getDocId());
            String fileName = documentList.get(i).getDocId().toString() + "_" + documentList.get(i).getVersion().toString();
            File deleteFile = new File(FOLDER + projectId + "//" + fileName);
            FileUtils.deleteDir(deleteFile);
        }
        res.put("result", true);
        res.put("msg", "删除成功");
        res.put("docId", docId);
        return res;
    }

//    public String fileMove() {
//
//    }

    @RequestMapping("/fileCopy")
    @ResponseBody
    public Object fileCopy() {
        Map<String, Object> result = new HashMap<>();
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        Document srcDoc = documentService.getLatestVersionInfo(docId);
        // 向数据库中写入该文件的记录
        Document destDoc = new Document();
        destDoc.setDocId(documentService.getMaxDocId() + 1);
        destDoc.setProjectId(projectId);
        destDoc.setName(getCopyName(srcDoc.getName()));
        destDoc.setVersion(0);
        destDoc.setAccountIdCreate((Integer) session.getAttribute("accountId"));
        destDoc.setAccountIdModify((Integer) session.getAttribute("accountId"));
        destDoc.setType(false);
        destDoc.setParentId(srcDoc.getParentId());
        destDoc.setLevel(srcDoc.getLevel());
        destDoc.setPower(srcDoc.getPower());
        destDoc.setDelFlag(srcDoc.getDelFlag());
        documentService.insert(destDoc);
        File srcFile = new File(FOLDER + projectId + "//" +
                srcDoc.getDocId().toString() + "_" + srcDoc.getVersion().toString());
        File destFile = new File(FOLDER + projectId + "//" +
                destDoc.getDocId().toString() + "_" + destDoc.getVersion().toString());
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("result", true);
        result.put("document", destDoc);
        return result;
    }

    // 文件&&文件夹重命名
    @RequestMapping("/fileRename")
    @ResponseBody
    public Object rename() {
        Map<String, Object> result = new HashMap<>();
        String newName = request.getParameter("newName");
        String oldName = request.getParameter("oldName");
        boolean type = Boolean.parseBoolean(request.getParameter("isFolder"));
        Long docId = Long.parseLong(request.getParameter("docId"));

        String fullNewName = getNewName(oldName, newName, type);
        documentService.renameByDocId(docId, fullNewName);

        result.put("result", true);
        result.put("name", fullNewName);
        return result;
    }

    @RequestMapping("/newFolder")
    @ResponseBody
    public Object folderCreate() {
        Map<String, Object> result = new HashMap<>();
        String folderName = request.getParameter("folderName");
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long parentId = Long.parseLong(request.getParameter("parentId"));
        Integer level = Integer.parseInt(request.getParameter("level"));
        Document document = new Document();
        document.setDocId(documentService.getMaxDocId() + 1);
        document.setProjectId(projectId);
        document.setName(folderName);
        document.setVersion(0);
        document.setAccountIdCreate((Integer) session.getAttribute("accountId"));
        document.setAccountIdModify((Integer) session.getAttribute("accountId"));
        document.setType(true);
        document.setParentId(parentId);
        document.setLevel(level);
        document.setPower(1);
        document.setDelFlag(0);
        documentService.insert(document);
        result.put("result", 1);
        result.put("msg", "创建成功");
        result.put("document", document);
        return result;
    }

    @RequestMapping("/historyVersion")
    public String getHistoryVersion() {
        Long docId = Long.parseLong(request.getParameter("docId"));
        List<Document> documentList = documentService.getAllDocInfoByDocId(docId);
        request.setAttribute("documentList", documentList);
        return "history_version";
    }

//    @RequestMapping()
//    @ResponseBody
//    public Object changePermission() {
//        Map<String, Object> result = new HashMap<>();
//        Integer projectId = (Integer) session.getAttribute("projectId");
//        Integer accountId = (Integer) session.getAttribute("accountId");
//        Long docId = Long.parseLong(request.getParameter("docId"));
//        Integer power = Integer.parseInt(request.getParameter("power"));
//
////        if (!hasPermission(projectId, accountId)) {
////            result.put("msg", "没有权限，修改失败");
////            return result;
////        }
//        documentService.changePermission(docId, power);
//        result.put("msg", "修改成功");
//        return result;
//    }


    private String getCopyName(String fileName) {
        int index = fileName.indexOf('.');
        if (index == -1) {
            return fileName + "-副本";
        }
        return fileName.substring(0, index) + "-副本" + fileName.substring(index);
    }

    private String getNewName(String oldName, String newName, boolean type) {
        if (type) {
            return newName;
        }
        int index = oldName.indexOf('.');
        if (index == -1) {
            return newName;
        }
        return newName + oldName.substring(index);
    }

//    private boolean hasPermission(Integer projectId, Integer accountId) {
//
//    }
}
