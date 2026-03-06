package com.roklimovich.grsmu_application.controller;

import com.roklimovich.grsmu_application.service.ClasspathContentService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class FileController {
    private final ClasspathContentService content;

    public FileController(ClasspathContentService content) {
        this.content = content;
    }

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(value = "path", required = false) String path, Model model) {
        String p = path == null ? "" : path;
        List<ClasspathContentService.Item> items = content.listChildren(p);
        List<ClasspathContentService.Breadcrumb> crumbs = content.breadcrumbs(p);
        model.addAttribute("items", items);
        model.addAttribute("breadcrumbs", crumbs);
        model.addAttribute("path", p);
        return "home";
    }

    @GetMapping("/support")
    public String support() {
        return "support";
    }

    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }

    @GetMapping("/view")
    public ResponseEntity<Resource> view(@RequestParam("path") String path) {
        Resource res = content.getResource(path);
        String filename = res.getFilename();
        String ct = guessContentType(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(filename, StandardCharsets.UTF_8)
                                .build().toString())
                .contentType(MediaType.parseMediaType(ct))
                .body(res);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String path) {
        Resource res = content.getResource(path);
        String filename = res.getFilename();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(filename, StandardCharsets.UTF_8).build().toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(res);
    }

    private static String guessContentType(String filename) {
        String ct = URLConnection.guessContentTypeFromName(filename);
        return ct != null ? ct : MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;
    }
}
