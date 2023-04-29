package az.compar.fileprovider.config.events;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;

@Configuration
public class AppListener {
    @EventListener(ApplicationReadyEvent.class)
    public void onAppReady() {
        File file = new File("src/main/resources/assets");
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
