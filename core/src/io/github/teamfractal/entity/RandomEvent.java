package io.github.teamfractal.entity;

/**
 * Created by Richard on 18/02/2017.
 */

public interface RandomEvent {
    void activate(Player player);
    String getEventName();
}