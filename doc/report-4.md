# Rapport – innlevering 4
**Team:** Fireboys – Petter, Brage, Oscar, Magnus
**Gruppenummer:** *(legges inn)*
**Innleveringsdato:** 2026-05-04

---

## Kort om prosjektet
Fireboys er et 2D co-op platformer-spill inspirert av *Fireboy and Watergirl*.
To spillere styrer hver sin karakter (FIRE og WATER) gjennom brett med
hindringer, gems og fiender, og må samarbeide for å nå hver sin dør.

Spillet er bygget med Java 25, Maven og Swing (`Graphics2D`). Tester kjører
hodeløst med JUnit 5.

For en teknisk gjennomgang og klassediagram, se [`arkitektur.md`](./arkitektur.md).

---

# Del 1 — Team og prosjekt

## Roller
Vi har fire områdebaserte roller. Detaljer i [`roller.md`](./roller.md):

| Person | Rolle | Hva det betyr i praksis |
|--------|-------|------------------------|
| Petter | GitLab-ansvarlig | Holder issue board oppdatert, lager branches, går gjennom MR-er, tagger innleveringer. |
| Magnus | Referat / møte / rapport-ansvarlig | Skriver møtereferater, holder møter i gang, oppdaterer rapporter til hver innlevering. |
| Brage | Test-ansvarlig | Sørger for testdekning, at testene faktisk fanger feil, og at de kan kjøres uten skjerm. |
| Oscar | Arkitekturansvarlig + level-design | Holder kodebasen ryddig (MVC, interfaces, refactoring) og lager nivåer. |

Vi har ikke en formell **teamlead** eller **kundekontakt**. Det har fungert
greit for oss fordi gruppen er liten og kommunikasjonen er tett — alle tar
ansvar for sin del og snakker direkte med hverandre. For et større prosjekt
ville vi sannsynligvis trengt en mer eksplisitt prosjekteier.

**Konklusjon:** Rollene fungerer som de skal. Ingen oppdatering nødvendig
for siste sprint.

## Prosjektmetodikk
**Kanban via GitLab Issue Board** — fra møte 12.02 og fremover. Detaljer i
[`prosess.md`](./prosess.md).

**Hva fungerer:**
- Issues er små nok til å plukkes og fullføres innen en uke.
- Branch per issue + merge request gir oss code review nesten gratis.
- Ukentlige møter holder alle synkroniserte uten at det blir for mye møter.

**Hva kunne vært bedre:**
- Vi var trege med å lage issues for små refactor- og dokumentasjonsoppgaver.
  De ble ofte gjort uten at noen visste at det skjedde.
- Vi har ikke hatt faste sprint-grenser annet enn innleveringene, så små
  oppgaver kunne flyte i ukevis.

**Konklusjon:** Metodikken fungerer for oss. Vi ville ikke gjort dette
annerledes hvis vi startet på nytt.

## Gruppedynamikk
Tonen i gruppen er god. Ingen større uenigheter har oppstått.
Oppmøte-statistikken (se [`møtereferater.md`](./m%C3%B8tereferater.md)) viser
3 av 5 logged møter med 4/4, og to med 3/4 — bra fremmøte. Diskusjoner
foregår åpent, og avgjørelser tas i fellesskap (f.eks. valg av kanban,
engelske commit-meldinger 29.01).

## Kommunikasjon
- **Discord** — primært verktøy for alt som ikke er kode. Fungerer godt.
- **Fysiske møter på campus** — ukentlig. Effektivt for å diskutere ting
  som er vanskelige å forklare i tekst.
- **GitLab issues og MR-er** — for kode-spesifikke diskusjoner.

Kommunikasjonen har vært et av de sterkeste områdene i prosjektet.

## Commit-fordeling
Vi forventer noe skjevhet fordi rollene er ulike (test- og arkitektur-
ansvarlig committer mer enn rolle-ansvarlig for møter), men ingen i gruppen
har vært "passive". Etter gruppeleder-møtet 13.04 ble vi mer bevisste på å
balansere bidragene.

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
- Testdekningen endte på **44.9 %** (instruction). Modellen er bra dekket
  (78–94 %), men `view` (3.4 %) og `controller` (0 %) trekker ned. Vi
  undervurderte hvor tungvint det er å teste Swing-UI hodeløst.
- Møtereferater for slutten av februar og mars ble aldri skrevet ned —
  bare januar/februar og gruppeleder-møtet 13.04 er logget.

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
- **Sette opp testdekning som en egen sjekk i pom.xml fra første uke.**
  Da hadde vi ikke fått sjokket ved siste innlevering.
- **Skrive møtereferater i sanntid.** Vi forsøkte å huske i etterkant, og
  det fungerte dårlig.
- **Definert UI-grensen tydeligere fra start.** Vi mistet flere uker på å
  skille modell-state fra view-state etter at koden hadde grodd seg fast.
- **Brukt mer pair programming på vanskelige biter** (kollisjoner,
  fiende-AI). Når én person sitter alene blir det flaskehalser.

### Tre forbedringspunkter for siste sprint (planlagt og utført)
1. ✅ Fikse open-door-animasjon og win-fade.
2. ✅ Oppdatere all dokumentasjon (brukerhistorier, prosess, roller, arkitektur).
3. ⚠ Øke testdekning over 75 % — *delvis*. Vi nådde 44.9 % og må
   dokumentere hvorfor i stedet for å skjule det.

## Møtereferater
Se [`møtereferater.md`](./m%C3%B8tereferater.md). Inkluderer alle 5 logged
møter, inklusive møte med gruppeleder 13.04.2026.

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
- Bevegelige plattformer, boost-plater, bokser, knapper, spaker.
- Fiender med tilstandsmaskin (PATROL / ALERT / CHASE) og sprite-animasjon.
- Tema-system (abstract factory) — `CastleTheme` og `NullTheme`.
- Lyd (musikk + effekter) som kan slås av/på i settings.
- Beste tid lagres per nivå.
- Open-door-animasjon og win-fade.

### Hvorfor disse prioriteringene
- **MVP først, alltid.** Vi tok ikke fatt på noe stretch goal før alle
  10 MVP-krav var implementert.
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
1. Øke testdekning for view/controller (eller ekskludere fra coverage).
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
- **103 tester**, alle passerer.
- **Testdekning: 44.9 %** instruction (ned fra målet på 75 %).
  - Modell-pakker: 78–94 %.
  - View: 3.4 %.
  - Controller: 0 %.
  - App: 0 %.
- Tester kan kjøres hodeløst (LibGDX headless backend + JUnit 5).
- Testene kjører på under et sekund, så de er ikke en flaskehals.

**Hvorfor view/controller har lav dekning:**
- Swing-rendering er tungvint å teste hodeløst (krever stub-Graphics2D).
- `GameController` er stort sett glue-kode mellom Swing-events og
  modell-metoder, der det meste ville være integrasjons-tester.

**Hva vi burde gjort:** ekskludere `view` og `controller` fra
JaCoCo-rapporten, slik at coverage-tallet reflekterer det vi faktisk
testet (modell-logikken).

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
- [`roller.md`](./roller.md) — rollefordeling
- [`møtereferater.md`](./m%C3%B8tereferater.md) — møtereferater
- [`kilder.md`](./kilder.md) — grafikk- og lydkilder

---

*AI (Claude) ble brukt til å designe strukturen og formatet på denne filen.
Innholdet er kontrollert og tilpasset av gruppen.*
