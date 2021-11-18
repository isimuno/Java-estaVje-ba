package main.hr.java.covidportal.main;

import ch.qos.logback.classic.selector.ContextJNDISelector;
import main.hr.java.covidportal.enumeracija.VrijednostiSimptoma;
import main.hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import main.hr.java.covidportal.iznimke.BolestIstihSimptomaException;
import main.hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobeException;
import main.hr.java.covidportal.model.*;
import main.hr.java.covidportal.sortiranje.CovidSorter;
import main.hr.java.covidportal.sortiranje.VirusSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Klasa Glavna predstavlja glavnu klasu programa
 *
 * @author isimun2
 */

public class Glavna {
    public static final Integer BROJ_OSOBA = 4;
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @param args polje tipa String
     */
    public static void main(String[] args) throws IOException, DuplikatKontaktiraneOsobeException {
        logger.info("Aplikacija pokrenuta! ");

        Scanner unos = new Scanner(System.in);
        Set<Zupanija> zupanije = new LinkedHashSet<>();
        Integer brojZupanija = 0;
        boolean pogreskaKodUnosa = false;
        BufferedReader reader = new BufferedReader(new FileReader("dat/zupanije.txt"));
        Integer brojLinija = 0;
        while (reader.readLine() != null) brojLinija++;
        reader.close();
        brojZupanija = brojLinija / 4;
        Integer brZup = 0;
        for (int i = 0; i < brojZupanija; i++) {
            zupanije.add(unesiZupaniju(i, unos, brZup));
            brZup += 4;
        }

        Set<Simptom> simptomi = new LinkedHashSet<>();
        brojLinija = 0;
        reader = new BufferedReader(new FileReader("dat/simptomi.txt"));
        while (reader.readLine() != null) brojLinija++;
        reader.close();
        Integer brojSimptoma = brojLinija / 3;
        Integer brSimp = 0;
        for (int i = 0; i < brojSimptoma; i++) {
            simptomi.add(unesiSimptome(i, unos, brSimp));
            brSimp += 3;
        }

        Set<Bolest> bolesti = new LinkedHashSet<>();
        brojLinija = 0;
        reader = new BufferedReader(new FileReader("dat/bolesti.txt"));
        while (reader.readLine() != null) brojLinija++;
        reader.close();
        Integer brojBolesti = brojLinija / 3;
        Integer brBoles = 0;
        for (int i = 0; i < brojBolesti; i++) {
            bolesti.add(unesiBolesti(i, unos, simptomi, bolesti, brBoles));
            brBoles += 3;
        }
        brojLinija = 0;
        reader = new BufferedReader(new FileReader("dat/virusi.txt"));
        while (reader.readLine() != null) brojLinija++;
        reader.close();
        Integer brojVirusa = brojLinija / 3;
        Integer brVirus = 0;
        for (int i = 0; i < brojVirusa; i++) {
            bolesti.add(unesiViruse(i, unos, simptomi, bolesti, brVirus));
            brVirus += 3;
        }

        List<Osoba> osobe = new ArrayList<>();
        brojLinija = 0;
        reader = new BufferedReader(new FileReader("dat/osobe.txt"));
        while (reader.readLine() != null) brojLinija++;
        reader.close();
        Integer brojOsoba = brojLinija / 7;
        Integer brOsb = 0;
        for (int i = 0; i < brojOsoba; i++) {
            osobe.add(unesiOsobe(i, unos, bolesti, zupanije, osobe, brOsb));
            brOsb += 7;
        }

        List<Osoba> listaSvihOsoba = osobe;

        HashMap<Bolest, List<Osoba>> mapaOsobaIBolesti = new HashMap<>();
        List<Bolest> listaBolesti = new ArrayList<>();
        Bolest bolest = null;
        for (int i = 0; i < osobe.size(); i++) {
            if (i < 1) {
                bolest = new Bolest(osobe.get(i).getZarazenBolescu().getId(), osobe.get(i).getZarazenBolescu().getNaziv(), osobe.get(i).getZarazenBolescu().getSimptomi());
                listaBolesti.add(bolest);
            } else {
                if (bolest.getNaziv().equals(osobe.get(i).getZarazenBolescu().getNaziv())) {
                    continue;
                } else {
                    bolest = new Bolest(osobe.get(i).getZarazenBolescu().getId(), osobe.get(i).getZarazenBolescu().getNaziv(), osobe.get(i).getZarazenBolescu().getSimptomi());
                    listaBolesti.add(bolest);
                }
            }
        }

        Set<String> namesAlreadySeen = new HashSet<>();
        listaBolesti.removeIf(p -> !namesAlreadySeen.add(p.getNaziv()));

        for (Bolest b : listaBolesti) {
            System.out.println(b.getNaziv());
        }

        ArrayList<Osoba> listaBolesnihOsoba = null;
        for (int i = 0; i < listaBolesti.size(); i++) {
            listaBolesnihOsoba = new ArrayList<>();
            for (Osoba o : osobe) {
                if (listaBolesti.get(i).getNaziv().equals(o.getZarazenBolescu().getNaziv())) {
                    listaBolesnihOsoba.add(o);
                }
            }
            mapaOsobaIBolesti.put(listaBolesti.get(i), listaBolesnihOsoba);
        }


        System.out.println("Unesene osobe: ");
        for (int i = 0; i < brojOsoba; i++) {
            System.out.println("================================================================");
            System.out.println("Id: " + osobe.get(i).getId());
            System.out.println("Ime i prezime: " + osobe.get(i).getIme() + "  " + osobe.get(i).getPrezime());
            System.out.println("Starost: " + osobe.get(i).getStarost());
            System.out.println("Županija prebivališta: " + osobe.get(i).getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osobe.get(i).getZarazenBolescu().getNaziv());
            if (osobe.get(i).getKontaktiraneOsobe() != null) {
                System.out.print("Kontakt osobe: ");
                for (int o = 0; o < osobe.get(i).getKontaktiraneOsobe().size(); o++)
                    if (osobe.get(i).getKontaktiraneOsobe().get(o) != null) {
                        System.out.print(osobe.get(i).getKontaktiraneOsobe().get(o).getIme() + " " + osobe.get(i).getKontaktiraneOsobe().get(o).getPrezime() + ", ");
                    }
                System.out.println("");
            } else {
                System.out.println("Kontakt osobe:  Nema kontaktiranih osoba!");
            }
        }
        System.out.println("================================================================");
        System.out.println("================================================================");
        System.out.println("Ispis mape: ");
        for (Bolest b : listaBolesti) {
            System.out.print("Od bolesti " + b.getNaziv() + " boluju: ");
            for (Map.Entry<Bolest, List<Osoba>> entry : mapaOsobaIBolesti.entrySet()) {
                if (entry.getKey().getNaziv().equals(b.getNaziv())) {
                    for (Osoba o : mapaOsobaIBolesti.get(b)) {
                        System.out.print(o.getIme() + " " + o.getPrezime() + ",  ");
                    }
                }
            }
            System.out.println("");
        }
        System.out.println("================================================================");

        List<Zupanija> listZupanija = new ArrayList<>(zupanije);
        Collections.sort(listZupanija, new CovidSorter());
        Double brStanovnikaZ1 = listZupanija.get(0).getBrojStanovnika().doubleValue();
        Double brStanovnikaZ2 = listZupanija.get(listZupanija.size() - 1).getBrojStanovnika().doubleValue();
        Double brZarazenihZ1 = listZupanija.get(0).getBrojZarazenihOsoba().doubleValue();
        Double brZarazenihZ2 = listZupanija.get(listZupanija.size() - 1).getBrojZarazenihOsoba().doubleValue();
        Double brojZarazenihZupanija1 = (brZarazenihZ1 / (brStanovnikaZ1));
        Double brojZarazenihZupanija2 = (brZarazenihZ2 / (brStanovnikaZ2));

        System.out.println("Najviše zaraženih ima u zupaniji " + listZupanija.get(0).getNaziv() + " " + brojZarazenihZupanija1 * 100 + " %");
        System.out.println("Najmanje zaraženih ima u zupaniji " + listZupanija.get(listZupanija.size() - 1).getNaziv() + " " + brojZarazenihZupanija2 * 100 + " %");

        System.out.println("================================================================");

        List<Virus> uneseniVirusi = new ArrayList<>();
        List<Osoba> oboljeleOsobe = new ArrayList<>();
        for (Bolest b : bolesti) {
            if (b instanceof Virus) {
                uneseniVirusi.add((Virus) b);
                for (Osoba o : osobe) {
                    if (o.getZarazenBolescu() instanceof Virus && o.getZarazenBolescu().getNaziv().equals(b.getNaziv())) {
                        oboljeleOsobe.add(o);
                    }
                }
            }
        }

        KlinikaZaInfektivneBolesti klinika = new KlinikaZaInfektivneBolesti(uneseniVirusi, oboljeleOsobe);

        Instant pocetakMjerenja1 = Instant.now();
        Collections.sort(klinika.getListaVirusa(), new VirusSorter());
        Instant krajMjerenja1 = Instant.now();
        System.out.println("Sortirani virusi : ");
        for (int i = 0; i < klinika.getListaVirusa().size(); i++) {
            System.out.println(((Virus) klinika.getListaVirusa().get(i)).getNaziv());
        }
        System.out.println("Ukupno trajanje sortiranja pomoću klase VirusSorter je : " + Duration.between(pocetakMjerenja1, krajMjerenja1).toMillis() + " milisekundi.");


        System.out.println("================================================================");

        Instant pocetakMjerenja2 = Instant.now();
        Collections.sort(klinika.getListaVirusa(), (Virus v1, Virus v2) -> v2.getNaziv().compareTo(v1.getNaziv()));
        Instant krajMjerenja2 = Instant.now();
        System.out.println("Sortirani virusi : ");
        for (int i = 0; i < klinika.getListaVirusa().size(); i++) {
            System.out.println(((Virus) klinika.getListaVirusa().get(i)).getNaziv());
        }
        System.out.println("Ukupno trajanje sortiranja pomoću lambda funkcije je : " + Duration.between(pocetakMjerenja2, krajMjerenja2).toMillis() + " milisekundi.");
        long saLambda = Duration.between(pocetakMjerenja2, krajMjerenja2).toMillis();

        System.out.println("================================================================");

        Instant pocetakMjerenja3 = Instant.now();
        klinika.getListaVirusa().sort(Comparator.comparing(Virus::getNaziv).reversed());
        Instant krajMjerenja3 = Instant.now();
        long bezLambda = Duration.between(pocetakMjerenja3, krajMjerenja3).toMillis();
        System.out.println("Sortirani virusi : ");
        for (int i = 0; i < klinika.getListaVirusa().size(); i++) {
            System.out.println(((Virus) klinika.getListaVirusa().get(i)).getNaziv());
        }
        System.out.println("Ukupno trajanje sortiranja pomoću metode sort(): " + Duration.between(pocetakMjerenja3, krajMjerenja3).toMillis() + " milisekundi.");

        System.out.println("================================================================");

        System.out.println("Sortiranje objekata korištenjem lambdi traje " + saLambda + " milisekundi, a bez lambda traje " +
                bezLambda + " milisekundi.");

        System.out.println("================================================================");

        System.out.print("Unesite string za pretragu po prezimenu: ");
        String uvjetPretrage = unos.nextLine();
        System.out.println("Osobe čije prezime sadrži " + uvjetPretrage + " su sljedeće:");

        Optional<List<Osoba>> optional = Optional.ofNullable(osobe);
        optional.get().stream()
                .filter(o -> o.getPrezime().contains(uvjetPretrage))
                .map(o -> o.getIme() + " " + o.getPrezime())
                .forEach(System.out::println);


        System.out.println("================================================================");

        bolesti.stream()
                .map(b -> b.getNaziv() + " ima " + b.getSimptomi().stream().count() + " simptoma.")
                .forEach(System.out::println);

        List<Zupanija> zupanijeSerijalizacija = new ArrayList<>();
        for (int i = 0; i < listZupanija.size(); i++) {
            Double brStanovnikaZupanije = listZupanija.get(i).getBrojStanovnika().doubleValue();
            Double brZarazenihZupanije = listZupanija.get(i).getBrojZarazenihOsoba().doubleValue();
            if (((brZarazenihZupanije / (brStanovnikaZupanije) * 100) > Double.valueOf(2))) {
                zupanijeSerijalizacija.add(listZupanija.get(i));
            }
        }

        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("dat/zupanije.dat"));
        out.writeObject(zupanijeSerijalizacija);
        out.close();


