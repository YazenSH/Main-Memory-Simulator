/**
 Group ID: 1
 Yazan Yahya Alshaebi			        ID: 2142647
 Mohanad Sulaiman Ali Al Dakheel         ID: 2135847
 Ammar Abdulilah Omar Bin Madi	        ID: 2135146
 Fahad Adil Alghamdi                     ID: 2135938

 - Hardware
 CPU: AMD Ryzen 5800hs 3.20 GHz
 RAM: 16GB
 OS: Windows 11.0
 IDE: IntelliJ IDEA 2023.1 (Ultimate Edition)
 Runtime version: 17.0.6+10-b829.5 amd64
 VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
 **/
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
public class Main {
    static ArrayList<Partition> Memory = new ArrayList<>();
    static Long memorySize;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("---------------------------------------------------------");
        System.out.print("Enter your Main Memory size: ");
        memorySize = input.nextLong();

        //initialize the memory as one big unused hole
        Memory.add(new Partition(0L, memorySize, "Unused"));

        System.out.println("1- RQ: Request command: ");
        System.out.println("2- RL: Release command: ");
        System.out.println("3- C: Compact command: ");
        System.out.println("4- STAT: Report  command: ");
        System.out.println("5- X: Exit the program: ");
        System.out.println("---------------------------------------------------------");

        String choose;
        while (true) {

            choose = input.next();

            switch (choose.toUpperCase()) {
                case "RQ" -> rq();
                case "RL" -> rl();
                case "C" -> compact();
                case "STAT" -> STAT();
                case "X" -> System.exit(0);
                default -> System.out.println("Wrong input, try again");
            }
        }

    }
//----------------------------------------------------------------------------------------------------------------------
    //Release Function
    public static void rl() {
        System.out.print("Enter the process name: ");
        String name = input.next();
        int indexP = -1;

        for (int i = 0; i < Memory.size(); i++) {
            if (Memory.get(i).getProcessName().equalsIgnoreCase(name)) {
                indexP = i;
            }

        //handle error
        }
        if (indexP == -1) {
            System.out.println("Release Rejected, process "+name+" Doesn't exist. ");
            return;
        }
        //assign as an empty hole
        Memory.get(indexP).setProcessName("Unused");
        //check of hole before
        if (indexP != 0 && Memory.get(indexP-1).getProcessName().equalsIgnoreCase("Unused")){
            Memory.get(indexP-1).addHole(Memory.get(indexP).getSize());
            Memory.remove(indexP);
            indexP--;
        }
        //check if Hole After
        if (indexP < Memory.size()-1 && Memory.get(indexP+1).getProcessName().equalsIgnoreCase("Unused")){
            Memory.get(indexP).addHole(Memory.get(indexP + 1).getSize());
            Memory.remove(indexP + 1);
        }
        System.out.println("Release Accepted");
    }
//----------------------------------------------------------------------------------------------------------------------
    //Compact
    public static void compact() {
        //find all holes size around the memory and delete them
        Long holes_sizes = 0L;
        for (int i = 0; i < Memory.size(); ++i) {
            if (Memory.get(i).getProcessName().equalsIgnoreCase("Unused")) {
                holes_sizes += Memory.get(i).getSize();
                Memory.remove(i);
                i--; //since the list will be shifted, then we have to decrement the counter
            }
        }
        //shuffle all process
        Long track =0L;
        for (Partition partition : Memory) {
            if (!Objects.equals(partition.getStartAddress(), track)) {
                partition.setStartAddress(track); // change the start address
            }
            track = partition.getStartAddress() + partition.getSize();

        }

        Memory.add(new Partition(track, holes_sizes,"Unused"));
        System.out.println("compact Accepted");
    }
//----------------------------------------------------------------------------------------------------------------------
//STAT
    public static void STAT() {
        for (Partition partition : Memory) {
            String prName = partition.getProcessName();
            if (!prName.equalsIgnoreCase("Unused")) {
                prName = "Process " + partition.getProcessName();
            }
            System.out.println("Addresses [" + partition.getStartAddress() + " : " +
                    (partition.getStartAddress() + partition.getSize() - 1) + "] " + prName);
        }
    }
//------------------------------------------------------------------------------------------------------------
    //Request Function with Best fit , Worst fit and First fit
    public static void rq() {
        System.out.print("Enter the name of process: ");
        String ProcessName = input.next();
        System.out.print("Enter the size of process: ");
        Long ProcessSize = input.nextLong();
        System.out.print("Enter the policy type, f for First fit, w for Worst fit and b for Best fit: ");
        String algorithm = input.next();
        //to handle if there is no place in memory
        int i;
        switch (algorithm.toLowerCase()) {
            case "f" -> i = FirstFit(ProcessSize);
            case "w" -> i = WorstFit(ProcessSize);
            case "b" -> i = BestFit(ProcessSize);
            default -> {
                System.out.println("Bad input. Request Failed");
                return;
            }
        }
        if (i == -1) {
            System.out.println("Request Rejected. There is no place in memory");
        } else {
            Long new_index = Memory.get(i).getStartAddress();
            Long possibleHole = Memory.get(i).getSize();
            Memory.remove(i);
            Partition process = new Partition(new_index,ProcessSize,ProcessName);
            Memory.add(i, process);
            //Check if there is a hole, and adding it
            if ((possibleHole - ProcessSize) != 0) {
                Memory.add(i + 1, new Partition(process.getStartAddress() + ProcessSize,
                        possibleHole - ProcessSize, "Unused"));
            }
            System.out.println("Request Accepted");
        }
    }

    public static int FirstFit(Long size) {
        for (int i = 0; i < Memory.size(); ++i)
            if (Memory.get(i).getProcessName().equals("Unused") && Memory.get(i).getSize() >= size)
                return i;
        return -1;
    }
    public static int BestFit( Long size) {
        int index = -1;
        Long currentMin = Long.MAX_VALUE;

        for (int i = 0; i < Memory.size(); ++i) {
            if (Memory.get(i).getProcessName().equalsIgnoreCase("Unused")) {
                if (Memory.get(i).getSize() >= size && Memory.get(i).getSize() < currentMin) {
                    currentMin = Memory.get(i).getSize();
                    index = i;
                }
            }
        }
        return index;

    }
    public static int WorstFit(Long size) {
        int index = -1;
        Long currentMax = 0L;
        for (int i = 0; i < Memory.size(); ++i) {
            if (Memory.get(i).getProcessName().equals("Unused")) {
                if (Memory.get(i).getSize() >= size && Memory.get(i).getSize() > currentMax) {
                    currentMax = Memory.get(i).getSize();
                    index = i;
                }
            }
        }
        return index;
    }
}

