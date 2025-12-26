package worker;

import javax.swing.SwingWorker;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataWorker<T> extends SwingWorker<T, Void> {
    private final Supplier<T> fetchData;    // Logika ambil data (Service)
    private final Consumer<T> onSuccess;    // Logika update UI
    private final Consumer<Boolean> loading; // Kontrol Progress Bar

    public DataWorker(Supplier<T> fetchData, Consumer<T> onSuccess, Consumer<Boolean> loading) {
        this.fetchData = fetchData;
        this.onSuccess = onSuccess;
        this.loading = loading;
    }

    @Override
    protected T doInBackground() throws Exception {
        if (loading != null) loading.accept(true);
        return fetchData.get();
    }

    @Override
    protected void done() {
        try {
            T result = get();
            if (onSuccess != null) onSuccess.accept(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (loading != null) loading.accept(false);
        }
    }
}