
# Konsept
Spill inspirert av Fireboy and watergirl. Prøver å lage en egen liten vri

Ansvarsfordeling
    Magnus
        - Boy og girl klassen
        -
    Brage
        - Lage meny
        - Animasjoner
    Petter(Git ansvarlig)
        - Elementer
        - 
    Oscar
        - Brettlogikk
## UML diagram
```mermaid
classDiagram
    class IStaticEntity {
        <<interface>>
        +getPos()
        +setPos()
        +getWidth()
        +getHeight()
    }

    class IMovable {
        <<interface>>
        +getVelocityX()
        +setVelocityX()
        +getVelocityY()
        +setVelocityY()
        +getWeight()
    }

    class IPlayer {
        <<interface>>
        +getElementState()
    }

    class StaticEntity {
        <<abstract>>
        Position position
        double width
        double height
        +getPos()
        +setPos()
        +getWidth()
        +getHeight()
    }

    class Door {
        bool isOpen
        +isOpen()
        +setOpen()
    }

    class Lever {
        bool isActivated
        +isActivated()
        +setActivated()
    }

    class Pool {
        ElementState elementState
        +getElement()
        +setElement()
    }

    class Button {
        bool isPressed
        +isPressed()
        +setPressed()
    }

    IStaticEntity <|-- IMovable : Extends
    IMovable <|-- IPlayer : Extends
    IStaticEntity <|.. StaticEntity : Implements
    StaticEntity <|-- Door : Extends
    StaticEntity <|-- Lever : Extends
    StaticEntity <|-- Pool : Extends
    StaticEntity <|-- Button : Extends
```


Innlevering:
https://git.app.uib.no/inf112/25v/inf112-25v/-/wikis/prosjekt/innlevering



                


