package apes.strong.together.repositories;

import apes.strong.together.services.HostDetailsService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Reads data from the provided files and passes it to matching classes.
 */
public class DataReader {


    private HostDetailsService hostDetailsService;

    /**
     * Constructor for this class requiring the collection
     *
     * @param hostDetailsService is injected into this class
     */
    public DataReader(HostDetailsService hostDetailsService) {
        this.hostDetailsService = hostDetailsService;
    }

    /**
     * Read the CCE file from the given directory line by line
     *
     * @param path  directory of the read file
     * @throws IOException when something went wrong with the file
     */
    public void loadCCEFile(String path) throws IOException {
        List<String> contentOfCCE = Files.readAllLines(Path.of(path), Charset.forName("Windows-1252"));
        contentOfCCE.remove(0);
        for (String singleLine : contentOfCCE) {

            String[] splitCCEContent = singleLine.split(",");
            hostDetailsService.addToHostNameAndHostDetailsFromCCE(splitCCEContent);
        }
    }

    /**
     * Read the SCCM file from the given directory line by line
     *
     * @param path  directory of the read file
     * @throws IOException when something went wrong with the file
     */
    public void loadSCCMFile(String path) throws IOException {
        List<String> contentOfSCCM = Files.readAllLines(Path.of(path));
        contentOfSCCM.remove(0);
        for (String singleLine : contentOfSCCM) {
            String[] splitSCMMContent = singleLine.split(";");
            hostDetailsService.addToHostNameAndHostDetailsFromSCCM(splitSCMMContent);
        }
    }

    /**
     * Read the LastScan file from the given directory line by line
     *
     * @param path  directory of the read file
     * @throws IOException when something went wrong with the file
     */
    public void loadLastScanFile(String path) throws IOException {
        List<String> contentOfLastScan = Files.readAllLines(Path.of(path));
        contentOfLastScan.remove(0);
        for (String singleLine : contentOfLastScan) {
            String[] splitLastScanContent = singleLine.split(";");
            hostDetailsService.addToHostNameAndHostDetailsFromLastScan(splitLastScanContent);
        }
    }
}