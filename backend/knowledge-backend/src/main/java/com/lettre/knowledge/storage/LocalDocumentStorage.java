package com.lettre.knowledge.storage;


import com.lettre.knowledge.config.DocumentProperties;
import com.lettre.knowledge.exception.BusinessException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;



@Component
public class LocalDocumentStorage {


    private final DocumentProperties documentProperties;


    public LocalDocumentStorage(DocumentProperties documentProperties) {
        this.documentProperties = documentProperties;
    }


    public StorageResult store(
            MultipartFile file,
            Long userId,
            String extension
    ) {

        try {

            Path storageRoot = resolveStorageRoot();
            String relativePath = userId + "/" + UUID.randomUUID() + "." + extension;
            Path targetPath = storageRoot.resolve(relativePath);

            Files.createDirectories(targetPath.getParent());

            String fileHash = saveAndHash(file, targetPath);

            return new StorageResult(relativePath, fileHash);

        } catch (IOException e) {

            throw new BusinessException(50001, "文件保存失败");

        } catch (NoSuchAlgorithmException e) {

            throw new BusinessException(50002, "文件哈希计算失败");

        }

    }


    public Path resolveAbsolutePath(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {

            throw new BusinessException(40003, "文件路径无效");

        }

        try {

            Path storageRoot = resolveStorageRoot();
            Path targetPath = storageRoot.resolve(relativePath).normalize();

            if (!targetPath.startsWith(storageRoot)) {

                throw new BusinessException(50004, "非法文件路径");

            }

            if (!Files.exists(targetPath)) {

                throw new BusinessException(40402, "文件不存在或已被删除");

            }

            return targetPath;

        } catch (IOException e) {

            throw new BusinessException(50001, "文件读取失败");

        }

    }


    public void delete(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return;
        }

        try {

            Path storageRoot = resolveStorageRoot();
            Path targetPath = storageRoot.resolve(relativePath).normalize();

            if (!targetPath.startsWith(storageRoot)) {

                throw new BusinessException(50004, "非法文件路径");

            }

            Files.deleteIfExists(targetPath);

        } catch (IOException e) {

            throw new BusinessException(50003, "文件删除失败");

        }

    }


    private String saveAndHash(
            MultipartFile file,
            Path targetPath
    ) throws IOException, NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (
                InputStream inputStream = file.getInputStream();
                DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest)
        ) {

            Files.copy(
                    digestInputStream,
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

        }

        return HexFormat.of().formatHex(digest.digest());

    }


    private Path resolveStorageRoot() throws IOException {

        Path storageRoot = Path.of(documentProperties.getStoragePath()).toAbsolutePath().normalize();

        Files.createDirectories(storageRoot);

        return storageRoot;

    }


}
