package com.roklimovich.grsmu_application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private final Path ROOT;

    public StorageService(@Value("${files.root}") String root) throws IOException {
        this.ROOT = Paths.get(root).toAbsolutePath().normalize();
        Files.createDirectories(this.ROOT);
    }

    public List<Path> listDirectories() throws IOException {
        try (var stream = Files.list(ROOT)) {
            return stream.filter(Files::isDirectory)
                    .collect(Collectors.toList());
        }

    }

    public Path getPath(String dirName) {
        return ROOT.resolve(dirName);
    }

    public List<Path> listFiles(Path directory) throws IOException {
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new IOException("Directory not found");
        }

        try (var stream = Files.list(directory)) {
            return stream.collect(Collectors.toList());
        }
    }
}
