package ru.neoanon.simpleweather.model;

/**
 * Created by eshtefan on  11.10.2018.
 */

public class MyPair<F, S> {
    public final F first;
    public final S second;

    public MyPair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}