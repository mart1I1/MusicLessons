package com.mart1I1.music.storage.service;

import com.mart1I1.music.storage.exception.StorageException;
import com.mart1I1.music.storage.exception.StorageFileNotFoundException;
import com.mart1I1.music.storage.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public Path store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path filePath = load(file.getOriginalFilename());
            if (Files.exists(filePath)) {
                filePath = renameIfExist(filePath);
            }
            Files.copy(file.getInputStream(), filePath);
            return filePath.getFileName();
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Path load(String filename) {
        try {
            return rootLocation.resolve(filename);
        } catch (NullPointerException e) {
            throw new StorageFileNotFoundException("file not found");
        }

    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    private Path renameIfExist(Path filePath) {
        String fileName = filePath.toString();
        int idxOfDot = fileName.lastIndexOf('.');
        String extension = fileName.substring(idxOfDot + 1);
        String name = fileName.substring(0, idxOfDot);

        Path path = Paths.get(fileName);
        int counter = 1;
        while(Files.exists(path)){
            fileName = name+"("+counter+")."+extension;
            path = Paths.get(fileName);
            counter++;
        }
        return path;
    }
}
