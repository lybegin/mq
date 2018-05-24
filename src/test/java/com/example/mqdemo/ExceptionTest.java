package com.example.mqdemo;

import com.example.mqdemo.exception.RetryException;
import com.example.mqdemo.exception.UnRetryException;
import org.apache.kafka.common.errors.NetworkException;

public class ExceptionTest {

  public static void main(String[] args){

    try {
      testException();
    } catch (UnRetryException e) {
      System.out.println("1234");
      e.printStackTrace();
    } catch (RetryException e){
      System.out.println("123456");
      e.printStackTrace();
    } catch (Exception e){
      System.out.println("12345678");
      e.printStackTrace();
    }
  }

  public static void testException() throws UnRetryException, RetryException {
    try {
      if (true) {
        throw new NullPointerException("adasdds");
      }
    } catch (NetworkException e) {
      throw new RetryException("test");
    } catch (RuntimeException e){
      throw new UnRetryException("test123");
    }
  }

}
