package apes.strong.together.models;

/**
 * Possible CIStatus
 * @see #INSTALLED
 * @see #RETIRED
 * @see #ORDERED
 * @see #RESERVED
 * @see #TESTPOWEROFF
 */
public enum CiStatus {
    /**
     * Device has installed software and is ready
     */
    INSTALLED,
    /**
     * Device has been scanned before, but is no longer used
     */
    RETIRED,
    /**
     * Device is ordered, but not yet delivered
     */
    ORDERED,
    /**
     * Part of the server is temporarily disabled
     */
    RESERVED,
    /**
     * Server was unavailable during the scan
     */
    TESTPOWEROFF
}
