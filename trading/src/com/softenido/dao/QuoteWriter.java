/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.dao;

/**
 *
 * @author franci
 */
public interface QuoteWriter
{
    boolean addQuote(Quote m);
    boolean updateQuote(Quote m);
    boolean deleteQuote(Quote m);
}
