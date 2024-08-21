import java.io.*;
import java.util.*;

public class FlowLogParser {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java FlowLogParser <flow_log_file> <lookup_table_file>");
            return;
        }

        String flowLogFile = args[0];
        String lookupTableFile = args[1];

        // Load the lookup table from the CSV file
        Map<String, String> lookupTable = loadLookupTable(lookupTableFile);

        // Initialize maps to store counts for tags and port/protocol combinations
        Map<String, Integer> tagCounts = new HashMap<>();
        Map<String, Integer> portProtocolCounts = new HashMap<>();

        // Process each line in the flow log file
        processFlowLogs(flowLogFile, lookupTable, tagCounts, portProtocolCounts);

        // Write the results to the output file
        writeResultsToFile("output.txt", tagCounts, portProtocolCounts);

        System.out.println("Processing complete. Results written to output.txt");
    }

    private static Map<String, String> loadLookupTable(String lookupTableFile) {
        Map<String, String> lookupTable = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(lookupTableFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length == 3) {
                    String key = parts[0].trim() + "_" + parts[1].trim().toLowerCase();
                    String tag = parts[2].trim();
                    lookupTable.put(key, tag);
                    System.out.println("Added to lookup table: " + key + " -> " + tag);
                } else {
                    System.out.println("Skipping malformed line in lookup table: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading lookup table file: " + e.getMessage());
        }
        return lookupTable;
    }

    private static void processFlowLogs(String flowLogFile, Map<String, String> lookupTable,
                                        Map<String, Integer> tagCounts,
                                        Map<String, Integer> portProtocolCounts) {
        try (BufferedReader logReader = new BufferedReader(new FileReader(flowLogFile))) {
            String line;
            while ((line = logReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] logParts = line.split("\\s+");
                if (logParts.length < 14) {
                    System.out.println("Skipping malformed line in flow log: " + line);
                    continue;
                }

                String dstPort = logParts[6];
                String protocolNumber = logParts[7];
                String protocolName = getProtocolName(protocolNumber);

                if (protocolName.equals("unknown")) {
                    System.out.println("Unknown protocol for line: " + line);
                    continue;
                }

                String key = dstPort + "_" + protocolName;
                String tag = lookupTable.getOrDefault(key, "Untagged");

                // Update tag counts
                tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

                // Update port/protocol combination counts
                String portProtocolKey = dstPort + "," + protocolName;
                portProtocolCounts.put(portProtocolKey, portProtocolCounts.getOrDefault(portProtocolKey, 0) + 1);
            }
        } catch (IOException e) {
            System.err.println("Error reading flow log file: " + e.getMessage());
        }
    }

    private static void writeResultsToFile(String outputFileName, Map<String, Integer> tagCounts,
                                           Map<String, Integer> portProtocolCounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write("Tag Counts:\n");
            writer.write("Tag,Count\n");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }

            writer.write("\nPort/Protocol Combination Counts:\n");
            writer.write("Port,Protocol,Count\n");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing results to file: " + e.getMessage());
        }
    }

    /**
     * Converts a protocol number to its corresponding protocol name.
     *
     * @param protocolNumber The protocol number as a string.
     * @return The protocol name as a string.
     */
    private static String getProtocolName(String protocolNumber) {
        switch (protocolNumber) {
            case "6":
                return "tcp";
            case "17":
                return "udp";
            case "1":
                return "icmp";
            default:
                return "unknown";
        }
    }
}
