package de.kimminich.kata.tcg;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;

public class Card implements Comparable<Card> {

    private final int manaCost;

    public static List<Card> list(Integer... manaCosts) {
        return stream(manaCosts).map(Card::new).collect(toCollection(ArrayList::new));
    }

    public Card(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getDamage() {
        return manaCost;
    }

    @Override
    public String toString() {
        return "" + manaCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (manaCost != card.manaCost) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return manaCost;
    }

    @Override
    public int compareTo(Card other) {
        return manaCost - other.getManaCost();
    }
}
