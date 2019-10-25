/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia06.boundary;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Genero;
import ues.occ.edu.sv.ingenieria.prn335.guia06.controller.GeneroFacade;

/**
 *
 * @author mario
 */
@ServerEndpoint(value = "/genero")
public class WebSocketServer implements Serializable {

    /**
     * Se injecta la entidad a trabajar y sus metodos decoder y encoder.
     */
    @Inject
    private GeneroDecoder decoder;
    @Inject
    private GeneroEncoder encoder;
    @Inject
    private GeneroFacade generofacade;

    public Session sessiongenero;
    private static final Set<WebSocketServer> clients = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.sessiongenero = session;
        clients.add(this);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            if (isJSON(message)) {
                persist(message);
            }
            listSend();
        } catch (EncodeException | DecodeException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {

        try {
            clients.remove(this);
            String message = "Ha salido satisfactoriamente.";
            broadcast(message);
        } catch (EncodeException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private void broadcast(String message) throws IOException, EncodeException {
        clients.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.sessiongenero.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }

            }
        });
    }

    private boolean isJSON(String cadena) throws NullPointerException {
        if (cadena != null && !cadena.equals("") && !cadena.isEmpty()) {
            return (cadena.charAt(0) == '{' && cadena.charAt(cadena.length() - 1) == '}')
                    || (cadena.charAt(0) == '[' && cadena.charAt(cadena.length() - 1) == ']');
        }
        return false;
    }

    private void persist(String message) throws DecodeException, IOException, EncodeException {
        if (message.charAt(0) != '[') {
            Genero nuevo = decoder.decode(message);
            generofacade.create(nuevo);
        }
    }

    private void listSend() throws IOException, EncodeException {

        String jsonMessage = encoder.generoToJson(generofacade.findAll());
        broadcast(jsonMessage);
    }

}
