package mcb.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import mcb.persistenz.McbException;
import mcb.persistenz.PersistenceStore;
import mcb.persistenz.filter.AlleFilter;
import mcb.persistenz.filter.SelectedFilter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.SelectionInList;

public abstract class SelectionInListPanel<T extends Model> extends JPanel {

	private static final long serialVersionUID = -3683947066010395220L;
	private SelectionInList<T> modelliste;
	private JList list;
	private JTextField anzahlText;
	private PresentationModel<T> detailModel;
	protected final PersistenceStore persistenceStore;

	public SelectionInListPanel(PersistenceStore persistenceStore) {
		super();
		this.persistenceStore = persistenceStore;
		this.initialize();
	}

	private void createList() {
		this.list = BasicComponentFactory.createList(this.modelliste);
		Dimension preferredSize = this.list.getPreferredSize();
		preferredSize.width = preferredSize.width + 40;
		this.setMinimumSize(preferredSize);
	}

	public void createNewAndAdd() throws McbException {
		SelectedFilter.set(AlleFilter.getInstance());
		T neu = this.createNewModel();
		this.updateModelliste();
		this.list.setSelectedValue(neu, true);
	}

	protected abstract T createNewModel() throws McbException;

	protected abstract List<T> getContents();

	public PresentationModel<T> getDetailModel() {
		if (this.detailModel == null) {
			this.detailModel = new PresentationModel<T>(this.modelliste);
		}
		return this.detailModel;
	}

	public boolean hasSelection() {
		return this.modelliste.getSelection() != null;
	}

	private void initialize() {
		this.modelliste = new SelectionInList<T>(this.getContents());
		this.setLayout(new BorderLayout());
		this.createList();
		JScrollPane scrollpane = new JScrollPane(this.list);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollpane, BorderLayout.CENTER);
		if (!this.modelliste.getList().isEmpty()) {
			this.modelliste.setSelectionIndex(0);
		}
		this.anzahlText = new JTextField();
		this.add(this.anzahlText, BorderLayout.SOUTH);
		this.anzahlText.setText(String.valueOf(this.getContents().size()));
	}

	protected abstract void loescheObjekt(T objekt) throws McbException;

	public void loescheSelection() throws McbException {
		this.loescheObjekt(this.modelliste.getSelection());
		this.updateModelliste();
	}

	public void refreshList() {
		this.list.repaint();
	}

	public void setListEnabled(boolean b) {
		this.list.setEnabled(b);
	}

	public void updateModelliste() {
		this.modelliste.setList(this.getContents());
		this.anzahlText.setText(String.valueOf(this.getContents().size()));
	}
}
