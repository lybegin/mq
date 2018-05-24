package com.example.mqdemo.exception;

public class UnRetryException extends RuntimeException{

  public UnRetryException(){
    super();
  }

  public UnRetryException(String errMessage){
    super(errMessage);
  }

}