        List<Cijepivo> listaCjepiva = new ArrayList<>();
        brojLinija = 0;
        reader = new BufferedReader(new FileReader("dat/cjepivo.txt"));
        while (reader.readLine() != null) brojLinija++;
        reader.close();
        Integer brojCijepiva = brojLinija / 5;
        Integer brCjep = 0;
        for (int i = 0; i < brojCijepiva; i++) {
            listaCjepiva.add(unesiCijepiva(i, unos,listaSvihOsoba, brCjep));
            brCjep += 5;
        }


        List<Cijepljenje> listaCijepljenja = new ArrayList<>();
        brojLinija = 0;
        reader = new BufferedReader(new FileReader("dat/cijepljenje.txt"));
        while (reader.readLine() != null) brojLinija++;
        reader.close();
        Integer brojCijepljenja = brojLinija / 4;
        Integer brCjepljenja = 0;
        for (int i = 0; i < brojCijepljenja; i++) {
            listaCijepljenja.add(unesiCijepljenje(i, unos, osobe, listaCjepiva, brCjepljenja));
            brCjepljenja += 4;
        }

        System.out.println("Osobe koje su cijepljene: ");
        for(Cijepljenje c: listaCijepljenja){
            System.out.println("Ime i prezime: " + c.getIdOsobe().getIme() + " " + c.getIdOsobe().getPrezime());
        }


