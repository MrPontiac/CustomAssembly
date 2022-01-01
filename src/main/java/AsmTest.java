//class made specifically to test the language's speed
public class AsmTest extends Assembly{
    //just warm up the JVM
    public static void warmUp() {
        MachineState shared = new MachineState();
        shared.variables.put("test", 5D);
        silentTestMovX(1000, shared);
    }

    //test the move command in a variety of ways
    public static void testMove(int iter) {
        MachineState shared = new MachineState();
        shared.variables.put("test", 5D);
        testMovX(iter, shared);
        testMovY(iter, shared);
        testMovA(iter, shared);
        testMovVar(iter, shared);
    }

    //helper for the warm-up function
    //calls movIntoX without any timing info
    public static void silentTestMovX(int iter, MachineState shared) {
        for (int i = 0; i < iter; i++) {
            movIntoX("1", shared);
        }

        for (int i = 0; i < iter; i++) {
            movIntoX("test", shared);
        }
    }

    //times mov x in various scenarios
    public static void testMovX(int iter, MachineState shared) {
        System.out.println("Testing movIntoX with number...");
        long start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoX("1", shared);
        }
        long end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");


        System.out.println("Testing movIntoX with variable...");
        start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoX("test", shared);
        }
        end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");
    }

    //times mov y in various scenarios
    public static void testMovY(int iter, MachineState shared) {
        System.out.println("Testing movIntoY with number...");
        long start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoY("1", shared);
        }
        long end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");


        System.out.println("Testing movIntoY with variable...");
        start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoY("test", shared);
        }
        end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");
    }

    //times mov a in various scenarios
    public static void testMovA(int iter, MachineState shared) {
        System.out.println("Testing movIntoA with number...");
        long start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoA("1", shared);
        }
        long end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");


        System.out.println("Testing movIntoA with variable...");
        start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoA("test", shared);
        }
        end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");
    }

    //times mov [var] in various scenarios
    public static void testMovVar(int iter, MachineState shared) {
        System.out.println("Testing movIntoVar with number...");
        long start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoVar("lol", "1", shared);
        }
        long end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");


        System.out.println("Testing movIntoVar with register...");
        start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoVar("lol", "x", shared);
        }
        end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");

        System.out.println("Testing movIntoVar with variable...");
        start = System.nanoTime();
        for (int i = 0; i < iter; i++) {
            movIntoVar("lol", "test", shared);
        }
        end = System.nanoTime();
        System.out.println("Avg: " + ((end - start) / iter) / 1e6 + "ms, Raw: " + (end - start) / 1e6 + "ms\n");
    }
}
