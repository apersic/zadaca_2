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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.podaci.Korisnik;

/**
 *
 * @author dkermek
 */
public class KorisnikDAO {

    /**
     * Provjerava postoji li korisnik u BP.
     *
     * @param korisnik
     * @param lozinka
     * @param bpk
     * @return
     */
    public Boolean provjeriKorisnika(String korisnik, String lozinka, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA, DATUM_KREIRANJA, DATUM_PROMJENE FROM korisnici";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String korisnik1 = rs.getString("kor_ime");
                    String lozinka1 = rs.getString("lozinka");
                    if (korisnik1.equals(korisnik) && lozinka1.equals(lozinka)) {
                        return true;
                    }
                }
                return false;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Dohvaća korisnika iz BP.
     *
     * @param korisnik
     * @param lozinka
     * @param prijava
     * @param bpk
     * @return
     */
    public Korisnik dohvatiKorisnika(String korisnik, String lozinka, Boolean prijava, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA FROM korisnici WHERE "
                + "KOR_IME = '" + korisnik + "'";

        if (prijava) {
            upit += " and LOZINKA = '" + lozinka + "'";
        }

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String korisnik1 = rs.getString("korIme");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("emailAdresa");
                    Timestamp kreiran = rs.getTimestamp("datumKreiranja");
                    Timestamp promijena = rs.getTimestamp("datumPromjene");
                    Korisnik k = new Korisnik(korisnik1, ime, prezime, "******", email, kreiran, promijena);
                    return k;
                }

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Ažurira postojećeg korisnika u BP.
     *
     * @param k
     * @param korIme
     * @param lozinka
     * @param bpk
     * @return
     */
    public boolean azurirajKorisnika(Korisnik k, String korIme, String lozinka, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "";

        if (!k.getKorIme().equals(korIme)) {
            return false;
        }
        if (!k.getLozinka().isEmpty() && k.getLozinka() != null) {
            upit = "UPDATE korisnici SET IME = '" + k.getIme() + "', PREZIME = '" + k.getPrezime()
                    + "', EMAIL_ADRESA = '" + k.getEmailAdresa() + "', LOZINKA = '" + lozinka + "' WHERE " + "KOR_IME = '" + k.getKorIme() + "'";
        }else{
            upit = "UPDATE korisnici SET IME = '" + k.getIme() + "', PREZIME = '" + k.getPrezime() + 
                "', EMAIL_ADRESA = '" + k.getEmailAdresa() + "' WHERE " + "KOR_IME = '" + k.getKorIme() + "'";
        }

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement()) {

                int brojAzuriranja = s.executeUpdate(upit);

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Unosi novog korisnika u BP.
     *
     * @param k
     * @param lozinka
     * @param bpk
     * @return
     */
    public boolean dodajKorisnika(Korisnik k, String lozinka, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "INSERT INTO korisnici (IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA, DATUM_KREIRANJA, DATUM_PROMJENE) VALUES ("
                + "'" + k.getIme() + "', '" + k.getPrezime()
                + "', '" + k.getEmailAdresa() + "', '" + k.getKorIme() + "', '" + k.getLozinka() + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement()) {

                int brojAzuriranja = s.executeUpdate(upit);

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Dohvaća sve korisnike iz BP.
     *
     * @param korisnik
     * @param lozinka
     * @param bpk
     * @return
     */
    public List<Korisnik> dajSveKorisnike(String korisnik, String lozinka, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA, DATUM_KREIRANJA, DATUM_PROMJENE FROM korisnici";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Korisnik> korisnici = new ArrayList();
                while (rs.next()) {
                    String korisnik1 = rs.getString("kor_ime");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("email_adresa");
                    Timestamp kreiran = rs.getTimestamp("datum_kreiranja");
                    Timestamp promijena = rs.getTimestamp("datum_promjene");
                    Korisnik k = new Korisnik(korisnik1, ime, prezime, "******", email, kreiran, promijena);
                    korisnici.add(k);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     *Dohvaća sve korisnike koji imaju aerodrome iz BP.
     * @param korisnik
     * @param lozinka
     * @param bpk
     * @return
     */
    public List<Korisnik> dajSveKorisnikeSAerodromima(String korisnik, String lozinka, BP_Konfiguracija bpk){
        String upit = "SELECT IME, PREZIME, EMAIL_ADRESA, KOR_IME, LOZINKA, DATUM_KREIRANJA, DATUM_PROMJENE FROM korisnici "
                + "INNER JOIN myairports ON korisnici.KOR_IME = myairports.USERNAME";
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Korisnik> korisnici = new ArrayList();
                while (rs.next()) {
                    String korisnik1 = rs.getString("kor_ime");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("email_adresa");
                    Timestamp kreiran = rs.getTimestamp("datum_kreiranja");
                    Timestamp promijena = rs.getTimestamp("datum_promjene");
                    Korisnik k = new Korisnik(korisnik1, ime, prezime, "******", email, kreiran, promijena);

                    korisnici.add(k);               
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
