# Krav og våre mål

## MVP-krav

- Vise et spillebrett
- Vise spiller på spillebrett
- Flytte spiller (vha taster e.l.)
- Spiller interagerer med terreng
- Spiller har poeng og interagerer med poenggjenstander
- Vise fiender/monstre; de skal interagere med terreng og spiller
- Spiller kan dø (ved kontakt med fiender, eller ved å falle utfor skjermen)
- Mål for spillbrett (enten et sted, en mengde poeng, drepe alle fiender e.l.)
- Nytt spillbrett når forrige er ferdig
- Start-skjerm ved oppstart / game over

## Ikke-funksjonelle krav

- Koden skal være strukturert og lett å vedlikeholde
- Prosjektet skal kunne bygges og kjøres med Maven
- Dokumentasjon skal være oppdatert for hver innlevering
- Tester skal dekke sentral modell-logikk

## Final sprint-mål

### Design / prosjekt / prosess / rapportering (20%)

**Formalia**
- README.md-fil med navn på teammedlemmer, teamet og prosjektet, kort beskrivelse og brukerveiledning, teknisk info om hvordan koden kjøres, og informasjon om hvor grafikk/lyd er hentet fra (kilde/opphavsrett)
- doc/report-X.md-fil med oversiktlig svar på oppgaver
- Alt er oversiktlig og riktig format

**Team**
- Møtereferater (i, eller lenket til fra rapporten)
- Teambeskrivelse og rollefordeling
- Beskrivelse av prosjektmetodikk
- Retrospektiv – hva var planlagt, hva ble gjort og hvorfor, hva ville dere gjort annerledes
- Project board, issues etc. er oppdatert
- Gruppedynamikk og kommunikasjon: alle meninger blir hørt, alle bidrar jevnt, tonen er god
- Alle bidrar til normalt, godt arbeidsmiljø

**Git / versjonskontroll**
- Commits er ryddige
- Ingen filer mangler
- Innlevering er tagget riktig
- Commit-meldinger er meningsfulle
- doc/report-X.md har oversikt over evt. større endringer eller forbedringer dere har blitt bedt om å gjøre
- Jevn fordeling av commits mellom teammedlemmer

### Programvare, produkt og kvalitet (20%)

**Spesifikasjon**
- Overordnet beskrivelse av konsept
- Brukerhistorier
- Akseptansekriterier og arbeidsoppgaver (kort beskrivelse)
- Hva inngår i MVP? Hva er evt. stretch goal?

**Produktleveranse**
- Koden sjekker ut og bygger
- Kan bygges og kjøres på alle operativsystem
- Kan bygges og testes ikke-interaktivt (dvs. på en server uten tilhørende skjerm/tastatur)
- Kravene er oppfylt
- Spillet er spillbart
- Teknisk dokumentasjon om oppsett (i README.md)
- pom.xml er oppdatert med korrekt prosjektnavn, main-klassenavn etc.
- Kan kjøres med `mvn exec:java` og `java -jar …`
- Teknisk beskrivelse av prosjektet og arkitekturen (inkl. klassediagram)

**Kodekvalitet**
- God kodestil (formattering, meningsfulle variabelnavn, unngå code smells), koden er tilstrekkelig dokumentert
- Følger single responsibility principle; high cohesion, low coupling
- God bruk av interface, arv, kodegjenbruk – open-closed principle
- Korrekt bruk av arv – Liskov substitution principle
- Unngå unødvendige avhengigheter – interface segregation principle
- Referer til abstraksjoner (interfaces), ikke konkrete klasser – dependency inversion principle
- God navngivning
- Public metoder dokumentert
- Don't Repeat Yourself – bruk abstraksjon heller enn copy/paste
- Unngå død og råtnende kode
- Pakkenavn er fornuftige og utlevert eksempelkode er fjernet

**Testing**
- Test coverage
- Tester som faktisk kan finne feil
- Tester er automatiske
- Automatiske tester kan kjøres «hodeløst»
- Minimum 75% test coverage