        System.out.println("Osobe koje su cijepljene s dovoljnim brojem: ");
        List<Osoba> listaDovoljnoCijepljenih = new ArrayList<>();
        for(int c = 0; c< listaCijepljenja.size();c++){
            Osoba trazenaOsoba = listaCijepljenja.get(c).getIdOsobe();
            for(int c2=0; c2< listaCijepljenja.size();c2++){
                if(listaCijepljenja.get(c2).getIdOsobe().equals(trazenaOsoba) && c2!= c || listaCijepljenja.get(c2).getIdCjepiva().getBrojDoza()==1){
                    listaDovoljnoCijepljenih.add(listaCijepljenja.get(c2).getIdOsobe());
                }
            }
        }
        Set<String> idAlreadySeen = new HashSet<>();
        listaDovoljnoCijepljenih.removeIf(p -> !idAlreadySeen.add(p.getId().toString()));

        for(Osoba o: listaDovoljnoCijepljenih){
            System.out.println(o.getIme() + " "+ o.getPrezime());
        }


        System.out.println("Osobe koje nisu cijepljene: ");
        List<Osoba> listaNecijepljenihOsoba = new ArrayList<>();
        for(int o=0; o< listaSvihOsoba.size(); o++){
            for(int o2=0; o2<listaDovoljnoCijepljenih.size();o2++){
                if(listaSvihOsoba.get(o).getId().equals(listaDovoljnoCijepljenih.get(o2).getId()) == false){
                    listaNecijepljenihOsoba.add(listaSvihOsoba.get(o));
                }
            }
        }

