package com.hitao.codegen.util;

import java.io.Serializable;

/**
 * An object that is made up of two or more other objects.
 * The {@link #equals(Object)} and {@link #hashCode()} methods take all
 * component members into account.  This useful as a multi-part typed
 * key when storing values in maps with more than one object making up the key.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: CompositeObject.java 12 2011-02-20 10:50:23Z guest $
 */
public class CompositeObject implements Serializable {
  private static final long serialVersionUID = 1L;
  
  final Object[] parts_;

  /**
   * Constructor for 2-N sized composite with no field accessors.
   * 
   * @param list the parts of the composite
   * @throws IllegalArgumentException if the number of parts is less than 2
   */
  public CompositeObject(Object... list) {
//    if (list.length < 2) {
//      throw new IllegalArgumentException("too few components");
//    }
    parts_ = list;
  }

  @SuppressWarnings("unchecked")
  protected <T> T get(int idx) {
    return (T) parts_[idx];
  }

  /**{@inheritDoc}*/
  @Override
  public final boolean equals(Object argObj) {
    if (argObj == this) { return true; }
    if (!(argObj instanceof CompositeObject)) { return false; }
    CompositeObject other = (CompositeObject) argObj;
    if (parts_.length != other.parts_.length) { return false; }
    for (int i = 0; i < parts_.length; i++ ) {
      if (!eq(parts_[i], other.parts_[i])) { return false; }
    }
    return true;
  }

  /**{@inheritDoc}*/
  @Override
  public final int hashCode() {
    int result = 17;
    for (Object o : parts_) {
      if (o != null) { result = 37 * result + o.hashCode(); }
    }
    return result;
  }

  /**{@inheritDoc}*/
  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder("[CompositeObject{");
    for (Object o : parts_) {
      sb.append(o);
      sb.append(",");
    }
    sb.setCharAt(sb.length() - 1, '}');
    sb.append("]");
    return sb.toString();
  }

  /////////////////////////////////////////////////
  // MAKER METHODS
  /////////////////////////////////////////////////

  /**
   * Maker method for a two part composite object with accessor methods for the parts.
   * @param a   first part
   * @param b   second part
   * @param <A> type of first part
   * @param <B> type of second part
   * @return two part composite
   */
  public static <A, B> TwoPiece<A, B> make(A a, B b) {
    return new TwoPiece<A, B>(a, b);
  }

  /**
   * Maker method for a three part composite with accessor methods for the parts.
   * @param a   first part
   * @param b   second part
   * @param c   third part
   * @param <A> type of first part
   * @param <B> type of second part
   * @param <C> type of third part
   * @return three part composite
   */
  public static <A, B, C> ThreePiece<A, B, C> make(A a, B b, C c) {
    return new ThreePiece<A, B, C>(a, b, c);
  }

  /////////////////////////////////////////////////
  // CONCRETE IMPLEMENTATIONS
  /////////////////////////////////////////////////
  /**
   * Implementation of a two part composite.
   * @param <A> type of first part
   * @param <B> type of second part
   */
  public static class TwoPiece<A, B>
      extends CompositeObject {

    private static final long serialVersionUID = 1L;

    protected TwoPiece(A aa, B bb) {
      super(aa, bb);
    }

    /**
     * Gets the first part.
     * @return the first part
     */
    @SuppressWarnings("unchecked")
    public A a() {
      return (A) get(0);
    }

    /**
     * Gets the second part.
     * @return the second part
     */
    @SuppressWarnings("unchecked")
    public B b() {
      return (B) get(1);
    }
  }

  /**
   * Implementation of a three part composite.
   * @param <A> type of first part
   * @param <B> type of second part
   * @param <C> type of third part
   */
  public static class ThreePiece<A, B, C>
      extends CompositeObject {

    private static final long serialVersionUID = 1L;

    protected ThreePiece(A aa, B bb, C cc) {
      super(aa, bb, cc);
    }

    /**
     * Gets the first part.
     * @return the first part
     */
    @SuppressWarnings("unchecked")
    public A a() {
      return (A) get(0);
    }

    /**
     * Gets the second part.
     * @return the second part
     */
    @SuppressWarnings("unchecked")
    public B b() {
      return (B) get(1);
    }

    /**
     * Gets the third part.
     * @return the third part
     */
    @SuppressWarnings("unchecked")
    public C c() {
      return (C) get(2);
    }
  }

  /////////////////////////////////////////////////
  // UTILITY METHODS
  /////////////////////////////////////////////////
  private static boolean eq(Object o1, Object o2) {
	if (o1 == null) { return o2 == null; }
    if (o1 == o2) { return true; }
    return o1.equals(o2);
  }

}
