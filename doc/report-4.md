# Rapport – innlevering 4
**Team:** Fireboys – Petter, Brage, Oscar, Magnus
**Gruppenummer:** *(legges inn)*
**Innleveringsdato:** 2026-05-04

---

## 1. Kort om prosjektet
Fireboys er et 2D co-op platformer-spill inspirert av *Fireboy and Watergirl*.
To spillere styrer hver sin karakter (FIRE og WATER) gjennom brett med
hindringer, gems og fiender, og må samarbeide for å nå hver sin dør.

Spillet bygges med Java 25, Maven og Swing (`Graphics2D`). Tester kjører
hodeløst med JUnit 5.

For en teknisk gjennomgang og klassediagram, se [`arkitektur.md`](./arkitektur.md).

## 2. Status mot kravene

### MVP
Alle 10 MVP-krav fra [`Krav og våre mål.md`](./Krav%20og%20v%C3%A5re%20m%C3%A5l.md)
er implementert. Se [`brukerhistorier.md`](./brukerhistorier.md) for hvilken
historie som dekker hvilket krav.

### Stretch goals (ferdig)
- Multiplayer / co-op med to spillere på samme tastatur.
- Element-system (FIRE / WATER) med matchende pools og gems.
- Bevegelige plattformer, boost-plater, bokser, knapper, spaker.
- Fiender med tilstandsmaskin (PATROL / ALERT / CHASE) og sprite-animasjon.
- Tema-system (abstract factory) — `CastleTheme` og `NullTheme`.
- Lyd (musikk + effekter) som kan slås av/på i settings.
- Beste tid lagres per nivå.
- Open-door-animasjon og win-fade.

### Krav som ikke er oppfylt
- **Test coverage er 44.9 %** (instruction coverage), godt under kravet på
  75 %. Modell-koden er bra dekket (78–94 %), men `view` (3.4 %) og
  `controller` (0 %) drar ned. Vi bør vurdere å enten skrive flere
  view/controller-tester eller ekskludere disse pakkene fra
  coverage-rapporten der det gir mening.

## 3. Endringer siden innlevering 3
- **Issue #42** — Theme-system implementert som abstract factory
  (`CastleTheme`, `NullTheme`), bytte av tema gjøres i `GameView`-konstruktøren.
- **Issue #67** — Skjelett-fiender med patrol/alert/chase-tilstand og
  sprite-animasjon.
- **Issue #65 / #75** — Helkropps-sprites for Fireboy og Watergirl, og
  redesign av menyene med konsistent visuelt uttrykk.
- **Issue #69** — To dører per brett; vinner krever at *begge* spillere står
  på en dør samtidig (i tillegg til at alle gems er samlet).
- **Issue #66** — Bevegelige plattformer som bærer spilleren.
- **Issue #62** — Fireboy-gems og fargekoding av gems per element.
- **Issue #70** — Fix på «laggy» heis i level 3.
- **Issue #78 (open-door-effect)** — Døren tegnes med en åpen variant når en
  spiller står på den, og skjermen fader hvit i ett sekund før vi går til
  level select.
- **Issue #80 (denne)** — Oppdatering av all dokumentasjon
  (`brukerhistorier.md`, `prosess.md`, `roller.md`, `arkitektur.md`,
  `møtereferater.md`).

## 4. Hva ble planlagt vs. gjort
**Planlagt:** ferdigstille åpen-dør-animasjon, polere visuell stil, oppdatere
all dokumentasjon, øke testdekning over 75 %.

**Gjort:** alt unntatt testdekningen. Den landet på 44.9 % på grunn av lav
dekning i `view` og `controller`. Modellen er fortsatt over 78 %.

## 5. Project board
GitLab Issue Board brukes som Kanban-tavle. Alle issues for denne
innleveringen er enten lukket eller markert med **FSR** (Final Sprint
Requirements). Branches er navngitt `<issue-nr>-kort-beskrivelse`, og merges
går via merge requests.

## 6. Retrospektiv (deloppgave A5)

### Hva gikk bra
- Tydelig oppgavefordeling i issue board fra 12.02 og fremover gjorde at vi
  alltid visste hva neste lille bit var.
- Ukentlige møter holdt oss synkroniserte, og vi unngikk at noen jobbet for
  lenge på feil ting.
- MVC-strukturen holdt seg ren gjennom hele semesteret. Modellen vet ikke
  om viewet, og viewet leser kun gjennom `ViewableGameModel`.
- Abstract factory for tema (`Theme`) viste seg å være et godt valg — det
  gjorde det enkelt å hot-swappe utseendet og gjorde at vi kunne kjøre
  spillet uten ressurser via `NullTheme`.

### Hva gikk ikke som forventet
- **Testdekningen** ble dårligere enn vi håpet. Vi planla å skrive UI-tester,
  men Swing-kode er tungvint å teste hodeløst, og vi nedprioriterte det til
  fordel for funksjonalitet. Dette er den største utestående utfordringen.
- Møtereferater ble ikke skrevet konsistent gjennom hele semesteret — vi har
  notater fra januar/februar og en gruppeleder-samtale i april, men flere
  møter fra slutten av februar og mars har vi ikke skrevet ned.
- Commit-fordelingen var litt skjev tidlig i prosjektet. Etter
  gruppeleder-møtet 13.04 har vi vært mer bevisste på å fordele commits
  jevnere.

### Hva ville vi gjort annerledes
- Skrive møtereferater i sanntid hver gang, ikke i etterkant.
- Sette opp coverage-mål (eller ekskludere view/controller fra
  coverage-rapporten) tidligere, slik at vi ikke ble overrasket på slutten.
- Mer paring (pair programming) på vanskelige refactors for å unngå at
  enkeltpersoner blir flaskehals.

## 7. Lenker
- [`arkitektur.md`](./arkitektur.md) — arkitektur og klassediagram
- [`brukerhistorier.md`](./brukerhistorier.md) — alle brukerhistorier med
  akseptansekriterier, arbeidsoppgaver og MVP-krav
- [`Krav og våre mål.md`](./Krav%20og%20v%C3%A5re%20m%C3%A5l.md) — kravsspek
- [`konsept.md`](./konsept.md) — spillkonsept
- [`prosess.md`](./prosess.md) — prosess og metodikk
- [`roller.md`](./roller.md) — rollefordeling
- [`møtereferater.md`](./m%C3%B8tereferater.md) — møtereferater
- [`kilder.md`](./kilder.md) — grafikk- og lydkilder
