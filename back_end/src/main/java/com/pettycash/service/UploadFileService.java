package com.pettycash.service;

import com.pettycash.dto.UploadFileResponse;
import javassist.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface UploadFileService {
    UploadFileResponse uploadFile(MultipartFile file, long transactionId) throws IOException, NotFoundException;
    Resource loadFile(long transactionId) throws MalformedURLException, FileNotFoundException;
}
