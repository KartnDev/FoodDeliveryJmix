package com.company.restaurantsystem.service;

import com.company.restaurantsystem.entity.Restaurant;
import com.company.restaurantsystem.model.HasIcon;
import io.jmix.core.FileStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class AttachmentService {

    private final FileStorage fileStorage;

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
        try(var stream = getStreamFromEntityAttachment(hasIcon)) {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
