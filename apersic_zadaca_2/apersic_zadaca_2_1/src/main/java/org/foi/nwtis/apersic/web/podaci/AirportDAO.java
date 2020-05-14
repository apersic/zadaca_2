/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.apersic.web.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author dkermek
 */
public class AirportDAO {

    /**
     * Dohvaća sve aerodrome iz BP.
     *
     * @param bpk
     * @return
     */
    public List<Airport> dajSveAerodrome(BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT ident, type, name, elevation_ft, continent, iso_country, "
                + "iso_region, municipality, gps_code, iata_code, local_code, coordinates FROM airports";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Airport> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    String elevation_ft = rs.getString("elevation_ft");
                    String continent = rs.getString("continent");
                    String iso_country = rs.getString("iso_country");
                    String iso_region = rs.getString("iso_region");
                    String municipality = rs.getString("municipality");
                    String gps_code = rs.getString("gps_code");
                    String iata_code = rs.getString("iata_code");
                    String local_code = rs.getString("local_code");
                    String coordinates = rs.getString("coordinates");
                    Airport a = new Airport(ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);

                    aerodromi.add(a);
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Prihvaća konfiguraciju i vraća sve elemente iz tablice MYAIRPORTS.
     *
     * @param bpk
     * @return
     */
    public List<MyAirport> dajSveMojeAerodrome(BP_Konfiguracija bpk) throws ParseException {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT id, ident, username, stored FROM myairports";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<MyAirport> mojiAerodromi = new ArrayList<>();

                while (rs.next()) {
                    int id = Integer.parseInt(rs.getString("id"));
                    String ident = rs.getString("ident");
                    String username = rs.getString("username");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                    Date parsedDate = dateFormat.parse(rs.getString("stored"));;
                    Timestamp stored = new java.sql.Timestamp(parsedDate.getTime());

                    MyAirport myAirport = new MyAirport(id, ident, username, stored);

                    mojiAerodromi.add(myAirport);
                }
                return mojiAerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Prihvaća konfiguraciju i vraća sve elemente tablice MYAIRPORTSLOGS.
     *
     * @param bpk
     * @return
     * @throws ParseException
     */
    public List<MyAirportLog> dajAerodromeLog(BP_Konfiguracija bpk) throws ParseException {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT ident, flightdate, stored FROM myairportslog";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<MyAirportLog> myLogs = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    SimpleDateFormat dateFormatStored = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                    SimpleDateFormat dateFormatFlightDate = new SimpleDateFormat("yyyy-MM-dd");
                    Date flightDate = dateFormatFlightDate.parse(rs.getString("flightdate"));

                    Date parsedDate = dateFormatStored.parse(rs.getString("stored"));;
                    Timestamp stored = new java.sql.Timestamp(parsedDate.getTime());

                    MyAirportLog myLog = new MyAirportLog(ident, flightDate, stored);

                    myLogs.add(myLog);
                }
                return myLogs;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Unos entiteta u tablicu MYAIRPORTSLOG
     *
     * @param bpk
     * @param icao24
     * @param stored
     * @throws SQLException
     */
    public void unesiAerodromLog(BP_Konfiguracija bpk, String icao24, Date stored) throws SQLException {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String flightdateString = new SimpleDateFormat("yyyy-MM-dd").format(stored);;
        String storedString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stored);
        String upit = "INSERT INTO myairportslog (IDENT, FLIGHTDATE, STORED) VALUES "
                + "('" + icao24 + "', '" + flightdateString + "', '" + storedString + "')";

        try {
            Class.forName(bpk.getDriverDatabase(url));
            Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
            Statement s = con.createStatement();
            int rs = s.executeUpdate(upit);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Funkcija za unos entiteta u tablicu AIRPLANES
     *
     * @param bpk
     * @param avion
     * @param stored
     * @throws SQLException
     */
    public void unesiAvion(BP_Konfiguracija bpk, AvionLeti avion, Date stored) throws SQLException {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String storedString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stored);

        String upit = "INSERT INTO airplanes (ICAO24, FIRSTSEEN, ESTDEPARTUREAIRPORT, LASTSEEN, ESTARRIVALAIRPORT, CALLSIGN, "
                + "ESTDEPARTUREAIRPORTHORIZDISTANCE, ESTDEPARTUREAIRPORTVERTDISTANCE, ESTARRIVALAIRPORTHORIZDISTANCE, ESTARRIVALAIRPORTVERTDISTANCE, "
                + "DEPARTUREAIRPORTCANDIDATESCOUNT, ARRIVALAIRPORTCANDIDATESCOUNT, STORED) "
                + "VALUES ('" + avion.getIcao24() + "', " + avion.getFirstSeen() + ", '" + avion.getEstDepartureAirport() + "', "
                + avion.getLastSeen() + ", '" + avion.getEstArrivalAirport() + "', '" + avion.getCallsign() + "', " + avion.getEstDepartureAirportHorizDistance()
                + ", " + avion.getEstDepartureAirportVertDistance() + ", " + avion.getEstArrivalAirportHorizDistance() + ", " + avion.getEstArrivalAirportVertDistance()
                + ", " + avion.getDepartureAirportCandidatesCount() + ", " + avion.getArrivalAirportCandidatesCount() + ", '" + storedString + "')";

        try {
            Class.forName(bpk.getDriverDatabase(url));
            Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
            Statement s = con.createStatement();
            int rs = s.executeUpdate(upit);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Dohvaća sve aerodrome iz BP koje imaju naziv sličan traženom.
     *
     * @param korIme
     * @param lozinka
     * @param naziv
     * @param bpk
     * @return
     */
    public List<Airport> dajAerodromeNaziv(String korIme, String lozinka, String naziv, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT ident, type, name, elevation_ft, continent, iso_country, "
                + "iso_region, municipality, gps_code, iata_code, local_code, coordinates FROM airports";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Airport> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    if (rs.getString("name").contains(naziv)) {
                        String ident = rs.getString("ident");
                        String type = rs.getString("type");
                        String name = rs.getString("name");
                        String elevation_ft = rs.getString("elevation_ft");
                        String continent = rs.getString("continent");
                        String iso_country = rs.getString("iso_country");
                        String iso_region = rs.getString("iso_region");
                        String municipality = rs.getString("municipality");
                        String gps_code = rs.getString("gps_code");
                        String iata_code = rs.getString("iata_code");
                        String local_code = rs.getString("local_code");
                        String coordinates = rs.getString("coordinates");
                        Airport a = new Airport(ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);

                        aerodromi.add(a);
                    }
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dohvaća iz BP sve aerodrome koji su iz tražene države.
     *
     * @param korIme
     * @param lozinka
     * @param drzava
     * @param bpk
     * @return
     */
    public List<Airport> dajAerodromeDrzava(String korIme, String lozinka, String drzava, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT ident, type, name, elevation_ft, continent, iso_country, "
                + "iso_region, municipality, gps_code, iata_code, local_code, coordinates FROM airports "
                + "WHERE iso_country = '" + drzava + "'";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Airport> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    String elevation_ft = rs.getString("elevation_ft");
                    String continent = rs.getString("continent");
                    String iso_country = rs.getString("iso_country");
                    String iso_region = rs.getString("iso_region");
                    String municipality = rs.getString("municipality");
                    String gps_code = rs.getString("gps_code");
                    String iata_code = rs.getString("iata_code");
                    String local_code = rs.getString("local_code");
                    String coordinates = rs.getString("coordinates");
                    Airport a = new Airport(ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);

                    aerodromi.add(a);
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dohvaća sve aerodrome korisnika iz BP.
     *
     * @param korIme
     * @param lozinka
     * @param bpk
     * @return
     */
    public List<Airport> dajMojeAerodrome(String korIme, String lozinka, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT airports.ident, type, name, elevation_ft, continent, iso_country, "
                + "iso_region, municipality, gps_code, iata_code, local_code, coordinates FROM airports INNER JOIN myairports "
                + "ON myairports.USERNAME = '" + korIme + "' AND airports.IDENT = myairports.IDENT";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Airport> aerodromi = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    String elevation_ft = rs.getString("elevation_ft");
                    String continent = rs.getString("continent");
                    String iso_country = rs.getString("iso_country");
                    String iso_region = rs.getString("iso_region");
                    String municipality = rs.getString("municipality");
                    String gps_code = rs.getString("gps_code");
                    String iata_code = rs.getString("iata_code");
                    String local_code = rs.getString("local_code");
                    String coordinates = rs.getString("coordinates");
                    Airport a = new Airport(ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates);

                    aerodromi.add(a);
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Provjerava nalazi li se traženi aerodrom u kolekciji korisnika.
     *
     * @param korIme
     * @param lozinka
     * @param ident
     * @param bpk
     * @return
     */
    public Boolean provjeriMojeAerodrome(String korIme, String lozinka, String ident, BP_Konfiguracija bpk) {
        List<Airport> mojiAerodromi = dajMojeAerodrome(korIme, lozinka, bpk);

        for (Airport aerodrom : mojiAerodromi) {
            if (aerodrom.getIdent().equals(ident)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Dohvača aerodrom iz korisnikovih aerodroma.
     *
     * @param korIme
     * @param lozinka
     * @param ident
     * @param bpk
     * @return
     * @throws java.text.ParseException
     */
    public Airport dajMojAerodrom(String korIme, String lozinka, String ident, BP_Konfiguracija bpk) throws ParseException {
        List<MyAirport> mojiAerodromi = dajSveMojeAerodrome(bpk);
        List<Airport> aerodromi = dajSveAerodrome(bpk);

        for (MyAirport trazeniAerodrom : mojiAerodromi) {
            if (trazeniAerodrom.getIdent().equals(ident) && trazeniAerodrom.getUsername().equals(korIme)) {
                for (Airport a : aerodromi) {
                    if (a.getIdent().equals(trazeniAerodrom.getIdent())) {
                        return a;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Dodaje aerodrom u tablicu MYAIRPORTS za korisnika.
     *
     * @param korIme
     * @param lozinka
     * @param ident
     * @param bpk
     * @return
     * @throws SQLException
     */
    public Boolean dodajMojAerodrom(String korIme, String lozinka, String ident, BP_Konfiguracija bpk) throws SQLException {
        List<Airport> aerodromi = dajSveAerodrome(bpk);

        for (Airport a : aerodromi) {
            if (a.getIdent().equals(ident)) {
                String url = bpk.getServerDatabase() + bpk.getUserDatabase();
                String bpkorisnik = bpk.getUserUsername();
                String bplozinka = bpk.getUserPassword();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date sada = new Date();
                String stored = formatter.format(sada);

                String upit = "INSERT INTO myairports (IDENT, USERNAME, STORED) "
                        + "VALUES ('" + ident + "', '" + korIme + "', '" + stored + "')";

                try {
                    Class.forName(bpk.getDriverDatabase(url));
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    int rs = s.executeUpdate(upit);
                    return true;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    /**
     *Dohvaća sve avione koji polaze sa zadanog aerodroma
     * u zadanom intervalu.
     * @param icao
     * @param odVrijeme
     * @param doVrijeme
     * @param bpk
     * @return
     * @throws java.text.ParseException
     */
    public List<AvionLeti> dohvatiAvioneOdDo(String icao, String odVrijeme, String doVrijeme, BP_Konfiguracija bpk) throws ParseException {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = dateFormat.parse(odVrijeme);
        Date endDate = dateFormat.parse(doVrijeme);
        String upit = "SELECT ICAO24, FIRSTSEEN, ESTDEPARTUREAIRPORT, LASTSEEN, ESTARRIVALAIRPORT, CALLSIGN, "
                + "ESTDEPARTUREAIRPORTHORIZDISTANCE, ESTDEPARTUREAIRPORTVERTDISTANCE, ESTARRIVALAIRPORTHORIZDISTANCE, ESTARRIVALAIRPORTVERTDISTANCE, "
                + "DEPARTUREAIRPORTCANDIDATESCOUNT, ARRIVALAIRPORTCANDIDATESCOUNT, STORED FROM airplanes WHERE "
                + "ESTDEPARTUREAIRPORT = '" + icao + "' AND FIRSTSEEN <= " + beginDate.getTime()/1000 + " AND LASTSEEN >= " + endDate.getTime()/1000;

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery(upit)) {

                List<AvionLeti> avioni = new ArrayList<>();

                while (rs.next()) {
                    String ident = rs.getString("icao24");
                    int firstSeen = Integer.parseInt(rs.getString("FIRSTSEEN"));
                    String estDepartureAirport = rs.getString("ESTDEPARTUREAIRPORT");
                    int lastSeen = Integer.parseInt(rs.getString("LASTSEEN"));
                    String estArrivalAirport = rs.getString("ESTARRIVALAIRPORT");
                    String callsign = rs.getString("CALLSIGN");
                    int estDepartureHorizDistance = Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTHORIZDISTANCE"));
                    int estDepartureVertDistance = Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTVERTDISTANCE"));
                    int estArrivalHorizDistance = Integer.parseInt(rs.getString("ESTARRIVALAIRPORTHORIZDISTANCE"));
                    int estArrivalVertDistance = Integer.parseInt(rs.getString("ESTARRIVALAIRPORTVERTDISTANCE"));
                    int departureCandidatesCount = Integer.parseInt(rs.getString("DEPARTUREAIRPORTCANDIDATESCOUNT"));
                    int arrivalCandidatesCount = Integer.parseInt(rs.getString("ARRIVALAIRPORTCANDIDATESCOUNT"));
                    String stored = rs.getString("STORED");
                    AvionLeti a = new AvionLeti(ident, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureHorizDistance, estDepartureVertDistance, estArrivalHorizDistance, estArrivalVertDistance, departureCandidatesCount, arrivalCandidatesCount);
                    avioni.add(a);                 
                }
                return avioni;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