        listaNecijepljenihOsoba.removeIf(p -> !idAlreadySeen.add(p.getId().toString()));

        for(Osoba o: listaNecijepljenihOsoba){
            System.out.println(o.getIme() + " " + o.getPrezime());
        }


        List<Osoba> listaZakasnjelih= new ArrayList<>();

        for(int c = 0; c< listaCijepljenja.size();c++){
            for(int c2=0; c2< listaCijepljenja.size();c2++){
                LocalDate datumPrveDoze = listaCijepljenja.get(c).getDatumCijepljenja();
                LocalDate datumDrugeDoze =  listaCijepljenja.get(c2).getDatumCijepljenja();
                Period period = Period.between(datumDrugeDoze,datumPrveDoze);
                if(period.getDays() > listaCijepljenja.get(c).getIdCjepiva().getVremenskaRazlika()){
                    listaZakasnjelih.add(listaCijepljenja.get(c2).getIdOsobe());
                }
            }
        }


        System.out.println("Lista zakasnjelih: ");
        for(Osoba o: listaZakasnjelih){
            System.out.println(o.getIme() + " " + o.getPrezime());
        }


        System.out.println("Osobe koje nisu dovoljno cijepljeni: ");
        List<Osoba> listaNedovoljnoCijepljenih = new ArrayList<>();

