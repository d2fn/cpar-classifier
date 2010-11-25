public class Main {

    public static void main(String args[]) {
        Cmd command = Cmd.valueOf(args[0]);
        String[] classifierArgs = new String[args.length-1];
        for(int i = 1; i < args.length; i++) {
            classifierArgs[i-1] = args[i];
        }
        command.run(classifierArgs);
    }

    private enum Cmd {

        test {
            @Override
            public void run(String[] args) {
               new Tester(args[0],args[1]);
            }},
        predict {
            @Override
            public void run(String[] args) {
                new Predicter(args[0],args[1],args[2]);
            }};

        public abstract void run(String[] args);
    }
}
