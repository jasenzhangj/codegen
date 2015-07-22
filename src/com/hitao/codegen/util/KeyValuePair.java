package com.hitao.codegen.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A key-value pair that can be put in a hash.<br><br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @param <K> type of the key
 * @param <V> type of the value
 * @version $Id: KeyValuePair.java 12 2011-02-20 10:50:23Z guest $
 */
public class KeyValuePair<K, V> implements IKeyedValue<K, V>{
  private final K key_;
  private final V value_;

  private int hashCode_ = -1;

  /**
   * Constructs a <code>KeyValuePair</code>.
   *
   * @param argKey the key for the object
   * @param argValue the value for the object
   */
  public KeyValuePair(K argKey, V argValue) {
    this.key_ = argKey;
    this.value_ = argValue;
  }

  /**
   * Extracts the key-value pairs from the specified map and converts them into
   * an equivalent array of key-value pair value objects.
   *
   * @param <K> type of the key
   * @param <V> type of the value
   * @param argMap the map to convert to an array of key-value pairs
   * @return the set of key-value pairs
   */
  public static <K, V> KeyValuePair<K, V>[] valueOf(Map<K, V> argMap) {
    if (argMap == null) {
      return null;
    }
    Set<Entry<K, V>> entries = argMap.entrySet();
    KeyValuePair<K, V>[] pairs = newArray(entries.size());
    int i = 0;
    for (Map.Entry<K, V> entry : entries) {
      pairs[i++] = new KeyValuePair<K, V>(entry.getKey(), entry.getValue());
    }
    return pairs;
  }

  @SuppressWarnings("unchecked")
  private static final <K, V> KeyValuePair<K, V>[] newArray(int size) {
    return new KeyValuePair[size];
  }

  /**
   * Returns the key for this pair.
   * @return the key for this pair
   */
  public K getKey() {
    return key_;
  }

  /**
   * Returns the value for the pair.
   * @return the value for the pair
   */
  public V getValue() {
    return value_;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param argObj  the object to test for equality
   * @return <code>true</code> if argObj is a KeyValuePair with equal key
   *     and value to this one.
   */
	@SuppressWarnings("rawtypes")
	@Override
  public boolean equals(Object argObj) {
    if (argObj instanceof KeyValuePair) {
      KeyValuePair<?, ?> other = (KeyValuePair) argObj;

      return ((ObjectUtils.equivalent(getKey(), other.getKey())) &&
          (ObjectUtils.equivalent(getValue(), other.getValue())));
    }
    return false;
  }

  /**{@inheritDoc}*/
  @Override
  public int hashCode() {
    if (hashCode_ == -1) {
      hashCode_ = 17;
      hashCode_ = (37 * hashCode_) + ((key_ != null) ? key_.hashCode() : 0);
      hashCode_ = (37 * hashCode_) + ((value_ != null) ? value_.hashCode() : 0);
    }
    return hashCode_;
  }

  /**
   * Gets a string representation.
   * 
   * @return subject to change, but typically {key's toString} -&gt; {value's toString}
   */
  @Override
  public String toString() {
    return getKey() + " -> " + getValue();
  }
}
