package apes.strong.together.services;

import apes.strong.together.models.HostDetails;
import apes.strong.together.models.OperatingSystem;
import apes.strong.together.models.Persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates collection HostDetails which stores host's parameters
 */
public class HostDetailsService {

    private Map<String, HostDetails> hostNameAndHostDetails = new HashMap<>();

    /**
     * Creates an entry for a host if it's not present in the collection.
     * Takes an array of host's parameters from a SCCM file and adds them to the collection.
     *
     * @param splitSCCMContent single line from a SCCM file split into parameters
     */
    public void addToHostNameAndHostDetailsFromSCCM(String[] splitSCCMContent) {
        String hostname = splitSCCMContent[0].toUpperCase().trim();
        addEntryIfHostNotFound(hostname);
        hostNameAndHostDetails.get(hostname).setSCCMProperties(splitSCCMContent);
    }

    /**
     * Creates an entry for a host if it's not present in the collection.
     * Takes an array of host's parameters from a LastScan file and adds them to the collection.
     *
     * @param splitLastScanContent single line from a LastScan file split into parameters
     */
    public void addToHostNameAndHostDetailsFromLastScan(String[] splitLastScanContent) {

        int hostNameIndex = splitLastScanContent.length - 2;
        String hostName = splitLastScanContent[hostNameIndex].toUpperCase().trim();
        addEntryIfHostNotFound(hostName);

        hostNameAndHostDetails.get(hostName).setLastScanProperties(splitLastScanContent);
    }

    /**
     * Creates an entry for a host if it's not present in the collection.
     * Takes an array of host's parameters from a CCE file and adds them to the collection.
     *
     * @param splitCCEContent single line from a CCE file split into parameters
     */
    public void addToHostNameAndHostDetailsFromCCE(String[] splitCCEContent) {

        String hostName = splitCCEContent[0].toUpperCase().trim();

        addEntryIfHostNotFound(hostName);

        hostNameAndHostDetails.get(hostName).setCCEProperties(splitCCEContent);

    }

    /**
     * Calculates how many hosts are in each file
     *
     * @return array of Integers containing information about occurrence of hosts
     */
    public Integer[] calculateOccurrenceTable() {

        Integer[] result = {0, 0, 0, 0, 0, 0, 0};

        for (Map.Entry<String, HostDetails> entry : hostNameAndHostDetails.entrySet()) {

            result[calculateArrayPosition(entry.getValue())] += 1;
        }
        return result;
    }



    /**
     * Export information about hosts: whether it is present in SCCM, CCE and LastScan files, what is its operating
     * system and is it persistent
     *
     */
    public void printOccurrenceForAllHosts() {

        try (FileWriter output = new FileWriter("occurrenceForAllHosts.csv")) {
            output.append("HostName,SCCM,CCE,LASTSCAN,OPERATINGSYSTEM,PERSISTENCE").append(System.lineSeparator());
            for (Map.Entry<String, HostDetails> entry : hostNameAndHostDetails.entrySet()) {
                formatCSVOutput(output, entry);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets what operating system is installed on the machine
     */
    public void assignOperatingSystemToHosts() {

        hostNameAndHostDetails
                .keySet()
                .forEach(this::assignOperatingSystemToSpecificHost);
    }

    /**
     * Sets if host is persistent, non-persistent or unknown
     */
    public void assignPersistenceToHosts() {

        for (String hostName : hostNameAndHostDetails.keySet()) {

            assignPersistenceToSpecificHost(hostName);
        }

    }

    private void assignPersistenceToSpecificHost(String hostName) {
        if (hostName.contains("WS") && hostName.startsWith("SA")) {
            hostNameAndHostDetails.get(hostName).setPersistence(Persistence.PERSISTENT);
        } else if (hostName.contains("PW") && hostName.startsWith("SA")) {
            hostNameAndHostDetails.get(hostName).setPersistence(Persistence.NON_PERSISTENT);
        } else {
            hostNameAndHostDetails.get(hostName).setPersistence(Persistence.UNKNOWN);
        }
    }

    private void assignOperatingSystemToSpecificHost(String hostName) {
        if (hostName.contains("SV") || hostName.startsWith("SA160PS") || hostName.startsWith("SF160PS") || hostName.startsWith("SF475PS")) {
            hostNameAndHostDetails.get(hostName).setOperatingSystem(OperatingSystem.WINDOWS);
        } else if (hostName.contains("LX")) {
            hostNameAndHostDetails.get(hostName).setOperatingSystem(OperatingSystem.LINUX);
        } else if (hostName.contains("AIX") || hostName.contains("AX")) {
            hostNameAndHostDetails.get(hostName).setOperatingSystem(OperatingSystem.AIX);
        } else {
            hostNameAndHostDetails.get(hostName).setOperatingSystem(OperatingSystem.UNKNOWN);
        }

    }

    private void formatCSVOutput(FileWriter output, Map.Entry<String, HostDetails> entry) throws IOException {
        int scmmOccurrence = entry.getValue().isPresentInSCCM() ? 1 : 0;
        int cceOccurrence = entry.getValue().isPresentInCCE() ? 1 : 0;
        int lastScanOccurrence = entry.getValue().isPresentInLastScan() ? 1 : 0;
        output.append(entry.getKey())
                .append(',')
                .append(String.valueOf(scmmOccurrence))
                .append(',')
                .append(String.valueOf(cceOccurrence))
                .append(',')
                .append(String.valueOf(lastScanOccurrence))
                .append(",")
                .append(entry.getValue().getOperatingSystem().toString())
                .append(",")
                .append(entry.getValue().getPersistence().toString())
                .append(System.lineSeparator());
        output.flush();
    }

    private Integer calculateArrayPosition(HostDetails hostDetails) {
        int toReturn = 0;
        if (hostDetails.isPresentInSCCM()) toReturn += 4;
        if (hostDetails.isPresentInCCE()) toReturn += 2;
        if (hostDetails.isPresentInLastScan()) toReturn += 1;
        return toReturn - 1;
    }


    private void addEntryIfHostNotFound(String hostName) {
        if (!hostNameAndHostDetails.containsKey(hostName)) {
            hostNameAndHostDetails.put(hostName, new HostDetails());
        }
    }

    /**
     * Gets collection containing hosts' names and their details
     *
     * @return collection with hosts and their parameters
     */
    public Map<String, HostDetails> getHostNameAndHostDetails() {
        return hostNameAndHostDetails;
    }
}
