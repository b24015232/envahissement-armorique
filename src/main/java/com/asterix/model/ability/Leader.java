package com.asterix.model.ability;

/**
 * Interface defining the capability to lead others.
 * Used by chiefs, generals, and druids (spiritual leaders)
 */
public interface Leader {

    /**
     * Issues commands or guidance to others
     */
    void command();
}