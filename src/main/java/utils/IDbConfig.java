package utils;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:db.properties")
public interface IDbConfig extends Config {
    String url();
    String db_name();
    String username();
    String password();
}
