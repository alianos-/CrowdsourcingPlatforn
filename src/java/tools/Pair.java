/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package tools;

import java.io.Serializable;

/**
 */
public class Pair<A, B> implements Serializable {

    private static final long serialVersionUID = 7526475874355726147L;

   
    A valueA;
    B valueB;

    public Pair( A valueA, B valueB ) {
        this.valueA = valueA;
        this.valueB = valueB;
    }

    public A getValueA() {
        return valueA;
    }

    public void setValueA( A valueA ) {
        this.valueA = valueA;
    }

    public B getValueB() {
        return valueB;
    }

    public void setValueB( B valueB ) {
        this.valueB = valueB;
    }

    @Override
    public String toString() {
        return valueA + " - " + valueB;
    }

    @Override
    public boolean equals( Object that ) {
        if( !(that instanceof Pair) ) {
            return false;
        }

        Pair thatPair = (Pair) that;
        if( this.getValueA().equals( thatPair.getValueA() ) && this.getValueB().equals( thatPair.getValueB() ) ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.valueA != null ? this.valueA.hashCode() : 0);
        hash = 97 * hash + (this.valueB != null ? this.valueB.hashCode() : 0);
        return hash;
    }
}