        for(int c = 0; c< listaCijepljenja.size();c++){
            Osoba trazenaOsoba = listaCijepljenja.get(c).getIdOsobe();
            for(int c2=0; c2< listaCijepljenja.size();c2++){
                if(listaCijepljenja.get(c2).getIdOsobe().equals(trazenaOsoba) && c2!= c || listaCijepljenja.get(c2).getIdCjepiva().getBrojDoza()==1){
                    listaNedovoljnoCijepljenih.add(listaCijepljenja.get(c2).getIdOsobe());
                }
            }
        }



        listaNedovoljnoCijepljenih.removeIf(p -> !idAlreadySeen.add(p.getId().toString()));
        for(Osoba o: listaNedovoljnoCijepljenih){
            System.out.println(o.getIme() + " "+ o.getPrezime());
        }

    }

    private static Cijepljenje unesiCijepljenje(int i, Scanner unos, List<Osoba> listaOsoba, List<Cijepivo> listaCjepiva, Integer brCjepljenja) throws IOException {
        Long idCijepljenja = Long.valueOf(Files.readAllLines(Paths.get("dat/cijepljenje.txt")).get(brCjepljenja));
        System.out.println("Id cijepljenja:  " + idCijepljenja);
        Long idOsobe = Long.valueOf(Files.readAllLines(Paths.get("dat/cijepljenje.txt")).get(brCjepljenja +1));
        System.out.println("Id osobe:  " + idOsobe);

        Osoba cijepljenaOsoba = null;
        for (Osoba o: listaOsoba){
            if(o.getId().equals(idOsobe)){
               cijepljenaOsoba = o;
            }
        }
        System.out.println(cijepljenaOsoba.getIme());

        Long idCijepiva = Long.valueOf(Files.readAllLines(Paths.get("dat/cijepljenje.txt")).get(brCjepljenja +2));
        System.out.println("Id cjepiva:  " + idCijepiva);
        Cijepivo cijepivo = null;
        for (Cijepivo c : listaCjepiva){
            if(c.getIdCjepiva().equals(idCijepiva)){
                cijepivo=c;
            }
        }
        System.out.println(cijepivo.getNazivCjepiva());
        String datumCjepljenja = String.valueOf(Files.readAllLines(Paths.get("dat/cijepljenje.txt")).get(brCjepljenja +3));
        LocalDate datum = LocalDate.parse(datumCjepljenja);
        System.out.println("Datum cijepljenja:  " + datum);

        Cijepljenje novoCjepivo = new Cijepljenje(idCijepljenja, cijepljenaOsoba, cijepivo, datum);
        return novoCjepivo;
    }


    private static Cijepivo unesiCijepiva(int i, Scanner unos, List<Osoba> osobe, Integer brCjep) throws IOException {
        Long idCijepiva = Long.valueOf(Files.readAllLines(Paths.get("dat/cjepivo.txt")).get(brCjep));
        System.out.println("Id cjepiva:  " + idCijepiva);
        String nazivCjepiva = Files.readAllLines(Paths.get("dat/cjepivo.txt")).get(brCjep + 1);
        System.out.println("Naziv zupanije: " + nazivCjepiva);
        String nazivProizvodaca = String.valueOf(Files.readAllLines(Paths.get("dat/cjepivo.txt")).get(brCjep + 2));
        System.out.println("Naziv proizvodaca: " + nazivProizvodaca);
        Integer brojDoza = Integer.valueOf(Files.readAllLines(Paths.get("dat/cjepivo.txt")).get(brCjep + 3));
        System.out.println("Broj doza: " + brojDoza);
        Integer vremenskaRazlikaUDanima = Integer.valueOf(Files.readAllLines(Paths.get("dat/cjepivo.txt")).get(brCjep + 4));
        System.out.println("Vremenska razlika: " + vremenskaRazlikaUDanima);
        Cijepivo novoCjepivo = new Cijepivo(idCijepiva, nazivCjepiva, nazivProizvodaca, brojDoza, vremenskaRazlikaUDanima);
        return novoCjepivo;
    }



    /**
     * Predstavlja metodu za unos Osoba u polje osobe
     *
     * @param i        predstavlja brojac Osoba tipa Integer
     * @param unos     instanca klase za unos podataka
     * @param bolesti  zbirka tipa set bolesti
     * @param zupanije zbirka tipa set zupanija
     * @param osobe    lista osoba
     * @param brOsb
     * @return instanca Osobe
     * @throws DuplikatKontaktiraneOsobeException iznimka
     */

    private static Osoba unesiOsobe(int i, Scanner unos, Set<Bolest> bolesti, Set<Zupanija> zupanije, List<Osoba> osobe, Integer brOsb) throws DuplikatKontaktiraneOsobeException, IOException {

        Integer starost = 0;
        Zupanija zupanijaPrebivalista = new Zupanija(Long.valueOf(1), "Naziv", 0, 0);
        Bolest zarazenBolescu = new Bolest(Long.valueOf(1), "Naziv", null);
        List<Osoba> kontaktOsobe = new ArrayList<>();

        Long idOsobe = Long.valueOf(Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb));
        System.out.println("Id osobe: " + idOsobe);
        String ime = Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb + 1);
        System.out.println("Ime osobe: " + ime);
        String prezime = Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb + 2);
        System.out.println("Prezime osobe: " + prezime);
        starost = Integer.valueOf(Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb + 3));
        System.out.println("Starost osobe: " + starost);
        ArrayList<Bolest> listaBolesti = new ArrayList<>(bolesti);
        ArrayList<Zupanija> listaZupanija = new ArrayList<>(zupanije);

        Integer idZupanije = Integer.valueOf(Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb + 4));
        for (int j = 0; j < zupanije.size(); j++) {
            Integer indexZupanije = Math.toIntExact(listaZupanija.get(j).getId());
            if (idZupanije == indexZupanije) {
                zupanijaPrebivalista = listaZupanija.get(j);
            }
        }
        System.out.println("Zupanija prebivalista: " + zupanijaPrebivalista.getNaziv());


        Integer idBolesti = Integer.valueOf(Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb + 5));
        for (int j = 0; j < listaBolesti.size(); j++) {
            Integer indexBolesti = Math.toIntExact(listaBolesti.get(j).getId());
            if (idBolesti == indexBolesti) {
                zarazenBolescu = listaBolesti.get(j);
            }
        }
        System.out.println("Zarazen bolescu: " + zarazenBolescu.getNaziv());

        if(Integer.valueOf(Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb + 5)) ==0){
            System.out.println("Nema kontakt osoba! ");
        }

        if (i < 1) {
            Osoba novaOsoba = new Osoba.Builder()
                    .imaId(idOsobe)
                    .seZove(ime)
                    .sePreziva(prezime)
                    .imaGodina(starost)
                    .pripadaZupaniji(zupanijaPrebivalista)
                    .imaBolest(zarazenBolescu)
                    .build();
            return novaOsoba;
        } else {
            String indexiKontaktOsoba = Files.readAllLines(Paths.get("dat/osobe.txt")).get(brOsb + 6);
            String[] formatKontaktOsoba = indexiKontaktOsoba.split("[,]", 0);
            System.out.println("Broj KontsktOsoba: " + formatKontaktOsoba.length);
            for (int j = 0; j < formatKontaktOsoba.length; j++) {
                System.out.println(formatKontaktOsoba[j]);
                kontaktOsobe.add(osobe.get(Integer.parseInt(formatKontaktOsoba[j]) - 1));
            }

            Osoba novaOsoba = new Osoba.Builder()
                    .imaId(idOsobe)
                    .seZove(ime)
                    .sePreziva(prezime)
                    .imaGodina(starost)
                    .pripadaZupaniji(zupanijaPrebivalista)
                    .imaBolest(zarazenBolescu)
                    .kontaktiraneOsobe(kontaktOsobe)
                    .build();
            return novaOsoba;
        }
    }


    /**
     * @param kontaktOsobe polje kontaktiranih osoba
     * @param osoba        objekt Osoba
     * @param o            brojač tipa Integer
     * @return boolean vrijednost
     * @throws DuplikatKontaktiraneOsobeException
     */
    private static boolean DuplikatKontaktOsoba(List<Osoba> kontaktOsobe, Osoba osoba, int o) throws
            DuplikatKontaktiraneOsobeException {
        boolean postojiDuplikat = false;
        for (int i = 0; i < o; i++) {
            if (kontaktOsobe.get(i).equals(osoba)) {
                postojiDuplikat = true;
            }
        }
        if (postojiDuplikat == true) {
            throw new DuplikatKontaktiraneOsobeException("Odabrana osoba se već nalazi među kontaktiranim osobama. " +
                    "Molimo Vas da odaberete neku drugu osobu !");
        }
        return postojiDuplikat;
    }

    /**
     * @param i        brojač bolesti tipa Integer
     * @param unos     instanca klase za unos podataka
     * @param simptomi zbirk kreiranih simptoma tipa set
     * @param bolesti  zbirka tipa set koja predstavlja bolesti
     * @param brBoles
     * @return instanca Bolest
     */

    private static Bolest unesiBolesti(int i, Scanner unos, Set<Simptom> simptomi, Set<Bolest> bolesti, Integer brBoles) throws IOException {
        Set<Simptom> simptomiBolesti = new LinkedHashSet<>();
        ArrayList<Simptom> listaSimptoma = new ArrayList<>(simptomi);
        Long vrstaBolesti = Long.valueOf(Files.readAllLines(Paths.get("dat/bolesti.txt")).get(brBoles));
        System.out.println("Id bolesti: " + vrstaBolesti);

        String nazivBolesti = Files.readAllLines(Paths.get("dat/bolesti.txt")).get(brBoles + 1);
        System.out.println("Naziv bolesti : " + nazivBolesti);

        String indexiSimptoma = Files.readAllLines(Paths.get("dat/bolesti.txt")).get(brBoles + 2);
        String[] formatSimptoma = indexiSimptoma.split("[,]", 0);
        System.out.println("Broj simptoma: " + formatSimptoma.length);
        for (int j = 0; j < formatSimptoma.length; j++) {
            System.out.println(formatSimptoma[j]);
            simptomiBolesti.add(listaSimptoma.get(Integer.parseInt(formatSimptoma[j]) - 1));
        }
        Bolest novaBolest = new Bolest(vrstaBolesti, nazivBolesti, simptomiBolesti);
        return novaBolest;

    }

    private static Bolest unesiViruse(int i, Scanner unos, Set<Simptom> simptomi, Set<Bolest> bolesti, Integer brVirus) throws IOException {
        Set<Simptom> simptomiBolesti = new LinkedHashSet<>();
        ArrayList<Simptom> listaSimptoma = new ArrayList<>(simptomi);
        Long idVirusa = Long.valueOf(Files.readAllLines(Paths.get("dat/virusi.txt")).get(brVirus));
        System.out.println("Id virusa: " + idVirusa);

        String nazivVirusa = Files.readAllLines(Paths.get("dat/virusi.txt")).get(brVirus + 1);
        System.out.println("Naziv virusa : " + nazivVirusa);

        String indexiSimptoma = Files.readAllLines(Paths.get("dat/virusi.txt")).get(brVirus + 2);
        String[] formatSimptoma = indexiSimptoma.split("[,]", 0);
        System.out.println("Broj simptoma: " + formatSimptoma.length);
        for (int j = 0; j < formatSimptoma.length; j++) {
            System.out.println(formatSimptoma[j]);
            simptomiBolesti.add(listaSimptoma.get(Integer.parseInt(formatSimptoma[j]) - 1));
        }
        Bolest noviVirus = new Virus(idVirusa, nazivVirusa, simptomiBolesti);
        return noviVirus;
    }


    /**
     * @param bolesti         zbirka tipa set unesenih bolesti
     * @param simptomiBolesti polje unesenih simptoma bolesti
     * @param nazivBolesti    varijabla koja sadrzi nazivBolesti
     * @param i               brojac tipa Integer
     * @return boolean vrijednost postoji duplikat ili ne
     */
    private static boolean DuplikatBolesti(Set<Bolest> bolesti, Set<Simptom> simptomiBolesti, String nazivBolesti, int i) {
        ArrayList<Simptom> listaSimptoma = new ArrayList<>(simptomiBolesti);
        ArrayList<Bolest> listaBolesti = new ArrayList<>(bolesti);
        boolean postojiDuplikat = false;
        for (int j = 0; j < i; j++) {
            if (listaBolesti.get(j) != null) {
                if (listaBolesti.get(j).getNaziv().equals(nazivBolesti)) {
                    for (int s = 0; s < listaBolesti.get(j).getSimptomi().size(); s++) {
                        for (int s2 = 0; s2 < simptomiBolesti.size(); s2++) {
                            ArrayList<Simptom> listaSimptomaBolesti = new ArrayList<>(listaBolesti.get(j).getSimptomi());
                            if (listaSimptomaBolesti.get(s).getVrijednost().equals(listaSimptoma.get(s2).getVrijednost()) && s == s2) {
                                if (simptomiBolesti.size() == listaBolesti.get(j).getSimptomi().size()) {
                                    postojiDuplikat = true;
                                }

                            }
                        }
                    }
                }
            }
        }
        if (postojiDuplikat == true) {
            System.out.println("Pogrešan unos, već ste unijeli bolest ili virus s istim simptomima." +
                    " Molimo ponovite unos!");
            throw new BolestIstihSimptomaException("Unešena bolest/virus s istim simptomima.");
        }
        return postojiDuplikat;
    }

    /**
     * @param i      brojac Simptoma tipa Integer
     * @param unos   instanca klase za unos podataka
     * @param brSimp
     * @return instanca Simptoma
     */
    private static Simptom unesiSimptome(int i, Scanner unos, Integer brSimp) throws IOException {

        Long idSimptoma = Long.valueOf(Files.readAllLines(Paths.get("dat/simptomi.txt")).get(brSimp));
        System.out.println("Id " + idSimptoma);
        String naziv = Files.readAllLines(Paths.get("dat/simptomi.txt")).get(brSimp + 1);
        System.out.println("Naziv " + naziv);
        String vrijednost = (Files.readAllLines(Paths.get("dat/simptomi.txt")).get(brSimp + 2));
        System.out.println("Vrijednost " + vrijednost);
        VrijednostiSimptoma vrijednostiSimptoma = null;
        if (vrijednost.equals(VrijednostiSimptoma.VRIJEDNOSTI_SIMPTOMA1.naziv)) {
            vrijednostiSimptoma = VrijednostiSimptoma.VRIJEDNOSTI_SIMPTOMA1;

        } else if (vrijednost.equals(VrijednostiSimptoma.VRIJEDNOSTI_SIMPTOMA2.naziv)) {
            vrijednostiSimptoma = VrijednostiSimptoma.VRIJEDNOSTI_SIMPTOMA2;

        } else if (vrijednost.equals(VrijednostiSimptoma.VRIJEDNOSTI_SIMPTOMA3.naziv)) {
            vrijednostiSimptoma = VrijednostiSimptoma.VRIJEDNOSTI_SIMPTOMA3;

        } else {
            System.out.println("Molimo koristite samo zadane vrijednosti (RIJETKO;SREDNJE;ČESTO)!");

        }
        Simptom noviSimptom = new Simptom(idSimptoma, naziv, vrijednostiSimptoma);
        return noviSimptom;
    }

    /**
     * @param i    brojač Zupanija tipa Integer
     * @param unos instanca klase za unos podataka
     * @return instanca Zupanije
     */

    private static Zupanija unesiZupaniju(int i, Scanner unos, Integer brZup) throws IOException {
        Long idZupanije = Long.valueOf(Files.readAllLines(Paths.get("dat/zupanije.txt")).get(brZup));
        System.out.println("Id zupanije:  " + idZupanije);
        String naziv = Files.readAllLines(Paths.get("dat/zupanije.txt")).get(brZup + 1);
        System.out.println("Naziv zupanije: " + naziv);
        Integer brojStanovnika = Integer.valueOf(Files.readAllLines(Paths.get("dat/zupanije.txt")).get(brZup + 2));
        System.out.println("Broj stanovnika: " + brojStanovnika);
        Integer brojzarazenihStanovnika = Integer.valueOf(Files.readAllLines(Paths.get("dat/zupanije.txt")).get(brZup + 3));
        System.out.println("Broj zarazenih: " + brojzarazenihStanovnika);

        Zupanija novaZupanija = new Zupanija(idZupanije, naziv, brojStanovnika, brojzarazenihStanovnika);
        return novaZupanija;
    }
}
