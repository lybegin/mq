package com.example.mqdemo.exception;

public class RetryException extends Exception {

  public RetryException(){
    super();
  }

  public RetryException(String errMessage){
    super(errMessage);
  }

}
