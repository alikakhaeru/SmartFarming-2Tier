package worker.tanaman;

import javax.swing.SwingWorker;
import java.util.List;
import model.Tanaman;
import service.TanamanService;
import view.TanamanPanel;

public class LoadTanamanWorker extends SwingWorker<List<Tanaman>, Void> {

    private final TanamanService service;
    private final TanamanPanel panel;

    public LoadTanamanWorker(TanamanService service, TanamanPanel panel) {
        this.service = service;
        this.panel = panel;
    }

    @Override
    protected List<Tanaman> doInBackground() {
        return service.getAll();
    }

    @Override
    protected void done() {
        try {
            panel.getModel().setData(get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
