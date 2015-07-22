package com.hitao.codegen.util;

/**
 * An exception thrown to indicate that an error occurred while performing a
 * reflection-based operation.  This includes such processes as instantiating
 * an object from a <code>Constructor</code>, invoking a method from a
 * <code>String</code> description, and the like.<br><br>
 * <b>Note:</b> <code>ReflectionException</code>s are only meant to be thrown
 * in response to checked exceptions caught during reflection operations.  In
 * essence, <code>ReflectionException</code> consolidates the numerous types of
 * exceptions that can be produced during such operations and then, by
 * converting them to runtime exceptions, relieves the calling client of the
 * obligation of catching them in all cases.<br><br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ReflectionException.java 12 2011-02-20 10:50:23Z guest $
 */
public class ReflectionException
    extends RuntimeException {

  private static final long serialVersionUID = 6690338190230964079L;

  /**
   * Constructs a <code>ReflectionException</code> produced in response to the
   * specified error.
   * @param cause  the exception or error that motivated the creation of this
   * <code>ReflectionException</code>
   */
  public ReflectionException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a <code>ReflectionException</code> with the specified detail
   * message and produced in response to the specified error.
   * @param msg  a detailed text description of the error that occurred
   * @param cause  the exception or error that motivated the creation of this
   * <code>ReflectionException</code>
   */
  public ReflectionException(String msg, Throwable cause) {
    super(msg, cause);
  }
}


