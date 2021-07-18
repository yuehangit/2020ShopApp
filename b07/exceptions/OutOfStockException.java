package com.b07.exceptions;

public class OutOfStockException extends Exception {

  /**
   * An exception that is thrown when attempting to check out with goods that exceed their stock
   * count
   */
  private static final long serialVersionUID = 1L;
}