# JavaSestaVjezba

6.Šesta laboratorijska vježba

6.1. TEMA VJEŽBE
Svrha laboratorijske vježbe je korištenje tekstualnih i binarnih
datoteka za čitanje podataka iz njih te zapisivanje podataka u njih, kao
i serijaliziranje i deserijaliziranje objekata korištenih u aplikaciji.
6.2. ZADATAK ZA PRIPREMU
Proširiti rješenje pete laboratorijske vježbe na način da se kopira
rješenje te preimenuje u naziv koji sadrži indeks „6“, umjesto „5“. Osim
same mape s projektom, potrebno je promijeniti i naziv projekta unutar
IntelliJ-a korištenjem opcije „Refactor->Rename“. Program je potrebno
proširiti na sljedeći način:
1. Klasu „ImenovaniEntitet“ iz paketa „hr.java.covidportal.model“
proširiti novom varijablom „id“ tipa „Long“. Proširiti konstruktor kako
bi primao i varijablu „id“ te za tu varijablu kreirati „get“ i „set“
metode. S parametrom „id“ potrebno je proširiti i konstruktore svih
ostalih entiteta u aplikaciji koji nasljeđuju klasu „ImenovaniEntitet“.
2. Kreirati mapu „dat“ na razini projekta na istoj razini kao što je
kreirana mapa „logs“.
3. Unutar mape „dat“ kreirati datoteku „zupanije.txt“ koja će sadržavati
informacije o županijama formatiranih na sljedeći način:
1
GRAD ZAGREB
1000000
20000
2
MEĐIMURSKA
100000
500
3
VARAŽDINSKA
200000
1000
Svaki zapis o županiji mora sadržavati četiri varijable: „id“, „naziv“,
„brojStanovnika“ i „brojZarazenih“. Nakon što se pročitaju podaci iz
datoteke, moraju se kreirati odgovarajući objekti klase „Zupanija“ i
koristiti u ostatku aplikacije.
Na navedenom primjeru prikazana su tri skupa podataka od po četiri
retka za tri županije označene s tri različite boje.
Na sličan način je potrebno kreirati datoteke koje će sadržavati zapise
i za sve ostale entitete korištene u aplikaciji: „simptomi.txt“,
„bolesti.txt“, „virusi.txt“ i „osobe.txt“.
U slučajevima kad je npr. potrebno povezivati bolesti i viruse sa
simptomima, to je potrebno učiniti na način da se u jednu liniju
postave svi identifikatori simptoma odvojeni zarezom, npr. „1,2,3“.
1
COVID-19
1,2,3
Navedeni primjer bi označavao da virus „COVID-19“ ima identifikator
„1“ te ima tri simptoma kojima su redom dodijeljeni simptomi s
identifikatorima „1“, „2“ i „3“.
4. U svim klasama koje predstavljaju entitete u aplikaciji implementirati
sučelje koje omogućava serijalizaciju objekata.
5. Na kraju programa serijalizirati sve objekte klase „Zupanija“ u kojoj
je postotak zaraženosti stanovnika veća od 2 %.
Primjer izvođenja programa:
Učitavanje podataka o županijama…
Učitavanje podataka o simptomima…
Učitavanje podataka o bolestima…
Učitavanje podataka o virusima…
Učitavanje osoba…
Popis osoba: 
Ime i prezime: Pero Perić
Starost: 88
Županija prebivališta: GRAD ZAGREB
Zaražen bolešću: Gripa
Kontaktirane osobe:
Nema kontaktiranih osoba.
Ime i prezime: Janko Janić
Starost: 77
Županija prebivališta: MEĐIMURSKA
Zaražen bolešću: Gripa
Kontaktirane osobe:
Pero Perić
Ime i prezime: Marica Ždrerić
Starost: 44
Županija prebivališta: VARAŽDINSKA
Zaražen bolešću: Gripa
Kontaktirane osobe:
Pero Perić
Janko Janić
Od virusa Gripa boluju: Pero Perić, Janko Jankić, Marica Žderić
Najviše zaraženih osoba ima u županiji VARAŽDINSKA: 5%.
Virusi sortirani po nazivu suprotno od poretka abecede:
1. KONJUKTIVITIS
2. GRIPA
3. COVID
Sortiranje objekata korištenjem lambdi traje X milisekundi, a bez lambda traje
X milisekundi.
Unesite string za pretragu po prezimenu: ić
Osobe čije prezime sadrži “ić” su sljedeće:
Pero Perić
Janko Janić
Marina Žderić
COVID ima 1 simptoma.
GRIPA ima 1 simptoma.
KONJUKTIVITIS ima 1 simptoma.
