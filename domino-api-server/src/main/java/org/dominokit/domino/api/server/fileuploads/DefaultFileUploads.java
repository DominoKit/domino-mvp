package org.dominokit.domino.api.server.fileuploads;

public class DefaultFileUploads implements FileUpload {

    private final io.vertx.ext.web.FileUpload vertxFileUpload;

    public DefaultFileUploads(io.vertx.ext.web.FileUpload vertxFileUpload) {
        this.vertxFileUpload = vertxFileUpload;
    }

    @Override
    public String name() {
        return vertxFileUpload.name();
    }

    @Override
    public String uploadedFileName() {
        return vertxFileUpload.uploadedFileName();
    }

    @Override
    public String fileName() {
        return vertxFileUpload.fileName();
    }

    @Override
    public long size() {
        return vertxFileUpload.size();
    }

    @Override
    public String contentType() {
        return vertxFileUpload.contentType();
    }

    @Override
    public String contentTransferEncoding() {
        return vertxFileUpload.contentTransferEncoding();
    }

    @Override
    public String charSet() {
        return vertxFileUpload.charSet();
    }
}
