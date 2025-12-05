package montresor.condeminov.ihm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import montresor.condeminov.mng.Calcul;
import montresor.condeminov.mng.InputDataPbB;
import montresor.condeminov.mng.InputDataPbBException;
import montresor.condeminov.mng.OutputDataPbB;
import montresor.condeminov.mng.ParamsPbB;
import montresor.condeminov.mng.RootMng;



@SuppressWarnings("restriction")
public class IhmElems {

	private static final Logger LOGGER = LoggerFactory.getLogger(IhmElems.class);
	
	private static final String ERROR_TITLE = "Erreur!";
	
	public static final int AVEC_PRISE_EN_CHARGE_CSO_PAR_EURL = 1;
	public static final int SANS_PRISE_EN_CHARGE_CSO_PAR_EURL = 0;
	
	public enum FilterModeExcel {

		XLSX_FILES("xlsx files (*.xlsx)", "*.xlsx");
		


		private ExtensionFilter extensionFilter;

		FilterModeExcel(String extensionDisplayName, String... extensions){
			extensionFilter = new ExtensionFilter(extensionDisplayName, extensions);
		}

		public ExtensionFilter getExtensionFilter(){
			return extensionFilter;
		}
	}

	private Scene laScene = null;
	private Stage leStage = null;
	
	private Font mainFont = null;
	
	private TextField mnTxtFieldNbParts = null;
	private TextField mnTxtFieldResFiscalSoumisIs = null;
	private TextField mnTxtFieldOtrRevFiscal = null;
	
	private TextField mnTxtFieldDepsHorsCso = null;
	
	private TextField mnTxtFieldTauxPressionSociale = null;
	
	private TextField mnTxtFieldRti = null;
	private TextField mnTxtFieldRsm = null;
	private TextField mnTxtFieldRs = null;
	private TextField mnTxtFieldResultatAnneePrec = null;
	
	
	private List<RadioButton> mnLstRadioBoutonsChoixPecCsoEurl = null;


	
	private TextArea mnTraceTxtArea;
	


	private Button mnBtnLancerCalcul;
	private BorderPane mnBorderPane;
	private MenuBar mnMenuBar;
	private GridPane mnGridPane;
	private BorderPane mnCalculBorderPane;

	
	private boolean mnPriseEnChargeCsoParEurl = true;

	public IhmElems(Stage stagggg, Scene sceeeeene) {
		super();
		this.leStage = stagggg;
		this.laScene = sceeeeene;
	
		
	}
	
	
	
	public TextField getMnTxtFieldNbParts() {
		return mnTxtFieldNbParts;
	}



	public void setMnTxtFieldNbParts(TextField mnTxtFieldNbParts) {
		this.mnTxtFieldNbParts = mnTxtFieldNbParts;
	}



	private void ecrDansTraceChoixFichierParametrage(File file, String info) {
		if (file != null ) {


			this.mnTraceTxtArea.appendText(info);
			this.mnTraceTxtArea.appendText(file.getAbsolutePath());
			this.mnTraceTxtArea.appendText(System.lineSeparator());
		}


	}
	
	


