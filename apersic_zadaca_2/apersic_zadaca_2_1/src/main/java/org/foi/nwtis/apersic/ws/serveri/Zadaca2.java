/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.apersic.ws.serveri;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import org.foi.nwtis.apersic.web.podaci.Airport;
import org.foi.nwtis.apersic.web.podaci.AirportDAO;
import org.foi.nwtis.apersic.web.podaci.KorisnikDAO;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author NWTiS_4
 */
@WebService(serviceName = "Zadaca2")
public class Zadaca2 {

    @Inject
    ServletContext context;
    
    /**
     *Provjerava postoji li korisnik u BP.
     * @param korIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "provjeriKorisnika")
    public Boolean provjeriKorisnika(@WebParam(name = "korIme") String korIme,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        if (korisnikDAO.provjeriKorisnika(korIme, lozinka, bpk)) {
            return true;
        }else 
            return false;
    }
    /**
     * Metoda web servisa za dodavanje novog korisnika.
     * @param noviKorisnik
     * @param lozinka
     * @return 
     */
    @WebMethod(operationName = "dodajKorisnika")
    public Boolean dodajKorisnika(@WebParam(name = "noviKorisnik") Korisnik noviKorisnik,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        return korisnikDAO.dodajKorisnika(noviKorisnik, lozinka, bpk);
    }
    
    /**
     *Ažuriranje korisnika u BP.
     * @param korisnik
     * @param lozinka
     * @param korIme
     * @return
     */
    @WebMethod(operationName = "azurirajKorisnika")
    public Boolean azurirajKorisnika(@WebParam(name = "korisnik") Korisnik korisnik,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "korIme") String korIme) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        return korisnikDAO.azurirajKorisnika(korisnik, korIme, lozinka, bpk);
    }
    
    /**
     *Ispis svih korisnika iz BP.
     * @param korIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dajKorisnike")
    public List<Korisnik> dajKorisnike(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        return korisnikDAO.dajSveKorisnike(korIme, lozinka, bpk);
    }
    
    /**
     *Dohvaća sve korisnike koji imaju aerodrome u BP.
     * @param korIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dajKorisnikeSAerodromima")
    public List<Korisnik> dajKorisnikeSAerodromima(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        KorisnikDAO korisnikDAO = new KorisnikDAO();
        return korisnikDAO.dajSveKorisnikeSAerodromima(korIme, lozinka, bpk);
    }
    
    /**
     *Dohvaća sve aerodrome koje imaju sličan naziv koji se traži.
     * @param korIme
     * @param lozinka
     * @param naziv
     * @return
     */
    @WebMethod(operationName = "dajAerodromeNaziv")
    public List<Airport> dajAerodromeNaziv(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "naziv") String naziv) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO = new AirportDAO();
        return airportDAO.dajAerodromeNaziv(korIme, lozinka, naziv, bpk);
    }
    
    /**
     *Dohvaća sve aerodrome koji su iz tražene države.
     * @param korIme
     * @param lozinka
     * @param drzava
     * @return
     */
    @WebMethod(operationName = "dajAerodromeDrzava")
    public List<Airport> dajAerodromeDrzava(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "drzava") String drzava) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO = new AirportDAO();
        return airportDAO.dajAerodromeDrzava(korIme, lozinka, drzava, bpk);
    }
    
    /**
     *Vraća sve aerodrome korisnika.
     * @param korIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dajMojeAerodrome")
    public List<Airport> dajMojeAerodrome(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO = new AirportDAO();
        return airportDAO.dajMojeAerodrome(korIme, lozinka, bpk);
    }
    
    /**
     *Provjerava postoji li aerodrom u tablici
     * MyAirports za korisnika.
     * @param korIme
     * @param lozinka
     * @param ident
     * @return
     */
    @WebMethod(operationName = "provjeriMojeAerodrome")
    public Boolean provjeriMojeAerodrome(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ident") String ident) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO = new AirportDAO();
        return airportDAO.provjeriMojeAerodrome(korIme, lozinka, ident, bpk);
    }
    
    /**
     *Vraća traženi aerodrom korisnika.
     * @param korIme
     * @param lozinka
     * @param ident
     * @return
     * @throws ParseException
     */
    @WebMethod(operationName = "dajMojAerodrom")
    public Airport dajMojAerodrom(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ident") String ident) throws ParseException {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO = new AirportDAO();
        return airportDAO.dajMojAerodrom(korIme, lozinka, ident, bpk);
    }
    
    /**
     *Dodaje aerodrom (ako postoji u tablici AIRPORTS) u
     * tablicu MYAIRPORTS za korisnika.
     * @param korIme
     * @param lozinka
     * @param ident
     * @return
     * @throws ParseException
     * @throws SQLException
     */
    @WebMethod(operationName = "dodajMojAerodrom")
    public Boolean dodajMojAerodrom(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ident") String ident) throws ParseException, SQLException {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO = new AirportDAO();
        return airportDAO.dodajMojAerodrom(korIme, lozinka, ident, bpk);
    }
    
    /**
     *Dohvaća sve avione koji polaze sa zadanog aerodroma
     * u danom intervalu.
     * @param korIme
     * @param lozinka
     * @param ident
     * @param odVrijeme
     * @param doVrijeme
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    @WebMethod(operationName = "dohvatiAvioneOdDo")
    public List<AvionLeti> dohvatiAvioneOdDo(@WebParam(name = "korIme") String korIme, 
            @WebParam(name = "lozinka") String lozinka, @WebParam(name = "ident") String ident, 
            @WebParam(name = "odVrijeme") String odVrijeme, @WebParam(name = "doVrijeme") String doVrijeme) throws SQLException, ParseException {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        AirportDAO airportDAO = new AirportDAO();
        return airportDAO.dohvatiAvioneOdDo(ident, odVrijeme, doVrijeme, bpk);
    }
}
