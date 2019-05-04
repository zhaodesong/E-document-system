package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import com.zhaodesong.Edocumentsystem.util.FileUtils;
import com.zhaodesong.Edocumentsystem.vo.DocumentWithPower;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有方法中的flag均表示是否为首页的操作，1表示为首页
 */
@Controller
@Slf4j
public class DocumentController extends BaseController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ProjectAccountService projectAccountService;

    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long parentId = Long.parseLong(request.getParameter("parentId"));
        int flag = Integer.valueOf(request.getParameter("flag"));
        log.debug("DocumentController fileUpload开始, 参数为 projectId = {}, parentId = {}, flag = {}", projectId, parentId, flag);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "请选择要上传的文件");
            if (flag == 1) {
                return "redirect:/toProject?pid=" + projectId;
            } else {
                return "redirect:/toSingleFolder?docId=" + parentId;
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
            log.error("DocumentController fileUpload ERROR, {}", e.getMessage());
        }

        log.debug("DocumentController fileUpload结束");
        if (flag == 1) {
            return "redirect:/toProject?pid=" + projectId;
        } else {
            return "redirect:/toSingleFolder?docId=" + parentId;
        }
    }

    @RequestMapping("/download")
    public void fileDownload(HttpServletResponse response) throws UnsupportedEncodingException {
        Integer projectId = (Integer) session.getAttribute("projectId");
        String documentId = request.getParameter("did");
        String documentVersion = request.getParameter("dver");
        log.debug("DocumentController fileDownload开始, 参数为 projectId = {}, documentId = {}, documentVersion = {}", projectId, documentId, documentVersion);
        //设置文件路径
        File file = new File(FOLDER + projectId + "//" + documentId + "_" + documentVersion);
        if (file.exists()) {
            // 在数据库中查询该文件的原文件名
            String fileName = documentService.getNameByDocIdAndVersion(Long.parseLong(documentId), Integer.parseInt(documentVersion));
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
                log.error("DocumentController fileDownload ERROR, {}", e.getMessage());
            }
        }
        log.debug("DocumentController fileDownload结束");
    }

    @RequestMapping("/update")
    public String fileUpdate(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        Long parentId = Long.parseLong(request.getParameter("parentId"));
        int flag = Integer.valueOf(request.getParameter("flag"));
        log.debug("DocumentController fileUpdate开始, 参数为 projectId = {}, docId = {}, parentId = {}, flag = {}", projectId, docId, parentId, flag);

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
            log.debug("DocumentController fileUpdate ERROR, {}", e.getMessage());
        }
        log.debug("DocumentController fileUpdate结束");
        if (flag == 1) {
            return "redirect:/toProject?pid=" + projectId;
        } else {
            return "redirect:/toSingleFolder?docId=" + parentId;
        }
    }

    @RequestMapping("/fileDelete")
    @ResponseBody
    public Object fileDelete() {
        Map<String, Object> res = new HashMap<>();
        if (sessionCheck()) {
            res.put("msg", "登录失效，请重新登录");
            res.put("result", -1);
            return res;
        }
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        log.debug("DocumentController fileDelete开始, 参数为 projectId = {}, docId = {}", projectId, docId);

        List<Document> deleteDocList = documentService.getAllDocInfoByDocId(docId, 1);
        if (deleteDocList == null || deleteDocList.size() == 0) {
            res.put("result", 0);
            res.put("msg", "删除失败");
            log.debug("DocumentController fileDelete结束");
            return res;
        }
        boolean isFolder = deleteDocList.get(0).getType();
        List<Document> documentList;
        if (isFolder) {
            documentList = documentService.getDeleteInfoByParentId(docId);
        } else {
            documentList = deleteDocList;
        }

        for (Document document : documentList) {
            documentService.deleteByDocId(document.getDocId());
            String fileName = document.getDocId().toString() + "_" + document.getVersion().toString();
            File deleteFile = new File(FOLDER + projectId + "//" + fileName);
            FileUtils.deleteDir(deleteFile);
        }
        documentService.deleteByDocId(deleteDocList.get(0).getDocId());
        res.put("result", 1);
        res.put("msg", "删除成功");
        res.put("docId", docId);
        res.put("projectId", projectId);

        log.debug("DocumentController fileDelete结束");
        return res;
    }

    @RequestMapping("/fileDeleteRecycle")
    @ResponseBody
    public Object fileDeleteRecycle() {
        Map<String, Object> res = new HashMap<>();
        if (sessionCheck()) {
            res.put("msg", "登录失效，请重新登录");
            res.put("result", -1);
            return res;
        }
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        log.debug("DocumentController fileDeleteRecycle开始, 参数为 projectId = {}, docId = {}", projectId, docId);

        List<Document> deleteDocList = documentService.getAllDocInfoByDocId(docId, 0);
        if (deleteDocList == null || deleteDocList.size() == 0) {
            res.put("result", 0);
            res.put("msg", "删除失败");
            log.debug("DocumentController fileDeleteRecycle结束");
            return res;
        }
        boolean isFolder = deleteDocList.get(0).getType();

        if (isFolder) {
            // 删除间接删除的部分
            List<Document> documentList = documentService.getDeleteInfoByParentId(docId);
            for (Document document : documentList) {
                documentService.recycleDeleteIndirectlyByDocId(document.getDocId());
            }
            // 删除直接删除的部分
            documentService.recycleDeleteDirectlyByDocId(deleteDocList.get(0).getDocId());
        } else {
            documentService.recycleDeleteDirectlyByDocId(deleteDocList.get(0).getDocId());
        }

        res.put("result", 1);
        res.put("msg", "删除成功");
        res.put("docId", docId);
        res.put("projectId", projectId);
        res.put("parentId", deleteDocList.get(0).getParentId());
        log.debug("DocumentController fileDeleteRecycle结束");
        return res;
    }

    @RequestMapping("/fileRecovery")
    @ResponseBody
    public Object fileRecovery() {
        Map<String, Object> res = new HashMap<>();
        if (sessionCheck()) {
            res.put("msg", "登录失效，请重新登录");
            res.put("result", -1);
            return res;
        }
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        log.debug("DocumentController fileRecovery开始, 参数为 projectId = {}, docId = {}", projectId, docId);

        List<Document> recoveryDocList = documentService.getAllDocInfoByDocId(docId, 1);
        if (recoveryDocList == null || recoveryDocList.size() == 0) {
            res.put("result", 0);
            res.put("msg", "删除失败");
            log.debug("DocumentController fileDeleteRecycle结束");
            return res;
        }
        boolean isFolder = recoveryDocList.get(0).getType();

        if (isFolder) {
            // 恢复直接删除的部分
            List<Document> documentList = documentService.getDeleteInfoByParentId(docId);
            for (Document document : documentList) {
                documentService.recoveryDeleteByDocId(document.getDocId());
            }
            // 恢复间接删除的部分
            for (Document document : recoveryDocList) {
                documentService.recoveryDeleteByDocId(document.getDocId());
            }
        } else {
            for (Document document : recoveryDocList) {
                documentService.recoveryDeleteByDocId(document.getDocId());
            }
        }

        res.put("result", 1);
        res.put("msg", "删除成功");
        res.put("docId", docId);
        res.put("projectId", projectId);
        log.debug("DocumentController fileRecovery结束");
        return res;
    }

    @RequestMapping("/fileCopy")
    @ResponseBody
    public Object fileCopy() {
        Map<String, Object> result = new HashMap<>();
        if (sessionCheck()) {
            result.put("msg", "登录失效，请重新登录");
            result.put("result", -1);
            return result;
        }
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        log.debug("DocumentController fileCopy开始, 参数为 projectId = {}, docId = {}", projectId, docId);

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
        destDoc.setPower(srcDoc.getPower());
        destDoc.setDelFlag(srcDoc.getDelFlag());
        int sqlResult = documentService.insert(destDoc);
        if (sqlResult == 0) {
            result.put("result", 0);
            result.put("msg", "创建副本失败");
            log.debug("DocumentController fileCopy结束");
            return result;
        }
        File srcFile = new File(FOLDER + projectId + "//" +
                srcDoc.getDocId().toString() + "_" + srcDoc.getVersion().toString());
        File destFile = new File(FOLDER + projectId + "//" +
                destDoc.getDocId().toString() + "_" + destDoc.getVersion().toString());
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            log.error("DocumentController fileCopy ERROR {}", e.getMessage());
            result.put("result", 0);
            result.put("msg", "创建副本失败");
            log.debug("DocumentController fileCopy结束");
            return result;
        }
        result.put("result", 1);
        result.put("msg", "创建副本成功");
        result.put("doc", destDoc);
        result.put("projectId", projectId);
        result.put("parentId", srcDoc.getParentId());
        log.debug("DocumentController fileCopy结束");
        return result;
    }

    @RequestMapping("/fileRename")
    @ResponseBody
    public Object fileRename() {
        Map<String, Object> result = new HashMap<>();
        if (sessionCheck()) {
            result.put("msg", "登录失效，请重新登录");
            result.put("result", -1);
            return result;
        }
        String newName = request.getParameter("newName");
        String oldName = request.getParameter("oldName");
        boolean type = Boolean.parseBoolean(request.getParameter("isFolder"));
        Long docId = Long.parseLong(request.getParameter("docId"));
        log.debug("DocumentController fileRename开始, 参数为 newName = {}, oldName = {}, type = {}, docId = {}",
                newName, oldName, type, docId);

        String fullNewName = getNewName(oldName, newName, type);
        int sqlResult = documentService.renameByDocId(docId, fullNewName);
        if (sqlResult >= 1) {
            result.put("result", 1);
            result.put("msg", "重命名成功");
            result.put("name", fullNewName);
        } else {
            result.put("result", 0);
            result.put("msg", "重命名失败");
        }
        log.debug("DocumentController fileRename结束");
        return result;
    }

    @RequestMapping("/newFolder")
    @ResponseBody
    public Object newFolder() {
        Map<String, Object> result = new HashMap<>();
        if (sessionCheck()) {
            result.put("msg", "登录失效，请重新登录");
            result.put("result", -1);
            return result;
        }
        String folderName = request.getParameter("folderName");
        Integer projectId = (Integer) session.getAttribute("projectId");
        Long parentId = Long.parseLong(request.getParameter("parentId"));
        log.debug("DocumentController newFolder开始, 参数为 folderName = {}, projectId = {}, parentId = {}",
                folderName, projectId, parentId);

        Document document = new Document();
        document.setDocId(documentService.getMaxDocId() + 1);
        document.setProjectId(projectId);
        document.setName(folderName);
        document.setVersion(0);
        document.setAccountIdCreate((Integer) session.getAttribute("accountId"));
        document.setAccountIdModify((Integer) session.getAttribute("accountId"));
        document.setType(true);
        document.setParentId(parentId);
        document.setPower(1);
        document.setDelFlag(0);
        int sqlResult = documentService.insert(document);
        if (sqlResult == 1) {
            result.put("result", 1);
            result.put("msg", "创建成功");
            result.put("doc", document);
            result.put("projectId", projectId);
            result.put("parentId", parentId);
        } else {
            result.put("result", 0);
            result.put("msg", "创建失败");
        }
        log.debug("DocumentController newFolder结束");
        return result;
    }

    @RequestMapping("/historyVersion")
    public String historyVersion() {
        Long docId = Long.parseLong(request.getParameter("docId"));
        log.debug("DocumentController historyVersion开始, 参数为 docId = {}", docId);
        List<Document> documentList = documentService.getAllDocInfoByDocId(docId, 0);
        request.setAttribute("documentList", documentList);
        log.debug("DocumentController historyVersion结束");
        return "history_version";
    }

    @RequestMapping("/changeDocPower")
    @ResponseBody
    public Object changeDocPermission() {
        Map<String, Object> result = new HashMap<>();
        if (sessionCheck()) {
            result.put("msg", "登录失效，请重新登录");
            result.put("result", -1);
            return result;
        }
        Integer projectId = (Integer) session.getAttribute("projectId");
        Integer accountId = (Integer) session.getAttribute("accountId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        Integer power = Integer.parseInt(request.getParameter("power"));
        log.debug("DocumentController changeDocPermission开始, 参数为 projectId = {}, accountId = {}, docId = {}, power = {}",
                projectId, accountId, docId, power);

        if (!hasPermissionEditDoc(projectId, accountId)) {
            result.put("msg", "您没有权限进行操作");
            result.put("result", 0);
            log.debug("DocumentController changeDocPermission结束");
            return result;
        }

        int sqlResult = documentService.changePermission(docId, power);
        if (sqlResult >= 1) {
            result.put("msg", "修改成功");
            result.put("result", 1);
        } else {
            result.put("msg", "修改失败");
            result.put("result", 0);
        }
        log.debug("DocumentController changeDocPermission结束");
        return result;
    }

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

    private void move(Long parentId) {
        List<Document> docList = documentService.getDocInfoByParentId(parentId);
        for (Document document : docList) {
            if (document.getType()) {
                documentService.moveByDocId(document.getDocId(), parentId);
                move(document.getDocId());
            } else {
                documentService.moveByDocId(document.getDocId(), parentId);
            }
        }
    }

    private boolean hasPermissionEditDoc(Integer projectId, Integer accountId) {
        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, accountId);
        String p = projectAccount.getPermission();
        return "10".equals(p) || "11".equals(p);
    }
}
