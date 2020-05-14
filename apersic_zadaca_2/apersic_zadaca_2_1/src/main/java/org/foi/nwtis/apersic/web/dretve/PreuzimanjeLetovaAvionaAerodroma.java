/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.apersic.web.dretve;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.apersic.web.podaci.Airport;
import org.foi.nwtis.apersic.web.podaci.AirportDAO;
import org.foi.nwtis.apersic.web.podaci.MyAirport;
import org.foi.nwtis.apersic.web.podaci.MyAirportLog;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author NWTiS_4
 */
public class PreuzimanjeLetovaAvionaAerodroma extends Thread {

    private BP_Konfiguracija konf;

    private String username;
    private String password;
    private int intervalCiklusa;
    private int intervalPauze;
    private Date pocetniDatum;
    private Date krajDatum;
    private OSKlijent osKlijent;
    AirportDAO airportDAO;
    private Date vazeciDatum;
    private Date krajPreuzimanja;

    public PreuzimanjeLetovaAvionaAerodroma(BP_Konfiguracija konf) {
        airportDAO = new AirportDAO();
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        int brojac = 0;
        izracunajDatumDo(pocetniDatum);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date sada = new Date();
        String date = formatter.format(sada);
        try {
            vazeciDatum = new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Datum važeći: " + vazeciDatum);
        while ((pocetniDatum.compareTo(vazeciDatum) <= 0) || (pocetniDatum.compareTo(krajPreuzimanja) <= 0)) {
            System.out.println("brojac: " + brojac++);
            System.out.println("Datum pocetni: " + pocetniDatum);
            System.out.println("Datum kraj: " + krajDatum);
            System.out.println("Datum važeći: " + vazeciDatum);
            System.out.println("Datum prekid: " + krajPreuzimanja);
            try {
                try {
                    List<MyAirport> mojiAerodromi = airportDAO.dajSveMojeAerodrome(konf);

                    for (MyAirport aerodrom : mojiAerodromi) {
                        if (!provjeriLog(aerodrom)) {
                            List<AvionLeti> avioni = osKlijent.getDepartures(aerodrom.getIdent(), pocetniDatum.getTime() / 1000, krajDatum.getTime() / 1000);
                            for (AvionLeti avion : avioni) {
                                airportDAO.unesiAvion(konf, avion, pocetniDatum);
                                System.out.println(avion.getIcao24());
                            }
                            airportDAO.unesiAerodromLog(konf, aerodrom.getIdent(), pocetniDatum);
                        } else {
                            System.out.println("Postoji unos za datum.");
                        }
                        Thread.sleep(intervalPauze);
                    }
                } catch (ParseException | SQLException ex) {
                    Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (pocetniDatum.compareTo(vazeciDatum) == 0) {
                    System.out.println("Spavam jedan dan.");
                    pocetniDatum = povecajDatum(pocetniDatum);
                    krajDatum = povecajDatum(krajDatum);
                    Thread.sleep(86400000);
                } else {
                    pocetniDatum = povecajDatum(pocetniDatum);
                    krajDatum = povecajDatum(krajDatum);
                    Thread.sleep(intervalCiklusa);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        username = konf.getOsUsername();
        password = konf.getOsPassword();
        intervalCiklusa = Integer.parseInt(konf.getPreuzimanjeCiklus());
        intervalPauze = Integer.parseInt(konf.getPreuzimanjePauza());
        String datum = konf.getPreuzimanjePocetak();
        String kraj = konf.getPreuzimanjeKraj();
        try {
            pocetniDatum = new SimpleDateFormat("dd.MM.yyyy").parse(datum);
            krajPreuzimanja = new SimpleDateFormat("dd.MM.yyyy").parse(kraj);
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        osKlijent = new OSKlijent(username, password);
        super.start();
    }

    /**
     * Izračunava datum DO pomoću datuma OD iz
     * konfigutacijske datoteke.
     * @param datumOd 
     */
    private void izracunajDatumDo(Date datumOd) {
        Calendar c = Calendar.getInstance();
        c.setTime(datumOd);
        c.add(Calendar.DAY_OF_MONTH, 1);
        krajDatum = c.getTime();
    }

    /**
     * Povecava datum za jedan dan.
     * @param datum
     * @return 
     */
    private Date povecajDatum(Date datum) {
        Calendar c = Calendar.getInstance();
        c.setTime(datum);
        c.add(Calendar.DAY_OF_MONTH, 1);
        datum = c.getTime();
        return datum;
    }

    /**
     * Provjerava postoji li zapis u Log-u aerodroma za datum.
     * @param aerodrom
     * @return 
     */
    private synchronized Boolean provjeriLog(MyAirport aerodrom) {
        try {
            List<MyAirportLog> myLogs = airportDAO.dajAerodromeLog(konf);
            for (MyAirportLog log : myLogs) {
                if (log.getIdent().equals(aerodrom.getIdent()) && log.getStored().getTime() == pocetniDatum.getTime()) {
                    return true;
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
