package mcb.panel;

import java.util.List;

import mcb.model.Treffen;
import mcb.persistenz.McbException;
import mcb.persistenz.PersistenceStore;
import mcb.persistenz.filter.Treffens;

public class TreffenSelectionInListPanel extends SelectionInListPanel<Treffen> {

  private static final long serialVersionUID = -3850904257220547857L;

  public TreffenSelectionInListPanel(PersistenceStore persistenceStore) {
    super(persistenceStore);
  }

  @Override
  protected Treffen createNewModel() throws McbException {
    Treffen neu = new Treffen();
    neu.setName("");
    this.treffens().add(neu);
    return neu;
  }

  @Override
  protected List<Treffen> getContents() {
    return this.treffens().getAlleTreffen();
  }

  @Override
  protected void loescheObjekt(Treffen treffen) throws McbException {
    this.treffens().remove(treffen);
    this.persistenceStore.saveAll();
  }

  private Treffens treffens() {
    return this.persistenceStore.getTreffens();
  }

}