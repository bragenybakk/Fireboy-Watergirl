# Rapport – innlevering 4
**Team:** Fireboys – Petter, Brage, Oscar, Magnus
**Innleveringsdato:** 2026-05-04

---

## Kort om prosjektet
Fireboy & Watergirl er et 2D co-op platformer-spill inspirert av *Fireboy and Watergirl*.
To spillere styrer hver sin karakter (FIRE og WATER) gjennom brett med
hindringer, gems og fiender, og må samarbeide for å nå hver sin dør.

Spillet er bygget med Java 25, Maven og Swing (`Graphics2D`). Tester kjører
hodeløst med JUnit 5.

For en teknisk gjennomgang og klassediagram, se [`arkitektur.md`](./arkitektur.md).

---

# Del 1 — Team og prosjekt

## Roller
Vi har fire områdebaserte roller:

| Person | Rolle | Hva det betyr i praksis |
|--------|-------|------------------------|
| Petter | Teamlead + GitLab-ansvarlig | Planlegger og innkaller til fysiske møter. Holder issue board oppdatert, lager branches, går gjennom MR-er, tagger innleveringer. |
| Magnus | Referat / møte / rapport-ansvarlig | Skriver møtereferater, holder møter i gang, oppdaterer rapporter til hver innlevering. |
| Brage | Test-ansvarlig | Sørger for testdekning, at testene faktisk fanger feil, og at de kan kjøres uten skjerm. |
| Oscar | Arkitekturansvarlig + level-design | Holder kodebasen ryddig (MVC, interfaces, refactoring) og lager nivåer. |

