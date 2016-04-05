https://prezi.com/9topfiv9rn98/ciws/

#C.I.W.S Beágyazott alapokon
## Mi az a C.I.W.S
Close In Weapon System: Önálló autonóm védelmi fegyver.
Emberi beavatkozás nélkül képes a fenyegetések felismerésére, bemérésére és megsemmisítésére.
## Katonai felhasználás (szenzorok)
### Search radar (target acquisition radar)
A célpontok keresésére és kiválasztására használt radar. Nagy területet fed le, de nem elég pontos a célpontok lelövéséhez
### Tracking radar
Nagyon kis látószögű, de nagyon pontos radar. A kereső radar által megjelölt célpont pontos bemérésére, valamint a kilőtt lövedékek röppályályának elemzésére használják.
A lövedékek követésére a célzás pontosítása miatt van szükség.
### Infravörös kamera
Lassab célpontok (motorcsónak, drónok) bemérésére használt szenzor.
### Commercial felhasználás (paintball sentry)
A paintball sentry boltban is megvásárolható autonóm fegyver. Ebben a formályában ugyan nem CIWS, de érdemes megemlíteni.





# 1. slide
Projekt céljának összefoglalása 1 mondatban (CIWS rendszer építése, arduino + pi)

# 2. slide
** Két része van. Az autonóm szót külön kiemeltem, az autonóm rész magyarázásához**

A CIWS elmagyarázása az autonóm fegyverekből kiindulva:
- Mit nevezünk autonóm fegyvernek
- Mit csinál a CIWS.
Az olyan pont védelmi fegyver rendszereket, melyeket elsősorban csak repülő célpontok ellen használnak, close in weapon system-nek nevezik. Ilyen célpontok lehetnek például: 
- drónok 
- fix- és forgószárnyas repülők
- rakéták 
- tüzérségi gránátok.

(Itt esetleg meg lehet említeni, hogy milyen nehéz feladatról is van szó: Egy apró nagyon gyors tárgy eltalálása még apróbb tárgyakkal egy mozgó járműről úgy, hogy az apró tárgy amit el akarok találni el akarja kerülni azt, hogy eltalálják) vagy valami ilyesmi. 

# 3. slide
A Phalanx bemutatása. 2 különböző radar:
1. Kereső radar (TAR) Target Acquisition Radar
2. Követő radar (TTR, vagy AAA) Target Tracking Radar [Monopulzusos radar]
Nem hiszem, hogy nagyon kéne magyarázni a radarokat. Molnár András amúgy is mesélni akar majd róla. Vagy már meg is tette az előadás előtt.

# 4. slide
Egy kép a Goalkeeperről. Ha nem egyértelmű a nyilakból, akkor érdemes lenne mondani, hogy a baloldali a keresőradar a másik pedig a követő.

# 5. slide
Paintball sentry bemutatása

Ez a cucc már csak egy kamerát használ a célpont bemérésére. Követni tudja a mozgást a képen és ha megfelelően van kalibrálva, szépen le is tudja lőni
Van olyan implementációja, ami csak egy bizonyos színű tárgyat tud követni.

# 6. slide
Ciwsduino + logo. A projekt céljának bemutatása néhány szóban. 

# 7. slide
A projekt fő részeinek rövid bemutatása. A 3 fő rész egymás után jelenik meg. (Arduino, Raspberry, Android)
Tényleg csak néhány szóban kéne elmondani, hogy mi a feladatuk

**Arduino: ** Szenzor adatok gyűjtése, terület megfigyelése és a célpont megsemmisítése.

**Raspberry: ** Kamerakép rögzítése és továbbítása androidra

**Android: ** Kommunikáció a két másik rendszerrel; Kézi vezérlés; Beállítások

# 8. slide (Az arduino-s rész)
Csak annyit kéne mondani, hogy most ezt a részt fogod bővebben bemutatni és már menni is a következőre

# 9. slide 
Az egész egy kép a mikrokontrollerről. Fontosabb paraméterek bemutatása

# 10. slide (szenzorok)
## hc-sr04
Ultrahangos távolságmérő. Hogyan használjuk radarnak.
Megemlíthető az implementáció (milyen algoritmusok)
## hc-sr501
Mozgásérzékelő. Mozgás esetén aktiválja a radart.
## mpu-6050
Gyro és gyorsulásmérő. Sérülések detektálására.

## 11. slide (3d nyomtatott alkatrészek)
3D nyomtatott alkatrészek. Miért nyomtattunk, miket nyomtattunk és miket nem nyomtattunk :) 

# 12. slide (A Raspberry-s rész)
Szintén csak anniyt kéne mondani, hogy a raspberry-s rész bemutatása. 

# 13. slide
Kép a Raspberry-ről. Raspberry Pi 2 model b rövid ismertető.
1gb ram, 800 Mhz Quad core, SD kártya. Raspbian (Debian Jessie)

# 14. slide
Kameramodul a pi-hez. 
5mp 2592x1944; OmniVision 5647

# 15. slide (Az androidos rész)
Az androidos rész bemutatása. 
Interfészek: Bluetooth és WiFi

Az alkalmazás célja:
1. A két másik modulból érkező információk megjelenítése
2. Manuális vezérlés
3. Beállítások

# 16. slide (Bluetooth)
 


# vége: ha van idő egy király CIWS videóra, akkor még az is mehet
