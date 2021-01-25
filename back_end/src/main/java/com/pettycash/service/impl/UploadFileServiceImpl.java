package com.pettycash.service.impl;

import com.pettycash.dto.UploadFileResponse;
import com.pettycash.entity.PendingTransaction;
import com.pettycash.entity.Transaction;
import com.pettycash.entity.User;
import com.pettycash.service.PendingTransactionService;
import com.pettycash.service.TransactionService;
import com.pettycash.service.UploadFileService;
import com.pettycash.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private final UserService userService;
    private final TransactionService transactionService;
    private final PendingTransactionService pendingTransactionService;

    @Autowired
    public UploadFileServiceImpl(PendingTransactionService pendingTransactionService, UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.pendingTransactionService = pendingTransactionService;
    }

    private static final String uploadDir = "/pettycash/dir";

    @Override
    public UploadFileResponse uploadFile(MultipartFile file, long transactionId) throws IOException, NotFoundException {
        UploadFileResponse response = new UploadFileResponse();
        User admin = userService.getUserById(1);

        if (transactionService.getById(transactionId) == null) {
            throw new NotFoundException("transaction not found " + transactionId);
        }

        Transaction transaction = transactionService.getById(transactionId);
        long userId = transaction.getUser().getUserId();

        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("user not found for userId " + transaction.getUser().getUserId());
        }

        Path fileStorageLocation = Paths.get(uploadDir.concat("\\").concat(String.valueOf(admin.getUserId())).concat("\\").concat(String.valueOf(transactionId)))
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(fileStorageLocation);

        String fileExtension = "";
        int index = Objects.requireNonNull(file.getContentType()).lastIndexOf('/');

        if(index > 0){
            fileExtension = file.getContentType().substring(index + 1);
        }

        String fileName = transaction.getTransactionType().getTransactionTypeName().concat(".").concat(fileExtension);

        try (InputStream inputStream = file.getInputStream()) {
            fileStorageLocation = fileStorageLocation.resolve(StringUtils.cleanPath(Objects.requireNonNull(fileName)));

            if (Files.copy(inputStream, fileStorageLocation, StandardCopyOption.REPLACE_EXISTING) != file.getSize()) {
                Files.deleteIfExists(fileStorageLocation);
                throw new FileNotFoundException(file.getOriginalFilename() + " is not uploaded");
            }

            response.setFileName(fileName);
            response.setDownloadPath(ServletUriComponentsBuilder.fromCurrentContextPath().path("/v1").path("/file").path("/download/").path(String.valueOf(transactionId)).path("/").path(fileName).toUriString());

            response.setSize(file.getSize());
            response.setFileType(file.getContentType());

            transactionService.updateTransactionImageName(transaction, fileName);
        }
        return response;
    }

    @Override
    public Resource loadFile(long transactionId) throws FileNotFoundException, NotFoundException {
        User admin = userService.getUserById(1);
        try {
            Transaction transaction = transactionService.getById(transactionId);

            Path fileStorageLocation = Paths.get(uploadDir.concat("\\").concat(String.valueOf(admin.getUserId())).concat("\\").concat(String.valueOf(transactionId)))
                    .resolve(transaction.getFileName())
                    .toAbsolutePath()
                    .normalize();

            Resource resource = new UrlResource(fileStorageLocation.toUri());

            if (resource.exists()) {
                return resource;
            } else return null;
        } catch (Exception e) {
            throw new FileNotFoundException("File not found for transaction id " + transactionId);
        }
    }

    @Override
    public UploadFileResponse uploadPendingRequest(MultipartFile file, long transactionId) throws IOException, NotFoundException {
        UploadFileResponse response = new UploadFileResponse();

        if (pendingTransactionService.getByTransactionId(transactionId) == null) {
            throw new NotFoundException("transaction not found " + transactionId);
        }

        Transaction transaction = transactionService.getById(transactionId);

        PendingTransaction pendingTransaction = pendingTransactionService.getByTransactionId(transactionId);
        long userId = pendingTransaction.getUser().getUserId();

        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("user not found for userId " + pendingTransaction.getUser().getUserId());
        }

        Path fileStorageLocation = Paths.get(uploadDir.concat("\\").concat("pending-update").concat("\\").concat(String.valueOf(transactionId)))
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(fileStorageLocation);

        String fileExtension = "";
        int index = Objects.requireNonNull(file.getContentType()).lastIndexOf('/');

        if(index > 0){
            fileExtension = file.getContentType().substring(index + 1);
        }

        String fileName = transaction.getTransactionType().getTransactionTypeName().concat(".").concat(fileExtension);

        try (InputStream inputStream = file.getInputStream()) {
            fileStorageLocation = fileStorageLocation.resolve(StringUtils.cleanPath(Objects.requireNonNull(fileName)));

            if (Files.copy(inputStream, fileStorageLocation, StandardCopyOption.REPLACE_EXISTING) != file.getSize()) {
                Files.deleteIfExists(fileStorageLocation);
                throw new FileNotFoundException(file.getOriginalFilename() + " is not uploaded");
            }

            response.setFileName(fileName);
            response.setDownloadPath(ServletUriComponentsBuilder.fromCurrentContextPath().path("/v1").path("/file").path("/download/").path(String.valueOf(transactionId)).path("/").path(fileName).toUriString());

            response.setSize(file.getSize());
            response.setFileType(file.getContentType());

            pendingTransaction.setFileName(fileName);
            pendingTransactionService.updateBasic(pendingTransaction);
        }
        return response;
    }
}