	private void buildMenubar() {


		// Create menus
		Menu actionsMenu = new Menu("Actions");
		


		MenuItem effaceTraceItem = new MenuItem("Effacer la trace");
		effaceTraceItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				try {
					mnTraceTxtArea.clear();

				} catch (Exception e) {

				}

			}
		});
		MenuItem exitItem = new MenuItem("Quitter");
		exitItem.setOnAction((actionEvent) -> this.leStage.close());
		
		Menu paramMenu = new Menu("Paramètres");
		MenuItem paramItem = new MenuItem("Lire le fichier de paramétrage");
		

		paramItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				File file = null;
				try {
					String paramRep = RootMng.getInstance().getRootDirApp()+"/param";
					File paramDir = new File(paramRep);
					boolean isGoodParamDir = false;
					if (paramDir.exists() == true) {
						if (paramDir.isDirectory() == true) {
							isGoodParamDir = true;
						}
					}
					if (isGoodParamDir == false ) {
						Alert infoErrDirParamDlg = new Alert(AlertType.ERROR);
						infoErrDirParamDlg.setTitle(ERROR_TITLE);
						infoErrDirParamDlg.setHeaderText("Répertoire de paramétrage inexistant:"+ paramRep);
						infoErrDirParamDlg.setContentText("Ayez la bonté de le créer et d'y mettre au moins un fichier de paramétrage.");
						infoErrDirParamDlg.showAndWait();
						return;
					}
					FileChooser fileChooserParamFile = new FileChooser();
					fileChooserParamFile.getExtensionFilters().setAll(
							Stream.of(FilterModeExcel.XLSX_FILES)
							.map(FilterModeExcel::getExtensionFilter)
							.collect(Collectors.toList()));
					fileChooserParamFile.setInitialDirectory(paramDir);
					file = fileChooserParamFile.showOpenDialog(leStage);
					if (file != null) {
						mnTraceTxtArea.clear();
						ecrDansTraceChoixFichierParametrage(file, "FICHIER DE PARAMETRAGE: ");
					
						ParamsPbB.newInstance();
						boolean ficParamCorrect = ParamsPbB.getInstance().lireFichierParametrage(file.getAbsolutePath());
						if (ficParamCorrect == false) {
							Alert infoErrFicParamDlg = new Alert(AlertType.ERROR);
							infoErrFicParamDlg.setTitle(ERROR_TITLE);
							infoErrFicParamDlg.setHeaderText("Fichier de paramétrage non conforme: "+ file.getAbsolutePath());
							infoErrFicParamDlg.setContentText("Voir la cause dans le fichier log de l'application");
							infoErrFicParamDlg.showAndWait();
							return;
						}
					
						mnTraceTxtArea.appendText(ParamsPbB.getInstance().getBaremeIrpp().toString());
						mnTraceTxtArea.appendText(ParamsPbB.getInstance().getBaremeIs().toString());
						mnTraceTxtArea.appendText(ParamsPbB.getInstance().getAutresParams().toString());
						mnBtnLancerCalcul.setDisable(false);
				
					}
						
				} catch (Exception e) {
					
						LOGGER.error(e.toString());
						
						Alert infoErrFicParamDlg = new Alert(AlertType.ERROR);
						infoErrFicParamDlg.setTitle(ERROR_TITLE);
						infoErrFicParamDlg.setHeaderText("Fichier de paramétrage non conforme: "+ file.getAbsolutePath());
						infoErrFicParamDlg.setContentText(e.toString());
						infoErrFicParamDlg.showAndWait();
					
				}
				

			}
		});

	
		Menu helpMenu = new Menu("?");
		MenuItem aboutItem = new MenuItem("A propos du logiciel");

		aboutItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(MainIhmPbB.NOM_LOGICIEL);
				alert.setHeaderText("Version: "+ MainIhmPbB.VERSION);
				alert.setContentText("Date: 03/04/2023");

				alert.showAndWait();

			}
		});










		// Add menuItems to the Menus

		actionsMenu.getItems().addAll( effaceTraceItem, exitItem);
		paramMenu.getItems().addAll(paramItem);

		helpMenu.getItems().addAll(aboutItem);
		
		
		

		// Create MenuBar
		this.mnMenuBar = new MenuBar();
		
		// Add Menus to the MenuBar
		this.mnMenuBar.getMenus().addAll(actionsMenu, paramMenu, helpMenu);
		this.mnMenuBar.prefWidthProperty().bind(this.leStage.widthProperty());
		
	}
	
	private HBox buildZoneToggleAvecOuSansPriseEnChargeCsoParEurl() {
		
		// FONCTIONNALITE GERBAGE POUR CALCUL
		// Titre
		Label labelTitrePecCsoEurl = new Label("Prise en charge des CSO par l'EURL: ");
		labelTitrePecCsoEurl.setFont(mainFont);

		// Radio boutons du choix nb max cams double-plancher pour calcul
		ToggleGroup groupPecScoEurl = new ToggleGroup();


		mnLstRadioBoutonsChoixPecCsoEurl = new ArrayList<RadioButton>();
		RadioButton lRadioButton = new RadioButton("Sans");
		lRadioButton.setToggleGroup(groupPecScoEurl);
		lRadioButton.setUserData(new Integer(SANS_PRISE_EN_CHARGE_CSO_PAR_EURL));
		lRadioButton.setSelected(false);
		lRadioButton.setFont(mainFont);
		mnLstRadioBoutonsChoixPecCsoEurl.add(lRadioButton);
		lRadioButton = new RadioButton("Avec");
		lRadioButton.setToggleGroup(groupPecScoEurl);
		lRadioButton.setUserData(new Integer(AVEC_PRISE_EN_CHARGE_CSO_PAR_EURL));
		lRadioButton.setSelected(true);
		lRadioButton.setFont(mainFont);
		mnLstRadioBoutonsChoixPecCsoEurl.add(lRadioButton);
		
		groupPecScoEurl.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				// Has selection.
				if (groupPecScoEurl.getSelectedToggle() != null) {
					RadioButton button = (RadioButton) groupPecScoEurl.getSelectedToggle();

					int userDataRadioButton = ((Integer) button.getUserData()).intValue();
					if (userDataRadioButton == AVEC_PRISE_EN_CHARGE_CSO_PAR_EURL) {
						mnPriseEnChargeCsoParEurl = true;
					

					} else {
						mnPriseEnChargeCsoParEurl = false;
						
					}

				}
			}
		});
		
		HBox choixPecCsoEurlHBox = new HBox();
		choixPecCsoEurlHBox.setPadding(new Insets(10));
		choixPecCsoEurlHBox.setSpacing(10);
		choixPecCsoEurlHBox.getChildren().add(labelTitrePecCsoEurl);
		choixPecCsoEurlHBox.getChildren().addAll(mnLstRadioBoutonsChoixPecCsoEurl);
		
		return(choixPecCsoEurlHBox);

	}
	
	private HBox buildZoneSaisieNbPartsRevenuHorsEurlRevenuSoumisIs() {

		Label lLabelTitreNbParts = new Label("Nb parts: ");
		lLabelTitreNbParts.setFont(mainFont);

		mnTxtFieldNbParts = new TextField("1");
		mnTxtFieldNbParts.setMinWidth(20);
		mnTxtFieldNbParts.setFont(mainFont);
	
		
		HBox hbNbParts = new HBox();
		hbNbParts.setPadding(new Insets(10));
		hbNbParts.setSpacing(2);
		hbNbParts.setAlignment(Pos.CENTER_LEFT);
		hbNbParts.getChildren().addAll(lLabelTitreNbParts, mnTxtFieldNbParts);
		
		Label lLabelTitreOtrRevFiscal = new Label("Revenus imposables hors EURL: ");
		lLabelTitreOtrRevFiscal.setFont(mainFont);
		mnTxtFieldOtrRevFiscal = new TextField("60000");
		mnTxtFieldOtrRevFiscal.setMinWidth(120);
		mnTxtFieldOtrRevFiscal.setFont(mainFont);

		HBox hbOtrRevFiscal = new HBox();
		hbOtrRevFiscal.setPadding(new Insets(10));
		hbOtrRevFiscal.setSpacing(2);
		hbOtrRevFiscal.setAlignment(Pos.CENTER_LEFT);
		hbOtrRevFiscal.getChildren().addAll(lLabelTitreOtrRevFiscal, mnTxtFieldOtrRevFiscal);

		Label lLabelTitreResFiscalSoumisIs = new Label("Résultat EURL soumis à l'IS: ");
		lLabelTitreResFiscalSoumisIs.setFont(mainFont);
		mnTxtFieldResFiscalSoumisIs = new TextField("0");
		mnTxtFieldResFiscalSoumisIs.setMinWidth(120);
		mnTxtFieldResFiscalSoumisIs.setFont(mainFont);

		HBox hbResFiscalSoumisIs = new HBox();
		hbResFiscalSoumisIs.setPadding(new Insets(10));
		hbResFiscalSoumisIs.setSpacing(2);
		hbResFiscalSoumisIs.setAlignment(Pos.CENTER_LEFT);
		hbResFiscalSoumisIs.getChildren().addAll(lLabelTitreResFiscalSoumisIs, mnTxtFieldResFiscalSoumisIs);
		
		
	

		
		
		
		

		HBox hbpn = new HBox();
		hbpn.setPadding(new Insets(0));
		hbpn.setSpacing(30);

		hbpn.getChildren().addAll(hbNbParts, hbOtrRevFiscal, hbResFiscalSoumisIs);
		
		

		return (hbpn);

	}
	
	private HBox buildZoneSaisieDepsHorsCsoTauxPressionSociale() {
		
		Label lLabelTitreDepensesHorsCso = new Label("Dépenses hors CSO: ");
		lLabelTitreDepensesHorsCso.setFont(mainFont);

		mnTxtFieldDepsHorsCso = new TextField("62000");
		mnTxtFieldDepsHorsCso.setMinWidth(120);
		mnTxtFieldDepsHorsCso.setFont(mainFont);
		
		HBox hbDespHorsCso = new HBox();
		hbDespHorsCso.setPadding(new Insets(10));
		hbDespHorsCso.setSpacing(2);
		hbDespHorsCso.setAlignment(Pos.CENTER_LEFT);
		hbDespHorsCso.getChildren().addAll(lLabelTitreDepensesHorsCso, mnTxtFieldDepsHorsCso);

		Label lLabelTitreTauxPressionSociale = new Label("Taux pression sociale: ");
		lLabelTitreTauxPressionSociale.setFont(mainFont);

		mnTxtFieldTauxPressionSociale = new TextField("27");
		mnTxtFieldTauxPressionSociale.setMinWidth(20);
		mnTxtFieldTauxPressionSociale.setFont(mainFont);

		HBox hbTauxPressionSociale = new HBox();
		hbTauxPressionSociale.setPadding(new Insets(10));
		hbTauxPressionSociale.setSpacing(2);
		hbTauxPressionSociale.setAlignment(Pos.CENTER_LEFT);
		hbTauxPressionSociale.getChildren().addAll(lLabelTitreTauxPressionSociale, mnTxtFieldTauxPressionSociale);


		
		

		HBox hbpn = new HBox();
		hbpn.setPadding(new Insets(0));
		hbpn.setSpacing(30);

		hbpn.getChildren().addAll(hbDespHorsCso, hbTauxPressionSociale);

		return (hbpn);

	}
	
	private HBox buildZoneSaisieRtiRsmRs() {

		Label lLabelTitreRti = new Label("RTI: ");
		lLabelTitreRti.setFont(mainFont);

		mnTxtFieldRti = new TextField("160000");
		mnTxtFieldRti.setMinWidth(20);
		mnTxtFieldRti.setFont(mainFont);

		HBox hbRti = new HBox();
		hbRti.setPadding(new Insets(10));
		hbRti.setSpacing(2);
		hbRti.setAlignment(Pos.CENTER_LEFT);
		hbRti.getChildren().addAll(lLabelTitreRti, mnTxtFieldRti);

		Label lLabelTitreRsm = new Label("RSM: ");
		lLabelTitreRsm.setFont(mainFont);
		mnTxtFieldRsm = new TextField("176000");
		mnTxtFieldRsm.setMinWidth(120);
		mnTxtFieldRsm.setFont(mainFont);

		HBox hbRsm = new HBox();
		hbRsm.setPadding(new Insets(10));
		hbRsm.setSpacing(2);
		hbRsm.setAlignment(Pos.CENTER_LEFT);
		hbRsm.getChildren().addAll(lLabelTitreRsm, mnTxtFieldRsm);
		
		Label lLabelTitreRs = new Label("RS: ");
		lLabelTitreRs.setFont(mainFont);

		mnTxtFieldRs = new TextField("160000");
		mnTxtFieldRs.setMinWidth(120);
		mnTxtFieldRs.setFont(mainFont);
		
	
		
		
		
		HBox hbRs = new HBox();
		hbRs.setPadding(new Insets(10));
		hbRs.setSpacing(2);
		hbRs.setAlignment(Pos.CENTER_LEFT);
		hbRs.getChildren().addAll(lLabelTitreRs, mnTxtFieldRs);
		
		
		
		Label lLabelTitreResultAnnPrec = new Label("Résultat N-1: ");
		lLabelTitreResultAnnPrec.setFont(mainFont);

		mnTxtFieldResultatAnneePrec = new TextField("160000");
		mnTxtFieldResultatAnneePrec.setMinWidth(120);
		mnTxtFieldResultatAnneePrec.setFont(mainFont);
		HBox hbResAnnPrec = new HBox();
		hbResAnnPrec.setPadding(new Insets(10));
		hbResAnnPrec.setSpacing(2);
		hbResAnnPrec.setAlignment(Pos.CENTER_LEFT);
		hbResAnnPrec.getChildren().addAll(lLabelTitreResultAnnPrec, mnTxtFieldResultatAnneePrec);	
		

		HBox hbpn = new HBox();
		hbpn.setPadding(new Insets(0));
		hbpn.setSpacing(30);

		hbpn.getChildren().addAll(hbRti, hbRsm, hbRs, hbResAnnPrec);

		return (hbpn);

	}
	
	

	private void buildGridpane() {

		GridPane root = new GridPane();

		

		root.setPadding(new Insets(20));
		root.setHgap(25);
		root.setVgap(12);
		
		HBox hbZoneToggleAvecOuSansPriseEnChargeCsoParEurl = buildZoneToggleAvecOuSansPriseEnChargeCsoParEurl();
		root.add(hbZoneToggleAvecOuSansPriseEnChargeCsoParEurl, 0, 1, 1, 1);
		
		HBox hbZoneSaisieNbPartsRevenuHorsEurlRevenuSoumisIs =  buildZoneSaisieNbPartsRevenuHorsEurlRevenuSoumisIs();
		
		root.add(hbZoneSaisieNbPartsRevenuHorsEurlRevenuSoumisIs, 0, 2, 1, 1);
		
		HBox hbZoneSaisieDepsHorsCsoTauxPressionSociale =  buildZoneSaisieDepsHorsCsoTauxPressionSociale();
		root.add(hbZoneSaisieDepsHorsCsoTauxPressionSociale, 0, 3, 1, 1);
		
		
		HBox hbZoneSaisieRtiRsmRs =  buildZoneSaisieRtiRsmRs();
		root.add(hbZoneSaisieRtiRsmRs, 0, 4, 1, 1);

		
		
		
				

		BorderPane lBorderPaneTrace = new  BorderPane();
		lBorderPaneTrace.setPadding(new Insets(10, 10, 10, 10));
		

		Label labelTrace = new Label("TRACE ET RESULTATS");
		labelTrace.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 18));


		lBorderPaneTrace.setTop(labelTrace);
		BorderPane.setMargin(labelTrace, new Insets(10, 10, 20, 10));
		BorderPane.setAlignment(labelTrace, Pos.CENTER);


		// TextArea
		TextArea textArea = new TextArea();
		textArea.setText("");

		String style=
				"-fx-text-fill: blue;"+
						"-fx-background-color: black;"+
						"-fx-font-size: 18;" +
						"-fx-font-weight: " + "bold;";  
		textArea.setStyle(style);   
		textArea.setPrefHeight((double)500);
		textArea.setPrefWidth((double)1200);
		textArea.setEditable(false);
		this.mnTraceTxtArea = textArea;

		lBorderPaneTrace.setCenter(textArea);

		root.add(lBorderPaneTrace,0, 5,1,1);
		
		root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	   
		this.mnGridPane = root;

	}

	private void buildBorderpane() {

		this.mnBorderPane = new BorderPane();
		this.mnBorderPane.setTop(this.mnMenuBar);
		this.mnBorderPane.setCenter(this.mnGridPane);
		this.mnBorderPane.setBottom(this.mnCalculBorderPane);
		this.mnBorderPane.prefHeightProperty().bind(this.laScene.heightProperty());
		this.mnBorderPane.prefWidthProperty().bind(this.laScene.widthProperty());
	
	}
	
	private InputDataPbB recupererDonneesEntree() throws NumberFormatException, InputDataPbBException {
		

		String str = this.mnTxtFieldNbParts.getText().trim();
		int nbParts = Integer.parseInt(str);
		
		str = this.mnTxtFieldOtrRevFiscal.getText().trim().replaceAll(",", ".");
		double revenuFiscalHorsEurl = Double.parseDouble(str);
		str = this.mnTxtFieldResFiscalSoumisIs.getText().trim().replaceAll(",", ".");
		double revenuFiscalSoumisIs = Double.parseDouble(str);
		
		str = this.mnTxtFieldDepsHorsCso.getText().trim().replaceAll(",", ".");
		double depHorsCso = Double.parseDouble(str);
		str = this.mnTxtFieldTauxPressionSociale.getText().trim().replaceAll(",", ".");
		double tauxPs = Double.parseDouble(str);
		
		str = this.mnTxtFieldRti.getText().trim().replaceAll(",", ".");
		double rti = Double.parseDouble(str);
		
		str = this.mnTxtFieldRsm.getText().trim().replaceAll(",", ".");
		double rsm = Double.parseDouble(str);
		
		str = this.mnTxtFieldRs.getText().trim().replaceAll(",", ".");
		double rs = Double.parseDouble(str);
		
		
		str = this.mnTxtFieldResultatAnneePrec.getText().trim().replaceAll(",", ".");
		double resAnnPrec = Double.parseDouble(str);
		
		

		InputDataPbB lInputDataPbB = 
				new InputDataPbB
						(
							nbParts,
							revenuFiscalHorsEurl,
							revenuFiscalSoumisIs, 
							depHorsCso,
							tauxPs,
							rti,
							rsm,
							rs,
							resAnnPrec
						);
		lInputDataPbB.setAvecPriseEnCharge(mnPriseEnChargeCsoParEurl);
		lInputDataPbB.verification();
		return (lInputDataPbB);

	}
	private OutputDataPbB calcul(InputDataPbB pInputDataPbB) {
		Calcul lCalcul = new Calcul();
		return (lCalcul.launch(pInputDataPbB));
		
	}
	
	private String renvoyerMessErreurDetailleDonneesEntree(String zeUglyError) {
		
		StringBuilder sb = new StringBuilder("Détail: ");
		sb.append(zeUglyError);
		sb.append(System.lineSeparator());
		sb.append("\nAyez la bonté de corriger lesdites données...");
		return(sb.toString());

	}
	private void buildCalculBorderPane() {
		BorderPane bpn = new BorderPane();

		bpn.setPadding(new Insets(10, 10, 10, 10));

		mnBtnLancerCalcul= new Button("Lancer le calcul");
		mnBtnLancerCalcul.setFont(mainFont);
		mnBtnLancerCalcul.setDisable(true);
		
		
		mnBtnLancerCalcul.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mnTraceTxtArea.clear();
				InputDataPbB lInputDataPbB = null;
				try {
					lInputDataPbB = recupererDonneesEntree();
					String strInputDataPbB = lInputDataPbB.toStringSpecial();
					LOGGER.info(strInputDataPbB);
					mnTraceTxtArea.appendText(strInputDataPbB);

				} catch (NumberFormatException e) {
					String zeError = e.toString();
					LOGGER.error(e.toString());
					Alert infoErrInputDataDlg = new Alert(AlertType.ERROR);
					infoErrInputDataDlg.setTitle(ERROR_TITLE);
					infoErrInputDataDlg.setHeaderText("Erreur de format dans les données d'entrée!");
					infoErrInputDataDlg.setContentText(renvoyerMessErreurDetailleDonneesEntree(zeError));
					infoErrInputDataDlg.showAndWait();
					return;
				} catch (InputDataPbBException e1) {
					String zeError = e1.toString();
					LOGGER.error(e1.toString());
					Alert infoErrInputDataDlg = new Alert(AlertType.ERROR);
					infoErrInputDataDlg.setTitle(ERROR_TITLE);
					infoErrInputDataDlg.setHeaderText("Erreur sur au moins une valeur dans les données d'entrée!");
					infoErrInputDataDlg.setContentText(renvoyerMessErreurDetailleDonneesEntree(zeError));
					infoErrInputDataDlg.showAndWait();
					return;					
				}
				
				mnTraceTxtArea.appendText(System.lineSeparator());
				mnTraceTxtArea.appendText(System.lineSeparator());
				OutputDataPbB lOutputDataPbB = calcul(lInputDataPbB);
				String resultatsCalcul = lOutputDataPbB.toStringSpecial();
				mnTraceTxtArea.appendText(resultatsCalcul);
//				LOGGER.info("{}{}", System.lineSeparator(), resultatsCalcul);
			}
		});
		
		
		FlowPane fpnber = new FlowPane();
		fpnber.setPadding(new Insets(5, 5, 5, 5));
		fpnber.setHgap(5); //inutile car un seul élément , mais bon...
		fpnber.getChildren().add(mnBtnLancerCalcul);

		bpn.setLeft(fpnber);
		// Set margin for left area.
		BorderPane.setMargin(fpnber, new Insets(10, 10, 10, 10));
		BorderPane.setAlignment(fpnber, Pos.BOTTOM_LEFT);



		this.mnCalculBorderPane = bpn;

	}







	public void initialize() {
		
		this.mainFont = Font.font("Arial", FontWeight.BOLD, 14);
		buildMenubar();
		buildGridpane();
		buildCalculBorderPane();
		buildBorderpane();


	}



	public BorderPane getMnBorderPane() {
		return mnBorderPane;
	}
	public void setMnBorderPane(BorderPane mnBorderPane) {
		this.mnBorderPane = mnBorderPane;
	}
	public MenuBar getMnMenuBar() {
		return mnMenuBar;
	}
	public void setMnMenuBar(MenuBar mnMenuBar) {
		this.mnMenuBar = mnMenuBar;
	}
	public GridPane getMnGridPane() {
		return mnGridPane;
	}
	public void setMnGridPane(GridPane mnGridPane) {
		this.mnGridPane = mnGridPane;
	}


	public Scene getLaScene() {
		return laScene;
	}


	public void setLaScene(Scene laScene) {
		this.laScene = laScene;
	}



}
