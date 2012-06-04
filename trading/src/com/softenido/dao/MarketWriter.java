/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.dao;

/**
 *
 * @author franci
 */
public interface MarketWriter
{
    boolean addMarket(Market m);
    boolean updateMarket(Market m);
    boolean deleteMarket(Market m);
}
