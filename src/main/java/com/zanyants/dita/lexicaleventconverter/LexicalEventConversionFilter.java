/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zanyants.dita.lexicaleventconverter;

import javax.xml.parsers.SAXParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author tom.glastonbury
 */
public class LexicalEventConversionFilter extends XMLFilterImpl implements LexicalHandler {

    private static final String SAX_PARSER_FEATURE_LEXICALHANDLER = "http://xml.org/sax/properties/lexical-handler";

    LexicalHandler originalLexicalHandler;

    public LexicalEventConversionFilter() {
    }

    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == SAX_PARSER_FEATURE_LEXICALHANDLER){
            originalLexicalHandler=(LexicalHandler)value;
        } else {
            super.setProperty(name, value);
        }
    }
    
    @Override
    public void setParent(XMLReader parent) {
        XMLReader currentParent = super.getParent();
        if (currentParent != null) {
            try {
                currentParent.setProperty(SAX_PARSER_FEATURE_LEXICALHANDLER, originalLexicalHandler);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        }

        super.setParent(parent);

        if (parent == null) {
            originalLexicalHandler = null;
        } else {
            try {
                originalLexicalHandler = (LexicalHandler) parent.getProperty(SAX_PARSER_FEATURE_LEXICALHANDLER);
                parent.setProperty(SAX_PARSER_FEATURE_LEXICALHANDLER, this);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        getContentHandler().processingInstruction("doctype-public", publicId);
        getContentHandler().processingInstruction("doctype-system", systemId);
        if (originalLexicalHandler != null) {
            originalLexicalHandler.startDTD(name, publicId, systemId);
        }
    }

    @Override
    public void endDTD() throws SAXException {
        if (originalLexicalHandler != null) {
            originalLexicalHandler.endDTD();
        }
    }

    @Override
    public void startEntity(String name) throws SAXException {
        if (originalLexicalHandler != null) {
            originalLexicalHandler.startEntity(name);
        }
    }

    @Override
    public void endEntity(String name) throws SAXException {
        if (originalLexicalHandler != null) {
            originalLexicalHandler.endEntity(name);
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        if (originalLexicalHandler != null) {
            originalLexicalHandler.startCDATA();
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        if (originalLexicalHandler != null) {
            originalLexicalHandler.endCDATA();
        }
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (originalLexicalHandler != null) {
            originalLexicalHandler.comment(ch, start, length);
        }
    }
}
