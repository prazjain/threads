package com.prash.generics;

import java.io.Serializable;

/**
 * https://www.youtube.com/watch?v=K1iu1kXkVoA
 * Generics in Java - Full simple tutorial
 * T can extend multiple types, but only one can be class type and it has to be first in declaration, after that interfaces can come
 */
public class Printer<T extends Animal & Serializable> {
    T thingToPrint;

    public Printer(T thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    public <E> void shout(E thingToShout) {
        System.out.println(thingToPrint.makeSound() + " " + thingToShout);
    }

    public void print() {
        System.out.println(thingToPrint.makeSound());
    }
}
