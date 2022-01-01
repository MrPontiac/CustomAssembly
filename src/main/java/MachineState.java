import java.util.HashMap;
import java.util.Stack;

//a simple way of storing a state for the assembly parser
public class MachineState {
    public double x = 0;
    public double y = 0;
    public double a = 0;
    public HashMap<String, Double> variables = new HashMap<>();
    public Stack<Integer> callStack = new Stack<>();

    public MachineState() {

    }

    public MachineState(int x, int y, int a, HashMap<String, Double> variables, Stack<Integer> callStack) {
        this.x = x;
        this.y = y;
        this.a = a;
        this.variables = variables;
        this.callStack = callStack;
    }

    @Override
    public String toString() {
        return "x = " + x + "\n" +
                "y = " + y + "\n" +
                "a = " + a + "\n" +
                "variables = " + variables.toString() + "\n" +
                "callStack = " + callStack.toString() + "\n";
    }
}
