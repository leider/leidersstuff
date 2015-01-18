package mcb.frame;

import javax.swing.JMenuBar;

import mcb.model.Treffen;
import mcb.panel.TreffenMitListePanel;
import mcb.persistenz.PersistenceStore;

public class TreffenFrame extends SimpleFrame<Treffen> {

  private static final long serialVersionUID = -6092233954906826211L;

  public TreffenFrame(PersistenceStore persistenceStore) {
    super("MCB Treffen", persistenceStore);
  }

  @Override
  protected void addExtraMenu(JMenuBar bar) {
    // don't needed here
  }

  @Override
  protected TreffenMitListePanel createPanel() {
    return new TreffenMitListePanel(this.persistenceStore);
  }

}