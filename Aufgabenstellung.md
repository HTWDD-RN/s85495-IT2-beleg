# Aufgabenstellung RTSP-Streaming
Die Aufgaben beziehen sich auf den Beleg Videostreaming für das Modul Internettechnologien für zeitkritische Übertragungen.

## Lernaspekte des Belegs
* Funktion und Aufbau eines Echtzeit-Videostreaming-Systems
* Internetprotokolle und RFCs
* Verständnis und Anwendung des Streaming-Steuerungsprotokolls RTSP
* Verständnis und Anwendung des Echtzeit-Übertragungsprotokolls RTP
* Verständnis des Einsatzes weiterer Protokolle wie RTCP, SDP etc.
* Datenformate und Serialisierung
* Einfluss von Paketverlusten und -verzögerungen auf die Übertragungsqualität
* Statistische Analyse und Beschreibung von Netzwerkfehlern
* Kompensation von wechselnden Verzögerungen durch Jitterpuffer
* Kompensation von Paketverlusten durch Forward Error Correction (FEC)
* FEC-Verfahren und deren Implementierung
* Kompensation von Paketverlusten durch Fehlerverdeckung
* Komplexe Datenstrukturen und Algorithmen mit Java
* Plattformunabhängige Programmierung
* Testen und Dokumentieren
* Fehlersuche in Client-/Serveranwendungen

## Aufgaben
Im weiteren wird davon ausgegangen, dass die IDE Intellij verwendet wird.

### 0. Vorarbeiten
1. Clonen des Projekt aus dem Repository, Anleitung: [Git](git.md).
2. Konfiguration der Projektabhängigkeiten in der IDE:
  * Projekt-Structure -> Modules -> Reiter Dependencies
  * Add (+) -> 2 Library -> Maven
  * Markierung Downloads to lib
  * Abhängigkeiten:
    * Webcam: com.github.sarxos:webcam-capture:0.3.12
    * JUnit:  org.junit.jupiter:junit-jupiter:5.9.0
