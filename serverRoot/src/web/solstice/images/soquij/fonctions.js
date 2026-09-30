function bloc_deroulant(objet) {
	objet.next().slideToggle("normal");
	objet.toggleClass("selected");
	objet.parent().toggleClass("bloc_deroulant_selected");

	return false;
}

function document_ready() {

	// Entête - fêtes 2012
	/*$("#entete").append("<div class='fetes seq1' /><div class='fetes seq2' /><div class='fetes seq3' /><div class='fetes seq4' />");
	$(".seq1").fadeIn(1500, function(){
		$(".seq2").fadeIn(2000, function(){
			$(".seq3").fadeIn(2000, function(){
				setTimeout(function() {
				  $(".seq4").fadeIn(1500);
				}, 5000);				
			})		
		})
	});

	*/
	// Blocs déroulants
	$(".zone_deroulante:not(.ouvert)").hide();
	
	$("a.lien_deroulant").click(
		function(){
			return bloc_deroulant($(this));
		}
	);

	// Tracking Google Analytics
	$("a.lien_deroulant[onclick='']").each(function(){
		$(this).attr('onclick',"_gaq.push(['_trackEvent', 'Infobloc', '" + $('title').text().split(' - ')[0].replace(/\'/g,"\\\'") + "', '" + $(this).html().replace(/\'/g,"\\\'") + "']);");
	});

	// Tableaux colorés
	$(".tmpl_caract tr:even").addClass("paire");
	
	if ($(".contenu a.selected").length) {
		$(".contenu a.selected").parent().addClass("bloc_deroulant_selected");
	}
	
	// Select du premier input
	if(typeof(no_focus_on_load) == 'undefined') {
		no_focus_on_load = false;
	}
	
	if (!no_focus_on_load) {
		if (document.forms.length > 0) {
			for(j=0;j<document.forms.length;j++) {
				var l = document.forms[j].elements.length;
				var e = document.forms[j].elements;
				for(i=0;i<l;i++) {
					var focused = false;
					if((e[i].type=="text") || (e[i].type=="textarea") || (e[i].type=="iframe")){
						if ((e[i].id != "mots_cles") && (e[i].id != "utilisateur_rapide") && (e[i].id != "mot_passe_rapide") && (e[i].id != "label_mot_passe_rapide")) {
							if (!focused) {
								try {
									e[i].focus();
									focused = true;
									break;
								} catch(err) {
									
								}
							}
						}
					}
				}
			}
		}
	}
	
	// Liens Thickbox pour les QUIZ
	$(".fcktexte a.ajouter_thickbox").each(function(i){
		$(this).addClass("thickbox").attr("href",$(this).attr("href")+"?width=700&height=500&TB_iframe=true");
	});
	
	// Submit du form de recherche
	$('.form_recherche').submit(function(){
		var mots_cles = $(this).children('.mots_cles').val();
		if (mots_cles.length < 3) {
			alert(message_erreur_recherche);
		} else {
			window.location.href = $(this).attr('action') + '?q=' + encodeURIComponent($(this).children('.mots_cles').val());
		}
		return false;
	});
	
	$(".mots_cles").focus(function(){
		if ($(this).val() == default_str) {
			$(this).val("");
		}
	});
	
	$(".mots_cles").blur(function(){
		if (!$(this).val()) {
			$(this).val("");
		}
	});
	$("body").addClass("js");
	
	// Ajout de class contextuel à l'élément body
	if( $('#accueil #clienteles').length > 0 ) {$('body').addClass('accueil');}
	if (window.location.href.indexOf('services-aux-citoyens') != -1) {$('body').addClass('services-aux-citoyens');}
	if (window.location.href.indexOf('services-aux-professionnels') != -1) {$('body').addClass('services-aux-professionnels');}
	if (window.location.href.indexOf('services-aux-etudiants') != -1) {$('body').addClass('services-aux-etudiants');}
	if (window.location.href.indexOf('a-propos') != -1) {
		$('body').addClass('a-propos');
		$('#sous_navig_corpo').prepend('<li><a href="#">&Agrave; PROPOS</a></li>');
	}
	if (window.location.href.indexOf('a-signaler') != -1) {
		$('body').addClass('a-signaler');
		$('#sous_navig_corpo').prepend('<li><a href="#">&Agrave; SIGNALER</a></li>');
	}	
	if (window.location.href.indexOf('nous-joindre') != -1) {
		$('body').addClass('nous-joindre');
		
	}	
	if (window.location.href.indexOf('a-l-agenda') != -1) {
		$('body').addClass('a-l-agenda');
		
	}	
		
	if (window.location.href.indexOf('catalogue-des-produits-et-services-soquij') != -1) {$('body').addClass('catalogue-des-produits-et-services-soquij');}	
	if (window.location.href.indexOf('ressources-pour-tous') != -1) {
		$('body').addClass('ressources-pour-tous');
		$('#sous_navig_corpo').prepend('<li><a href="#">RESSOURCES POUR TOUS</a></li>');
	}

	if (window.location.href.indexOf('formulaire-d-inscription') != -1) {
		$('body').addClass('formulaire-d-inscription');
		var thisheader = $('#contact h2');
		var thisheaderText = $('#contact h2').text();
		thisheader.replaceWith($('<h1 class="titrepage">' + thisheaderText + '</h1>'));
		$('h1.titrepage').after('<h2 class="titrepage2">Formulaire d\'inscription</h2>');
		
		$('.droite label, #inscription_activite_commentaire, .contenu_padding .bouton').appendTo('#contact form');

	}		

	if (window.location.href.indexOf('express-2-0') != -1) {
		$('body').addClass('express-2-0');
		}
	
	if (window.location.href.indexOf('banques-de-donnees-azimut') != -1) {
		$('body').addClass('banques-de-donnees-azimut');
		}	

	if (window.location.href.indexOf('collections') != -1) {
		$('body').addClass('collections');
		}
		
	if (window.location.href.indexOf('publications-imprimees') != -1) {
		$('body').addClass('publications-imprimees');
		}		
	if (window.location.href.indexOf('centre-de-formation') != -1) {
		$('body').addClass('centre-de-formation');
		}
	if (window.location.href.indexOf('service-des-ventes') != -1) {
		$('body').addClass('service-des-ventes');
		}	
	if (window.location.href.indexOf('service-aux-utilisateurs') != -1) {
		$('body').addClass('service-aux-utilisateurs');
		}	
	if (window.location.href.indexOf('english') != -1) {
		$('body').addClass('english');
		}	
	if (window.location.href.indexOf('essai30jours') != -1) {
		$('body').addClass('essai30jours');
		}
	if (window.location.href.indexOf('soquij-des-aujourd-hui-pour-les-jeunes-juristes') != -1) {
		$('body').addClass('starwars');
		}		
		
	// Ajout d'un lien "En savoir plus" pour les nouvelles à signaler
	$('#a_signaler li').each(function(){
		var a_signaler = $('h3 a', this).attr('href');
		$(this).append('<a title="En savoir plus" class="savoir-plus" href"#">En savoir plus</a>');
		$('.savoir-plus', this).attr('href', a_signaler );
	});	
	
	/* Login Azimut*/
	$('#info_login_azimut img').attr('src', '/images/soquij2013/cadenas.png'); // à enlever quand le template sera modifié 
	//$('.pros .niveau2, .etudiants .niveau2').append('<li class="jaja"><a href="#">&nbsp;</a></li>');
	
	$("#form_login_azimut .bouton").click(function(){
		$("#acceder_azimut").trigger("click");
	})
	
	/* Remplace l'image par du texte - à enlever quand le template sera modifié */
	var serviceTitle = $('#clientele_entete h1 img').attr('alt')
	$('#clientele_entete h1 img').hide();
	$('#clientele_entete h1').append(serviceTitle); 
	
	/* Mise en page de Pros*/
	$('.services-aux-professionnels #catalogue_apercu').after('<div class="coldroite" />')
	$('.services-aux-professionnels .categorie_vedette, .services-aux-professionnels #service_utilisateurs').appendTo('.coldroite');
	
	/* Mise en page de étudiants */
	$('.services-aux-etudiants #vedettes').after('<div class="coldroiteEtudiant" />')
	$('.services-aux-etudiants #bloc_documentation, .services-aux-etudiants .categorie_vedette').appendTo('.coldroiteEtudiant');
	$('#clientele_entete').next('h2').addClass('pas-etoiles');

	$('div#actions').prependTo('.formulaire-d-inscription .droite');
	
	
}

function update_select(args){
	// le id selectionne du select source

	var select_src = $(args['select_src']);
	var select_dst = $(args['select_dst']);
	
	var arr = {id : $(select_src).val()};
	
	if ($(select_src).val() == "") {
	
		$(select_dst).empty();
		$(select_dst).append('<option value="">'+args['default']+'</option>\n');
		
	} else {
		$.getJSON(args['post_path'],arr,function(json){

			$(select_dst).empty();
			
			if (args['default']) {
				$(select_dst).append('<option value="">'+args['default']+'</option>\n');
			}
			
			$(json).each(function(i){
				$(select_dst).append('<option value="'+json[i].id+'"' + (json.length == 1 ? ' selected="selected"' : '') + '>'+json[i].name+'</option>\n');
			});
			
		});
	}
}

        jQuery(document).ready(function($) {
		
		  
          

          
        });


