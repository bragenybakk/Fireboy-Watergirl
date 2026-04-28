# Møtereferater

Korte referater fra teammøter og møter med gruppeleder.

---

## 2026-01-23 — Første møte
**Til stede:** Petter, Brage, Oscar, Magnus (4/4)

**Plan:**
- Oppgavefordeling: Oscar – Spillbrett, Petter – GIT, Brage – Meny++, Magnus – Player.
- Planlegge spill-idé: Watergirl & Fireboy-type spill — platformer med to spillere og hindringer, knapper og fiender med skudd.
- Få kjennskap til Git (teste branches, commit, pull/push).

**Til neste møte:**
- Få til noe visuelt.
- Lese seg opp på Git.
- Presentere individuelt arbeid for gruppen.
- Fortsette med inndelte oppgaver.

---

## 2026-01-29 — Andre møte
**Til stede:** Petter, Brage, Oscar, Magnus (3/4)

**Fullført fra forrige møte:**
- Fått til noe visuelt ✓
- Lest opp på Git og branching ✓
- Presentert ✓

**Under møtet:**
- Endret kode-struktur.
- Konkludert med at alle commit-meldinger skal være engelsk.
- Laget UML-diagram.

**Til neste møte:**
- Fikse errors og røde streker i koden.

---

## 2026-02-05 — Tredje møte
**Til stede:** Petter, Brage, Oscar, Magnus (3/4)

**Fullført fra forrige møte:**
- Fungerende og interaktiv meny ✓

**Under møtet:**
- Fikset errors slik at programmet kjører.

**Til neste møte:**
- MVP — Minimal Viable Product:
  - Boks (Player) som kan beveges fra side til side, kan hoppe og treffer vegg.

---

## 2026-02-12 — Fjerde møte
**Til stede:** Petter, Brage, Oscar, Magnus (4/4)

**Fullført fra forrige møte:**
- Delvis fullført MVP — spiller (firkant) kan bevege seg frem og tilbake, men ikke hoppe.

**Under møtet:**
- Fikset issue board for å fordele oppgaver i Git.

**Til neste møte:**
- Legge til hopping i MVP og sjekke kollisjon mellom spiller og vegg.
- Lagre ulike sprites i en fil.
- Overføre repo til riktig prosjekt («Fireboys»).

**Etter møtet:**
- Lagt til hoppelogikk ✓
- Endret litt på menyen og lagt til mulighet for valg av level/brett ✓

**Fortsettelse videre:**
- Plukke oppgaver fra issue board og jobbe jevnlig.

---

## 2026-02-19 — Femte møte
**Til stede:** Petter, Brage, Oscar, Magnus (4/4)

**Fullført fra forrige møte:**
- Hoppelogikk og kollisjon mot vegger fungerer ✓
- Sprites lagret i én felles fil ✓

**Under møtet:**
- Diskuterte hvordan vi skulle representere Fireboy og Watergirl som separate karakterer.
- Startet arbeid med å laste inn karaktersprites fra spritesheet.
- Planlagt strukturen for elementbaserte farer (pools).

**Til neste møte:**
- Implementere vann- og ildpytter med elementtilstand.
- Få karakterene til å se ut som Fireboy og Watergirl.

---

## 2026-02-26 — Sjette møte
**Til stede:** Petter, Brage, Oscar (3/4)

**Fullført fra forrige møte:**
- Karaktersprites for Fireboy og Watergirl lastet inn ✓
- Grunnleggende pool-system på plass ✓

**Under møtet:**
- Implementerte gem-system med elementtilstand (røde gems til Fireboy, blå til Watergirl).
- Diskuterte og planla dør-mekanikk og vinnbetingelse.
- **Justering av roller:** vi gikk bort fra kode-spesifikke roller
  (Oscar–spillbrett, Petter–Git, Brage–meny, Magnus–spiller) — alle skal
  kunne plukke hvilken som helst issue og kode i hele kodebasen, slik at
  ingen blir flaskehalser. I stedet fikk hver person et ansvarsområde for
  *review* av merge requests innenfor sitt område: Petter (teamlead +
  GitLab), Magnus (referat/møte/rapport), Brage (test), Oscar
  (arkitektur + level-design). Begrunnelsen var at vi så behov for tydelig
  eierskap på testing, møteplanlegging og dokumentasjon.

**Til neste møte:**
- Fullføre gem-innsamling og poengsum.
- Legge til dør som åpnes når alle gems er samlet.

---

## 2026-03-05 — Andre møte med gruppeleder
**Til stede:** Petter, Brage, Oscar, Magnus (4/4)

**Notater fra møtet:**
- Bra fremdrift generelt, men bør jobbe mer strukturert med issues og branches.
- Mer fokus på testing — skrive tester underveis, ikke bare til slutt.
- Tenk på MVC-skillet: modellen skal ikke inneholde visningslogikk.

**Plan videre:**
- Rydde opp i kodestrukturen og sørge for tydeligere skille mellom lag.
- Begynne å skrive enhetstester for modellen.

---

## 2026-03-12 — Sjuende møte
**Til stede:** Petter, Brage, Oscar, Magnus (4/4)

**Fullført fra forrige møte:**
- Gem-innsamling med poengsum fungerer ✓
- Dør som åpnes ved fullføring av brett ✓

**Under møtet:**
- Lagt til støtte for flere nivåer via `levels.txt`.
- Startet arbeid med fiender.
- Diskuterte og begynte å skrive tester for modellen.

**Til neste møte:**
- Fullføre fiender med bevegelseslogikk.
- Legge til lyd/musikk.
- Skrive flere tester.

---

## 2026-04-02 — Åttende møte
**Til stede:** Petter, Brage, Oscar, Magnus (4/4)

**Fullført fra forrige møte:**
- Fiender med enkel patrol-bevegelse ✓
- Bakgrunnsmusikk og lydavstemming implementert ✓
- Tester for kjernefysikk skrevet ✓

**Under møtet:**
- Gikk gjennom kravlisten for siste innlevering.
- Markerte gjenstående krav med FSR (Final Sprint Requirements).
- Fordelte oppgaver for siste sprint.

**Til neste møte:**
- Ferdigstille alle FSR-krav.
- Skrive rapport og oppdatere dokumentasjon.

---

## 2026-04-13 — Tredje møte med gruppeleder
**Til stede:** Magnus, Petter, Brage (3/4)

**Notater fra møtet:**
- Ryddigere Git-meldinger og bedre bruk av issues og branches frem til siste innlevering.
- Tenke på rapportskriving.
- Huske å holde antall commits noenlunde likt mellom gruppemedlemmene.
- Den viktige delen av faget er ikke nødvendigvis kodingen, men å lære å jobbe i gruppe.

**Plan videre:**
- Jobbe mot siste innlevering og holde oversikt over hvilke krav som mangler og hva som er gjort. Markerer med **FSR (Final sprint requirements)** — krav hentet fra semesteroppgaven.
