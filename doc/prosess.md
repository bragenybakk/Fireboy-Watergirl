# Prosess

## Metodikk
Vi bruker **Kanban** med GitLab Issue Board. Issues representerer
brukerhistorier eller mindre tasks, og flyttes mellom kolonner (Open → In
progress → Closed) etterhvert som arbeidet skjer. Vi har ikke faste sprint-
lengder, men hver innlevering fungerer som en milepæl.

Vi har plukket elementer fra Issue-board underveis:
- **Iterativ utvikling** — små inkrementer fra møte til møte.
- **Code review** via merge requests før noe merges til `main`.
- **Felles eierskap** — alle kan jobbe i alle deler av kodebasen, men
  rolleansvaret (se [`report-4.md`](./report-4.md)) gjør at en person
  følger opp at sitt område blir tatt vare på.

## Møter
- **Jevnlige team-møter** hvor vi går gjennom hva som er
  gjort, tar avgjørelser, og planlegger neste uke.
- **Møter med gruppeleder** etter fasste innleveringer.
- Korte referater skrives ned i [`møtereferater.md`](./m%C3%B8tereferater.md).

## Kommunikasjon
- **Discord** for alt som skjer mellom møter — spørsmål, diskusjoner,
  beskjeder om når vi jobber.
- **GitLab** for kode, issues og merge requests.
- **Fysiske møter** på campus når det er praktisk.

## Arbeidsfordeling
- Oppgaver opprettes som issues i GitLab.
- Alle kan plukke opp hvilken som helst issue og kode i alle deler av
  prosjektet. Slik slipper vi flaskehalser der noen må vente på at andre
  blir ferdige.
- Når oppgaven er ferdig: merge request → reviewer (typisk den som har
  ansvarsområdet for koden som endres) → merge til `main`.
- Ansvarsområdene (se [`report-4.md`](./report-4.md)) styrer kun hvem som
  går gjennom merge requests for sitt område, ikke hvem som koder hva.

## Versjonskontroll
- Branch per issue.
- Engelske commit-meldinger (besluttet på møte 2026-01-29).
- Hver innlevering tagges på den aktuelle commit-en (`V1`, `v2`, `v3`).

## Erfaringer
- Tydelig oppgavefordeling i issue board gir god fremdrift.
- Jevnlige møter gjør at ingen jobber for lenge på feil ting.
- Tidlig integrasjon av kode reduserer merge-konflikter.
- Kontinuerlig oppdatering av dokumentasjon gjør innlevering enklere, men
  vi har slitt litt med å holde dokumentasjonen helt synkronisert med koden,
  så vi har en runde med dokumentopprydding mot hver innlevering.
- Ikke bite over for mye enn hva vi rekker å implementere. Mange
  gode ideer ved starten av prosjektet som var litt for ambisiøst og tidkrevende.
