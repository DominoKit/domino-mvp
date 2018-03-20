package org.dominokit.domino.api.client.request;

public interface CommandsRepository {

    void registerCommand(RequestHolder requestHolder);
    RequestHolder findRequestPresenterWrapper(String requestKey);
    void clear();

    class CommandCannotBeRegisteredMoreThanOnce extends RuntimeException {
        private static final long serialVersionUID = -4925004944311728702L;

        public CommandCannotBeRegisteredMoreThanOnce(String message) {
            super(message);
        }
    }
    class CommandKeyNotFoundException extends RuntimeException {
        private static final long serialVersionUID = -6084468443200600274L;

        public CommandKeyNotFoundException(String message) {
            super(message);
        }
    }
}
