package com.lifesimulator.lifesimulator.util;

import com.lifesimulator.lifesimulator.models.Player;

import java.util.concurrent.ThreadLocalRandom;

/*TODO:
Deve haver bem mais eventos.
Alguns eventos talvez só façam sentido em algumas faixas de idade.
Eventos INTERATIVOS.
Possivelmente Event se tornará uma tabela com EventType, EventStatus e tal.
Eventos em português indicam que o jogo está em português, futuramente ampliar para inglês e espanhol
 */
public enum RandomEvent {
    ACCIDENT("Aconteceu um acidente, sua saúde diminuiu!") {
        @Override
        public void apply(Player player) {
            player.setHealth(Math.max(0, player.getHealth() - ThreadLocalRandom.current().nextInt(5, 21)));
        }
    },
//    FATAL_DISEASE("Você teve uma doença fatal, morreu!") {
//        @Override
//        public void apply(Player player) {
//            player.setDead(true);
//            player.setHealth(0);
//        }
//    },
    NEW_FRIENDS("Você fez novos amigos e ficou mais feliz!") {
        @Override
        public void apply(Player player) {
            player.setHappyness(Math.min(100, player.getHappyness() + ThreadLocalRandom.current().nextInt(5, 16)));
        }
    },
    STUDY("Você começou a estudar mais e seu IQ aumentou!") {
        @Override
        public void apply(Player player) {
            player.setIq(Math.min(100, player.getIq() + ThreadLocalRandom.current().nextInt(3, 11)));
        }
    },
    EXERCISE("Você praticou exercícios e sua saúde aumentou!") {
        @Override
        public void apply(Player player) {
            player.setHealth(Math.min(100, player.getHealth() + ThreadLocalRandom.current().nextInt(3, 11)));
        }
    },
    DIFFICULT_TIME("Você passou por um momento difícil e sua felicidade diminuiu!") {
        @Override
        public void apply(Player player) {
            player.setHappyness(Math.max(0, player.getHappyness() - ThreadLocalRandom.current().nextInt(5, 16)));
        }
    },
    WORK_STRESS("Você teve um aumento de estresse devido ao trabalho!") {
        @Override
        public void apply(Player player) {
            player.setStress(Math.min(100, player.getStress() + ThreadLocalRandom.current().nextInt(5, 15)));
        }
    };

    private final String message;

    RandomEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public abstract void apply(Player player);
}