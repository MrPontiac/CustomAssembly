import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

//a class with a large switch statement because it requires it
public class Assembly {
    //we can prevent the bot from being rate-limited so much with this
    public static ArrayList<String> printBuffer = new ArrayList<>();

    public static void rigorousMoveTest() {
        AsmTest.warmUp();
        AsmTest.testMove(1000000);
    }

    //this is a "small" function to execute the assembly code given to it
    public static void testAssembly(String[] commands, MachineState state, boolean suppressOutput) {
        long start = System.nanoTime();
        //this assembly implementation has support for functions :)
        HashMap<String, Integer> functionPointers = findFunctions(commands);

        //program counter
        int pc = getEntry(commands);

        //the main execution loop
        for (; pc < commands.length; pc++) {
            //grab the opcode and arguments
            String opcode = commands[pc].split(" ")[0];
            String arg;
            if (commands[pc].split(" ").length < 2) {
                arg = "";
            } else {
                arg = Arrays.stream(commands[pc].split(" ")).skip(1).collect(Collectors.joining(" "));
            }

            //execute it and change the program counter if necessary
            pc = execute(opcode, arg, pc, state, functionPointers, false);
        }
        long end = System.nanoTime();
        //if we need to print things then we shall
        if (!printBuffer.isEmpty() && !suppressOutput) {
            StringBuilder s = new StringBuilder();
            printBuffer.forEach(message -> s.append(message).append("\n"));
            System.out.println(s);
            printBuffer = new ArrayList<>();
        }

        if (!suppressOutput)
            System.out.println("Milliseconds taken to process: " + (end - start)/1e6 + "ms");
    }
    public static void parseAssembly(String[] commands, MachineState state) {
        //this assembly implementation has support for functions :)
        HashMap<String, Integer> functionPointers = findFunctions(commands);

        //program counter
        int pc = getEntry(commands);

        //the main execution loop
        for (; pc < commands.length; pc++) {
            //grab the opcode and arguments
            String opcode = commands[pc].split(" ")[0];
            String arg;
            if (commands[pc].split(" ").length < 2) {
                arg = "";
            } else {
                arg = Arrays.stream(commands[pc].split(" ")).skip(1).collect(Collectors.joining(" "));
            }

            //execute it and change the program counter if necessary
            pc = execute(opcode, arg, pc, state, functionPointers, false);
        }

        //if we need to print things then we shall
        if (!printBuffer.isEmpty()) {
            StringBuilder s = new StringBuilder();
            printBuffer.forEach(message -> s.append(message).append("\n"));
            System.out.println(s);
            printBuffer = new ArrayList<>();
        }
    }

