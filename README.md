# DunkTheBaballe

# Conventions de nommage des commit

***Commit souvent, régulièrement, fréquement***

**Titre du commit** :
- Si c'est un ajout écrire ***Ajout*** puis ce qui a été ajouté
- Si c'est une modification simplement écrire ***Modif*** puis ce qui a été modifié
- Ne PAS mettre de point "**.**" à la fin
- Être succin, pour les détails il y a la description
- Commencer par une majuscule

Après je dois vous avouer que c'est pas si facil à respecter, mais bon c'est un peu l'objectif à atteindre

**Description du commit** :
- Si détails nécessaires, explications
- Faire référence à l'***Issue*** concernée par le commit si il y en a une. Par exemple : Issue #3 [ajouter un bouton "recommencer"]
dans la description du commit il faut alors penser à ajouter : `résout #3`

# Cheatsheet utilisation de github avec IntelliJ
- Au début d'une session de codage, pull du remote (récupérer les modifications des autres, update project) : Crtl+T
  **!!!** Lorsque vous le faites, utilisez "merge incoming changes" et PAS "Rebase the current…"
- Actualiser son projet avant de push sur le remote
- Commit souvent, un adage dirait : *si tu as besoin du mot **"et"** dans tes commit, c'est que tu ne commit pas assez souvent"*

## Pour implémenter une nouvelle fonctionnalité créez une nouvelle branche :
- Tout en haut (File, Edit, View etc…) Choisir le menu `git -> branches -> new branch`
- ou Tout en bas (Git, TODO, Terminal), choisir le panel **git**, puis `clic droit` sur le master et sélectionner `new branch from selected `

Faire attention à ce que la branche parte du master, et pas d'une autre branche (à moins que ça ne soit vraiment ce que vous vouliez faire). <br />
