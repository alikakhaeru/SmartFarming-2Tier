package worker.sensor;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import model.Sensor;
import service.SensorService;
import service.impl.LahanServiceDefault;
import model.Lahan;
import view.SensorPanel;

public class LoadSensorWorker extends SwingWorker<List<Sensor>, Void> {

    private SensorPanel panel;
    private SensorService service;

    public LoadSensorWorker(SensorPanel panel, SensorService service) {
        this.panel = panel;
        this.service = service;
    }

    @Override
    protected List<Sensor> doInBackground() {
        return service.getAll();
    }

    @Override
    protected void done() {
        try {
            List<Sensor> data = get();
            if (data != null) {
                // build lahan name map
                LahanServiceDefault lahanService = new LahanServiceDefault();
                Map<Integer, String> lahanMap = new HashMap<>();
                for (Lahan l : lahanService.getAll()) {
                    lahanMap.put(l.getId(), l.getNamaLahan());
                }
                panel.getModel().setLahanMap(lahanMap);
                
                // Set data ke tabel
                panel.getModel().setData(data);
                
                // UPDATE LABEL RECORD DI SINI
                panel.setRecordCount(data.size());
            }
            try { panel.showLoading(false); } catch (Throwable t) { }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}