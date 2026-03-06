package com.roklimovich.grsmu_application.dto;

public class FileItem {
    private final String name;
    private final boolean directory;
    private final long size;
    private final String href;

    public FileItem(String name, boolean directory, long size, String href) {
        this.name = name;
        this.directory = directory;
        this.size = size;
        this.href = href;
    }


    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public long getSize() {
        return size;
    }

    public String getHref() {
        return href;
    }
}
