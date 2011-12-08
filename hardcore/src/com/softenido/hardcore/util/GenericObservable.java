package com.softenido.hardcore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

class ObserverWrapper<S,D> implements Observer
{
    final S sender;
    final GenericObserver<S,D> observer;

    ObserverWrapper(S sender, GenericObserver<S,D> observer)
    {
        this.observer = observer;
        this.sender = sender;
    }

    public void update(Observable observable, Object data)
    {
        observer.update(sender, (D)data);
    }
}

class ChangedObservable extends Observable
{
    @Override
    public void setChanged()
    {
        super.setChanged();
    }

    @Override
    public void clearChanged()
    {
        super.clearChanged();
    }
}

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 3/12/11
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
public class GenericObservable<S,D>
{
    final Map<GenericObserver<S,D>,ObserverWrapper<S,D>> map = new HashMap<GenericObserver<S,D>,ObserverWrapper<S,D>>();
    final ChangedObservable observable = new ChangedObservable();
    final S sender;

    public GenericObservable(S sender)
    {
        this.sender = sender;
    }

    public void addObserver(GenericObserver<S,D> observer)
    {
        ObserverWrapper<S,D> wrapper = new ObserverWrapper<S,D>(sender,observer);
        map.put(observer,wrapper);
        observable.addObserver(wrapper);
    }

    public int countObservers()
    {
        return observable.countObservers();
    }

    public void deleteObserver(GenericObserver<S,D> observer)
    {
        ObserverWrapper<S,D> wrapper = map.remove(observer);
        observable.deleteObserver(wrapper);
    }

    public void deleteObservers()
    {
        observable.deleteObservers();
    }

    public boolean hasChanged()
    {
        return observable.hasChanged();
    }

    public void notifyObservers()
    {
        observable.notifyObservers();
    }

    public void notifyObservers(Object arg)
    {
        observable.notifyObservers(arg);
    }

    public void clearChanged()
    {
        observable.clearChanged();
    }

    public void setChanged()
    {
        observable.setChanged();
    }
}
