package ohtu.intjoukkosovellus;
import com.sun.corba.se.impl.ior.ObjectAdapterIdNumber;

import java.util.Arrays;
import java.util.stream.Collectors;

public class IntJoukko {

    public final static int KAPASITEETTI = 5, // aloitustalukon koko
                            OLETUSKASVATUS = 5; // luotava uusi taulukko on
    // näin paljon isompi kuin vanha
    private int kasvatuskoko; // Uusi taulukko on tämän verran vanhaa suurempi.
    private int[] luvut; // Joukon luvut säilytetään taulukon alkupäässä.
    private int alkioidenLkm; // Tyhjässä joukossa alkioiden_määrä on nolla.

    public IntJoukko() {
        luvut = new int[KAPASITEETTI];
        alkioidenLkm = 0;
        this.kasvatuskoko = OLETUSKASVATUS;
    }

    public IntJoukko(int kapasiteetti) {
        if (kapasiteetti < 0) {
            return;
        }
        luvut = new int[kapasiteetti];
        System.out.println(luvut);
        alkioidenLkm = 0;
        this.kasvatuskoko = OLETUSKASVATUS;
    }
    
    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        if (kapasiteetti < 0) {
            throw new IllegalArgumentException("Kapasiteetin täytyy olla suurempaa kuin nolla. ");
        }
        if (kasvatuskoko < 0) {
            throw new IllegalArgumentException("Kasvatuskoon täytyy olla suurempi kuin nolla. ");
        }
        luvut = new int[kapasiteetti];
        alkioidenLkm = 0;
        this.kasvatuskoko = kasvatuskoko;
    }

    public boolean lisaa(int luku) {
        if (kuuluu(luku)) {
            return false;
        }
        luvut[alkioidenLkm] = luku;
        alkioidenLkm++;
        if (!listassaTilaa()) {
            kasvataKapasiteettia();
        }
        return true;
    }

    private boolean listassaTilaa() {
        return luvut.length == alkioidenLkm;
    }

    private void kasvataKapasiteettia() {
        luvut = Arrays.copyOf(luvut, alkioidenLkm + kasvatuskoko);
    }

    public boolean kuuluu(int luku) {
        return Arrays.stream(luvut).anyMatch(i -> i == luku);
    }

    public boolean poista(int luku) {
        int sijainti = luvunIndeksi(luku);
        if (sijainti == -1) {
            return false;
        } else {
            luvut[sijainti] = luvut[alkioidenLkm - 1];
            luvut[alkioidenLkm - 1] = 0;
            alkioidenLkm--;
            return true;
        }
    }

    private int luvunIndeksi(int luku) {
        for (int i = 0; i < alkioidenLkm; i++) {
            if (luvut[i] == luku) {
                return i;
            }
        }
        return -1;
    }

    public int mahtavuus() {
        return alkioidenLkm;
    }

    @Override
    public String toString() {
        String tuloste = Arrays.stream(luvut)
                .filter(i -> i != 0)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
        return "{" + tuloste + "}";
    }

    public int[] toIntArray() {
        return Arrays.copyOf(luvut, alkioidenLkm);
    }

    public static IntJoukko yhdiste(IntJoukko a, IntJoukko b) {
        IntJoukko uusi = new IntJoukko();
        for (int luku: a.toIntArray()) {
            uusi.lisaa(luku);
        }
        for (int luku : b.toIntArray()) {
            uusi.lisaa(luku);
        }
        return uusi;
    }

    public static IntJoukko leikkaus(IntJoukko a, IntJoukko b) {
        IntJoukko uusi = new IntJoukko();
        for (int lukuA : a.toIntArray()) {
            for (int lukuB : b.toIntArray()) {
                if (lukuA == lukuB) {
                    uusi.lisaa(lukuA);
                }
            }
        }
        return uusi;
    }
    
    public static IntJoukko erotus (IntJoukko a, IntJoukko b) {
        IntJoukko uusi = new IntJoukko();
        for (int luku: a.toIntArray()) {
            uusi.lisaa(luku);
        }
        for (int luku: b.toIntArray()) {
            uusi.poista(luku);
        }
        return uusi;
    }
        
}