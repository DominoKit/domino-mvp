#!/bin/sh
docker build -t psbox/${rootArtifactId} -f container.docker .
docker run -d -p 18080:8080 psbox/${rootArtifactId}
