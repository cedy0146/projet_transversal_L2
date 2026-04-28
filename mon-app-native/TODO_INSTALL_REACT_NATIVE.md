# TODO - Installation React Native

## Étapes
- [x] Étape 1 : Lancer `npm install` pour installer toutes les dépendances et générer `package-lock.json`
- [x] Étape 2 : Installer React Native CLI dans le projet (`@react-native-community/cli`)
- [x] Étape 3 : Vérifier que Expo CLI fonctionne (`npx expo --version`)
- [x] Étape 4 : Vérifier la présence de React Native dans le projet
- [x] Étape 5 : Vérification complète et validation

## Résultat de l'installation

| Composant | Version | Statut |
|-----------|---------|--------|
| React Native | 0.81.5 | ✅ |
| Expo CLI | 54.0.24 | ✅ |
| React Native CLI | 20.1.3 | ✅ |
| Package-lock.json | 412 Ko | ✅ |
| Type de projet | Expo Managed | ✅ |

## Commandes disponibles

Depuis le dossier `mon-app-native` :

```bash
# Démarrer le serveur de développement
npx expo start

# Lancer sur Android
npm run android

# Lancer sur iOS (macOS uniquement)
npm run ios

# Lancer sur le web
npm run web
