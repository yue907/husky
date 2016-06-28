package com.husky.common.bean;

import java.io.Serializable;

/**
 * Created by google on 16/6/24.
 */
public class InvocationResponse implements Serializable{
    private static final long serialVersionUID = -8517836218530483915L;
    private Object result;
    private Throwable error;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
