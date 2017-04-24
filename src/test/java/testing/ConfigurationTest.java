package testing;


import org.aeonbits.owner.ConfigFactory;

/**
 * Created by JackSB on 3/18/2017.
 */
public class ConfigurationTest {
    public static void main(String[] args){
        TestConfig cfg = ConfigFactory.create(TestConfig.class);
        System.out.println("Server " + cfg.hostname() + ":" + cfg.port() +
                " will run " + cfg.maxThreads());
    }


}
