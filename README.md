# Centrifugeuse

### Demarrage
Ouvrez le patch principal: Centrifugeuse.maxpat

### Credit
cedric.camier@gmail.com

### Description
Patch de diffusion de sources virtuelles en mouvement circulaire pouvant atteindre de grandes vitesses. La spatialisation repose sur un algorithme maison, dérivé d'un algorithme d'Angle-based paning. Le traitement de la vitesse se faire dans le domaine audio, permettant d'atteindre des vitesses de rotation de l'ordre des fréquences audibles.

3  sources sont disponibles dans le patch, chaque source étant affectée à un mouvement circulaire d'un objet "TrAJECTOIRES CIRCULAIRES" qui peut être paramètré par une vitesse et un sens de rotation. La restitution se fait via l'objet MultchanOut qui inclut 12 haut-parleurs.
