/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.text;

/**
 *
 * @author franci
 */
class UnsupportedValueException extends RuntimeException
{
    final long value;

    UnsupportedValueException(long value)
    {
        super("Unsupported value "+value);
        this.value = value;
    }
}
