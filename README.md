# 🗄️ MyGamingJournal - Backend

Backend del progetto **MyGamingJournal**, una web app che permette agli utenti di gestire la propria libreria videoludica, monitorare progressi, statistiche e trofei, e trovare altri giocatori in base a preferenze condivise.

🔗 Frontend repository: [mygamingjournal-frontend](https://github.com/DGSimone99/mygamingjournal-frontend)

---

## ⚙️ Tecnologie utilizzate

- Spring Boot
- Spring Security + JWT
- JPA (Hibernate)
- PostgreSQL
- Integrazione con [RAWG Video Games Database API](https://rawg.io/apidocs)

---

## 🧠 Funzionalità principali del backend

- Autenticazione e registrazione con JWT
- Gestione utenti e profili pubblici
- CRUD completo per giochi personali
- Sistema di voto, recensione, ore giocate e obiettivi
- Matchmaking in base a lingua e piattaforma
- Livellamento utente in base all’attività
- Integrazione con RAWG per dati e immagini gioco

---

- Integrazione con l’API RAWG per il recupero di informazioni videoludiche (titolo, descrizione, copertine, trofei, ecc.)
- Sistema di mapping interno che trasforma i dati esterni in oggetti persistenti del database locale così da garantire una navigazione rapida

Integrazione con [RAWG Video Games Database API](https://rawg.io/apidocs)
