package alfred.services;

import alfred.models.ipify.IPInfo;
import alfred.models.logs.Logs;
import alfred.repositories.IPInfoRepository;
import alfred.repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LogsService {

    @Autowired
    private LogsRepository logsRepository;

    public List<Logs> list() {
        return logsRepository.findAll();
    }

    public void add(Logs logs) {
        logsRepository.save(logs);
    }

    public int getNumberOfTodaysErrorsByIp(String ip) {
        List<Logs> logs = logsRepository.findAll();
        int numberOfErrors = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        if (logs != null) {
            for (Logs log : logs) {
                if (log.getUserIp().equals(ip) && log.getDate().contains(dateFormat.format(new Date()))) {
                    numberOfErrors++;
                }
            }
        }

        return numberOfErrors;
    }
}
