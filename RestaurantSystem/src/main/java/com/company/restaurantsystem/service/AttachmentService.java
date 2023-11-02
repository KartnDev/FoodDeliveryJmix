package com.company.restaurantsystem.service;

import com.company.restaurantsystem.model.HasIcon;
import io.jmix.core.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class AttachmentService {
    private final Logger log = LoggerFactory.getLogger(AttachmentService.class);
    private final FileStorage fileStorage;

    public AttachmentService(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    public void replaceEntityAttachment(final HasIcon withAttachmentEntity, byte[] content) {
        var currentAttachment = withAttachmentEntity.getAttachment();

        if (currentAttachment != null) {
            fileStorage.removeFile(currentAttachment);
        }

        var fileRef = fileStorage.saveStream(withAttachmentEntity.getAttachmentName(), new ByteArrayInputStream(content));
        withAttachmentEntity.setAttachment(fileRef);
    }

    public BufferedInputStream getStreamFromEntityAttachment(final HasIcon withAttachmentEntity) {
        try {
            return new BufferedInputStream(fileStorage.openStream(withAttachmentEntity.getAttachment()));
        } catch (RuntimeException e) {
            log.error("Cannot read attachment", e);
            throw new RuntimeException("Cannot read attachment", e);
        }
    }

    public byte[] getAttachmentAsByteArray(HasIcon hasIcon) {
        try (var stream = getStreamFromEntityAttachment(hasIcon)) {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
