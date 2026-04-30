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
holder Git-arbeidsflyten i gang. Vi har ikke en formell **kundekontakt**;
gruppen er liten nok til at vi kan diskutere kundebehov i fellesskap når
det trengs.

**Konklusjon:** Rollene fungerer som de skal. Ingen oppdatering nødvendig
for siste sprint.

## Prosjektmetodikk
**Kanban via GitLab Issue Board** — fra
[fjerde møte (12.02)](./m%C3%B8tereferater.md#2026-02-12--fjerde-møte)
og fremover. Detaljer i [`prosess.md`](./prosess.md).

**Hva fungerer:**
- Issues er små nok til å plukkes og fullføres innen en uke.
- Branch per issue + merge request gir oss code review nesten gratis.
- Ukentlige møter holder alle synkroniserte uten at det blir for mye møter.

**Hva kunne vært bedre:**
- Vi var trege med å lage issues for små refactor- og dokumentasjonsoppgaver.
  De ble ofte gjort uten at noen visste at det skjedde.
- Vi har ikke hatt faste sprint-grenser annet enn innleveringene, så små
  oppgaver kunne flyte i ukevis.

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
[engelske commit-meldinger på 29.01](./m%C3%B8tereferater.md#2026-01-29--andre-møte)).

## Kommunikasjon
- **Discord** — primært verktøy for alt som ikke er kode. Fungerer godt.
- **Fysiske møter på campus** — ukentlig. Effektivt for å diskutere ting
  som er vanskelige å forklare i tekst.
- **GitLab issues og MR-er** — for kode-spesifikke diskusjoner.

Kommunikasjonen har vært et av de sterkeste områdene i prosjektet.

## Commit-fordeling
Vi forventer noe skjevhet fordi rollene er ulike (test- og arkitektur-
ansvarlig committer mer enn rolle-ansvarlig for møter), men ingen i gruppen
har vært "passive". Etter
[gruppeleder-møtet 13.04](./m%C3%B8tereferater.md#2026-04-13--tredje-møte-med-gruppeleder)
ble vi mer bevisste på å balansere bidragene.

## Retrospektiv

### For denne sprinten (innlevering 3 → 4)
**Hva gikk bra**
- Vi fullførte alle planlagte funksjons-issues (open-door-animasjon, win-fade,
  redesign av menyer, fiende-AI med tilstander).
- MVC-grensene holdt seg rene helt til slutten — ingen lekkasje av
  Swing-typer inn i modellen, ingen lekkasje av modell-detaljer ut til
  controller.
- Abstract factory-mønsteret for `Theme` viste sin verdi — vi kunne
  hot-swappe hele utseendet uten å røre logikken.

**Hva gikk ikke som planlagt**
- Vi undervurderte hvor tungvint det er å teste Swing-UI hodeløst, så
  view-koden har lav dekning (3.8 %). Per
  [krav-listen i emne-wikien](https://git.app.uib.no/inf112/26v/inf112-26v/-/wikis/prosjekt/krav#krav-til-tester)
  teller ikke view-kode på testdekningskravet siden den er godt adskilt fra
  forretningslogikken — så *effektiv* dekning (eksklusiv `view` og `app`)
  er **82.9 %**, godt over 75 %-kravet.

### For hele prosjektet
**Hva har vi gjort bra**
- Holdt fast på MVC-arkitekturen fra dag én. Det betalte seg når vi senere
  la til pools, fiender, gems, bevegelige plattformer — alt passet inn uten
  å rote til eksisterende kode.
- Bruk av interfaces gjorde at vi kunne legge til nye entitets-typer uten å
  endre `GameModel` (open-closed-prinsippet).
- Kanban + ukentlige møter ga oss synlighet på fremdrift uten for mye
  prosess-overhead.

**Hva ville vi gjort annerledes hvis vi startet på nytt**
- **Definert ordentlige roller (av den typen vi endte opp med) fra start
  istedenfor de kode-spesifikke områdene.** Først da
  [vi justerte rollene 26.02](./m%C3%B8tereferater.md#2026-02-26--sjette-møte)
  fikk testing, møteplanlegging og dokumentasjon tydelig eierskap. De
  første ukene led litt av at ingen følte seg ansvarlig for disse
  områdene.
- **Hatt en tydelig Git-fremgangsmåte fra dag én.** Alle var nye til Git i
  starten, og det var ganske kaotisk — uklart hvordan issues skulle
  plukkes/lages, hvordan merges skulle skje, hvordan commit-meldinger
  skulle se ut. Vi burde laget et eget `git-fremgangsmate.md`-dokument med
  regler for issue-flyt, branch-navngivning, commit-meldinger og MR-er,
  slik at alle gjorde det samme. Dette ble bedre utover prosjektet, men
  starten kunne vært mye smidigere.
- **Konfigurert JaCoCo-eksklusjoner for view/app fra første uke** slik at
  coverage-tallet hele tiden reflekterte det vi faktisk testet (modellen).
- **Brukt et grid-system for level-design.** Vi spesifiserer nivåer med rå
  doubles for hver entitet (`WALL 30 55 40 4`), noe som gjør det tungvint å
  plassere ting jevnt og pent — vi må regne ut piksel-koordinater manuelt
  hver gang. Et tile-/grid-basert system (f.eks. en ASCII-rute der hvert
  tegn er en blokk) ville gjort level-design mye raskere og mer visuelt.
  Hvis vi hadde fortsatt utviklingen er dette første ting vi ville lagt om.
- **Brukt mer pair programming på vanskelige biter** (kollisjoner,
  fiende-AI). Når én person sitter alene blir det flaskehalser.

### Tre forbedringspunkter for siste sprint (planlagt og utført)
1. ✅ Fikse open-door-animasjon og win-fade.
2. ✅ Oppdatere all dokumentasjon (brukerhistorier, prosess, arkitektur, kilder).
3. ✅ Øke testdekning over 75 %. Effektiv dekning (eksklusiv `view`/`app`)
   er **82.9 %**.

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
  element-system) ble bygget tidlig fordi den er sentral for konseptet —
  men polish-ting som open-door-animasjon og win-fade lå urørt til slutt.
- **Funksjonalitet før polish.** Open-door-animasjon og win-fade ble lagt
  til helt på slutten fordi det var "kirsebær på toppen", ikke kjernekrav.
- **Vi tok ikke på alt.** Vi vurderte å lage en level editor og skuddmekanikk
  for fiender, men droppet begge fordi kvalitet på det vi har var viktigere
  enn flere features.

### Endringer på MVP-listen
Vi har **ikke** justert MVP-listen siden første innlevering. Innholdet ble
låst tidlig og vi har holdt oss til det.

### Bugs i implementerte krav
- **Test-bug, fanget og fikset:** spilleren falt gjennom matchende pools i
  noen edge-cases (issue #61). Fikset.
- **Visuell quirk:** av og til hopper "open door"-spriten på en dør hvis
  en spiller står på kanten av hitboksen. Ikke kritisk, ikke fikset.
- **Ingen kjente kritiske bugs** i de implementerte kravene.

## Brukerhistorier
Alle implementerte funksjoner har en brukerhistorie i
[`brukerhistorier.md`](./brukerhistorier.md), med:
- Akseptansekriterier
- Arbeidsoppgaver
- Hvilke MVP-krav historien dekker

## Prioritering fremover
Det er ikke flere planlagte oppgaver — denne innleveringen er ferdig-
implementasjonen. Hvis vi hadde fortsatt:
1. Konfigurere JaCoCo med `<excludes>` for `view`/`app` i `pom.xml`.
2. Flere nivåer (krever bare en `levelN.txt`-fil).
3. Lagre fremgang mellom kjøringer (per nå nullstilles unlocked-status).

---

# Del 3 — Kode

## Arkitektur og designvalg
Strikt MVC. Detaljer i [`arkitektur.md`](./arkitektur.md). De viktigste
designvalgene har vist seg å være riktige:

- **Cross-layer-interfaces** (`ControllableGameModel`, `ViewableGameModel`)
  — modellen er fri for view-/controller-avhengigheter.
- **Template Method på `StaticEntity`** — alle entitets-typer deler kontakt-
  dispatch men implementerer egen `contactAction()`.
- **Abstract Factory for `Theme`** — bytting av visuell stil i
  `GameView`-konstruktøren.
- **Interface-arv** (`IStaticEntity` → `IMovable` → `IPlayer`/`IEnemy`)
  — `Box` er det eneste som realiserer både `StaticEntity` og `IMovable`,
  og det fungerer rent.

Det er **enkelt** å få ting til å henge sammen — kollisjons-systemet bruker
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

Alle i gruppen kan lese og endre alle deler av koden. Det viktigste er at
modellen ikke har egne UI-konsepter, så ingen sliter med å forstå hva som
foregår i en metode.

## Refaktoreringer
- **Theme abstract factory** (issue #42) — viktigste refactor. Ekstraherte
  alle visuelle valg fra `GameView` til et grensesnitt med to
  implementasjoner.
- **Pool-foot-point-fix** (issue #61) — endret kollisjon med pools fra
  AABB til "foot point", noe som fikset bug der spilleren falt gjennom
  matchende pools.
- **Open-door-state per tick** (issue #78) — flyttet `Door.isOpen`
  oppdatering ut av kollisjons-callback og inn i `clockTick()` slik at
  state matcher faktisk plassering hvert frame.

## Testing
- Alle tester passerer og kjører hodeløst (LibGDX headless backend +
  JUnit 5). Hele suiten kjører på under et sekund.
- **Testdekning per pakke (instruction):**

  | Pakke | Dekning |
  |-------|---------|
  | `coordinateSystem` | 100.0 % |
  | `model/enemy` | 96.2 % |
  | `model/player` | 94.3 % |
  | `controller` | 90.1 % |
  | `model/entity` | 85.1 % |
  | `model` | 78.0 % |
  | `view/theme` | 57.0 % |
  | `view` | 3.8 % |
  | `app` | 0.0 % |

- **Effektiv dekning (eksklusiv `view` og `app`): 82.9 %** — over kravet på 75 %.
- Råtall (med alt): 48.6 %.

**Hvorfor view/app er ekskludert:**
[Krav-listen i emne-wikien](https://git.app.uib.no/inf112/26v/inf112-26v/-/wikis/prosjekt/krav#krav-til-tester)
sier at "view-kode som er godt adskilt fra resten teller ikke på kravet til
test coverage." Vår view er ren `Graphics2D`-rendering uten egen tilstand —
den leser kun fra `ViewableGameModel` og kaller `theme.getX()`. `app`-pakken
inneholder bare `Main.java` (entry point). Begge er triviell glue uten
forretningslogikk.

## Hva vi trenger hjelp med
Ingenting akutt. Hvis vi hadde fortsatt etter siste innlevering, ville vi
prøvd å forstå hvordan andre INF112-grupper testet UI-koden sin.

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
Innholdet er kontrollert og tilpasset av gruppen.*
