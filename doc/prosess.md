# Prosess

## Metodikk
Vi bruker **Kanban** med GitLab Issue Board. Issues representerer
brukerhistorier eller mindre tasks, og flyttes mellom kolonner (Open → In
progress → Closed) etterhvert som arbeidet skjer. Vi har ikke faste sprint-
lengder, men hver innlevering fungerer som en milepæl.

Vi har plukket elementer fra Agile / XP underveis:
- **Iterativ utvikling** — små inkrementer fra møte til møte.
- **Code review** via merge requests før noe merges til `main`.
- **Felles eierskap** — alle kan jobbe i alle deler av kodebasen, men
  rolleansvaret (se [`roller.md`](./roller.md)) gjør at en person følger
  opp at sitt område blir tatt vare på.

## Møter
- **Ukentlige team-møter** (typisk torsdag) hvor vi går gjennom hva som er
  gjort, tar avgjørelser, og planlegger neste uke.
- **Møter med gruppeleder** ved behov, særlig før innleveringer.
- Korte referater skrives ned i [`møtereferater.md`](./m%C3%B8tereferater.md).

## Kommunikasjon
- **Discord** for alt som skjer mellom møter — spørsmål, diskusjoner,
  beskjeder om når vi jobber.
- **GitLab** for kode, issues og merge requests.
- **Fysiske møter** på campus når det er praktisk.

## Arbeidsfordeling
- Oppgaver opprettes som issues i GitLab.
- En person plukker en issue og lager en branch (`<issue-nr>-kort-beskrivelse`).
- Når oppgaven er ferdig: merge request → reviewer om nødvendig → merge til `main`.
- Hovedansvar per område fra første møte:
  - Oscar — spillbrett / verden / level-design
  - Petter — Git / infrastruktur
  - Brage — meny / UI
  - Magnus — spiller / fysikk

## Versjonskontroll
- Branch per issue.
- Engelske commit-meldinger (besluttet på møte 2026-01-29).
- Hver innlevering tagges (`oblig-1`, `oblig-2`, ...).

## Erfaringer
- Tydelig oppgavefordeling i issue board gir god fremdrift.
- Ukentlige møter gjør at ingen jobber for lenge på feil ting.
- Tidlig integrasjon av kode reduserer merge-konflikter.
- Kontinuerlig oppdatering av dokumentasjon gjør innlevering enklere — men
  vi har slitt litt med å holde dokumentasjonen helt synkronisert med koden,
  så vi har en runde med dokumentopprydding mot hver innlevering.
- Ikke bite over for mye enn hva vi rekker å implementere. Mange
  gode ideer ved starten av prosjektet som var litt for ambisiøst og tidkrevende.
