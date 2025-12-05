package com.asterix.model.ability;
/**
 * Interface defining the capability to perform work.
 * Any character implementing this interface must define how they work.
 */
public interface Worker {

    /**
     * Performs the working action specific to the job.
     */
    void work();
}