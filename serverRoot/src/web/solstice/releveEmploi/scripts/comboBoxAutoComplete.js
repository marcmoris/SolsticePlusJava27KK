	// ----------------------------------------------------------------------
	//  Ce code sert à compléter automatiquement la saisie dans un combo box
	//   pour le mettre en fonction, faire un lien en haut de la page :
	//   <script type="text/javascript" src="scripts/comboBoxAutoComplete.js"></script>
	//   ajouter dans le controle de select :
	//  			<select onkeypress="autoSelect(this)" ></script>
	// ----------------------------------------------------------------------
	var textBuffer = '';
	var control;
	var actualTimer;
	void function autoSelect(controlToCheck)
	{	
		 clearTimeout(actualTimer);
		 //SI on était deja en train de faire la completion de ce controle
		 if (controlToCheck == control)
		 {
			//On incrémente le buffer
			textBuffer += String.fromCharCode(event.keyCode).toUpperCase();
			//On retrouve l'option correspondant à la string
			for (i = 0; i < controlToCheck.options.length; i++)
			{
				if (controlToCheck.options[i].innerText.toUpperCase().indexOf(textBuffer) == 0)
				{
					controlToCheck.options[i].selected = true;
					event.returnValue = false;
					return;
				}
			}
		 }
		 else
		 {
			textBuffer = String.fromCharCode(event.keyCode).toUpperCase();
			//On conserve le nouveau controle pour lequel on vérifie
			control = controlToCheck;
		 }
		 actualTimer = setTimeout(resetCompletion, 500);
	}
	
	//**Pour les comboBox triés contenant beaucoup de valeurs
	//   permet de raccourcir le délai de sélection
	void function autoSelectTrie(controlToCheck)
	{	
		 clearTimeout(actualTimer);
		 //Si on était deja en train de faire la completion de ce controle
		 if (controlToCheck == control)
		 {
			//On incrémente le buffer
			textBuffer += String.fromCharCode(event.keyCode).toUpperCase();
			//On retrouve l'option correspondant à la string
			for (i = control.selectedIndex; i < controlToCheck.options.length; i++)
			{
				var stringToCompare = controlToCheck.options[i].innerText.substring(0, textBuffer.length).toUpperCase();
				//Si on est rendu
				if (stringToCompare == textBuffer)
				{
					//On sélectionne
					controlToCheck.options[i].selected = true;
					event.returnValue = false;
					actualTimer = setTimeout(resetCompletion, 500);
					return;
				}
				//Si on a dépassé, on recommence au début
				else if (stringToCompare > textBuffer)
				{
					//On garde le dernier caractère tapé
					textBuffer = textBuffer.charAt(textBuffer.length - 1);
					actualTimer = setTimeout(resetCompletion, 500);
					return;
				}
			}
			//Si on n'a pas trouvé après la boucle, on garde le dernier caractère tapé
			actualTimer = setTimeout(resetCompletion, 500);
			textBuffer = textBuffer.charAt(textBuffer.length - 1);
			return;
		 }
		 //Sinon, on la commence
		 else
		 {
			textBuffer = String.fromCharCode(event.keyCode).toUpperCase();
			//On conserve le nouveau controle pour lequel on vérifie
			control = controlToCheck;
		 }
		 actualTimer = setTimeout(resetCompletion, 500);
	}
	
	void function resetCompletion()
	{
		textBuffer = '';
		control = null;
	}