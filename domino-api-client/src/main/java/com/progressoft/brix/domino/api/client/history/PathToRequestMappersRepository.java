package com.progressoft.brix.domino.api.client.history;

public interface PathToRequestMappersRepository {

    void registerMapper(String path, RequestFromPath mapper);
    RequestFromPath getMapper(String path);
}
