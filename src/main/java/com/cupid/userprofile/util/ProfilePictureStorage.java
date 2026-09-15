package com.cupid.userprofile.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class ProfilePictureStorage {

    private static final String UPLOAD_DIRECTORY =
            "uploads/profile-pictures";

    private ProfilePictureStorage() {
    }

    public static String save(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        Path uploadPath =
                Paths.get(UPLOAD_DIRECTORY);

        Files.createDirectories(uploadPath);

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.isBlank()) {
            throw new IOException(
                    "Invalid profile picture name"
            );
        }

        String safeFileName =
                Paths.get(originalFileName)
                        .getFileName()
                        .toString();

        Path target =
                uploadPath.resolve(safeFileName);

        Files.copy(
                file.getInputStream(),
                target,
                StandardCopyOption.REPLACE_EXISTING
        );

        return safeFileName;
    }
}