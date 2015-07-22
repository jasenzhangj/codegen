package com.hitao.codegen.configs.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Config loader that uses SAX directly.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: SaxConfigLoader.java 12 2011-02-20 10:50:23Z guest $
 */
public class SaxConfigLoader
    extends DefaultHandler
    implements IConfigLoader {

  private static final Logger logger_ = Logger.getLogger(SaxConfigLoader.class);

  private static final String DTYPE = "dtype";
  private final StringBuffer characters_ = new StringBuffer();
  private Locator locator_;
  private final LinkedList<IConfigObject> parentStack_ = new LinkedList<IConfigObject>();
  private IConfigLoaderCallback callback_;
  private URL currentFile_;

  /**
   * Receive an object for locating the origin of SAX document events.
   *
   * @param argLocator An object that can return the location of
   *                any SAX document event.
   */
  @Override
  public void setDocumentLocator(Locator argLocator) {
    locator_ = argLocator;
  }

  /**
   * Receive notification of the beginning of an element.
   *
   * <p>The Parser will invoke this method at the beginning of every
   * element in the XML document; there will be a corresponding
   * {@link #endElement endElement} event for every startElement event
   * (even when the element is empty). All of the element's content will be
   * reported, in order, before the corresponding endElement
   * event.</p>
   *
   * <p>This event allows up to three name components for each
   * element:</p>
   *
   * <ol>
   * <li>the Namespace URI;</li>
   * <li>the local name; and</li>
   * <li>the qualified (prefixed) name.</li>
   * </ol>
   *
   * <p>Any or all of these may be provided, depending on the
   * values of the <var>http://xml.org/sax/features/namespaces</var>
   * and the <var>http://xml.org/sax/features/namespace-prefixes</var>
   * properties:</p>
   *
   * <ul>
   * <li>the Namespace URI and local name are required when
   * the namespaces property is <var>true</var> (the default), and are
   * optional when the namespaces property is <var>false</var> (if one is
   * specified, both must be);</li>
   * <li>the qualified name is required when the namespace-prefixes property
   * is <var>true</var>, and is optional when the namespace-prefixes property
   * is <var>false</var> (the default).</li>
   * </ul>
   *
   * <p>Note that the attribute list provided will contain only
   * attributes with explicit values (specified or defaulted):
   * #IMPLIED attributes will be omitted.  The attribute list
   * will contain attributes used for Namespace declarations
   * (xmlns* attributes) only if the
   * <code>http://xml.org/sax/features/namespace-prefixes</code>
   * property is true (it is false by default, and support for a
   * true value is optional).</p>
   *
   * @param argNamespaceURI The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param argLocalName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param qName The qualified name (with prefix), or the
   *        empty string if qualified names are not available.
   * @param argAttributes The attributes attached to the element.  If
   *        there are no attributes, it shall be an empty
   *        Attributes object.
   * @see #endElement
   * @see org.xml.sax.Attributes
   */
  @Override
  public void startElement(
      String argNamespaceURI, String argLocalName, String qName,
      Attributes argAttributes) {

    String dtype = getDtype(qName, argAttributes);
    IConfigObject configObject = callback_.getConfigObject(
        qName, dtype, peekParent(), currentFile_.toExternalForm() + ":"
                                    + locator_.getLineNumber());
    setSourceInfo(configObject);

    for (int i = 0; i < argAttributes.getLength(); i++ ) {
      String attName = argAttributes.getQName(i);
      if (attName.indexOf(':') > -1) {
        continue;
      }

      //you can specify the indicated dynamic configuration object.
      if (DTYPE.equals(attName)) {
        continue;
      }
      String name = attName;
      String value = argAttributes.getValue(i);
      StringConfig config = new StringConfig(value);
      setSourceInfo(config);
      configObject.setConfigObject(name, config);
    }

    pushParent(configObject);

    characters_.setLength(0);
  }

  private void setSourceInfo(IConfigObject argConfigObject) {
    try {
      argConfigObject.setSourceInfo(currentFile_.toExternalForm(), locator_
          .getLineNumber());
    }
    catch (Throwable ex) {
      logger_.error("CAUGHT EXCEPTION", ex);
    }
  }

  private void pushParent(IConfigObject argParent) {
    parentStack_.addLast(argParent);
  }

  private IConfigObject peekParent() {
    if (parentStack_.isEmpty()) {
      return null;
    }
    return parentStack_.getLast();
  }

  private IConfigObject popParent() {
    return parentStack_.removeLast();
  }

  private String getDtype(String qName, Attributes argAttributes) {
    // determine the dtype for this element
    String value = argAttributes.getValue("dtype");
    if (value == null) {
      return qName;
    }
    return value;
  }

  /**
   * Receive notification of the end of an element.
   *
   * <p>The SAX parser will invoke this method at the end of every
   * element in the XML document; there will be a corresponding
   * {@link #startElement startElement} event for every endElement
   * event (even when the element is empty).</p>
   *
   * <p>For information on the names, see startElement.</p>
   *
   * @param argNamespaceURI The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param argLocalName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param qName The qualified XML 1.0 name (with prefix), or the
   *        empty string if qualified names are not available.
   * @exception org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   */
  @Override
  public void endElement(
      String argNamespaceURI, String argLocalName, String qName)
      throws SAXException {

    try {
      IConfigObject configObject = popParent();
      configObject.setValue(characters_.toString());
      characters_.setLength(0);

      IConfigObject parent = peekParent();
      if (parent != null) {
        parent.setConfigObject(qName, configObject);
        if (configObject instanceof IParentConfig){
        	((IParentConfig)configObject).setParent(parent);
        }
      }
    }
    catch (Exception ex) {
      logger_.error("CAUGHT EXCEPTION " + currentFile_ + " line "
                    + locator_.getLineNumber() + " column "
                    + locator_.getColumnNumber(), ex);
      if (ex instanceof SAXException) {
        throw (SAXException) ex;
      }
      else {
        throw new RuntimeException(ex);
      }
    }
  }

  /**
   * Receive notification of character data.
   *
   * <p>The Parser will call this method to report each chunk of
   * character data.  SAX parsers may return all contiguous character
   * data in a single chunk, or they may split it into several
   * chunks; however, all of the characters in any single event
   * must come from the same external entity so that the Locator
   * provides useful information.</p>
   *
   * <p>The application must not attempt to read from the array
   * outside of the specified range.</p>
   *
   * <p>Note that some parsers will report whitespace in element
   * content using the {@link DefaultHandler#ignorableWhitespace ignorableWhitespace}
   * method rather than this one (validating parsers <em>must</em>
   * do so).</p>
   *
   * @param argCharacters The characters from the XML document.
   * @param argStart The start position in the array.
   * @param argLength The number of characters to read from the array.
   * @see DefaultHandler#ignorableWhitespace
   * @see org.xml.sax.Locator
   */
  @Override
  public void characters(char[] argCharacters, int argStart, int argLength) {
    characters_.append(argCharacters, argStart, argLength);
  }

  /**{@inheritDoc}*/
  public void loadDocument(
      IConfigLoaderCallback argCallback, URL argCurrentFile,
      InputStream argInputStream)
      throws IOException {

    callback_ = argCallback;
    currentFile_ = argCurrentFile;

    // Create a builder factory
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(false);

    try {
      // Create the builder and parse the file
      SAXParser parser = factory.newSAXParser();
      parser.parse(argInputStream, this);
    }
    catch (ConfigException ex) {
      ex.setConfigLocationDescription(currentFile_ + " line "
                                      + locator_.getLineNumber() + " column "
                                      + locator_.getColumnNumber());
      throw ex;
    }
    catch (Error ex) {
      logger_.fatal("RETHROWING ERROR FROM " + currentFile_ + " line " + locator_.getLineNumber() + " column "
                    + locator_.getColumnNumber(), ex);
      throw ex;
    }
    catch (SAXException ex) {
      ConfigException cfex = new ConfigException(ex);
      cfex.setConfigLocationDescription(currentFile_ + " line "
                                      + locator_.getLineNumber() + " column "
                                      + locator_.getColumnNumber());
      throw cfex;
    }
    catch (ParserConfigurationException ex) {
      throw new Error("No acceptable SAX XML parser is available on this system.", ex);
    }

  }

}
