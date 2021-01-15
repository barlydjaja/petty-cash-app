package com.pettycash.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileResponse {
    String fileName;
    String downloadPath;
    String fileType;
    long size;
}
