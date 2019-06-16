package br.com.appbase.dominio.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String localKey;
    private String hora;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String hora, String localKey) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.localKey = localKey;
        this.hora = hora;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLocalKey() {
        return localKey;
    }

    public void setLocalKey(String localKey) {
        this.localKey = localKey;
    }
}
