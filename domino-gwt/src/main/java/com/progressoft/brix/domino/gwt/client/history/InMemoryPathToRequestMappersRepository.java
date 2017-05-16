package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.client.history.PathToRequestMappersRepository;
import com.progressoft.brix.domino.api.client.history.RequestFromPath;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPathToRequestMappersRepository implements PathToRequestMappersRepository {

    private Map<String, RequestFromPath> mappers = new HashMap<>();

    @Override
    public void registerMapper(String path, RequestFromPath mapper) {
        if (mappers.containsKey(path)) {
            throw new PathMapperConnotBeRegisteredMoreThanOnce("path [" + path + "]");
        }
        mappers.put(path, mapper);
    }

    @Override
    public RequestFromPath getMapper(String path) {
        if (mappers.containsKey(path)) {
            return mappers.get(path);
        }
        throw new PathMapperNotFoundException("path [" + path + "]");
    }

    public class PathMapperConnotBeRegisteredMoreThanOnce extends RuntimeException {

        private static final long serialVersionUID = -9008155741784675300L;

        public PathMapperConnotBeRegisteredMoreThanOnce(String message) {
            super(message);
        }
    }

    public class PathMapperNotFoundException extends RuntimeException {

        private static final long serialVersionUID = -3263305059767382591L;

        public PathMapperNotFoundException(String message) {
            super(message);
        }
    }
}
