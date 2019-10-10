package alfred.services;

import alfred.models.ipify.IPInfo;
import alfred.repositories.IPInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IPInfoService {

    @Autowired
    private IPInfoRepository ipInfoRepository;

    public List<IPInfo> list() {
        return ipInfoRepository.findAll();
    }

    public void add(IPInfo iPinfo) {
        ipInfoRepository.save(iPinfo);
    }

    public boolean exist(String ip) {
        return ipInfoRepository.existsById(ip);
    }

    public IPInfo getByID(String ip) {
        Optional<IPInfo> ipInfo = ipInfoRepository.findById(ip);
        return ipInfo.orElseGet(() -> new IPInfo(ip, "N/A", "N/A", "N/A", "+00:00"));
    }
}
