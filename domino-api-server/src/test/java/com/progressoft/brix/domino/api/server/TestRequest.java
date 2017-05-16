package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;

public class TestRequest extends ServerRequest {

    private StringBuilder testWord=new StringBuilder("");

    public void appendTestWord(String testWord){
        this.testWord.append(testWord);
    }

    public String getTestWord(){
        return this.testWord.toString();
    }
}
