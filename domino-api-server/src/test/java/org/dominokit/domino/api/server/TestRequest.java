package org.dominokit.domino.api.server;

import org.dominokit.domino.api.shared.request.RequestBean;

public class TestRequest implements RequestBean {

    private StringBuilder testWord=new StringBuilder("");

    public void appendTestWord(String testWord){
        this.testWord.append(testWord);
    }

    public String getTestWord(){
        return this.testWord.toString();
    }
}
