package miyamoto.comunicaco_api.infraestructure.execptions;

public class IllegalStateException extends RuntimeException {

    public IllegalStateException(String mensagem) {
        super(mensagem);
    }

    public IllegalStateException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
