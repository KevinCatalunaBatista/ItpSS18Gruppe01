package de.hdm.itprojektss18.team01.sontact.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.itprojektss18.team01.sontact.client.gui.MessageBox;
import de.hdm.itprojektss18.team01.sontact.client.gui.NavigationReport;
import de.hdm.itprojektss18.team01.sontact.shared.EditorServiceAsync;
import de.hdm.itprojektss18.team01.sontact.shared.LoginServiceAsync;
import de.hdm.itprojektss18.team01.sontact.shared.ReportGeneratorAsync;
import de.hdm.itprojektss18.team01.sontact.shared.bo.LoginInfo;
import de.hdm.itprojektss18.team01.sontact.shared.bo.Nutzer;

/**
 * Die Klasse EntryPoint defniert die Methode <code>onModuleLoad()</code>. Diese Klasse ist der
 * Anfang des ReportGenerators. Der ReportGenerator soll nur statische HTML
 * ausgaben tätigen.
 * 
 * @author Ugur Bayrak, Kevin Batista, Dennis Lehle
 * 
 */
public class SontactReport implements EntryPoint {

	LoginInfo loginInfo = new LoginInfo();
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Bitte Melden Sie sich mit Ihren Google Account, um einen Zugriff auf den ReportGenerator zu bekommen.");
	private Anchor signInLink = new Anchor("Login");
	public Anchor signOutLink = new Anchor("Logout");
	ClientsideSettings clientSettings = new ClientsideSettings();

	LoginServiceAsync loginService = ClientsideSettings.getLoginService();
	EditorServiceAsync editorVerwaltung = ClientsideSettings.getEditorVerwaltung();

	/**
	 * ReportService und EditorService werden auf null gesetzt. Diese werden neu
	 * geladen, dies Dient zur Sicherheit.
	 */
	ReportGeneratorAsync reportGeneratorService = null;
	EditorServiceAsync editorService = null;

	/**
	 * Das ist die EntryPoint Methode <code>onModuleLoad()</code>	 
	 */
	public void onModuleLoad() {

		loginService.login(GWT.getHostPageBaseURL() + "SontactReport.html", new AsyncCallback<LoginInfo>() {

			@Override
			public void onFailure(Throwable error) {
				Window.alert("Fehler Login: " + error.toString());

			}

			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if (loginInfo.isLoggedIn() == true) {
					Nutzer n = new Nutzer();
					n.setId(Integer.valueOf(Cookies.getCookie("nutzerID")));
					n.setEmailAddress(Cookies.getCookie("nutzerGMail"));

					// Laden der ReportSeite.
					loadReport(n);
					
				} else {
					loadLogin();
				}
			}
		});
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("contentR").add(loginPanel);
	}

	private void loadReport(final Nutzer n) {
		MessageBox.alertWidget("Willkommen", "Hier können Sie ihren gewünschten Report generieren. ");
		RootPanel.get("navigatorR").add(new NavigationReport(n));
		
		HTML willkommen = new HTML("<div align=\"center\"> <h2> &nbsp; &nbsp; Willkommen beim Report Generator &nbsp; &nbsp;</h2> </div>");
		
		RootPanel.get("contentR").add(willkommen);
		RootPanel.get("contentR").add(new HTML("<div align=\"center\"> <image src='/images/ReportStart.png' width='350px' height='450px' align='center' /></div>"));
		
		HorizontalPanel footer = new HorizontalPanel();
		Anchor startseite = new Anchor("Startseite", "SontactReport.html");
		HTML copyrightText1 = new HTML(" | ");
		Anchor editorLink = new Anchor("Back 2 Sontact", "Sontact.html");
		HTML copyrightText2 = new HTML(" | 2018 Sontact | ");
		//Anchor impressumLink = new Anchor("Impressum");
		
		footer.add(startseite);
		footer.add(copyrightText1);
		footer.add(editorLink);
		footer.add(copyrightText2);

		RootPanel.get("footerR").add(footer);

	}
}
