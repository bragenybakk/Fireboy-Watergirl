# Brukerhistorier

Hver historie har akseptansekriterier, arbeidsoppgaver, og hvilke
MVP-krav fra [`Krav og våre mål.md`](./Krav%20og%20v%C3%A5re%20m%C3%A5l.md)
den dekker.

---

## Historie 1: Bevegelse
**Som spiller** vil jeg kunne gå til venstre/høyre og hoppe, slik at jeg kan
navigere brettet.

**Akseptansekriterier:**
- `A` / `Pil venstre` flytter spilleren til venstre.
- `D` / `Pil høyre` flytter spilleren til høyre.
- `W` / `Pil opp` utfører hopp når spilleren står på bakken.
- Bevegelse stopper mot vegger og kanter.

**Arbeidsoppgaver:**
- Implementere `Player`-klasse med posisjon og hastighet.
- Lese tastatur i `GameController` og kalle bevegelses-metoder på modellen.
- Legge til gravitasjon og friksjon i `GameModel.clockTick()`.

**MVP-krav dekket:** 2, 3, 4

---

## Historie 2: Co-op med to spillere
**Som to venner** vil vi spille samtidig på samme tastatur, slik at vi kan
samarbeide om å løse brettene.

**Akseptansekriterier:**
- Watergirl styres med piltastene, Fireboy med `WAD`.
- Begge spillere kan bevege seg uavhengig av hverandre.
- Begge må nå hver sin dør for å fullføre brettet.

**Arbeidsoppgaver:**
- Støtte to `Player`-instanser i `GameModel`.
- Mappe ulike taster til hver spiller i `GameController`.
- Win-condition krever at *begge* spillere står på en dør.

**MVP-krav dekket:** 2, 3, 8

---

## Historie 3: Element-baserte farer
**Som spiller** vil jeg ha farlige bassenger som passer til min karakter,
slik at vi må samarbeide om å lede hverandre trygt gjennom brettet.

**Akseptansekriterier:**
- Watergirl dør i FIRE-pool; Fireboy dør i WATER-pool.
- Spilleren kan bevege seg gjennom basseng som matcher elementet.
- Død spiller fører til `GAME_OVER`.

**Arbeidsoppgaver:**
- `ElementState`-enum med `FIRE` og `WATER`.
- `Pool`-entitet med tilhørende element.
- Sjekk i `handlePoolInteraction` om elementene matcher; ellers `player.kill()`.

**MVP-krav dekket:** 4, 7

---

## Historie 4: Samle gems
**Som spiller** vil jeg samle gjenstander på brettet, slik at jeg har et
delmål utover å nå døren.

**Akseptansekriterier:**
- Røde gems samles av Fireboy, blå av Watergirl.
- Alle gems må samles før døren slipper spilleren gjennom.
- Antall samlet vises i HUD.

**Arbeidsoppgaver:**
- `Gem`-entitet med element og collected-flag.
- Kollisjon i `handlePlayerCollisions` markerer gem som samlet hvis element matcher.
- Sjekke `areAllGemsCollected()` i win-conditions.

**MVP-krav dekket:** 5, 8

---

## Historie 5: Fiender
**Som spiller** vil jeg møte fiender på brettet, slik at det blir en
utfordring å unngå dem.

**Akseptansekriterier:**
- Skjelett-fiender patruljerer på plattformer.
- Fiender bytter til alert / chase når spilleren kommer nær.
- Kontakt med en fiende dreper spilleren.
- Fiendene har sprite-animasjon for hver tilstand.

**Arbeidsoppgaver:**
- `Enemy`-klasse med `EnemyState` (PATROL / ALERT / CHASE).
- AI som velger tilstand basert på avstand til spiller.
- `SkeletonSpriteSheet` med riktig animasjonsrad per tilstand.

**MVP-krav dekket:** 6, 7

---

## Historie 6: Døden og GAME OVER
**Som spiller** vil jeg få en tydelig GAME OVER-skjerm når jeg dør, slik at
jeg vet at jeg må starte på nytt.

**Akseptansekriterier:**
- Når en spiller dør går spillet til `GAME_OVER`.
- Skjermen viser «GAME OVER» og en meny.
- `Respawn` laster nivået på nytt; `Main Menu` går til hovedmenyen.

**Arbeidsoppgaver:**
- `GameState.GAME_OVER` med egen meny.
- `setGameState` ved død.
- `drawGameOver` i `GameView`.

**MVP-krav dekket:** 7, 10

---

