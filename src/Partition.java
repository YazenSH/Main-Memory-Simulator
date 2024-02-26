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
public class Partition {
    Long StartAddress;
    Long Size;
    String ProcessName;
    public Partition(Long StartAddress, Long Size, String ProcessName) {
        this.StartAddress = StartAddress;
        this.Size = Size;
        this.ProcessName = ProcessName;
    }
    public void setProcessName(String ProcessName) {
        this.ProcessName = ProcessName;
    }

    public void setStartAddress(Long StartAddress) {
        this.StartAddress = StartAddress;
    }

    public String getProcessName() {
        return ProcessName;
    }

    public Long getStartAddress() {
        return StartAddress;
    }

    public Long getSize() {
        return Size;
    }

    public void addHole(Long size) {
        this.Size += size;
    }

}