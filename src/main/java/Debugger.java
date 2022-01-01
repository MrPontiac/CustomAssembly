import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Debugger extends Assembly {
    public static void debug(String [] assembly){
        Scanner scan = new Scanner(System.in);
        MachineState state = new MachineState();

        //initial debug info
        HashMap<String, Integer> functionPointers = findFunctions(assembly);
        System.out.println("Detected functions and their indices: " + functionPointers);

        //a controllable main loop
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

            //print the state
            printInfo(pc, assembly, state);
            System.out.print("> ");
            String input = scan.nextLine();
            //here we ask if the user wants to go to the next line, run until a certain line, or just execute the whole program
            if (input.startsWith("next") || input.startsWith("until") || input.equals("continue")) {
                pc = execute(opcode, arg, pc, state, functionPointers, true);
            }
            //if nothing else, we try to execute whatever opcode and arguments the user inputs
            else {
                while (!input.startsWith("next") || !input.startsWith("until") || !input.equals("continue")) {
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
            //this is for the next command, i.e. skip the next 5 lines via next 5
            if (!input.equals("next") && !input.startsWith("until") && !input.equals("continue"))
                cyclesToSkip = Integer.parseInt(input.split(" ")[1]);
            //if the command is until then we go until we hit a certain line, i.e. until 20
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
            //continue goes till the end
            else if (input.equals("continue"))
                goToLine = assembly.length - 1;
        }
        System.out.println("Program has ended.");
    }

    //print info prints the code near the currently executing code
    //as well as stuff from the machine state
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
