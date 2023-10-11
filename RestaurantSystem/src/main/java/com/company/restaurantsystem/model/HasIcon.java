package com.company.restaurantsystem.model;

import io.jmix.core.FileRef;

public interface HasIcon {
    FileRef getAttachment();

    String getAttachmentName();

    void setAttachment(FileRef attachment);
}
