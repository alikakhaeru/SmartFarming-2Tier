package worker.tanaman;

import javax.swing.SwingWorker;
import model.Tanaman;
import service.TanamanService;
import view.TanamanPanel;

public class DeleteTanamanWorker extends SwingWorker<Void, Void> {

    private final TanamanService service;
    private final TanamanPanel panel;
    private final Tanaman tanaman;

    public DeleteTanamanWorker(TanamanService service, TanamanPanel panel, Tanaman tanaman) {
        this.service = service;
        this.panel = panel;
        this.tanaman = tanaman;
    }

    @Override
    protected Void doInBackground() {
        service.delete(tanaman);
        return null;
    }

    @Override
    protected void done() {
        new LoadTanamanWorker(service, panel).execute();
    }
}
