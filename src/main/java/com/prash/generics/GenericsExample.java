package com.prash.generics;

public class GenericsExample {

    public static void main(String[] args) {
        Printer<Animal> printer = new Printer<>(new Dog("Chase"));
        printer.print();
        printer.shout("Paw Patrol");
    }
}
