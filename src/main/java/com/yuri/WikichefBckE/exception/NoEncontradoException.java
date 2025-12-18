package com.yuri.WikichefBckE.exception;

public class NoEncontradoException extends RuntimeException{

    public NoEncontradoException(){
         super("recurso no encontrado");
    }
}
