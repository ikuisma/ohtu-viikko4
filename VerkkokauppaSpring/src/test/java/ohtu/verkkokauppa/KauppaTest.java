package ohtu.verkkokauppa;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class KauppaTest {

    Kauppa kauppa;
    Varasto varasto;
    Pankki pankki;
    Viitegeneraattori viitegeneraattori;

    @Before
    public void setup() {
        varasto = mock(Varasto.class);
        pankki = mock(Pankki.class);
        viitegeneraattori = mock(Viitegeneraattori.class);
        kauppa = new Kauppa(varasto, pankki, viitegeneraattori);

        when(viitegeneraattori.uusi()).thenReturn(1);
        when(varasto.saldo(1)).thenReturn(100);
        when(varasto.saldo(2)).thenReturn(100);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "Maito", 3));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "Kalja", 6));
    }

    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaan() {

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        verify(varasto, times(1)).haeTuote(1);

        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 3);
    }

    @Test
    public void ostoksenPaatyttyaKahdellaSamallaTuotteellaPankinMetodiaTilisiirtoKutsutaanOikein() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 6);
    }


    @Test
    public void ostoksenPaatyttyaKahdellaEriTuotteellaPankinMetodiaTilisiirtoKutsutaanOikein() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 9);
    }

    @Test
    public void ostoskoriJohonLisataanSaatavillaOlevaTuoteJaLoppuunmyytyTuoteKutsuuTilisiirtoMetodiaOikein() {
        when(varasto.saldo(2)).thenReturn(0);
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 3);
    }

    @Test
    public void aloitaAsiointiNollaaOstoskorin() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 6);
    }

    @Test
    public void uudellaMaksullaUusiViitenumero() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("paavo", "12345");
        verify(viitegeneraattori, times(1)).uusi();
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(viitegeneraattori, times(2)).uusi();
    }

    @Test
    public void ostoskoristaPoistaminenOnnistuu() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 3);
    }

}
