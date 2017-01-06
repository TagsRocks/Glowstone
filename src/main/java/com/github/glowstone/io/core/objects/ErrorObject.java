package com.github.glowstone.io.core.objects;

import java.io.Serializable;

public class ErrorObject implements Serializable {

    private static final long serialVersionUID = -2076720432177170055L;

    private final String title;
    private final String detail;

    public ErrorObject(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public ErrorObject(String title) {
        this.title = title;
        this.detail = null;
    }
}
