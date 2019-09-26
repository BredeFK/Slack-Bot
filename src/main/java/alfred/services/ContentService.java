package alfred.services;

import alfred.models.Content;
import alfred.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public List<Content> list() {
        return contentRepository.findAll();
    }

    public Long add(Content content) {
        contentRepository.save(content);
        return content.getId();
    }

}