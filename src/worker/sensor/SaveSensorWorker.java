package worker.sensor;

import javax.swing.SwingWorker;
import model.Sensor;
import service.SensorService;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SaveSensorWorker extends SwingWorker<Void, Void> {

	private SensorService service;
	private String nama;

	public SaveSensorWorker(SensorService service, String nama) {
		this.service = service;
		this.nama = nama;
	}

	@Override
	protected Void doInBackground() throws Exception {
		Sensor sensor = buildSensor(nama);
		service.create(sensor);
		return null;
	}

	// helper fleksibel untuk membuat instance Sensor walau API berbeda
	private Sensor buildSensor(String nama) {
		try {
			Class<?> cls = Sensor.class;
			// coba konstruktor (int, String)
			try {
				Constructor<?> c = cls.getConstructor(int.class, String.class);
				return (Sensor) c.newInstance(0, nama);
			} catch (NoSuchMethodException ignored) {}

			// coba konstruktor (String)
			try {
				Constructor<?> c = cls.getConstructor(String.class);
				return (Sensor) c.newInstance(nama);
			} catch (NoSuchMethodException ignored) {}

			// coba konstruktor default + setter setNama / setName
			Sensor s = (Sensor) cls.getConstructor().newInstance();
			try {
				Method m = cls.getMethod("setNama", String.class);
				m.invoke(s, nama);
			} catch (NoSuchMethodException e1) {
				try {
					Method m = cls.getMethod("setName", String.class);
					m.invoke(s, nama);
				} catch (NoSuchMethodException ignored) {}
			}
			return s;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
