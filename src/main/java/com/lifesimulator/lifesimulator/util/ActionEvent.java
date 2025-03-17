package com.lifesimulator.lifesimulator.util;

import com.lifesimulator.lifesimulator.models.Player;

import java.util.concurrent.ThreadLocalRandom;


public enum ActionEvent {
    NEW_FRIENDS("Você fez novos amigos e ficou mais feliz!") {
        @Override
        public void apply(Player player) {
            player.setHappyness(Math.min(100, player.getHappyness() + ThreadLocalRandom.current().nextInt(5, 16)));
        }
    },
    STUDY("Você começou a estudar mais e seu IQ aumentou e seu stress também!") {
        @Override
        public void apply(Player player) {
            player.setIq(Math.min(100, player.getIq() + ThreadLocalRandom.current().nextInt(4, 11)));
            player.setStress(Math.min(100, player.getStress() + ThreadLocalRandom.current().nextInt(3, 7)));
        }
    },
    CLEAN("Você passou a se cuidar melhor esteticamente!") {
        @Override
        public void apply(Player player) {
            player.setBeauty(Math.min(100, player.getBeauty() + ThreadLocalRandom.current().nextInt(5, 10)));
        }
    },
    EXERCISE("Você praticou exercícios e sua saúde e beleza aumentaram e seu stress diminuiu!") {
        @Override
        public void apply(Player player) {
            player.setHealth(Math.min(100, player.getHealth() + ThreadLocalRandom.current().nextInt(3, 11)));
            player.setStress(Math.min(100, player.getStress() - ThreadLocalRandom.current().nextInt(3, 11)));
            player.setBeauty(Math.min(100, player.getBeauty() + ThreadLocalRandom.current().nextInt(1, 4)));
        }
    };
    private final String message;

    ActionEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public abstract void apply(Player player);
}