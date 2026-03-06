package com.roklimovich.grsmu_application.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log
public class ClasspathContentService {

    private final Path baseDir;

    public ClasspathContentService(
            @Value("${app.content.base-dir:data}") String dir) {

        this.baseDir = Paths.get(dir).normalize().toAbsolutePath();
    }

    public List<Item> listChildren(String path) {
        String norm = normalize(path);
        Path dir = norm.isEmpty() ? baseDir : baseDir.resolve(norm);

        // If the directory doesn't exist yet, just return an empty list
        // so the UI can handle "no content" without failing.
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return List.of();
        }

        try {
            return Files.list(dir)
                    .map(p -> toItem(norm, p))
                    .sorted(Comparator.comparing((Item it) -> !it.directory)
                            .thenComparing(it -> it.name.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list directory: " + dir, e);
        }
    }

    public Resource getResource(String path) {
        String norm = normalize(path);
        try {
            Path file = baseDir.resolve(norm);
            if (!Files.exists(file) || !Files.isRegularFile(file)) {
                throw new NoSuchElementException("File not found: " + path);
            }
            return new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new NoSuchElementException("Invalid path: " + path);
        }
    }

    public List<Breadcrumb> breadcrumbs(String path) {
        String norm = normalize(path);
        if (norm.isEmpty()) return List.of();
        String[] parts = norm.split("/");
        List<Breadcrumb> crumbs = new ArrayList<>();
        String acc = "";
        for (int i = 0; i < parts.length; i++) {
            acc = i == 0 ? parts[i] : acc + "/" + parts[i];
            crumbs.add(new Breadcrumb(parts[i], acc));
        }
        return crumbs;
    }

    private Item toItem(String parent, Path childPath) {
        String name = childPath.getFileName().toString();
        boolean isDir = Files.isDirectory(childPath);
        long size = 0L;
        if (!isDir) {
            try {
                size = Files.size(childPath);
            } catch (IOException ignored) {
                // If size cannot be determined, leave as 0.
            }
        }
        String href = buildHref(parent, name, isDir);
        return new Item(name, isDir, size, href);
    }

    private String buildHref(String parent, String childName, boolean isDirectory) {
        String p = parent.isEmpty() ? "" : parent + "/";
        String rel = p + childName;
        return isDirectory ? "/home?path=" + url(rel) : "/view?path=" + url(rel);
    }

    private static String url(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String normalize(String rel) {
        if (rel == null) return "";
        String s = rel.replace("\\", "/");
        while (s.startsWith("/")) s = s.substring(1);
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }

    public static class Item {
        public final String name;
        public final boolean directory;
        public final long size;
        public final String href;
        public Item(String name, boolean directory, long size, String href) {
            this.name = name;
            this.directory = directory;
            this.size = size;
            this.href = href;
        }

        public String getSize() {
            if (directory) return "Directory";
            if (size < 1048576) return size + " Bytes";
            return String.format("%.1f MB", size / 1048576.0);
        }

    }

    public static class Breadcrumb {
        public final String label;
        public final String path;
        public Breadcrumb(String label, String path) {
            this.label = label; this.path = path;
        }
    }

}