## Historie 7: Mål for brettet
**Som spiller** vil jeg ha et tydelig mål når begge spillere når sine dører,
slik at vi opplever at vi vant.

**Akseptansekriterier:**
- Døren tegnes som «åpen» når en spiller står på den.
- Når begge spillere står på hver sin dør og alle gems er samlet, fader
  skjermen til hvit i ett sekund før vi går til level select.
- Beste tid lagres per nivå.

**Arbeidsoppgaver:**
- `Door.isOpen` oppdateres per tick basert på spillerposisjon.
- `winDelayTicks` teller opp i `tickWinDelay`; transition etter 60 tick.
- `getWinFadeProgress()` brukes av `GameView.drawWinFade`.

**MVP-krav dekket:** 8, 9

---

## Historie 8: Flere brett
**Som spiller** vil jeg ha flere nivåer å spille gjennom, slik at spillet
har mer innhold.

**Akseptansekriterier:**
- Tre nivåer (level1, level2, level3) finnes.
- Etter fullført brett går spilleren tilbake til level select.
- Neste nivå er låst opp etter at det forrige er fullført.
- Beste tid vises per nivå.

**Arbeidsoppgaver:**
- `GameReader` parser `.txt`-filer i `src/main/resources/`.
- `levels.txt` lister tilgjengelige nivåer.
- `unlockedLevelCount` styrer hvilke nivåer som er låst opp.

**MVP-krav dekket:** 1, 9

---

## Historie 9: Meny og navigasjon
**Som spiller** vil jeg starte spillet, pause, og endre innstillinger via
en meny, slik at jeg har full kontroll uten å bruke mus.

**Akseptansekriterier:**
- Hovedmeny ved oppstart med `START GAME`, `How to Play`, `Settings`, `Exit`.
- `Esc` pauser/fortsetter under spill.
- `W/S` eller piltaster navigerer; `Enter` velger.
- Settings inneholder Music, Sound Effects og Ad Blocker.

**Arbeidsoppgaver:**
- `GameState`-enum med alle skjermene.
- `GameView` har én `draw…`-metode per tilstand.
- `menuUp/menuDown/menuSelect` på `GameModel`.

**MVP-krav dekket:** 10

---

## Historie 10: Interaktive elementer (bokser, knapper)
**Som spiller** vil jeg kunne dytte bokser og bruke knapper, slik at
brettene kan ha mer enn bare hopping og løping.

**Akseptansekriterier:**
- Bokser kan dyttes; tunge bokser krever mer fart.
- Knapper kan trykkes på.
- Bevegelige plattformer bærer spilleren.

**Arbeidsoppgaver:**
- `Box`, `Button`, `MovingPlatform`, `BoostPlatform`-entiteter.
- `handlePush` regner ut force = `playerVelocity / boxWeight`.
- `carryPlayersOnMovingPlatforms` flytter spilleren med plattformen.

**MVP-krav dekket:** 4

---

## Historie 11: Lyd
**Som spiller** vil jeg ha musikk og lydeffekter, slik at spillet føles mer
levende — og kunne skru dem av.

**Akseptansekriterier:**
- Bakgrunnsmusikk spiller når spillet kjører.
- Lydeffekter ved viktige hendelser.
- Settings-menyen kan slå musikk og effekter av/på.

**Arbeidsoppgaver:**
- `AudioManager` med separate flagg for musikk og SFX.
- Settings-menyen toggler `musicEnabled` / `soundEnabled` på modellen.

**MVP-krav dekket:** *(stretch goal)*

---

## Historie 12: Tema (visuelt)
**Som utvikler** vil jeg kunne bytte hele det grafiske utseendet på spillet,
slik at vi enkelt kan teste alternative tema eller falle tilbake til
enkel grafikk hvis ressursene mangler.

**Akseptansekriterier:**
- `Theme`-interface gir alle sprites og farger som `GameView` trenger.
- `CastleTheme` bruker faktiske sprites; `NullTheme` returnerer `null` og
  faller tilbake til enkle farger.
- Bytte av tema gjøres ved å gi en annen `Theme` til `GameView`-konstruktøren.

**Arbeidsoppgaver:**
- Lage `Theme`-interface (abstract factory).
- Implementere `CastleTheme` og `NullTheme`.

**MVP-krav dekket:** *(ikke-funksjonelt: vedlikeholdbarhet)*

---

*AI (Claude) ble brukt til å designe strukturen og formatet på denne filen.
Innholdet er kontrollert og tilpasset av gruppen.*
