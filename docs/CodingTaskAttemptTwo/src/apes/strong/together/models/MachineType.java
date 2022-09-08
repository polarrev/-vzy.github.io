package apes.strong.together.models;

/**
 * Possible MachineType
 * @see #SRV
 * @see #WKS
 * @see #UNKNOWN
 */

public enum MachineType {
    /**
     * Host is a server
     */
    SRV,

    /**
     * Host is a workstation
     */
    WKS,

    /**
     * Type of host is unknown
     */
    UNKNOWN
}
