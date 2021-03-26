/*
 * Copyright © ${year} Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
