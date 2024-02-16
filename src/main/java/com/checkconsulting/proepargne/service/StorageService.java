//package com.checkconsulting.proepargne.service;
//
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.amazonaws.util.IOUtils;
//import com.checkconsulting.proepargne.exception.GlobalException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//@Slf4j
//public class StorageService {
//
//    @Value("${application.bucket.name}")
//    private String bucketName;
//
//    @Autowired
//    private AmazonS3 s3Client;
//
//    public String uploadFile(MultipartFile file) throws GlobalException {
//        try {
//            String fileExtension = (file.getOriginalFilename()).substring((file.getOriginalFilename()).lastIndexOf(".") + 1);
//            if (!isAllowedFileType(fileExtension)) {
//                throw new GlobalException("File type not supported", HttpStatus.BAD_REQUEST);
//            }
//
//            checkFileSize(file);
//
//            File fileObj = convertMultiPartFileToFile(file);
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
//            fileObj.delete();
//            log.info("File uploaded successfully: {}", fileName);
//            return "File uploaded : " + fileName;
//        } catch (Exception e) {
//            log.error("An unexpected error occurred: {}", e.getMessage());
//            throw new GlobalException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    private boolean isAllowedFileType(String fileExtension) {
//        List<String> allowedExtensions = Arrays.asList("pdf", "png", "jpg", "jpeg");
//        return allowedExtensions.contains(fileExtension.toLowerCase());
//    }
//
//    private void checkFileSize(MultipartFile file) throws GlobalException {
//        long fileSize = file.getSize();
//        long maxSize = 20 * 1024 * 1024; // 20 M
//        if (fileSize > maxSize) {
//            throw new GlobalException("File size exceeds the maximum limit", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    public byte[] downloadFile(String fileName) {
//        S3Object s3Object = s3Client.getObject(bucketName, fileName);
//        S3ObjectInputStream inputStream = s3Object.getObjectContent();
//        try {
//            byte[] content = IOUtils.toByteArray(inputStream);
//            return content;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private File convertMultiPartFileToFile(MultipartFile file) {
//        File convertedFile = new File(file.getOriginalFilename());
//        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
//            fos.write(file.getBytes());
//        } catch (IOException e) {
//            log.error("Error converting multipartFile to file", e);
//        }
//        return convertedFile;
//    }
//
//
//    public String deleteFile(String fileName) {
//        s3Client.deleteObject(bucketName, fileName);
//        return fileName + " removed ...";
//    }
//
//
//
//}
//
