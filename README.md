**Flow Log Parser**

**Overview**
This Java program parses flow log data from a file and maps each row to a tag based on a lookup table provided in a CSV file. The program generates an output file containing the count of matches for each tag and for each port/protocol combination.

**#Assumptions Made**
1. Log Format: The program only supports the default AWS VPC Flow Log format (version 2). Any custom formats are not supported.
2. Protocol Handling: The protocol numbers are mapped as follows:
    6 → TCP
    17 → UDP
    1 → ICMP
3. Tagging: If a port/protocol combination is not found in the lookup table, it is marked as "Untagged."
4. Input Constraints: The flow log file size can be up to 10 MB, and the lookup table can contain up to 10,000 mappings.
5. Operating Environment: The program is designed to run on any standard Java environment without requiring additional libraries or        
   dependencies.
