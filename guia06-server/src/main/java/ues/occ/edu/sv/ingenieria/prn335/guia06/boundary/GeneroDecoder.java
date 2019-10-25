/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia06.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Genero;

/**
 *
 * @author mario
 */
@LocalBean
public class GeneroDecoder implements Decoder.Text<Genero> {

    Jsonb jsonbserial = JsonbBuilder.create();

    /**
     * @param json
     * @return
     */
    public Genero jsonToGenero(String json) {
        if (json != null && !json.isEmpty() && !json.equals("")) {
            try {
                return jsonbserial.fromJson(json, Genero.class);
            } catch (JsonbException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * Metodos abstractos que se implementaron automaticamente
     *
     * @param g
     * @return
     * @throws DecodeException
     */
    @Override
    public Genero decode(String g) throws DecodeException {
        return jsonbserial.fromJson(g, Genero.class);

    }

    @Override
    public boolean willDecode(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init(EndpointConfig ec) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
