# Fireboy & Watergirl

## Team og prosjektinfo
- **Prosjekt:** Fireboy & Watergirl (inspirert av Fireboy and Watergirl (2009))
- **Teamnavn:** Fireboys
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

## Dokumentasjon
Se `doc/`-mappen for utviklingsrapporter og øvrig prosjektdokumentasjon.
