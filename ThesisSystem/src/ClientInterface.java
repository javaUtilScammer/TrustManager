

public class ClientInterface {
    private final String client_name, validation_type, key;
    private final int validation_time;
    private final double default_score, rating_scale;
    final ConnectionPool pool;
    private final Server server;
    
    public ClientInterface(String key, Configuration config, String url, Server server){
        this.key = key;
        pool = new ConnectionPool(url,server.getUsername(),server.getPassword());
        client_name = config.getClientName();
        validation_type = config.getValidationType();
        validation_time = config.getValidationTime();
        default_score = config.getDefaultScore();
        rating_scale = config.getRatingScale();
        this.server = server;
        ClientInterfaceHandler handler = new ClientInterfaceHandler();
    }
}
