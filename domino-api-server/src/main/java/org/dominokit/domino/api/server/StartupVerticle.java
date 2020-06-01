package org.dominokit.domino.api.server;

import io.vertx.core.AbstractVerticle;
import org.dominokit.domino.api.server.entrypoint.CleanupTask;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class StartupVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupVerticle.class);
    private VertxContext context;
    private boolean cleanupCompleted = false;
    private int retryCount = 0;
    CleanupTask first = null;

    @Override
    public void start() {
        LOGGER.info("Starting verticle --->");
        context = new DominoLoader(vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config).start();

        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    try {
                        while (!cleanupCompleted && retryCount < 50) {
                            retryCount += 1;
                            LOGGER.info("Waiting for cleanup services to complete, sleep for 100ms ... ");
                            Thread.sleep(100L);
                        }
                    } catch (Exception e) {
                        LOGGER.error("Failed to wait for vertx to stop", e);
                    }
                }));
    }

    @Override
    public void stop() throws Exception {

        List<CleanupTask> cleanupTasks = context.getCleanupTasks();
        if(cleanupTasks.isEmpty()){
            cleanupCompleted = true;
        }else{
            Iterator<CleanupTask> iterator = cleanupTasks.iterator();
            CleanupTask last = null;
            while (iterator.hasNext()) {
                if (isNull(first)) {
                    first = iterator.next();
                    last = first;
                } else {
                    CleanupTask next = iterator.next();
                    last.setNext(next);
                    last = next;
                }
            }

            if (nonNull(first)) {
                last.setCompleteConsumer(cleanupTask -> context.vertx().close(event -> {
                    cleanupCompleted = true;
                }));
                LOGGER.info("Stopping verticle <-----");
                first.run();
            }
        }



    }
}
