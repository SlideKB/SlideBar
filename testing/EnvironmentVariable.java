package testing;

/**
 * Created by JackSB on 3/18/2017.
 */
public class EnvironmentVariable {

        public static void main (String[] args) {
                String env = "test";
                String value = System.getenv(env);
                if (value != null) {
                    System.out.format("%s=%s%n", env, value);
                } else {
                    System.out.format("%s is not assigned.%n", env);
                }

        }


}
