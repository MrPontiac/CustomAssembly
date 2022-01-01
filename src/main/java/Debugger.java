import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Debugger extends Assembly {
    public static void debug(String [] assembly) throws InterruptedException {
        Scanner scan = new Scanner(System.in);
        MachineState state = new MachineState();

        HashMap<String, Integer> functionPointers = findFunctions(assembly);
        System.out.println("Detected functions and their indices: " + functionPointers);

        int pc = getEntry(assembly);
        System.out.println("Current program counter: " + pc);
        int cyclesToSkip = 0;
        int goToLine = -1;
        for (; pc < assembly.length; pc++) {
            //grab the opcode and arguments
            String opcode = assembly[pc].split(" ")[0];
            String arg;
            if (assembly[pc].split(" ").length < 2) {
                arg = "";
            } else {
                arg = Arrays.stream(assembly[pc].split(" ")).skip(1).collect(Collectors.joining(" "));
            }
            if (cyclesToSkip > 0) {
                pc = execute(opcode, arg, pc, state, functionPointers, true);
                cyclesToSkip--;
                continue;
            } else if (goToLine > -1) {
                if (pc != goToLine) {
                    pc = execute(opcode, arg, pc, state, functionPointers, true);
                    continue;
                } else {
                    goToLine = -1;
                }
            }

//            System.out.println("\nNext command to be executed: " + assembly[pc] + "; at line " + (pc + 1));
            printInfo(pc, assembly, state);
            System.out.print("> ");
            String input = scan.nextLine();
            if (input.startsWith("next") || input.startsWith("until") || input.equals("continue")) {
                pc = execute(opcode, arg, pc, state, functionPointers, true);
            }
            else {
                while (!input.startsWith("next")) {
                    try {
                        pc = execute(input.split(" ")[0], Arrays.stream(input.split(" ")).skip(1).collect(Collectors.joining(" ")), pc, state, functionPointers, true);
                        printInfo(pc, assembly, state);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("");
                    } finally {
                        System.out.print("> ");
                        input = scan.nextLine();
                    }
                }
            }
            if (!input.equals("next") && !input.startsWith("until") && !input.equals("continue"))
                cyclesToSkip = Integer.parseInt(input.split(" ")[1]);
            else if (!input.equals("until") && !input.startsWith("next") && !input.equals("continue"))
                try {
                    goToLine = Integer.parseInt(input.split(" ")[1]) - 1;
                } catch (NumberFormatException ignored) {
                    Integer pcStopPoint = functionPointers.get(input.split(" ")[1]);
                    if (pcStopPoint != null)
                        goToLine = pcStopPoint;
                    else
                        System.out.println(input.split(" ")[1] + " is not a valid line number or label");
                }
            else if (input.equals("continue"))
                goToLine = assembly.length - 1;
        }
        System.out.println("Program has ended.");
    }

    private static void printInfo(int pc, String[] assembly, MachineState state) {
        if (pc == 0)
            System.out.println(
                    "\u2192 " + (pc + 1) + ": " + assembly[pc] + "\n" +
                            "  " + (pc + 2) + ": " + assembly[pc + 1] + "\n"
            );
        else if (pc < assembly.length - 1)
            System.out.println(
                    "  " + (pc) + ": " + assembly[pc - 1] + "\n" +
                            "\u2192 " + (pc + 1) + ": " + assembly[pc] + "\n" +
                            "  " + (pc + 2) + ": " + assembly[pc + 1] + "\n"
            );
        else
            System.out.println(
                    "  " + (pc) + ": " + assembly[pc - 1] + "\n" +
                            "\u2192 " + (pc + 1) + ": " + assembly[pc] + "\n"
            );
        System.out.println("Current MachineState: \n" + state);
    }
}
