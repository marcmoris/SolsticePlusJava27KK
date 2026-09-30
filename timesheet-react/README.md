# Solstice+ TimeSheet React (Feuilles de Temps)

Application web moderne, haute densité et modulaire en **React JS**, reproduisant fidèlement l'écran principal des feuilles de temps Solstice+ (`VTimeSheetMain`) et ses sous-composants (`VEmployeeListPanel`, `VTimeSheetHeader`, `VTimeSheetCredits`, `VTimeSheetDetail`).

---

## Fonctionnalités Principales

- **Barre d'outils globale (`HeaderToolbar`)** :
  - Sauvegarder (Ctrl+S) avec pastille dynamique de modifications non enregistrées.
  - Nouvelle feuille, Supprimer, Valider/Calculer.
  - Navigation d'employés : Premier, Précédent, Suivant, Dernier (ou flèches clavier $\uparrow$ / $\downarrow$).
  - Export direct vers Excel (`.xlsx`) via SheetJS avec toutes les formules et totaux.
  - Recherche multicritère modal (F3).
  - Indicateurs de clavardage (Chat) et de pièces jointes.
  - Bascule de thème sombre / clair.

- **Panneau des employés (`EmployeeList`)** :
  - Recherche instantanée filtrant par nom, prénom ou matricule.
  - Filtres radio : *Actifs*, *Inactifs*, *Tous*.
  - Filtre par statut de feuille (Approuvée, Calculée, Émise, En erreur, Initiale, etc.) et par type (Régulière, Ajustement, etc.).
  - Positionnement dynamique ("Employé X / Total").

- **En-tête de la feuille (`TimeSheetHeader`)** :
  - Carte d'identité employé (Code, Département, Titre d'emploi, Livret, Convention, Région, etc.).
  - Sélecteur de période de paie avec dates de début/fin et date de paiement.
  - Badges d'état du workflow de paie (Initiale, Calculée, Validée, Approuvée, Émise).
  - Boîte sommaire des gains : Salaire brut et Salaire net estimé.

- **Bandeau des banques de crédits (`CreditsBank`)** :
  - Cartes métriques pour chaque banque (Vacances / BV20B, Congés Fériés / BV21, Banque d'heures / BV10, Maladie / BV05).
  - Solde initial, variation de la période courante, nouveau solde projeté.

- **Grille de saisie matricielle (`TimeSheetGrid`)** :
  - Découpage par semaine (Semaine 1, Semaine 2, ou Toutes).
  - Saisie des heures par jour du Dimanche au Samedi avec dates calendaires réelles.
  - Sélection par ligne : Affectation/Poste, Rubrique/Gain, Horaire, Activité.
  - Calculs instantanés des totaux journaliers, totaux par ligne et grand total de la période.
  - Ajout, duplication et suppression de lignes.
  - Onglets secondaires : Primes (Gain 106), Historique des mouvements de crédits, Journal des alertes et anomalies.

---

## Démarrage rapide (Local)

Dans ce répertoire `timesheet-react` :

```bash
# 1. Installation des dépendances (déjà effectuée)
npm install

# 2. Démarrage du serveur de développement
npm run dev

# L'application sera accessible sur http://localhost:5173
```

---

## Guide de Transfert vers Votre Autre Projet

Pour intégrer ces sources dans votre autre projet web (Next.js, Vite, React Router, etc.) :

### 1. Dépendances requises dans votre projet cible
Installez les bibliothèques suivantes :
```bash
npm install lucide-react xlsx clsx
```

### 2. Fichiers à copier
Copiez les dossiers suivants depuis `timesheet-react/src/` vers votre projet :
- `src/components/` : Tous les composants UI (`HeaderToolbar/`, `EmployeeList/`, `TimeSheetHeader/`, `CreditsBank/`, `TimeSheetGrid/`, `SearchModal/`).
- `src/services/timesheetService.js` : Le service centralisé de gestion des feuilles.
- `src/data/mockData.js` : Données de test / fallback.
- `src/utils/` : Fonctions utilitaires de formatage et de manipulation de dates.
- `src/index.css` : Les variables de thème (palette CSS, styles des tableaux et badges).

### 3. Connexion à votre backend réel
Dans [src/services/timesheetService.js](file:///d:/SolsticeKK-main/timesheet-react/src/services/timesheetService.js), remplacez simplement les appels locaux par vos endpoints REST ou GraphQL :

```javascript
// Exemple de remplacement dans timesheetService.js :
export const timesheetService = {
  async getTimeSheet(employeeId, periodId) {
    const res = await fetch(`/api/timesheet?employeeId=${employeeId}&periodId=${periodId}`);
    return await res.json();
  },
  
  async saveTimeSheet(timesheet) {
    const res = await fetch('/api/timesheet', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(timesheet)
    });
    return await res.json();
  }
};
```
