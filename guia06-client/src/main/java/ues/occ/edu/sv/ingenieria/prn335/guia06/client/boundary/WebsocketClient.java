/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia06.client.boundary;

import java.io.IOException;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author mario
 */
@ClientEndpoint
public class WebsocketClient {

    Session sessiongenero = null;
    String endpointURI = "ws://localhost:8080/guia06-server/genero";
    private MessageHandler MH;
    String mensaje;

    public WebsocketClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(endpointURI));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.sessiongenero = session;

    }

    @OnClose
    public void onClose(Session session) {
        this.sessiongenero = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.MH != null) {
            this.MH.handleMessage(message);
        }
        this.mensaje = message;
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.MH = msgHandler;
    }

    public void sendMessage(String message) throws IOException {
        this.sessiongenero.getBasicRemote().sendText(message);
    }

    public static interface MessageHandler {

        public void handleMessage(String message);
    }

    public String getMessage() {
        return mensaje;
    }

}
