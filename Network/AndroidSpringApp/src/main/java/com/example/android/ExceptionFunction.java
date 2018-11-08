package com.example.android;

public interface ExceptionFunction<T, R>
{
    R apply(T r) throws Exception;
}