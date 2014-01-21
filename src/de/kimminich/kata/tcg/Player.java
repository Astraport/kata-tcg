package de.kimminich.kata.tcg;

import de.kimminich.kata.tcg.exception.IllegalMoveException;
import de.kimminich.kata.tcg.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

public class Player {

    private static final Logger logger = Logger.getLogger(Game.class.getName());

    Random random = new Random();

    private static final int STARTING_HAND_SIZE = 3;
    private static final int MAXIMUM_HAND_SIZE = 5;

    private int health = 30;

    private int manaSlots = 0;
    private int mana = 0;

    private List<Card> deck = Card.list(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8);
    private List<Card> hand = new ArrayList<>();

    private final Strategy strategy;
    private final String name;

    public Player(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    Player(String name, Strategy strategy, int health, int manaSlots, int mana, List<Card> deck, List<Card> hand) {
        this.name = name;
        this.strategy = strategy;
        this.health = health;
        this.manaSlots = manaSlots;
        this.mana = mana;
        this.deck = deck;
        this.hand = hand;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getNumberOfDeckCardsWithManaCost(int manaCost) {
        return (int) deck.stream().filter(card -> card.getManaCost() == manaCost).count();
    }

    public int getNumberOfDeckCards() {
        return deck.size();
    }

    public Integer getNumberOfHandCardsWithManaCost(int manaCost) {
        return (int) hand.stream().filter(card -> card.getManaCost() == manaCost).count();
    }

    public int getNumberOfHandCards() {
        return hand.size();
    }

    public void drawCard() {
        if (getNumberOfDeckCards() == 0) {
            logger.info(this + " bleeds out!");
            health--;
        } else {
            Card card = deck.get(random.nextInt(deck.size()));
            deck.remove(card);
            logger.info(this + " draws card: " + card);
            if (getNumberOfHandCards() < MAXIMUM_HAND_SIZE) {
                hand.add(card);
            } else {
                logger.info(this + " drops card " + card + " from overload!");
            }
        }
    }

    public int getManaSlots() {
        return manaSlots;
    }

    public void giveManaSlot() {
        manaSlots++;
    }

    public void refillMana() {
        mana = manaSlots;
    }

    public void drawStartingHand() {
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            drawCard();
        }
    }

    public void playCard(Card card, Player opponent) {
        if (mana < card.getManaCost()) {
            throw new IllegalMoveException("Insufficient Mana (" + mana + ") to pay for card " + card + ".");
        }
        logger.info(this + " plays card: " + card);
        mana -= card.getManaCost();
        hand.remove(card);
        opponent.receiveDamage(card.getDamage());
    }

    private void receiveDamage(int damage) {
        health -= damage;
    }

    public boolean canPlayCards() {
        return hand.stream().filter(card -> card.getManaCost() <= mana).count() > 0;
    }

    public void playCard(Player opponent) {
        Optional<Card> card = strategy.nextCard(mana, hand);
        if (card.isPresent()) {
            playCard(card.get(), opponent);
        } else {
            throw new IllegalMoveException("No card can be played from hand " + hand + " with (" + mana + ") mana.");
        }
    }

    @Override
    public String toString() {
        return "Player:" + name + "{" +
                "health=" + health +
                ", mana=" + mana + "/" + manaSlots +
                ", hand=" + hand +
                ", deck=" + deck +
                '}';
    }

}
