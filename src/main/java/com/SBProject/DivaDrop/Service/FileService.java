package com.SBProject.DivaDrop.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public Boolean updloadFileS3(MultipartFile file, Integer bucketType) throws IOException;

}
