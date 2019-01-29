package org.dominokit.domino.api.shared.history;

public class TokenParameter {
    private String name;
    private String value;

    public TokenParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static TokenParameter of(String name, String value){
        return new TokenParameter(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
