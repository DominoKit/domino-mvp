package org.dominokit.domino.gwt.client.logging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.RemoteLogHandlerBase;
import com.google.web.bindery.event.shared.UmbrellaException;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.shared.logging.SerializableLogRecord;
import org.dominokit.domino.api.shared.logging.SerializableStackTraceElement;
import org.dominokit.domino.api.shared.logging.SerializableThrowable;
import org.fusesource.restygwt.client.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

public class RestfulRemoteLogHandler extends RemoteLogHandlerBase {

    private RemoteExceptionLoggingService service = GWT.create(RemoteExceptionLoggingService.class);
    private static final String PATH = "/service/remoteLogging";

    public interface RemoteExceptionLoggingService extends RestService {
        @POST
        @Path(PATH)
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        void send(SerializableLogRecord record, MethodCallback<Void> callback);

    }

    public RestfulRemoteLogHandler() {
        setLevel(Level.SEVERE);
    }

    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            if (!ServiceRootMatcher.hasServiceRoot(PATH))
                ((RestServiceProxy) service).setResource(new Resource(GWT.getHostPageBaseURL()));
            else
                ((RestServiceProxy) service).setResource(new Resource(ServiceRootMatcher.matchedServiceRoot(PATH)));
            service.send(asSerializableLogRecord(record), new RemoteLoggingCallBack());
        }
    }

    private SerializableLogRecord asSerializableLogRecord(LogRecord record) {
        SerializableLogRecord serializableLogRecord = new SerializableLogRecord();
        serializableLogRecord.level = record.getLevel().toString();
        serializableLogRecord.millis = record.getMillis();
        serializableLogRecord.message = record.getMessage();
        serializableLogRecord.loggerName = record.getLoggerName();
        serializableLogRecord.permutationStrongName = GWT.getPermutationStrongName();
        serializableLogRecord.thrown = serializableThrowable(unwrap(record.getThrown()));
        return serializableLogRecord;
    }

    private static Throwable unwrap(Throwable e) {
        if (e instanceof UmbrellaException) {
            UmbrellaException ue = (UmbrellaException) e;
            if (ue.getCauses().size() == 1)
                return unwrap(ue.getCauses().iterator().next());
        }
        return e;
    }

    private SerializableThrowable serializableThrowable(Throwable throwable) {
        if (throwable == null)
            return null;
        SerializableThrowable serializableThrowable = new SerializableThrowable();
        serializableThrowable.type = throwable.getClass().getName();
        serializableThrowable.message = throwable.getMessage();
        serializableThrowable.cause = serializableThrowable(throwable.getCause());
        serializableThrowable.stackTrace = asSerializableStackTrace(Optional.ofNullable(throwable.getStackTrace()).orElseGet(() -> new StackTraceElement[0]));
        return serializableThrowable;
    }

    private SerializableStackTraceElement[] asSerializableStackTrace(StackTraceElement[] stackTraceElements) {
        return Stream.of(stackTraceElements).map(this::asSerializableStackTraceElement)
                .toArray(SerializableStackTraceElement[]::new);
    }

    private SerializableStackTraceElement asSerializableStackTraceElement(StackTraceElement stackTraceElement) {
        SerializableStackTraceElement serializableStackTraceElement = new SerializableStackTraceElement();
        serializableStackTraceElement.className = stackTraceElement.getClassName();
        serializableStackTraceElement.fileName = stackTraceElement.getFileName();
        serializableStackTraceElement.methodName = stackTraceElement.getMethodName();
        serializableStackTraceElement.lineNumber = stackTraceElement.getLineNumber();
        return serializableStackTraceElement;
    }

    private class RemoteLoggingCallBack implements MethodCallback<Void> {

        @Override
        public void onFailure(Method method, Throwable throwable) {
            // TODO: implementing the behaviour for failing remote logging
        }

        @Override
        public void onSuccess(Method method, Void aVoid) {
            // No action required
        }
    }

}
