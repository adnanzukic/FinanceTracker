# Life Management System - Java Edition

## Opis projekta
Life Management System je desktop aplikacija razvijena u Javi koja korisnicima omogućava praćenje ličnih finansija i svakodnevnih navika. Aplikacija koristi Java Swing za GUI i MongoDB kao bazu podataka.

## Tehnologije
- Java 21
- Java Swing (GUI)
- MongoDB (baza podataka)
- mongodb-driver-sync 4.11.0

## Postavljanje projekta (Setup)

### Potrebno instalirati:
1. JDK 21 - https://www.oracle.com/java/technologies/downloads/
2. IntelliJ IDEA Community Edition - https://www.jetbrains.com/idea/download/
3. MongoDB Community Server 7.0 - https://www.mongodb.com/try/download/community

### Pokretanje projekta:
1. Klonirajte repozitorij
2. Otvorite projekt u IntelliJ IDEA
3. Dodajte MongoDB biblioteku: File → Project Structure → Libraries → + → From Maven → org.mongodb:mongodb-driver-sync:4.11.0
4. Pokrenite MongoDB servis (Services → MongoDB → Start)
5. Pokrenite Main.java

## Struktura projekta
src/
└── financeapp/
├── Main.java                 - Pokretanje aplikacije
├── MongoDBConnection.java    - Konekcija na MongoDB
├── User.java                 - Model korisnika
├── UserManager.java          - Registracija i login
├── LoginForm.java            - Ekran za prijavu
├── MainMenu.java             - Glavni meni
├── Transaction.java          - Model transakcije
├── TransactionManager.java   - Upravljanje transakcijama
├── FinanceTrackerForm.java   - Finance Tracker GUI
├── SleepTracker.java         - Sleep Tracker modul
├── MoodTracker.java          - Mood Tracker modul
└── HabitTracker.java         - Habit Tracker modul


## Funkcije aplikacije

### Login / Registracija
- Registracija novog korisnika sa korisničkim imenom, lozinkom i odabirom teme
- Prijava postojećeg korisnika
- Podaci se čuvaju u MongoDB bazi

### Finance Tracker (Projekat 1)
- Dodavanje prihoda i rashoda
- Kategorije: Plata, Hrana, Racuni, Zabava, Prijevoz, Ostalo
- Ažuriranje postojeće transakcije
- Brisanje transakcije sa potvrdom
- Export podataka u TXT fajl
- Prikaz ukupnog prihoda, rashoda i salda

### Sleep Tracker
- Unos broja sati sna po datumu
- Brisanje unosa
- Statistika: prosječan broj sati sna

### Mood Tracker
- Praćenje raspoloženja (Odlično, Dobro, Neutralno, Loše, Užasno)
- Dodavanje bilješke uz raspoloženje
- Statistika: najčešće raspoloženje

### Habit Tracker
- Dodavanje navika po datumu
- Status navike: Završeno / Nije završeno
- Statistika: postotak uspješnosti navika

## Korištenje aplikacije

1. Pokrenite aplikaciju kroz Main.java
2. Registrujte se ili prijavite
3. Na Main Meniju odaberite željeni modul
4. Unesite podatke i kliknite Dodaj
5. Za brisanje odaberite red u tabeli i kliknite Obriši
6. U Finance Trackeru možete koristiti Export za izvoz podataka
