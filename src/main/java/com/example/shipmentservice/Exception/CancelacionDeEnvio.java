package com.example.shipmentservice.Exception;

public class CancelacionDeEnvio extends RuntimeException {
    public CancelacionDeEnvio(String message) {
        super(message);
    }
}
