package com.boggle.example.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
//  @Value("${user.profile.directory}")
//  private String userProfileDirectory; // application.properties 또는 application.yml에서 설정한 디렉토리 경로

	public static void uploadUserProfile(MultipartFile file) {
        try {
        	byte[] bytes = file.getBytes();
        	String fileName = file.getOriginalFilename();
            String directory = "src/main/resources/static/images/user_profile/";

            // 디렉토리가 없으면 생성
            Path directoryPath = Paths.get(directory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            
            String filePath = directory + fileName;
            Path path = Paths.get(filePath);
        	
        	Files.write(path, bytes);
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
