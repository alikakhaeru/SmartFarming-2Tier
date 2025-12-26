package worker.lahan;

import javax.swing.SwingWorker;
import java.util.List;
import model.Lahan;
import service.LahanService;
import view.LahanPanel;

public class LoadLahanWorker extends SwingWorker<List<Lahan>, Void> {

    private final LahanService service;
    private final LahanPanel panel;

    public LoadLahanWorker(LahanService service, LahanPanel panel) {
        this.service = service;
        this.panel = panel;
    }

    @Override
    protected List<Lahan> doInBackground() {
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
