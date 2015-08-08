package mcb.ui.adresse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mcb.model.Adresse;
import mcb.model.Besuch;
import mcb.model.Fehlergruende;
import mcb.model.FruehstuecksTag;
import mcb.model.Land;
import mcb.persistenz.PersistenceStore;
import mcb.ui.base.BearbeitenAction;
import mcb.ui.base.ModelPanel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AdressePanel extends ModelPanel<Adresse> {

  private class FruehstuecksSpinner extends JPanel {

    private static final long serialVersionUID = 1L;

    private JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

    private final FruehstuecksTag tag;

    public FruehstuecksSpinner(FruehstuecksTag tag) {
      super(new BorderLayout());
      this.tag = tag;
      this.init();
    }

    public int getValue() {
      return ((Integer) this.spinner.getValue()).intValue();
    }

    private void init() {
      this.add(this.spinner, BorderLayout.CENTER);
      ((SpinnerNumberModel) this.spinner.getModel()).setMinimum(new Integer(0));
      this.spinner.addChangeListener(new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
          AdressePanel.this.fruehstueckChanged(FruehstuecksSpinner.this.getValue(), FruehstuecksSpinner.this.tag);
        }

      });
    }

    public void setValue(int anzahl) {
      this.spinner.setValue(new Integer(anzahl));
    }

  }

  private static List<String> alleLaenderKurzel() {
    List<String> result = new ArrayList<String>();
    Land[] values = Land.values();
    for (Land land : values) {
      result.add(land.name());
    }
    return result;
  }

  private static final long serialVersionUID = -427646783597528169L;
  private JTextField vornameTextfield;
  private JTextField nachnameTextfield;
  private JTextField strasseTextfield;
  private JTextField plzTextfield;
  private JTextField ortTextfield;
  @SuppressWarnings("rawtypes")
  private JComboBox landTextfield;
  private JTextField emailTextfield;
  @SuppressWarnings("rawtypes")
  private JComboBox emailgrundTextfield;
  private JCheckBox gespannCheckbox;
  private JCheckBox soloCheckbox;
  private JTextField geburtstagTextfield;
  @SuppressWarnings("rawtypes")
  private JList besuchListe;
  private JCheckBox meldungCheckbox;
  private FruehstuecksSpinner fruehstueckSamstagIntegerField;
  private FruehstuecksSpinner fruehstueckSonntagIntegerField;
  private JButton bearbeitenButton;

  private JCheckBox mitgliedCheckbox;

  public AdressePanel(PresentationModel<Adresse> presentationModel, BearbeitenAction<Adresse> bearbeitenAction,
      PersistenceStore persistenceStore) {
    super(presentationModel, bearbeitenAction, persistenceStore);
    this.initComponents();
    this.buildPanel();
    this.initListeners();
  }

  private void buildPanel() {
    FormLayout layout = new FormLayout(
        "3dlu, right:pref, 3dlu, 35dlu, 3dlu, 35dlu, 3dlu, 40dlu, 3dlu, 60dlu, 3dlu, 53dlu, 3dlu", // cols
        "3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu"); // rows

    PanelBuilder builder = new PanelBuilder(layout, this);
    CellConstraints cc = new CellConstraints();

    int row = 2;
    builder.add(this.bearbeitenButton, cc.xyw(2, row, 5));
    row++;
    row++;
    builder.addSeparator("Adresse", cc.xyw(2, row, 11));
    row++;
    row++;
    builder.addLabel("Vorname, Name", cc.xy(2, row));
    builder.add(this.vornameTextfield, cc.xyw(4, row, 5));
    builder.add(this.nachnameTextfield, cc.xyw(10, row, 3));
    row++;
    row++;
    builder.addLabel("Stra�e", cc.xy(2, row));
    builder.add(this.strasseTextfield, cc.xyw(4, row, 9));
    row++;
    row++;
    builder.addLabel("Land, PLZ, Ort", cc.xy(2, row));
    builder.add(this.landTextfield, cc.xy(4, row));
    builder.add(this.plzTextfield, cc.xy(6, row));
    builder.add(this.ortTextfield, cc.xyw(8, row, 5));
    row++;
    row++;
    builder.addLabel("Geburtstag", cc.xy(2, row));
    builder.add(this.geburtstagTextfield, cc.xyw(4, row, 5));
    builder.add(this.mitgliedCheckbox, cc.xyw(10, row, 3, "r,d"));
    row++;
    row++;
    builder.addSeparator("Email", cc.xyw(2, row, 11));
    row++;
    row++;
    builder.addLabel("Adresse", cc.xy(2, row));
    builder.add(this.emailTextfield, cc.xyw(4, row, 9));
    row++;
    row++;
    builder.add(this.emailgrundTextfield, cc.xyw(4, row, 9));
    row++;
    row++;
    builder.addSeparator("Fahrzeuge", cc.xyw(2, row, 11));
    row++;
    row++;
    builder.add(this.gespannCheckbox, cc.xyw(4, row, 5));
    builder.add(this.soloCheckbox, cc.xyw(10, row, 2));
    row++;
    row++;
    builder.addSeparator("vergangene Treffen", cc.xyw(2, row, 5));
    builder.addSeparator("aktuelles Treffen", cc.xyw(8, row, 5));
    row++;
    row++;
    builder.add(new JScrollPane(this.besuchListe), cc.xywh(2, row, 5, 5));
    builder.add(this.meldungCheckbox, cc.xyw(8, row, 4));
    row++;
    row++;
    builder.addLabel("Fr�hst�ck Samstag", cc.xyw(8, row, 3));
    builder.add(this.fruehstueckSamstagIntegerField, cc.xy(12, row));
    row++;
    row++;
    builder.addLabel("Fr�hst�ck Sonntag", cc.xyw(8, row, 3));
    builder.add(this.fruehstueckSonntagIntegerField, cc.xy(12, row));
  }

  private void fruehstueckChanged(int anzahl, FruehstuecksTag tag) {
    Adresse adresse = AdressePanel.this.getAdresse();
    if (adresse == null || adresse.getAktuellerBesuch() == null) {
      return;
    }
    tag.setFruehstueck(adresse.getAktuellerBesuch(), anzahl);
    AdressePanel.this.saveAdresse();
  }

  public Adresse getAdresse() {
    return this.presentationModel.getBean();
  }

  private void initComponents() {
    @SuppressWarnings("rawtypes")
    ListModel countryListModel = new ArrayListModel<String>(AdressePanel.alleLaenderKurzel());
    ValueModel countryModel = this.presentationModel.getBufferedModel(Adresse.LAND);
    SelectionInList<String> countrySil = new SelectionInList<String>(countryListModel, countryModel);
    this.landTextfield = BasicComponentFactory.createComboBox(countrySil);

    @SuppressWarnings("rawtypes")
    ListModel fehlerListModel = new ArrayListModel<String>(Fehlergruende.alleGruende());
    ValueModel fehlerModel = this.presentationModel.getBufferedModel(Adresse.FEHLERGRUND);
    SelectionInList<String> fehlerSil = new SelectionInList<String>(fehlerListModel, fehlerModel);
    this.emailgrundTextfield = BasicComponentFactory.createComboBox(fehlerSil);

    this.vornameTextfield = BasicComponentFactory.createTextField(this.presentationModel.getBufferedModel(Adresse.VORNAME), false);
    this.nachnameTextfield = BasicComponentFactory.createTextField(this.presentationModel.getBufferedModel(Adresse.NAME), false);
    this.strasseTextfield = BasicComponentFactory.createTextField(this.presentationModel.getBufferedModel(Adresse.STRASSE), false);
    this.plzTextfield = BasicComponentFactory.createTextField(this.presentationModel.getBufferedModel(Adresse.PLZ), false);
    this.ortTextfield = BasicComponentFactory.createTextField(this.presentationModel.getBufferedModel(Adresse.ORT), false);
    this.emailTextfield = BasicComponentFactory.createTextField(this.presentationModel.getBufferedModel(Adresse.EMAIL), false);
    this.gespannCheckbox = BasicComponentFactory.createCheckBox(this.presentationModel.getBufferedModel(Adresse.GESPANN), "Gespann");
    this.soloCheckbox = BasicComponentFactory.createCheckBox(this.presentationModel.getBufferedModel(Adresse.SOLO), "Solo");
    this.geburtstagTextfield = BasicComponentFactory.createTextField(this.presentationModel.getBufferedModel(Adresse.GEBURTSTAG), false);
    SelectionInList<Besuch> besuchList = new SelectionInList<Besuch>(this.presentationModel.getBufferedModel(Adresse.VERGANGENE_TREFFEN));
    this.besuchListe = BasicComponentFactory.createList(besuchList);
    this.besuchListe.setEnabled(false);
    this.meldungCheckbox = new JCheckBox("Meldung");
    this.mitgliedCheckbox = BasicComponentFactory.createCheckBox(this.presentationModel.getBufferedModel(Adresse.MCB_MITGLIED), "Mitglied");
    this.fruehstueckSamstagIntegerField = new FruehstuecksSpinner(FruehstuecksTag.Samstag);
    this.fruehstueckSonntagIntegerField = new FruehstuecksSpinner(FruehstuecksTag.Sonntag);
    this.bearbeitenButton = new JButton(this.bearbeitenAction);
    this.setEnabled(false);
  }

  private void initListeners() {
    this.presentationModel.addPropertyChangeListener(PresentationModel.PROPERTY_BEAN, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {
        AdressePanel.this.updateCheckboxes();
      }
    });

    this.meldungCheckbox.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        AdressePanel.this.meldungChanged();
      }

    });
  }

  protected void meldungChanged() {
    Adresse adresse = this.presentationModel.getBean();
    if (adresse == null) {
      this.meldungCheckbox.setSelected(false);
      return;
    }
    if (this.meldungCheckbox.isSelected()) {
      adresse.addTreffen(this.persistenceStore.getTreffens().getAktuellesTreffen());
      this.persistenceStore.saveAll();
    } else {
      adresse.removeAktuellesTreffen();
      this.persistenceStore.saveAll();
    }
    this.updateCheckboxes();
  }

  public void saveAdresse() {
    this.persistenceStore.saveAll();
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    this.vornameTextfield.setEditable(enabled);
    this.nachnameTextfield.setEditable(enabled);
    this.strasseTextfield.setEditable(enabled);
    this.plzTextfield.setEditable(enabled);
    this.ortTextfield.setEditable(enabled);
    this.landTextfield.setEnabled(enabled);
    this.emailTextfield.setEditable(enabled);
    this.gespannCheckbox.setEnabled(enabled);
    this.geburtstagTextfield.setEditable(enabled);
    this.soloCheckbox.setEnabled(enabled);
    this.emailgrundTextfield.setEnabled(enabled);
    this.mitgliedCheckbox.setEnabled(enabled);
  }

  protected void updateCheckboxes() {
    Adresse adresse = this.presentationModel.getBean();
    if (adresse != null) {
      Besuch aktuellesTreffen = adresse.getAktuellerBesuch();
      if (aktuellesTreffen == null) {
        this.fruehstueckSamstagIntegerField.setValue(0);
        this.fruehstueckSonntagIntegerField.setValue(0);
        this.meldungCheckbox.setSelected(false);
      } else {
        this.meldungCheckbox.setSelected(true);
        this.fruehstueckSamstagIntegerField.setValue(aktuellesTreffen.getFruehstueckSamstag());
        this.fruehstueckSonntagIntegerField.setValue(aktuellesTreffen.getFruehstueckSonntag());
      }
      this.emailTextfield.setForeground(adresse.isEmailfehler() ? Color.RED : this.nachnameTextfield.getForeground());
    }
  }

  protected void updateEmailColor() {
    this.emailTextfield.setBackground(Color.RED);
  }

}