3. Sie erstellen die "leeren" Klassen `rtsp.Rtsp`, `rtp.RtpPacket`, `rtp.FecHandler` und `JpegDisplay` und leiten diese aus den abstrakten Klassen `rtsp.RtspDemo`, `rtp.RtpPacketDemo`, `FecHandlerDemo` und `JpegDisplayDemo` ab (Stichwort `extends`).  Das Projekt sollte danach kompilierbar und ausführbar sein.  
Unter einigen IDEs z.B. IntelliJ können Sie die Klassenrümpfe automatisch erstellen lassen mittels: Generate Contructors sowie implement Methods
4. Sie konfigurieren die Kommandozeilenparameter für Client und Server wie in der [Projektbeschreibung](Projektbeschreibung.md#2.-programmstart) beschrieben.
5. Sie erstellen in Ihrem Gitverzeichnis ein Unterverzeichnis mit dem Namen `videos` und legen in dieses das Beispielvideo `htw.mjpeg`, siehe Praktikumsdateien auf der HTW-IT2-Homepage.


### 1. RTSP-Protokoll: Client-Methoden
Die gesamte RTSP-Funktionalität für Client und Server befindet sich in der abstrakten Klasse `rtsp.RtspDemo` und der von Ihnen abzuleitenden Klasse `rtsp.Rtsp`.
Programmieren Sie die Klasse `rtsp.Rtsp` entsprechend der in der Projektbeschreibung und den Kommentaren im Quelltext der abstrakten Klasse `rtsp.RtspDemo` gegebenen Hinweisen.

1. Buttonhandler für alle Methoden ausprogrammieren, als Beispiel siehe den Handler für die setup-Methode und [Hinweise zu Zuständen](Projektbeschreibung.md#zustände-des-clients).
2. Ausprogrammierung der Methode `send_RTSP_request()`. Hier muss über den vorhandenen Stream `RTSPBufferedWriter` der komplette RTSP-Request für alle möglichen Methoden als String zusammengebaut und verschickt werden. Orientieren Sie sich an der beispielhaften [RTSP-Kommunikation](Projektbeschreibung.md#beispiel).
3. Nach Ihren Arbeiten können Sie die RTSP-Funktionalität testen indem Sie die Konsolenausgaben inspizieren.


### 2. SDP-Protokoll
Ergänzen Sie die Server-RTSP-Response der RTSP-Methode DESCRIBE in der Klasse `rtsp.Rtsp` anhand der Beispiele aus [RFC 2326](https://www.ietf.org/rfc/rfc2326.txt) und [RFC 2327](https://www.ietf.org/rfc/rfc2327.txt).
Überschreiben Sie dazu die bereits vorhandene Methode `getDescribe()` aus der Klasse `rtsp.RtspDemo` in der Klasse `rtsp.Rtsp`.
Diese Methode generiert die SDP-Antwort auf den Client-Request der DESCRIBE-Methode.
In der Konsole des Clients wird die implementierte SDP-Antwort angezeigt.

Es ist ausreichend, sich bei der DESCRIBE-Methode auf das Beispielvideo zu beziehen und die Antwort auf dem Server statisch zu hinterlegen. 
Ausgewertet werden die u.a. die Parameter `framerate` und `range`.
Auch wenn momentan nur ein MJPEG-Stream (PT-26) nutzbar ist, sollte das SDP außerdem einen H.264-Stream und einen G.711-Audio-Stream anbieten, um die Möglichkeiten von SDP zu demonstrieren. Der Identifierer trackID=0 soll dabei für den MJPEG-Stream stehen. Nach Implementierung der FEC-Methoden sollten diese FEC-Informationen noch zum SDP hinzugefügt werden, um einem beliebigen standardkonformen Client zu ermöglich, FEC zu nutzen.

### 3. RTP-Protokoll
Programmieren Sie die Methode setRtpHeader() der Klasse `rtp.RtpPacket` entsprechend der Projektbeschreibung und den Kommentaren im Quelltext der abstrakten Klasse gegebenen Hinweisen.
Nach dem Setzen des korrekten RTP-Headers sollte das Demovideo abspielbar sein. Im Fehlerfall kann es hilfreich sein, mittels Wireshark den Inhalt der übertragenen RTP-Pakete zu inspizieren. Eventuell ist auch der zur Verfügung gestellte Paketmitschnitt hilfreich.


### 4. Auswertung der Fehlerstatistiken ohne Fehlerkorrektur
Sie können an der GUI des Servers eine Paketfehlerwahrscheinlichkeit einstellen und damit Netzwerkfehler simulieren. Probieren Sie verschiedene Einstellungen aus und betrachten Sie das Ergebnis in der Videoanzeige. 
Dokumentieren Sie, ab welcher Paketfehlerwahrscheinlichkeit die Videoanzeige spürbar beeinträchtigt wird.

Das RTP-Streaming ist so konfiguriert, dass ein JPEG-Bild in mehrere RTPs verpackt wird. 
Berechnen die Wahrscheinlichkeit für den Verlust eines Bildes in Abhängigkeit von der Kanalverlustrate, wenn pro Bild 1, 2, 5, 10 oder 20 RTPs versendet werden.
Von einem Bildverlust ist dabei auszugehen, wenn mindestens ein RTP der RTPs für ein Bild fehlt.
Nutzen Sie zur grafischen Darstellung das Programm Gnuplot. Eine Demodatei befindet sich im Projektverzeichnis `statistics`. 

Überprüfen und dokumentieren Sie das theoretische Ergebnis anhand einer Simulation für die Kanal-Paketverlustwahrscheinlichkeiten von 2% und 10% und begründen Sie eventuelle Abweichungen.

### 5. Implementierung des FEC-Schutzes
Implementieren Sie einen FEC-Schutz gemäß [RFC 5109](https://www.ietf.org/rfc/rfc5109.txt).
Der Server mit FEC-Schutz ist kompatibel zu Clients ohne FEC-Verfahren, da die FEC-Pakete den Payloadtype 127 im RTP-Header verwenden. Clients ohne FEC verwerfen diese RTP-Pakete.

Um nicht die komplette FEC-Funktionalität selbst entwickeln zu müssen, werden Ihnen zwei Klassen bereit gestellt:
1. [FecPacket](src/rtp.FECpacket.java): dies ist eine aus RtpPacket abgeleitete Klasse mit der erweiterten Funktionalität für das Handling von FEC-Paketen (vollständig implementiert)
2. [FecHandler](src/rtp.Fechandler.java): diese Klasse ist zuständig für die server- und clientseitige FEC-Bearbeitung unter Nutzung von FecPacket (teilweise implementiert)
   * Server: Kombination mehrerer Medienpakete zu einem FEC-Paket
   * Client: Jitterpuffer für empfangene Medien- und FEC-Pakete, Bereitstellung des aktuellen Bildinhaltes in Form einer Liste von RTP-Paketen mit gleichem TimeStamp.

Die eigentliche Fehlerkorrektur im FecHandler ist noch zu implementieren. Dazu ist die vorhandene Architektur zu analysieren und die abstrakten Methoden sind auszuprogrammieren.
Eine Übersicht über die relevanten Datenstrukturen und ein Beispiel ist hier zu finden [FEC-Diagramme](https://www2.htw-dresden.de/~jvogt/it2/fec-diagramme.html)

#### Architektur der RTP-Paketverarbeitung
##### Server
* der Server steuert die Verarbeitung im vorhandenen Timer-Handler
* Nutzdaten erstellen und speichern: `RtpHandler.jpegToRtpPacket()`
* Nutzdaten senden
* Prüfung auf Erreichen der FEC-Gruppengröße: `RtpHandler.isFecPacketAvailable()`
* nach Ablauf des Gruppenzählers berechnetes FEC-Paket entgegennehmen und senden: `RtpHandler.createFecPacket()`
* Kanalemulator jeweils für Medien- und FEC-Pakete aufrufen: `sendPacketWithError()`

##### Client
* Der Client nutzt einen Thread für den Empfang der RTP-Pakete und einen Timer für das periodische Anzeigen der Bilder un der GUI.
* Pakete empfangen per Receiver-Thread: `RtpHandler.Receiver`
* Pakete im Jitterpuffer speichern: `RtpHandler.processRtpPacket()`
* Statistiken aktualisieren
* zur richtigen Zeit (Timeraufruf) das nächste Bild anzeigen: `RtpHandler.nextPlaybackImage()`
    * Timer läuft standardmäßig mit 25Hz oder Abspielgeschwindigkeit des Videos, wenn diese per SDP übermittelt
* Verzögerung des Starts des Abspielens (ca. 10 Bilder), um den Jitterpuffer zu füllen

##### RtpHandler
* Server
    * Registrierung eines RTP-Paketes im FecHandler bei dessen Erstellung
    * Abruf fertiggestellter FEC-Pakete über den FeCHandler
* Client
    * Speicherung ankommender RTP-Pakete getrennt nach PayloadType (JPEG-Pakete im RtpHandler, FEC-Pakete im FecHandler)
    * Bei Anfrage des nächsten Bildes Generierung einer Liste aller RTP-Pakete für dieses Bild (gleicher Timestamp)
    * Überprüfung der Korrektur für fehlende RTP-Pakete per `FecHandler.checkCorrection()` und ggf. Korrektur über `FecHandler.correctRTP()`

##### FecHandler
* Generierung einer Liste aller betroffenen RTP-Pakete für jedes FEC-Paket
* Speicherung der Sequenznummer des FEC-Packets und der Liste aller betroffenen RTP-Pakete für jedes RTP-Paket in zwei HashMaps (fecNr, fecList)
* periodisches Löschen alter nicht mehr benötigter Einträge im Jitterpuffer

##### FecPacket
* Ableitung aus vorhandenem RtpPacket
* Sender: Konstruktor zur Generierung eines FEC-Objektes aus Media-RTPs
* Empfänger: Konstruktur zur Generierung eines FEC-Objektes aus einem empfangenen FEC-RTP
* getRtpList: Ermittlung aller in einem FEC involvierten Media-RTPs
* getPacket: Holt komplettes FEC-Paket als Bytearray
* addRtp: fügt ein Media-RTP zum FEC-Objekt hinzu, inklusive aller notwendigen Berechnungen
* getLostRtp: generiert das verlorene Media-RTP aus den vorhandenen mittels addRtp hinzugefügten RTPs


### 6. Analyse der Leistungsfähigkeit des implementierten FEC-Verfahrens
#### 6.1. Parameterwahl
Untersuchen Sie, welche Verlustrate der RTP-Pakete nach der FEC-Decodierung (k=2) bei einer Kanalverlustrate von 10% entsteht.

#### 6.2. Bestimmung der Verlustraten mittels Simulation
Tragen Sie die mittels Messung (Simulation) zu gewinnenden Paketverlustwahrscheinlichkeiten nach FEC (Restfehler) für verschiedene Kanalfehlerraten (Pe = 0...1) und verschiedene Gruppengrößen (k=2, 6, 12, 48) in dem bereits vorhandenen Gnuplot-Diagramm auf. 
Besonders interessant ist der Bereich mit geringen Fehleraten (0 -- 0,2). Die Restfehlerwahrscheinlichkeit können Sie direkt in den Statistikangaben ablesen (Ratio nach FEC). 
Sie müssen die Simulation nicht immer bis zum Ende ablaufen lassen, der Ergebniswert sollte allerdings stabil sein und das Ratio vor FEC der gewüschten Kanalfehlerrate entsprechen.

#### 6.3. Abschätzung der zu erwartenden Verlustraten mittels theoretischer Betrachtung
Versuchen Sie, mathematisch die Paketverlustwahrscheinlichkeit für die obigen Gruppengrößen zu bestimmen und ebenfalls grafisch darzustellen. 
Sie können von dem Zusammenhang zwischen Guppenfehler und Kanalfehler ausgehen (Folie FEC-Einführung Seite 11). Zu beachten ist allerdings, dass wir hier die Paketverlustwahrscheinlichkeit und nicht die Gruppenverlustwahrscheinlichkeit benötigen. 
Eine Näherungsformel für kleine Fehlerraten ist hier ausreichend. Ein einfacher Zusammenhang ergibt sich durch die Multiplikation der Gruppenfehlerwahrscheinlichkeit mit dem Term: dmin / k, siehe dazu auch [Formel 3.7.3](https://www.berndfriedrichs.de/downloads_kc/bconeu_gekuerzt_auf_Vorlesung.pdf)


#### 6.4 Abschätzung der Bilddefektwahrscheinlichkeit
Stellen Sie weiterhin die Wahrscheinlichkeit für einen Bilddefekt dar, wenn von folgenden hypothetischen Übertragungsmodies ausgegangen wird: 1 RTP/Bild, 5 RTPs/Bild und 20 RTPs/Bild.

Für die eigentlichen Berechnungen können Sie statt Gnuplot auch R oder ein anderes Tool nutzen.
Diskutieren Sie eventuelle Unterschiede der praktisch und theoretisch ermittelten Ergebnisse.

Für diese Aufgabe unterstützt Sie die Statistik am [Empfänger](Projektbeschreibung.md#client)


### 7. Generierung von Restart-Markern
Nutzen Sie das bereitgestellte Jpeg-Bild und erzeugen Sie Restart-Marker für jede MCU-Zeile in diesem Bild.
Nutzen Sie hierfür das auf den Praktikumsrechnern vorhandene Tool `jpegtran`.
Manipulieren Sie mit einem Hexeditor (z.B. bless oder ghex) eine Zeile im Bild und vergleichen Sie sich das Ergebnis mit dem Originalbild ohne Restart-Marker, bei welchen Sie ebenfalls Manipulationen vorgenommen haben.
Welche Erkenntnis können Sie aus dem Vergleich gewinnen?
Betrachten Sie das Demovideo bei 10% Paketverlustrate einmal mit und einmal ohne Restart-Marker. Dokumentieren Sie den visuellen Unterschied der jeweiligen Bildstörungen.

### 8. Fehlerkaschierung
Damit trotz Fehlerkorrektur fehlende Pakete nicht zu einem störenden Bild führen, ist eine Fehlerkaschierung zu implementieren.
Dazu dient die Methode setTransparency der Klasse `JpegDisplay` (abgeleitet von `JpegDisplayDemo`). Dieser wird das aktuelle Bild und das Vorgängerbild übergeben, sowie eine Liste an fehlenden Bildteilen. Dies funktioniert nur mit MJPEG-Videos, welche Restart-Marker beinhalten.
Versuchen Sie anhand dieser Informationen das aktuelle Bild so zu modifizieren, dass Fehler möglichst wenig stören.
Bei welcher Paketfehlerwahrscheinlichkeit ist das Video mit Fehlerkaschierung und FEC (k=2) noch in guter Qualität darstellbar?

Das Demovideo hat eine Auflösung von 640 x 360 Pixeln und basiert auf MCUs von 16x16 Pixeln. Die Restartmarker wurden für jede MCU-Zeile codiert. Dies ergibt 23 unabhängige MCU-Zeilen pro Bild, welche jeweils per separatem RTP verschickt werden. Ein RTP-Paketverlust bedeutet somit in dieser Konstellation das Fehlen von genau einer MCU-Zeile.

### 9. RS-Coding
Im Beleg ist nur eine einfache Ausfallstellenkorrektur basierend auf Parity-Check-Codes implementiert.
In dieser Aufgabe sollen Sie eine komplexere Ausfallstellenkorrektur betrachtet und genutzt werden.
Dazu wird zunächst ein RS(7,5,3)_8-Code mit dem Polynom x³+x+1 genutzt. 

#### Berechnung
Nutzen Sie als Infowort die 5 Ziffern Ihrer S-Nummer, wobei Sie die Ziffern jeweils modulo 8 behandeln.
Codieren Sie dieses Infowort.
Testen Sie die Ausfallstellencodeirung indem Sie zwei Symbole aus dem generierten Code lsöchen. 
Die Position der zwei zu löschenden Symbole ergibt sich aus den beiden letzten Ziffern Ihrer S-Nummer modulo 7 (Position 0-6). 
Sind die beiden Werte identisch, nehmen Sie zusätzlich den nachfolgenden Wert. 
Führen Sie mit dem so behandelten Codewort eine RS-Ausfallstellenkorrektur durch.
Stellen Sie den gesamten Rechenweg nachvollziehbar dar.

#### Programmierung
Nutzen Sie die [Java-Bibliothek](https://github.com/zxing/zxing/tree/master/core/src/main/java/com/google/zxing/common/reedsolomon) um die vorherige Berechnung zu programmieren.
Den in der obigen Rechnung verwendeten Code müssen Sie der Klasse GenericGF noch manuell hinzufügen.
Das Ergebnis dieser Teilaufgabe ist eine Klasse, welche die Encoder- und Decoderfunktionen des Frameworks nutzt und die geforderte Berechnung durchführt. Wenn alles passt, sollte sich ein identisches Ergebnis zur vorherigen manuellen Lösung ergeben. (manchmal ist die Symbolreihenfolge andersherum definiert)
Dokumentieren Sie die Berechnungsschritte des erstellen Programms.

### 10. Dokumentation
Fügen Sie dem Projekt eine Dokumentationsdatei mit den Lösungen und den Erkenntnissen aus der Belegbearbeitung hinzu. Diese Dokumentation kann als Markdown-Datei angelegt werden, wobei sie mit Github anzeigbar sein muss. Alternativ legen Sie die Dokumentation als PDF bei.
Manchen Sie konkrete Vorschläge um den Beleg in Zukunft interessanter zu machen.


### Hinweise
#### Debugging
Es ist relativ unwahrscheinlich, dass das Programm auf Anhieb fehlerfrei funktioniert. Deshalb ist es wichtig, ein Konzept für die Fehlersuche zu entwickeln.
Hier einige Tipps für die Fehlersuche:
* Nutzen Sie die Konsolenausgaben für den Server und Client
* Setzen des passenden Logging-Levels (z.B. FINEST für alle Ausgaben) in Client und Server
  * SEVERE: kritische Fehler
  * WARNING: Fehlermeldungen
  * INFO:   RTSP-Ausgaben
  * CONFIG:
  * FINE:   RTP-Ausgaben pro Paket
  * FINER:  FEC-Ausgaben
  * FINEST: RTP-Header 
* Nutzung eigener Logging-Ausgaben
* prüfen des Senders auf korrekte Pakete
* Einstellung eines festen Seeds des Kanalsimulators für wiederholbare Versuche
* Test ohne bzw. mit Fehlerkorrektur
* Test der Anzahl verlorener / wiederhergestellter Pakete auf Plausibilität
* Nutzung von Wireshark zum Analysieren der Pakete

#### Videocodierung
Das Demovideo ist in verschiedenen Qualitätsstufen von der Homepage abrufbar.
Falls Sie ein anderes Video nutzen wollen, ist dieses in das MJPEG-Format zu konvertieren.
Eine Umcodierung zu MJPEG kann zum Beispiel mittels FFMPEG oder VLC-Player erfolgen. 
Eventuell müssen Sie die Auflösung des Videos verringern, damit die Bilder jeweils in ein UDP-Paket passen.

`ffmpeg -i test.mp4 -vcodec mjpeg -q:v 10 -huffman 0 -r 10 -vf scale=720:-1 -an test.mjpeg`

#### KI-Nutzung
Sie können gern eine KI zur Unterstützung verwenden. Dann gelten folgende Bedingungen:
1. Dokumentation der Nutzung der KI
2. Sie sind für den erstellten Code verantwortlich und müssen diesen auch verstehen.


#### Notenorientierung
Bei Erfüllung der folgenden Aufgabenpunkte können Sie maximal mit der angegebenen Note rechen:
* Note 4: RTP/RTSP/Fehlerstatistik (Beleg funktioniert korrekt)
* Note 3: zzgl. FEC + Statistik
* Note 2: zzgl. Fehlerkaschierung
* Note 1: zzgl. Theorieteil

## Literatur
* Real Time Streaming Protocol (RTSP)                   [RFC 2326](http://www.ietf.org/rfc/rfc2326.txt)
* SDP: Session Description Protocol                     [RFC2327](http://www.ietf.org/rfc/rfc2327.txt)
* RTP: A Transport Protocol for Real-Time Applications  [RFC 3550](http://www.ietf.org/rfc/rfc3550.txt)
* RTP Payload Format for JPEG-compressed Video          [RFC 2435](http://www.ietf.org/rfc/rfc2435.txt)
* RTP Profile for Audio and Video Conferences with Minimal Control  [RFC 3551](http://www.ietf.org/rfc/rfc3551.txt)
* RTP Payload Format for Generic Forward Error Correction  [RFC 5109](http://www.ietf.org/rfc/rfc5109.txt)
* Reed-Solomon Forward Error Correction (FEC) Schemes   [RFC 5510](http://www.ietf.org/rfc/rfc5510.txt)
* JPEG-Format [Link](https://de.wikipedia.org/wiki/JPEG_File_Interchange_Format)
* Diplomarbeit Karsten Pohl "Demonstration des RTSP-Videostreamings mittels VLC-Player und einer eigenen Implementierung"  [pdf](https://www2.htw-dresden.de/~jvogt/abschlussarbeiten/Pohl-Diplomarbeit.pdf)
* Diplomarbeit Elisa Zschorlich "Vergleich von Video-Streaming-Verfahren unter besonderer Berücksichtigung des Fehlerschutzes und Implementierung eines ausgewählten Verfahrens" [pdf](https://www2.htw-dresden.de/~jvogt/abschlussarbeiten/zschorlich-diplomarbeit.pdf)