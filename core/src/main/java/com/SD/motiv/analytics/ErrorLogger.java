package com.SD.motiv.analytics;

public interface ErrorLogger {

    void reportError(Throwable throwable, Object... args);

}
