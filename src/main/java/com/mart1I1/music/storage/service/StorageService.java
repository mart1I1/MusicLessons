package com.mart1I1.music.storage.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void init();

    Path store(MultipartFile file);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}