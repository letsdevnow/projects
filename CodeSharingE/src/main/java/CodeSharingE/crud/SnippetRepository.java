package CodeSharingE.crud;

import CodeSharingE.common.Snippet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnippetRepository extends CrudRepository<Snippet, Integer> {
    @org.springframework.data.jpa.repository.Query
            (value = "SELECT * FROM snippets order by date desc limit 10", nativeQuery = true)
    List<Snippet> findLatestSnippets();
}
