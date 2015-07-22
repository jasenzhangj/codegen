package com.hitao.codegen.util;

/**
 * An exception thrown to indicate that an error occurred while performing a
 * resource bundle- or properties-based operation.  <code>Properties</code>
 * files and classes provide generalized key-value configuration support, while
 * <code>ResourceBundle</code>s are used to manage locale-specific settings,
 * often in conjunction with <code>Properties</code> files.<br><br>
 * <b>Note:</b> <code>RuntimeException</code>s are only meant to be thrown in
 * response to checked exceptions caught during resource bundle and properties
 * operations.  In essence, <code>RuntimeException</code> consolidates the
 * numerous types of exceptions that can be produced during such operations and
 * then, by converting them to runtime exceptions, relieves the calling client
 * of the obligation of catching them in all cases.<br><br
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ResourceException.java 12 2011-02-20 10:50:23Z guest $
 */
public class ResourceException
    extends RuntimeException {

  private static final long serialVersionUID = -5911405142227426061L;

  /**
   * Constructs a <code>ResourceException</code> produced in response to the
   * specified error.
   * @param cause  the exception or error that motivated the creation of this
   * <code>ResourceException</code>
   */
  public ResourceException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a <code>ResourceException</code> with the specified detail
   * message and produced in response to the specified error.
   * @param msg  a detailed text description of the error that occurred
   * @param cause  the exception or error that motivated the creation of this
   * <code>ResourceException</code>
   */
  public ResourceException(String msg, Throwable cause) {
    super(msg, cause);
  }
}


