package org.dominokit.domino.api.server.fileuploads;

public interface FileUpload {

    String name();

    String uploadedFileName();

    String fileName();

    long size();

    String contentType();

    String contentTransferEncoding();

    String charSet();
}
