package worker.lahan;

import javax.swing.SwingWorker;
import model.Lahan;
import service.LahanService;
import view.LahanPanel;

public class SaveLahanWorker extends SwingWorker<Void, Void> {

    private final LahanService service;
    private final LahanPanel panel;
    private final Lahan lahan;

    public SaveLahanWorker(LahanService service, LahanPanel panel, Lahan lahan) {
        this.service = service;
        this.panel = panel;
        this.lahan = lahan;
    }

    @Override
    protected Void doInBackground() {
        service.create(lahan);
        return null;
    }

    @Override
    protected void done() {
        new LoadLahanWorker(service, panel).execute();
    }
}
