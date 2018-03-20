package org.dominokit.domino.api.client.request;

public class RequestHolder {

    private final String requestName;
    private final String presenterName;

    public RequestHolder(String requestName, String presenterName) {
        this.requestName = requestName;
        this.presenterName = presenterName;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getPresenterName() {
        return presenterName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        if (!getRequestName().equals(((RequestHolder) other).getRequestName()))
            return false;
        return getPresenterName().equals(((RequestHolder) other).getPresenterName());
    }

    @Override
    public int hashCode() {
        int result = getRequestName().hashCode();
        result = 31 * result + getPresenterName().hashCode();
        return result;
    }
}
