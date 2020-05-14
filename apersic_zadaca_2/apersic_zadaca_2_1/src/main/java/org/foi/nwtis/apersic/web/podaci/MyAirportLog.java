/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.apersic.web.podaci;

import java.sql.Timestamp;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dkermek
 */
public class MyAirportLog {

    @Getter
    @Setter
    private String ident;
    @Getter
    @Setter
    private Date flightDate;
    @Getter
    @Setter
    private Timestamp stored;

    public MyAirportLog() {
    }

    public MyAirportLog(String ident, Date flightDate, Timestamp stored) {
        this.ident = ident;
        this.flightDate = flightDate;
        this.stored = stored;
    }
    
}
