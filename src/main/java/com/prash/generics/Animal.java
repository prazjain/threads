package com.prash.generics;

import java.io.Serializable;

public class Animal implements Serializable {
    String name;
    public Animal(String nm) { name = nm;}
    public String makeSound() { return ""; }
}

class Dog extends Animal  {

    public Dog(String nm) {
        super(nm);
    }
    public String makeSound() { return "woof woof"; }
}

class Cat extends Animal {

    public Cat(String nm) {
        super(nm);
    }
    public String makeSound() { return "purrr purrr"; }
}
