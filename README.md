# IntegrationIotApplication

Spring Boot Backend zur Integration von RFID-Scans in den JKU Möbelprozess via Camunda 8.

**Ablauf:** RFID-Scan → Backend → Camunda Prozess starten + in DB speichern → Prozess sichtbar in Operate

---

## Voraussetzungen

- Java 17+
- Maven
- Docker & Docker Compose (20.10.16+ / 1.27.0+)
- IntelliJ IDEA (oder anderer Editor)

---

## Modus A — Docker (Final für später)

> Alles läuft in Docker. IntelliJ nur zum Code lesen oder DB ansehen.

### 1. Camunda in Docker starten

Camunda 8 Docker Compose herunterladen: https://docs.camunda.io/docs/self-managed/quickstart/developer-quickstart/docker-compose/

ZIP entpacken, dann im entpackten Ordner:

```bash
docker compose up -d
```

Status prüfen:
```bash
docker compose ps
docker compose logs -f
```

Camunda läuft auf:

| URL                              | Was                      |
|----------------------------------|--------------------------|
| http://localhost:8080/operate    | Prozessinstanzen ansehen |
| http://localhost:8080/tasklist   | User Tasks               |
| http://localhost:8080/admin      | Admin                    |

Login: **demo / demo**

> Die BPMN muss **nicht** manuell über den Camunda Modeler deployed werden — das macht die App beim Start automatisch.

für manuelles testen kann das gemacht werden im Camunda Modeler:
- moebelprozess.bpmn öffnen
- Raketen-Symbol → Deploy
- Einstellungen: Camunda 8 Self-Managed, Authentication: None, Endpoint: http://localhost:26500 
- Unter http://localhost:8080/operate → Processes sollte der Prozess nun erscheinen.

### 2. Backend in Docker starten

im Ordner IntegrationIotApplication
```bash
docker compose up -d
```

> Die App startet auf **Port 8081**, die DB (PostgreSQL) auf **Port 5433**.

> Die BPMN (`Moebelprozess.bpmn`) wird beim App-Start **automatisch** in Camunda deployed — kein manuelles Deployen nötig. Unter http://localhost:8080/operate → Processes sollte sie danach erscheinen.

---

## Modus B — IntelliJ (Entwickeln / Debuggen)

> Camunda läuft in Docker, Backend läuft direkt in IntelliJ. 

### 1. Camunda in Docker starten

Gleich wie Modus A, Schritt 1 — Camunda Docker Compose starten.

### 2. Nur DB-Container starten (App NICHT in Docker!)

```bash
# Nur den DB-Container starten, App-Container weglassen:
docker compose up -d db
```

> Falls die App vorher in Docker lief, erst stoppen:
> ```bash
> docker compose down
> docker compose up -d db
> ```

### 3. App in IntelliJ starten

`IntegrationIotApplication.java` → Run

Die App verbindet sich automatisch mit:
- PostgreSQL auf `localhost:5433`
- Camunda Zeebe auf `localhost:26500`

>  Die BPMN wird beim Start automatisch deployed — kein Camunda Modeler nötig.


## RFID-Testprozess ausführen

Aktuell kann der Prozess u **`RfidTestProzess.bpmn`** erfolgreich getestet werden.

### Was funktioniert bereits?
Der folgende Ablauf funktioniert bereits end-to-end:

**RFID-Scan → Backend → Camunda-Prozess starten → in DB speichern → in Operate sichtbar und gestartet**

### Voraussetzungen
- Camunda läuft über Docker
- PostgreSQL läuft
- das Spring-Boot-Backend läuft lokal auf Port `8081`

### Starten
1. Camunda-Docker-Setup starten
2. Datenbank starten
3. Spring-Boot-Anwendung starten

### Test über Swagger
Swagger im Browser öffnen:

- `http://localhost:8081/swagger-ui/index.html`

Dann den Endpoint **`POST /api/scans`** verwenden und z. B. folgenden Request senden:

```json
{
  "rfidId": "RFID-123",
  "location": "Lager A",
  "condition": "GOOD",
  "timestamp": "2026-04-23T17:20:00+02:00"
}
```
Damit der gestartete Prozess in Camunda sichtbar ist, muss nach dem Testrequest **Operate** geöffnet werden:

- `http://localhost:8080/operate`

Dort kann nach dem Prozess **`RfidTestProzess`** gesucht werden.  
Falls keine Instanz direkt angezeigt wird, muss im Bereich der Instanzzustände **`Active`** ausgewählt werden.  
Dann ist die gestartete Prozessinstanz sichtbar.

Optional kann zusätzlich die **Tasklist** geöffnet werden:

- `http://localhost:8080/tasklist`
## TODO`moebelprozess.bpmn`

Der Möbelprozess ist aktuell noch **nicht lauffähig in Camunda 8**.  
Damit er gestartet und in Operate sichtbar werden kann, sind noch folgende Punkte offen:

- die im Prozess verwendeten **Message-Elemente** müssen vollständig für Camunda 8 / Zeebe konfiguriert werden
- für die betroffenen Nachrichten fehlt derzeit die erforderliche **`zeebe:subscription`**
- der Prozess muss danach **fehlerfrei deploybar** sein
- anschließend muss geprüft werden, ob alle verwendeten Tasks und Events von Camunda auch wirklich unterstützt und korrekt modelliert sind
- danach kann ein passender Backend-Endpoint ergänzt werden, um den Möbelprozess wie den RFID-Testprozess zu starten

### Aktueller Stand
Bereits funktionsfähig ist der technische Ablauf mit **`RfidTestProzess.bpmn`**:

---


