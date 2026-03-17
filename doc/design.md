# Design

## Arkitektur
Prosjektet er strukturert rundt en MVC-inspirert arkitektur:
- `model`: spilltilstand, regler, kollisjon og entiteter.
- `view`: rendering/animasjon av spill og menyer.
- `controller`: inputhåndtering og styring av game loop.

## Viktige komponenter
- `GameModel`: sentral spilltilstand og oppdateringslogikk.
- `GameView`: presentasjon av spillverden og menyer.
- `GameController`: tastaturinput og overgang mellom game states.

## Tilstander
- `MAIN_MENU`, `LEVEL_SELECT`, `SETTINGS`, `PLAYING`, `PAUSED`, `GAME_OVER`.

## Designprinsipper
- Hold modell-logikk uavhengig av visning.
- Bruk tydelige interfaces for entiteter (`IStaticEntity`, `IMovable`, `IPlayer`).
- Behold enkel, lesbar struktur i små klasser med klart ansvar.