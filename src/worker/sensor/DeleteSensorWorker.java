package worker.sensor;

import javax.swing.SwingWorker;
import model.Sensor;
import service.SensorService;

public class DeleteSensorWorker extends SwingWorker<Void, Void> {

	private SensorService service;
	private Sensor sensor;

	public DeleteSensorWorker(SensorService service, Sensor sensor) {
		this.service = service;
		this.sensor = sensor;
	}

	@Override
	protected Void doInBackground() throws Exception {
		service.delete(sensor);
		return null;
	}
}
