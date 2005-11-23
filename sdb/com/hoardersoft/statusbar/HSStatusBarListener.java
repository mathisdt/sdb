package com.hoardersoft.statusbar;

/**
 * Status bar listener.
 *
 * Copyright (c) 2002 Richard Kent (richardk@cee.hw.ac.uk)
 *
 * The license for this software is contained in COPYING
 *
 * @author Richard Kent
 */
public interface HSStatusBarListener {
    /**
     * Informs the listener that the status has changed.
     *
     * @param status the status text
     */
    public void status(String status);
}
