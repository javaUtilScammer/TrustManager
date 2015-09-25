

public class ClientInterface {
    private final String client_name, validation_type;
    private final int validation_time;
    
    public ClientInterface(Configuration config){
        client_name = config.getClientName();
        validation_type = config.getValidationType();
        validation_time = config.getValidationTime();
        
    }
}
