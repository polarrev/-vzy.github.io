package apes.strong.together.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.TreeSet;

/**
 * HostDetails is a class which contains hosts' properties  and simple operations.
 */
public class HostDetails {

    private boolean presentInCCE = false;
    private CiStatus ciStatus = null;
    private LocalDate statusChangeDate;
    private Integer epoID;

    private boolean presentInSCCM = false;
    private String clientName;
    private MachineType machineType;
    private Set<LocalDateTime> timeStampsSccm = new TreeSet<>();

    private boolean presentInLastScan = false;
    private Set<LocalDate> scanDates = new TreeSet<>();

    private OperatingSystem operatingSystem;
    private Persistence persistence;


    /**
     * Raises the flag that host is present in CCE file and sets its CIstatus, date of status change and epoID
     *
     * @param singleLineFromCCE  an array of parameters for a single host name extracted from a single csv line
     */

    public void setCCEProperties(String[] singleLineFromCCE) {

        this.presentInCCE = true;
        this.ciStatus = CiStatus.valueOf(singleLineFromCCE[1].toUpperCase());
        this.statusChangeDate = LocalDate.parse(singleLineFromCCE[2], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.epoID = Integer.parseInt(singleLineFromCCE[3]);
    }

    /**
     * Raises the flag that host is present in LastScan file and sets date of the last scan
     *
     * @param splitLastScanContent  an array of parameters for a single host name extracted from a single csv line
     */
    public void setLastScanProperties(String[] splitLastScanContent) {
        this.presentInLastScan = true;
        if (splitLastScanContent[splitLastScanContent.length - 1].equals("NULL")) {
            return;
        }
        String localDateInString = splitLastScanContent[splitLastScanContent.length - 1].split(" ")[0];
        this.scanDates.add(LocalDate.parse(localDateInString, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }


    /**
     * Raises the flag that host is present in SCCM file and sets client's name, machine type and timestamp of a scan
     * @param splitSCMMContent  an array of parameters for a single host name extracted from a single csv line
     */
    public void setSCCMProperties(String[] splitSCMMContent) {
        this.presentInSCCM = true;
        this.clientName = splitSCMMContent[1];
        try {
            this.machineType = MachineType.valueOf(splitSCMMContent[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            return;
        }

        this.timeStampsSccm.add(LocalDateTime.parse(splitSCMMContent[3].stripTrailing(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

    }


    /**
     * Gets machine type for a given host
     *
     * @return machine type of this host
     */
    public MachineType getMachineType(){
        return this.machineType;
    }

    /**
     * Returns information whether this host is present in CCE file
     *
     * @return true if host is present in CCE file and false otherwise
     */


    public boolean isPresentInCCE() {
        return presentInCCE;
    }

    /**
     * Returns information whether this host is present in SCCM file
     *
     * @return true if host is present in SCCM file and false otherwise
     */
    public boolean isPresentInSCCM() {
        return presentInSCCM;
    }

    /**
     * Returns information whether this host is present in LastScan file
     *
     * @return true if host is present in LastScan file and false otherwise
     */
    public boolean isPresentInLastScan() {
        return presentInLastScan;
    }


/**
     * Gets the CI status of this host
     *
     * @return what is the CIstatus of this host
     * @see CiStatus
     */
    public CiStatus getStatus() {
        return this.ciStatus;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public String toString() {
        return "HostDetails{" +
                "presentInCCE=" + presentInCCE +
                ", ciStatus=" + ciStatus +
                ", statusChangeDate=" + statusChangeDate +
                ", epoID=" + epoID +
                ", presentInSCCM=" + presentInSCCM +
                ", clientName='" + clientName + '\'' +
                ", machineType=" + machineType +
                ", timeStampsSccm=" + timeStampsSccm +
                ", presentInLastScan=" + presentInLastScan +
                ", scanDates=" + scanDates +
                ", operatingSystem=" + operatingSystem +
                ", persistence=" + persistence +
                '}';
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public Persistence getPersistence() {
        return persistence;
    }
}


