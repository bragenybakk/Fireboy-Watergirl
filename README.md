# Fireboy & Watergirl

## Team og prosjektinfo
- **Prosjekt:** Fireboy & Watergirl (inspirert av Fireboy and Watergirl (2009))
- **Teamnavn:** Fireboys
- **Gruppenummer:** *(legges inn)*
- **Teammedlemmer:** Petter, Brage, Oscar, Magnus

## Kort beskrivelse av spillet
Fireboy & Watergirl er et 2D-plattformspill for to spillere med meny, nivåvalg og enkel fysikk. Spillerne styrer hver sin karakter gjennom brett med vegger, bokser, dører, bassenger og farlige elementer. Målet er å samle alle gems og nå døren uten å dø.

## Styring

### Menyer
- `W` / `Pil opp`: flytt markør opp
- `S` / `Pil ned`: flytt markør ned
- `Enter` / `Mellomrom`: velg
- `Esc`: tilbake til hovedmeny

### I spill
| Handling | Watergirl | Fireboy |
|---|---|---|
| Gå venstre | `Pil venstre` | `A` |
| Gå høyre | `Pil høyre` | `D` |
| Hopp | `Pil opp` | `W` |
| Pause | `Esc` | `Esc` |

### Pause
- `Esc`: fortsett spillet
- `W` / `S` / piltaster: naviger
- `Enter`: velg

### Game Over
- `W` / `S` / piltaster: naviger
- `Enter`: velg

## Hvordan kjøre koden

### Krav
- Java 25
- Maven 3.6.3+

### Kjør lokalt
```bash
mvn clean compile
mvn exec:java
```

### Kjør tester
```bash
mvn test
```

## Grafikk- og lydkilder
Se [`doc/kilder.md`](doc/kilder.md) for kilder til musikk, sprites og bilder.

## Dokumentasjon
Se `doc/`-mappen for utviklingsrapporter og øvrig prosjektdokumentasjon:
- [`arkitektur.md`](doc/arkitektur.md) — teknisk arkitektur og klassediagram
- [`brukerhistorier.md`](doc/brukerhistorier.md) — brukerhistorier
- [`Krav og våre mål.md`](doc/Krav%20og%20v%C3%A5re%20m%C3%A5l.md) — krav og MVP
- [`konsept.md`](doc/konsept.md) — konsept
- [`prosess.md`](doc/prosess.md) — prosess og metodikk
- [`roller.md`](doc/roller.md) — rollefordeling
- [`møtereferater.md`](doc/m%C3%B8tereferater.md) — møtereferater
- [`oblig1.md`](doc/oblig1.md), [`oblig2.md`](doc/oblig2.md), [`oblig3.md`](doc/oblig3.md), [`oblig4.md`](doc/oblig4.md) — innleveringsrapporter
