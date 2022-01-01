import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Warming up JVM...");
        AsmTest.warmUp();

        System.out.println("Loading commands...");
        String asm = Files.lines(new File(System.getProperty("user.dir") + "/asm").toPath()).collect(Collectors.joining("\n"));

        Scanner scan = new Scanner(System.in);
        System.out.println("Do you wish to run or debug the file? [r/d] (default is run)");

        String input = scan.nextLine();
        if (input.equals("d")) {
            Debugger.debug(asm.split("\n"));
        } else {
            Assembly.parseAssembly(asm.split("\n"), new MachineState());
        }
    }
}
