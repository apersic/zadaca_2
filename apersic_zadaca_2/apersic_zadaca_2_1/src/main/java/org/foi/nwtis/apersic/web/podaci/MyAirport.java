/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.apersic.web.podaci;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dkermek
 */
public class MyAirport {

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String ident;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private Timestamp stored;

    public MyAirport() {
    }

    public MyAirport(int id, String ident, String username, Timestamp stored) {
        this.id = id;
        this.ident = ident;
        this.username = username;
        this.stored = stored;
    }
    
    
}
