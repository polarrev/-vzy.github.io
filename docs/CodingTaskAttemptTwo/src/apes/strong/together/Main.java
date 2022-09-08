package apes.strong.together;

import apes.strong.together.repositories.DataReader;
import apes.strong.together.services.HostDetailsService;

import java.io.IOException;

/**
 * This contains main method of a project.
 * Creates new instance of HostDetailsService and DataReader, which further loads three kinds of csv files:
 * CCE, LastScan and SCCM.
 *
 */
public class Main {

    public static void main(String[] args) {

        HostDetailsService hostDetailsService = new HostDetailsService();
        DataReader dataReader = new DataReader(hostDetailsService);

        try {
            dataReader.loadCCEFile("CodingTaskAttemptTwo/src/apes/strong/together/resource/files/CCE_EXP.csv");
            dataReader.loadLastScanFile("CodingTaskAttemptTwo/src/apes/strong/together/resource/files/LastScan.csv");
            dataReader.loadSCCMFile("CodingTaskAttemptTwo/src/apes/strong/together/resource/files/SCCM_AllResponsible.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }


        hostDetailsService.assignOperatingSystemToHosts();
        hostDetailsService.assignPersistenceToHosts();

        hostDetailsService.printOccurrenceForAllHosts();


    }
}