Roller og hovedansvarsområder ble fordelt på
[første møte (23.01)](./m%C3%B8tereferater.md#2026-01-23--første-møte)
og [justert på sjette møte (26.02)](./m%C3%B8tereferater.md#2026-02-26--sjette-møte)
etter at vi så behov for tydelige ansvarsområder for testing,
møteplanlegging og dokumentasjon. Alle bidrar til alle deler av prosjektet;
rollene betyr bare at én person har et særlig ansvar for at sitt område
blir fulgt opp.

**Petter** fungerer som teamlead i tillegg til å være GitLab-ansvarlig —
det henger naturlig sammen siden han allerede planlegger fysiske møter og
holder Git-arbeidsflyten i gang. Vi har ikke en formell **kundekontakt**.
Gruppen er liten nok til at vi kan diskutere kundebehov i fellesskap når
det trengs. Alle medlemmer som har mulighet deltar på gruppemøter og de som ikke kan delta, får oppdatering neste arbeidsmøte.

**Konklusjon:** Rollene fungerer som de skal. Ingen oppdatering nødvendig
for siste sprint.

## Prosjektmetodikk
**Kanban via GitLab Issue Board** — fra
[fjerde møte (12.02)](./m%C3%B8tereferater.md#2026-02-12--fjerde-møte)
og fremover. Detaljer i [`prosess.md`](./prosess.md).

Vi har delt prosjektet inn i **to sprinter**, hver med sin egen GitLab-milestone:
- **Sprint 1 — frem til [MVP](https://git.app.uib.no/inf112/26v/proj/fireboys/-/milestones/1#tab-issues).**
  Fokus på å få på plass de 10 MVP-kravene og kjernefunksjonaliteten
  (co-op, element-system, kollisjoner, nivå-parser).
- **Sprint 2 — frem til [FSR (Final Sprint Requirements)](https://git.app.uib.no/inf112/26v/proj/fireboys/-/milestones/2#tab-issues).**
  Polish, stretch goals og å heve testdekning og dokumentasjon til endelig nivå.

**Hva fungerer:**
- Issues er små nok til å plukkes og fullføres innen en uke.
- Branch per issue + merge request gir oss code review nesten gratis.
- Ukentlige møter holder alle synkroniserte uten at det blir for mye møter.

**Hva kunne vært bedre:**
- Vi var trege med å lage issues for små refactor- og dokumentasjonsoppgaver.
  De ble ofte gjort uten at noen visste at det skjedde.
- Vi tok litt for ofte "skippertak" på kodedokumentasjon og test-skriving
  mot slutten av hver sprint. Begge deler burde gått stegvis med selve
  koden. Det er lettere å skrive Javadoc og tester når logikken er fersk
  i hodet, og vi unngår at en stor pukkel hoper seg opp før innlevering.

**Konklusjon:** Selve Kanban-metodikken fungerer godt for oss og er noe vi
ville beholdt. Det vi *ville* gjort annerledes er å ha tydeligere
fremgangsmåter for Git-arbeidsflyten og rollene fra dag én — se
retrospektivet under. 

## Gruppedynamikk
Tonen i gruppen er god. Ingen større uenigheter har oppstått.
Oppmøte-statistikken (se [`møtereferater.md`](./m%C3%B8tereferater.md))
viser at de fleste møtene har vært 4/4, med kun et par på 3/4 — bra
fremmøte gjennom hele semesteret. Diskusjoner foregår åpent, og
avgjørelser tas i fellesskap (f.eks. valg av Kanban, og
[engelske commit-meldinger på 29.01](./m%C3%B8tereferater.md#2026-01-29--andre-møte)). Alle gruppemedlemmene kjente hverandre fra før, noe som bidro til enklere kommunikasjon og at alle var trygge på hverandre. Vi ser hverandre også utenfor de planlagte arbeidsmøtene og kan derfor ta opp småting ved behov.

## Kommunikasjon
- **Discord** — primært verktøy for alt som ikke er kode. Fungerer godt.
- **Fysiske møter på campus** — ukentlig. Effektivt for å diskutere ting
  som er vanskelige å forklare i tekst.
- **GitLab issues og MR-er** — for å fordele og delegere kodeoppgaver, samt dobbeltsjekke andres kode.

Kommunikasjonen har gått sømløst uten stor problemer.

## Retrospektiv

**Hva har vi gjort bra**
- Holdt fast på MVC-arkitekturen. Det lønnet seg når vi senere
  la til pools, fiender, gems, bevegelige plattformer uten 
  å endre eksisterende kode.
- Bruk av interfaces gjorde at vi kunne legge til nye entitets-typer uten å
  endre `GameModel` (open-closed-prinsippet).
- Kanban + ukentlige møter ga oss synlighet på fremdrift uten for mye
  prosess-overhead.

**Hva ville vi gjort annerledest**
- **Definert ordentlige roller (av den typen vi endte opp med) fra start
  istedenfor de kode-spesifikke områdene.** Først da
  [vi justerte rollene 26.02](./m%C3%B8tereferater.md#2026-02-26--sjette-møte)
  fikk testing, møteplanlegging og dokumentasjon tydelige ansvar. De
  første ukene led litt av at ingen følte seg ansvarlig for disse
  områdene.
- **Hatt en tydelig Git-fremgangsmåte fra dag én.** Alle var nye til Git i
  starten, og det var ganske kaotisk. Det var uklart hvordan issues skulle
  plukkes/lages, hvordan merges skulle skje, hvordan commit-meldinger
  skulle se ut. Vi burde laget et eget `git-fremgangsmate.md`-dokument med
  regler for issue-flyt, branch-navngivning, commit-meldinger og MR-er,
  slik at alle gjorde det samme. Dette ble bedre utover prosjektet, men
  starten kunne vært mye oversiktlig og effektivt.
- **Brukt et grid-system for level-design.** Vi spesifiserer nivåer med rå
  doubles for hver entitet (`WALL 30 55 40 4`), noe som gjør det tungvint å
  plassere der vi vil ha det. Vi må regne ut piksel-koordinater manuelt
  hver gang. Det var veldig mye manuelt arbeid i level-design prosessen 
  hvor man måtte åpne/lukke spillet etter hver lille endring for å sjekke hvordan det ser ut.
  Et tile-/grid-basert system (f.eks. en ASCII-rute der hvert
  tegn er en blokk) ville gjort level-design mye raskere og mer visuelt.
  Hvis vi hadde fortsatt utviklingen er dette første ting vi ville lagt om.

## Commit-fordeling
Det er noe skjevhet i commit-fordelingen, og det har to hovedårsaker.
For det første har enkelte roller mer commit-tunge oppgaver enn andre.
For det andre har vi til ulike tider hatt andre skoleprosjekter, ferie eller vært bortreist som har gjort at
folk har vært mindre tilgjengelige i perioder. I de tilfellene har vi
passet på at man kan ta igjen arbeid etterpå, slik at belastningen
jevner seg ut over tid.

Ingen i gruppen føler at de har gjort uforholdsmessig mye arbeid sammenlignet med resten. Etter
[gruppeleder-møtet 13.04](./m%C3%B8tereferater.md#2026-04-13--tredje-møte-med-gruppeleder)
ble vi i tillegg mer bevisste på å balansere bidragene.

## Møtereferater
Se [`møtereferater.md`](./m%C3%B8tereferater.md). Inkluderer alle ni
team-møter (23.01–20.04) og to gruppeleder-møter
([05.03](./m%C3%B8tereferater.md#2026-03-05--andre-møte-med-gruppeleder),
[13.04](./m%C3%B8tereferater.md#2026-04-13--tredje-møte-med-gruppeleder)).

---

# Del 2 — Krav og spesifikasjon

## Status mot kravene

### MVP — alle 10 krav er oppfylt
Se [`Krav og våre mål.md`](./Krav%20og%20v%C3%A5re%20m%C3%A5l.md) for selve
listen og [`brukerhistorier.md`](./brukerhistorier.md) for hvilken historie
som dekker hvilket krav.

### Stretch goals (alle ferdig)
- Multiplayer / co-op med to spillere på samme tastatur.
- Element-system (FIRE / WATER) med matchende pools og gems.
- Bevegelige plattformer, boost-plater, bokser, knapper.
- Fiender med tilstandsmaskin (PATROL / ALERT / CHASE) og sprite-animasjon.
- Tema-system (abstract factory) — `CastleTheme` og `NullTheme`.
- Lyd (musikk + effekter) som kan slås av/på i settings.
- Beste tid lagres per nivå.
- Open-door-animasjon og win-fade.

### Hvorfor disse prioriteringene
- **MVP-kravene først.** Vi prioriterte de 10 MVP-kravene før vi tok fatt
  på rene polish-oppgaver. Noe stretch-funksjonalitet (co-op og
  element-system) ble bygget tidlig fordi den er sentral for konseptet,
  men polish-ting som open-door-animasjon og win-fade lå urørt til slutt.
- **Funksjonalitet før polish.** Open-door-animasjon og win-fade ble lagt
  til helt på slutten fordi det var "kirsebær på toppen", ikke kjernekrav.
- **Ikke fullført.** Vi vurderte å lage en level editor og skuddmekanikk
  for fiender, men droppet begge fordi kvalitet på det vi har var viktigere
  enn flere features.

### Endringer på MVP-listen
Vi har **ikke** justert MVP-listen siden første innlevering. Innholdet ble
låst tidlig og vi har holdt oss til det.

### Bugs i implementerte krav
- **Spiller evegende Platform kollisjon:** Om spiller står under en platform og hopper opp så
  vil den "låses" til bunnen av platformen of følge den på vei ned.
- **Enemy bevegelses:** Om fienden ikke kommer seg nærmere en spiller vil den raskt bytte mellom å se til høyre og venstre. Det er ikke pent visuellt. Påvirker ikke spillet ellers.
- **Boks og Spiller kollisjon:** Kan oppstå feil hvor spiller låser seg mellom vegg og boks. Spiller kan hoppe mellom, men klarer av uvisst grunn ikke å hoppe ut.

## Brukerhistorier
Alle implementerte funksjoner har en brukerhistorie i
[`brukerhistorier.md`](./brukerhistorier.md), med:
- Akseptansekriterier
- Arbeidsoppgaver
- Hvilke MVP-krav historien dekker

---

# Del 3 — Kode


## Refaktoreringer

**Gjennomførte:**
- **Theme abstract factory** (issue #42) — flyttet visuelle valg fra `GameView` til et grensesnitt med to
  implementasjoner.
- **Pool-foot-point-fix** (issue #61) — endret kollisjon med pools fra
  AABB til "foot point", noe som fikset bug der spilleren falt gjennom
  matchende pools.
- **Open-door-state per tick** (issue #78) — flyttet `Door.isOpen`
  oppdatering ut av kollisjons-callback og inn i `clockTick()` slik at
  state matcher faktisk plassering hvert frame.

**Burde vært gjort:**
- **Splitte `GameModel` og `GameView`.** `GameModel` er på 955 linjer og
  `GameView` på 836, mens resten av filene ligger under 260. `GameModel`
  gjør kollisjoner, fysikk, tilstands-overganger og win-logikk i samme
  klasse, og `GameView` tegner alle scenene (meny, level-select, in-game,
  pause, game-over) i én. Det fungerer, men begge burde vært delt opp.
  Kollisjon og fysikk burde vært i egne klasser, og en renderer per scene. Vi rakk
  det ikke denne sprinten uten å risikere å ødelegge kode like før innlevering.


## Arkitektur og designvalg
Vi følger MVC. Detaljer i [`arkitektur.md`](./arkitektur.md). De viktigste
designvalgene har vist seg å være:

- **Egne interfaces for view og controller** (`ViewableGameModel` og
  `ControllableGameModel`). Viewet får bare lese tilstand fra modellen,
  controlleren får bare sende input til den. Modellen selv kjenner
  ikke til view eller controller, så vi kan bytte ut UI eller input
  uten å endre spill-logikken.
- **Felles kollisjons-håndtering i `StaticEntity`.** Alle entiteter arver
  samme metode for hva som skjer ved kontakt, men hver type bestemmer
  selv hva den gjør. En pool dreper spilleren, en boks blir dyttet, en
  knapp aktiveres, osv.
- **`Theme`-interface med to implementasjoner.** Alt det visuelle
  (sprites, farger, fonter) ligger bak ett interface, så vi kan bytte
  hele utseendet ved å sende inn et annet `Theme` til `GameView`.
- **Hierarki av interfaces:** `IStaticEntity` → `IMovable` →
  `IPlayer`/`IEnemy`. `Box` er den eneste som er både statisk *og*
  bevegelig, og kan implementere begge uten noe ekstra styr.

Det er enkelt å få ting til å henge sammen. Kollisjons-systemet bruker
samme dispatch-pattern for alle entitets-typer. Når vi la til pools og gems
sent i prosjektet kunne vi gjøre det uten å endre `GameModel`s
kollisjons-loop.

## Kodekvalitet
- Kort, fokuserte metoder. `GameModel.clockTick()` er ~10 linjer som kaller
  hjelpemetoder.
- Meningsfulle klasse- og metodenavn på engelsk.
- Public-grensesnitt-metoder er dokumentert (Javadoc der det trengs).
- Ingen død kode (vi ryddet `checkWinConditions()` og ubrukte imports
  underveis).
- Felles formatter (`java-formatter.xml`) brukes i prosjektet.

Alle i gruppen kan lese og endre alle deler av koden. Modell-koden
handler bare om spill-logikk og bruker ikke Swing eller andre UI-ting,
så det er enkelt å skjønne hva en metode gjør uten å måtte tenke på
hvordan ting tegnes på skjermen.


## Testing
- Alle tester passerer og kjører hodeløst (LibGDX headless backend +
  JUnit 5). Alle tester kjører på under et sekund. Vi har over 75% dekning på relevante filer.


**Hvorfor view/app er ekskludert:**
[Krav-listen i emne-wikien](https://git.app.uib.no/inf112/26v/inf112-26v/-/wikis/prosjekt/krav#krav-til-tester)
sier at "view-kode som er godt adskilt fra resten teller ikke på kravet til
test coverage." Vår view er ren `Graphics2D`-rendering uten egen tilstand —
den leser kun fra `ViewableGameModel` og kaller `theme.getX()`. `app`-pakken
inneholder bare `Main.java` (entry point).

## Hva vi trenger hjelp med
Ingenting akutt. Hvis vi hadde fortsatt etter siste innlevering, ville vi
prøvd å forstå hvordan andre INF112-grupper har jobbet for å designe levels.
Design av passe utfordrende og morsomme levels var vanskelig og det er nok mye
å lære av andres prosjekter. 

---

## Lenker
- [`arkitektur.md`](./arkitektur.md) — arkitektur og klassediagram
- [`brukerhistorier.md`](./brukerhistorier.md) — alle brukerhistorier
- [`Krav og våre mål.md`](./Krav%20og%20v%C3%A5re%20m%C3%A5l.md) — kravspek
- [`konsept.md`](./konsept.md) — spillkonsept
- [`prosess.md`](./prosess.md) — prosess og metodikk
- [`møtereferater.md`](./m%C3%B8tereferater.md) — møtereferater
- [`kilder.md`](./kilder.md) — grafikk- og lydkilder

---

*AI (Claude) ble brukt til å designe strukturen og formatet på denne filen.
Innholdet er kontrollert og tilpasset av gruppen med bruk av Live Share.*