    //this grabs the entry point of the assembly code (labeled global)
    protected static int getEntry(String[] commands) {
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].contains("global")) {
                return i;
            }
        }
        return 0;
    }

    //here is the gigantic switch statement detailing all the function possible, while modifying the machine state as needed
    public static int execute(String opcode, String arg, int pc, MachineState state, HashMap<String, Integer> functionPointers, boolean directOut) {
//        System.out.println("pc: " + pc + ", opcode: " + opcode + ", arg: " + arg);
        switch (opcode) {
            //generic move command
            case "mov":
                switch (arg.split(" ")[0]) {
                    case "a":
                        movIntoA(arg.split(" ")[1], state);
                        break;
                    case "x":
                        movIntoX(arg.split(" ")[1], state);
                        break;
                    case "y":
                        movIntoY(arg.split(" ")[1], state);
                        break;
                    default:
                        try {
                            Integer.parseInt(arg.split(" ")[0]);
                            throw new IllegalArgumentException("mov tried to take just a number; line: " + pc);
                        } catch (NumberFormatException ignored) {
                            movIntoVar(arg.split(" ")[0], arg.split(" ")[1], state);
                        }
                        break;
                }
                break;

            //add x and y, then store the result in a
            case "add":
                state.a = state.x + state.y;
                break;

            //reset the registers
            case "clr":
                state.a = 0;
                state.x = 0;
                state.y = 0;
                break;

            //unconditional jump
            case "jmp":
                try {
                    return Integer.parseInt(arg);
                } catch (Exception e) {
                    return functionPointers.get(arg);
                }

                //jump if x is not equal to y
            case "jne":
                try {
                    return (state.x != state.y ? Integer.parseInt(arg) : pc);
                } catch (Exception e) {
                    return (state.x != state.y ? functionPointers.get(arg) : pc);
                }

                //jump if x equals y
            case "jeq":
                try {
                    return (state.x == state.y ? Integer.parseInt(arg) : pc);
                } catch (Exception e) {
                    return (state.x == state.y ? functionPointers.get(arg) : pc);
                }

                //jump if x is greater than y
            case "jgt":
                try {
                    return (state.x > state.y ? Integer.parseInt(arg) : pc);
                } catch (Exception e) {
                    return (state.x > state.y ? functionPointers.get(arg) : pc);
                }

                //jump is x is less than y
            case "jlt":
                try {
                    return (state.x < state.y ? Integer.parseInt(arg) : pc);
                } catch (Exception e) {
                    return (state.x < state.y ? functionPointers.get(arg) : pc);
                }

                //return from a function
            case "ret":
                return state.callStack.pop();

            //call a function
            case "call":
                state.callStack.push(pc);
                return functionPointers.get(arg);

            //print a
            case "pta":
                if (!directOut) {
                    printBuffer.add("a = " + state.a);
                }
                else {
                    System.out.println("a = " + state.a);
                }
                break;

            //print x
            case "ptx":
                if (!directOut) {
                    printBuffer.add("x = " + state.x);
                }
                else {
                    System.out.println("x = " + state.x);
                }
                break;

            //print y
            case "pty":
                if (!directOut) {
                    printBuffer.add("y = " + state.y);
                }
                else {
                    System.out.println("y = " + state.y);
                }
                break;

            //print the variables
            case "ptv":
                if (!directOut) {
                    printBuffer.add("The list of variables contains " + state.variables.toString());
                }
                else {
                    System.out.println("The list of variables contains " + state.variables.toString());
                }
                break;

            //invalid opcodes do nothing
            case "#":
            default:
                break;
        }
        return pc;
    }

    protected static void movIntoY(String operand, MachineState state) {
        switch (operand) {
            case "x":
                state.y = state.x;
                break;
            case "a":
                state.y = state.a;
                break;
            default:
                try {
                    state.y = Integer.parseInt(operand);
                } catch (NumberFormatException ignored) {
                    state.y = state.variables.get(operand);
                }
                break;
        }
    }

    protected static void movIntoX(String operand, MachineState state) {
        switch (operand) {
            case "a":
                state.x = state.a;
                break;
            case "y":
                state.x = state.y;
                break;
            default:
                try {
                    state.x = Integer.parseInt(operand);
                } catch (NumberFormatException ignored) {
                    state.x = state.variables.get(operand);
                }
                break;
        }
    }

    protected static void movIntoA(String operand, MachineState state) {
        switch (operand) {
            case "x":
                state.a = state.x;
                break;
            case "y":
                state.a = state.y;
                break;
            default:
                try {
                    state.a = Integer.parseInt(operand);
                } catch (NumberFormatException ignored) {
                    state.a = state.variables.get(operand);
                }
                break;
        }
    }

    protected static void movIntoVar(String varName, String operand, MachineState state) {
        switch (operand) {
            case "a":
                state.variables.put(varName, state.a);
                break;
            case "x":
                state.variables.put(varName, state.x);
                break;
            case "y":
                state.variables.put(varName, state.y);
                break;
            default:
                try {
                    state.variables.put(varName, Double.parseDouble(operand));
                } catch (NumberFormatException ignored) {
                    state.variables.put(varName, state.variables.get(operand));
                }
                break;
        }
    }

    //grab the list of functions from the input
    protected static HashMap<String, Integer> findFunctions(String[] commands) {
        HashMap<String, Integer> functionPointers = new HashMap<>();
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].contains(":")) {
                functionPointers.put(commands[i].substring(0, commands[i].indexOf(":")).replaceAll("global ", ""), i);
            }
        }
        return functionPointers;
    }
}
