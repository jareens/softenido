package com.softenido.hardcore.util;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 3/12/11
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
public interface GenericObserver<S,D>
{
    void update(S sender, D data);
}
