package com.hitao.codegen.configs.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitao.codegen.util.IParent;

/**
 * Abstract config for a collection of values.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @param <E> the type of the child configs
 * @version $Id: AbstractSetConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public abstract class AbstractSetConfig<E extends IConfigObject>
    extends AbstractParentConfig
    implements ICascadableConfig, IParent<E> {

  private static final long serialVersionUID = 1L;
  private static final Logger logger_ = Logger.getLogger(AbstractSetConfig.class);
  private static final String TYPE_TAG = "Type";

  protected List<E> childList_ = new ArrayList<E>();
  protected String type_;

  /**{@inheritDoc}*/
  public List<E> getChildren() {
    return Collections.unmodifiableList(childList_);
  }

  /**
   * Adds a child to this set.
   *
   * @param argChild  child to add
   */
  @SuppressWarnings("unchecked")
  public void addChild(Object argChild) {
    if (argChild instanceof IConfigObject) {
      childList_.add((E) argChild);
    }
  }

  /**
   * Gets the type of the children
   *
   * @return  the type of the children
   */
  public String getType() {
    return type_;
  }

  /**
   * Sets the type of the children
   *
   * @param type the type of the children
   */
  protected void setType(String type) {
    type_ = type;
  }

  /**
   * Gets the tag for the children of this set.
   *
   * @return the tag for the children of this set
   */
  public abstract String getChildTag();

  /**{@inheritDoc}*/
  public void setConfigObject(String argKey, IConfigObject argValue) {
    if (argKey.equalsIgnoreCase(getChildTag())) {
      addChild(argValue);
    }
    else if (argKey.equalsIgnoreCase(TYPE_TAG)) {
      setType(argValue.toString());
    }
    else {
      warnUnsupported(argKey, argValue);
    }
  }

  /**{@inheritDoc}*/
  @SuppressWarnings("unchecked")
public void cascadeValues(IConfigObject argSourceConfig) {
    if ((argSourceConfig == null)
        || !(argSourceConfig instanceof AbstractSetConfig)) {
      logger_.error("Attempted to cascade from invalid configuration object!");
      return;
    }

    AbstractSetConfig<E> config = (AbstractSetConfig<E>) argSourceConfig;
    Collection<E> children = config.getChildren();

    if (children != null) {
      for (E child : children) {
        addChild(child);
      }
    }
  }
}
