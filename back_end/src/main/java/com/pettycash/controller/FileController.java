package com.pettycash.controller;

import com.pettycash.dto.UploadFileResponse;
import com.pettycash.service.UploadFileService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/v1/file")
public class FileController {

    private final UploadFileService uploadFileService;

    @Autowired
    public FileController(UploadFileService uploadFileService){
        this.uploadFileService = uploadFileService;
    }

    @PostMapping("/upload")
    @CrossOrigin
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("transactionId") long transactionId) throws IOException, NotFoundException {
        UploadFileResponse response = uploadFileService.uploadFile(file, transactionId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/download/{transactionId}")
    @CrossOrigin
    public ResponseEntity<Resource> downloadFIle(@PathVariable("transactionId") long transactionId, HttpServletRequest request) throws IOException {
        Resource resource = uploadFileService.loadFile(transactionId);

        String contentType;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new IOException("error on downloading file on transaction id " + transactionId);
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

}
