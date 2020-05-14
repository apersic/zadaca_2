/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.apersic.web.podaci;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dkermek
 */
public class Airport {

    @Getter
    @Setter
    private String ident;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String elevation_ft;
    @Getter
    @Setter
    private String continent;
    @Getter
    @Setter
    private String iso_country;
    @Getter
    @Setter
    private String iso_region;
    @Getter
    @Setter
    private String municipality;
    @Getter
    @Setter
    private String gps_code;
    @Getter
    @Setter
    private String iata_code;
    @Getter
    @Setter
    private String local_code;
    @Getter
    @Setter
    private String coordinates;

    public Airport() {
    }

    public Airport(String ident, String type, String name, String elevation_ft, String continent, String iso_country, String iso_region, String municipality, String gps_code, String iata_code, String local_code, String coordinates) {
        this.ident = ident;
        this.type = type;
        this.name = name;
        this.elevation_ft = elevation_ft;
        this.continent = continent;
        this.iso_country = iso_country;
        this.iso_region = iso_region;
        this.municipality = municipality;
        this.gps_code = gps_code;
        this.iata_code = iata_code;
        this.local_code = local_code;
        this.coordinates = coordinates;
    }
    
    
}
